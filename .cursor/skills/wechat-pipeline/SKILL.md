---
name: wechat-pipeline
description: >-
  Runs the 马瑙斯 WeChat Official Account pipeline: picks evergreen AI topics
  (how-to, postmortems, deep dives, comparisons—not daily hot news), drafts
  longer Chinese explainers with WeWrite skills, saves Markdown under
  wechat/articles, renders with wenyan, and publishes drafts via
  wechat/publish_remote.py (SSH sync + remote wenyan). Use when the user
  mentions 马瑙斯, 公众号, 微信草稿箱, wechat-pipeline, WeWrite, or wenyan
  publish/render for this repo.
---

# wechat-pipeline · 马瑙斯

## Account

- Author / 署名: **马瑙斯**
- Content line: **可复用的 AI 实操**（新号不靠日更抢时效）
- Audience: 想用 AI / Agent 把事做完的人，不要二手热搜复读
- Avoid: 国内热搜复读、海外 24h 资讯速报、提示词合集、空泛「AI 赋能」、编造「我在硅谷」经历

## Topic policy（硬规则：少资讯，多常青）

**默认不做**「今天/昨天海外刚爆的新闻解读」。资讯时效短、同质化高、冷启动无粉时几乎没阅读。

**优先四类选题**（候选必须标类型）：

| 类型 | 写什么 | 例子 |
|------|--------|------|
| 方法论 / 怎么用 | 一个具体场景的步骤、决策与边界 | 用 Cursor Skill + wenyan 搭公众号流水线 |
| 踩坑复盘 | 真实做过的过程：目标、踩坑、修好、还剩什么 | 草稿箱缺封面、IP 白名单、阅读量只有自测 +1 |
| 深度专题 | 一个主题拆透，不依赖「今天刚发」 | Agent Skills 标准怎么用、MCP 和 Skill 怎么分工 |
| 清单 / 对比 | 怎么选、怎么排优先级 | 排版发布工具怎么选；去 AI 味 skill 怎么用 |

选题扫描时：

1. 先问仓库与用户近期动作（`wechat/`、本机流水线、用户原话）——**亲历复盘优先于搜热点**
2. 需要外部材料时再 WebSearch；搜的是**方法、文档、对比**，不是 HN 日榜
3. 列出 3–5 个候选，每个标注：类型、读者读完能带走什么、为何适合存/转（不是「为何今天热」）
4. **丢弃**：纯快讯、无操作步骤的舆情复述、大号已洗烂的同题无新角度

用户明确要求「写今天某条新闻」时，才允许资讯向，并在开篇说明这是例外。

## Length & depth

成稿目标（中文正文，不含 frontmatter / 来源列表）：

- **字数**：约 **2800–4000 字**
- **结构建议**（按类型微调，一般 ≥5 个实质小节）：
  1. 开篇：读者具体问题 + 本文交付什么（别用「今日热点」开场）
  2. 场景 / 背景：缺什么上下文会做错
  3. 步骤或机制：可核对的操作、架构、取舍（至少 2–3 个可执行点）
  4. 踩坑与反例：什么情况下别用、常见翻车
  5. 怎么检验：成功长什么样（命令、产物路径、检查清单）
  6. 马瑙斯收束：下一步可做什么
- 复盘文：禁止编造亲历；只用用户提供或本仓库可验证的事实
- 需要外部依据时文末列 **主要来源**；事实与推断分开
- 发布前：`cover: ../covers/<slug>.jpg`；缺封面先补再 publish

## Paths (this repo)

| Item | Path |
|------|------|
| Articles | `wechat/articles/<slug>.md` |
| HTML preview | `wechat/preview/<slug>.html` |
| Covers | `wechat/covers/` |
| Credentials | `wechat/env`（远程发布机上也需已填） |
| Remote publish | `wechat/publish_remote.py` |
| SSH key | `wechat/ssh-key-2024-07-18.key` |
| Upstream write skills | `.cursor/skills/wewrite*` |
| Style | `%USERPROFILE%\.wewrite\style.yaml` |

## Progress checklist

```
- [ ] 1. Topic: 3–5 evergreen candidates (type + takeaway) → user picks
- [ ] 2. Draft: 2800–4000 字实操/复盘/专题稿 + review
- [ ] 3. Save: wechat/articles/<slug>.md (+ cover if publishing)
- [ ] 4. Render: wenyan render → wechat/preview/
- [ ] 5. Publish: only if user says 推到草稿箱 → python3 wechat/publish_remote.py <slug>
```

## Workflow

1. Read this skill; detail writing/review via `wewrite-write` / `wewrite-review` as needed.
2. **Topic**: 按 Topic policy 出候选。**确认前不写长文**（用户已点名选题除外）。
3. **Write + review**：写满 Length & depth；draft only；不用 WeWrite publish。
4. **Save**:

```yaml
---
title: <中文标题>
author: 马瑙斯
cover: ../covers/<slug>.jpg
---
```

5. **Render**:

```bash
wenyan render -f wechat/articles/<slug>.md -t default > wechat/preview/<slug>.html
```

6. **Publish**（须明确授权；用远程脚本，不要本机 `wenyan publish`）:

```bash
python3 wechat/publish_remote.py <slug>
```

脚本会：SSH 到 `ubuntu@79.72.84.5`（密钥 `wechat/ssh-key-2024-07-18.key`）→ 同步正文/封面/文内图到 `/home/ubuntu/dot`（结构与本地一致）→ 在远程用 `node "$(readlink -f "$(command -v wenyan)")"` 执行 publish。成功时应看到 `发布成功，Media ID: ...`；只有 exit 0 没有这行视为失败。

可选：`--dry-run` 只打印命令；`--sync-only` 只同步不发布。

## Hard rules

- 默认禁止日更式海外热搜速报；例外须用户点名
- 正文明显低于 ~2500 字视为未完成
- 未确认选题不写长文（已给题除外）
- 密钥不进仓库文稿；未授权不发布
- 发布只走 `wechat/publish_remote.py`，禁止本机直接 `wenyan publish`
- 事实与推断分开；不编造亲历

## WeWrite CLI (optional)

```bash
wewrite diagnose --json
wewrite home
```
