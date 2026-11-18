#!/usr/bin/env python3
"""Sync a WeChat article to the remote host and publish via wenyan.

Default remote:
  ubuntu@79.72.84.5:/home/ubuntu/dot
  key: wechat/ssh-key-2024-07-18.key

Usage:
  python3 wechat/publish_remote.py wechat/articles/<slug>.md
  python3 wechat/publish_remote.py <slug>
  python3 wechat/publish_remote.py <slug> --dry-run
  python3 wechat/publish_remote.py <slug> --sync-only
"""

from __future__ import annotations

import argparse
import os
import re
import shlex
import stat
import subprocess
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[1]
WECHAT_DIR = Path(__file__).resolve().parent

DEFAULT_HOST = "79.72.84.5"
DEFAULT_USER = "ubuntu"
DEFAULT_REMOTE_ROOT = "/home/ubuntu/dot"
DEFAULT_KEY = WECHAT_DIR / "ssh-key-2024-07-18.key"

MD_LINK_RE = re.compile(r"!\[[^\]]*\]\(([^)]+)\)")
COVER_RE = re.compile(r"(?m)^cover:\s*(.+?)\s*$")


def die(msg: str, code: int = 1) -> None:
    print(f"error: {msg}", file=sys.stderr)
    raise SystemExit(code)


def resolve_article(arg: str) -> Path:
    candidate = Path(arg)
    if candidate.suffix.lower() == ".md" and candidate.is_file():
        return candidate.resolve()

    slug = arg.removesuffix(".md")
    for path in (
        WECHAT_DIR / "articles" / f"{slug}.md",
        REPO_ROOT / "wechat" / "articles" / f"{slug}.md",
        Path(arg).expanduser(),
    ):
        if path.is_file():
            return path.resolve()
    die(f"article not found: {arg}")


def collect_assets(article: Path) -> list[Path]:
    """Article + cover + local images referenced by relative paths."""
    text = article.read_text(encoding="utf-8")
    assets = [article]

    cover_match = COVER_RE.search(text)
    if cover_match:
        cover = (article.parent / cover_match.group(1).strip().strip("\"'")).resolve()
        if not cover.is_file():
            die(f"cover missing: {cover}")
        assets.append(cover)

    for raw in MD_LINK_RE.findall(text):
        target = raw.strip().strip("\"'")
        if target.startswith(("http://", "https://", "data:")):
            continue
        path = (article.parent / target).resolve()
        if not path.is_file():
            die(f"referenced image missing: {target} -> {path}")
        assets.append(path)

    # de-dupe, keep order
    seen: set[Path] = set()
    ordered: list[Path] = []
    for path in assets:
        if path not in seen:
            seen.add(path)
            ordered.append(path)
    return ordered


def repo_relative(path: Path) -> Path:
    try:
        return path.resolve().relative_to(REPO_ROOT)
    except ValueError:
        die(f"file is outside repo root ({REPO_ROOT}): {path}")


def ensure_key_perms(key: Path) -> None:
    if not key.is_file():
        die(f"ssh key not found: {key}")
    mode = key.stat().st_mode
    if mode & (stat.S_IRWXG | stat.S_IRWXO):
        key.chmod(stat.S_IRUSR | stat.S_IWUSR)


def ssh_base(user: str, host: str, key: Path) -> list[str]:
    return [
        "ssh",
        "-i",
        str(key),
        "-o",
        "BatchMode=yes",
        "-o",
        "StrictHostKeyChecking=accept-new",
        f"{user}@{host}",
    ]


def run(cmd: list[str], *, dry_run: bool) -> None:
    printable = " ".join(shlex.quote(c) for c in cmd)
    print(f"$ {printable}")
    if dry_run:
        return
    subprocess.run(cmd, check=True)


def remote_mkdirs(ssh: list[str], remote_root: str, rel_dirs: set[Path], dry_run: bool) -> None:
    if not rel_dirs:
        return
    quoted = " ".join(shlex.quote(f"{remote_root}/{d.as_posix()}") for d in sorted(rel_dirs))
    run(ssh + [f"mkdir -p {quoted}"], dry_run=dry_run)


def scp_files(
    user: str,
    host: str,
    key: Path,
    remote_root: str,
    files: list[Path],
    dry_run: bool,
) -> None:
    for path in files:
        rel = repo_relative(path)
        remote = f"{user}@{host}:{remote_root}/{rel.as_posix()}"
        run(
            [
                "scp",
                "-i",
                str(key),
                "-o",
                "BatchMode=yes",
                "-o",
                "StrictHostKeyChecking=accept-new",
                str(path),
                remote,
            ],
            dry_run=dry_run,
        )


def remote_publish(
    ssh: list[str],
    remote_root: str,
    article_rel: Path,
    dry_run: bool,
) -> None:
    article_remote = article_rel.as_posix()
    env_remote = "wechat/env"
    # Remote /usr/local/bin/wenyan is a symlink. @wenyan-md/cli only parses argv when
    # process.argv[1] equals the real cli.js path, so invoking the symlink is a no-op
    # (exit 0, no publish). Always run the resolved path via node.
    cmd = (
        f"cd {shlex.quote(remote_root)} && "
        f'WENYAN_JS="$(readlink -f "$(command -v wenyan)")" && '
        f'node "$WENYAN_JS" publish -f {shlex.quote(article_remote)} '
        f"--env-file {shlex.quote(env_remote)}"
    )
    run(ssh + [cmd], dry_run=dry_run)


def parse_args(argv: list[str] | None = None) -> argparse.Namespace:
    p = argparse.ArgumentParser(
        description="Sync article assets to remote /home/ubuntu/dot and publish with wenyan.",
    )
    p.add_argument("article", help="slug or path under wechat/articles/")
    p.add_argument("--host", default=DEFAULT_HOST)
    p.add_argument("--user", default=DEFAULT_USER)
    p.add_argument("--remote-root", default=DEFAULT_REMOTE_ROOT)
    p.add_argument("--key", type=Path, default=DEFAULT_KEY)
    p.add_argument("--sync-only", action="store_true", help="upload files only, skip wenyan publish")
    p.add_argument("--dry-run", action="store_true", help="print commands without executing")
    return p.parse_args(argv)


def main(argv: list[str] | None = None) -> int:
    args = parse_args(argv)
    article = resolve_article(args.article)
    assets = collect_assets(article)
    article_rel = repo_relative(article)

    print(f"article: {article_rel}")
    print("sync:")
    for path in assets:
        print(f"  - {repo_relative(path)}")

    ensure_key_perms(args.key)
    ssh = ssh_base(args.user, args.host, args.key)

    rel_dirs = {repo_relative(path).parent for path in assets}
    remote_mkdirs(ssh, args.remote_root, rel_dirs, args.dry_run)
    scp_files(args.user, args.host, args.key, args.remote_root, assets, args.dry_run)

    if args.sync_only:
        print("done (sync-only)")
        return 0

    remote_publish(ssh, args.remote_root, article_rel, args.dry_run)
    print("done")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except subprocess.CalledProcessError as exc:
        die(f"command failed with exit {exc.returncode}", exc.returncode or 1)
    except KeyboardInterrupt:
        die("interrupted", 130)
