# 马瑙斯 · 公众号流水线

账号：**马瑙斯**。内容线：**AI 实操 / 复盘 / 专题 / 对比**（不默认追日更热点）。

## 工具

- **WeWrite**：选题 / 写作 / 审稿（`.cursor/skills/wewrite*`；编排见 `wechat-pipeline`）
- **wenyan-cli**：本地排版预览（`wenyan render`）
- **publish_remote.py**：推草稿箱（SSH → 远程 wenyan）

## 路径

| 用途 | 路径 |
|------|------|
| 成稿 | `wechat/articles/*.md` |
| 预览 | `wechat/preview/` |
| 封面 | `wechat/covers/` |
| 凭证 | `wechat/env`（勿提交；远程机也需已填） |
| 远程发布 | `wechat/publish_remote.py` |
| SSH 密钥 | `wechat/ssh-key-2024-07-18.key` |

## 用法

```
用 wechat-pipeline 写一篇公众号
```

Agent 会给 3～5 个常青向候选（怎么用 / 踩坑 / 专题 / 对比），你确认后再写长文。

```bash
# 本地预览
wenyan render -f wechat/articles/<slug>.md > wechat/preview/<slug>.html

# 推到微信草稿箱（须明确授权；唯一发布入口）
python3 wechat/publish_remote.py <slug>
```

脚本会 SSH 到 `ubuntu@79.72.84.5`，把正文/封面/文内图同步到 `/home/ubuntu/dot`，再在远程发布。成功应看到 `发布成功，Media ID: ...`。

```bash
python3 wechat/publish_remote.py <slug> --dry-run      # 只打印命令
python3 wechat/publish_remote.py <slug> --sync-only   # 只同步
```

未说「推到草稿箱」时不 publish；不要本机直接 `wenyan publish`。
