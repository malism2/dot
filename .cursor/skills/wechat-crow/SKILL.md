---
name: wechat-crow
description: >-
  Writes a WeChat Official Account article from a given topic as a comic:
  settles the angle with agent-reach, writes overview and captions with
  khazix-writer, keeps captions short with humanizer-zh, draws most of the
  piece with hand-drawn-styles, then formats and optionally publishes a draft
  with wenyan-cli the same way as wechat-pipeline (wechat/articles, wechat/env).
  Use only when the user mentions wechat-crow. Do not use for 马瑙斯, WeWrite,
  or wechat-pipeline.
---

# wechat-crow

编排 skill。不复述上游 skill 正文。每一步先读完该 skill 的 `SKILL.md`，再只做这一步。

署名：**马瑙斯**（草稿箱凭证与 wechat-pipeline 同一账号）。不套用 wechat-pipeline 的常青选题政策，也不走 WeWrite。

成稿是漫画：大部分是图，灰色幽默。文字**必须能听出卡兹克**（khazix-writer），不走 wechat-article-writer，也不许写成资讯通报腔。

口吻优先级：**khazix 活人感 > 漫画短句**。短可以，但不能短到只剩新闻社通稿。

## 标题与开头

- 标题要能总结全文主结论，短句即可。不要只起一个俏皮切口、读完还不知道文章在说什么
- 正文第一段先写总结性概述：发生了什么、卡点或对照在哪、读者读完能带走什么。概述写完再上第一格漫画
- 概述三到六句，像跟人聊天。必须从具体当下的事件或场景切入（「这两天刷到…」「事情是这样的」），禁止宏大叙事
- 概述里至少出现一次第一人称判断或不确定（「我觉得」「坦率的讲」「我还没亲自试过」）。没有真实经历就坦诚没试过，禁止编「比如有一次」
- 后面图下说明不再把概述整段复读一遍

## 读者与口吻

读者文化程度一般。写作身份按 khazix-writer：**有见识的普通人在认真聊一件打动他的事**。先把别国/行业里这件事的脉络讲清，再接到国内读者能对上的情况。

- 先按时间画别国或主流做法发生了什么、几套规则各管什么
- 术语第一次出现就用一句白话解释（可以带一句口语转场，如「怎么说呢」「你想想看」）
- 然后画国内同一类问题现在处在哪一步、和外面差在哪。只讲脉络，不站队，不喊口号
- 灰色幽默放在规则彼此拧着的地方。不嘲人，不解释笑点
- 节奏、禁区、口语词组、标点规则一律读并遵守 `khazix-writer`（尤其绝对禁区与高频踩雷词）
- **页下说明仍短，但必须像说话**：每页一到三句；至少有一句口语（转场、判断、自嘲、读者直呼或短句断裂），不能全篇只剩「团队写 / README 写明 / 结论是」
- 验收自检：把概述和全部页下说明连起来读，若听不出「有人在聊」而像「新闻摘要配图」，整段重写后再往下画
- 不把整篇卡兹克长文贴进成稿；不在文末做文化升维或人生道理收尾

## 找 skill

按顺序找 `<name>/SKILL.md`：

1. `.cursor/skills/<name>/SKILL.md`
2. `%USERPROFILE%/.cursor/skills/<name>/SKILL.md`

找不到就停，给出安装来源，不要自己补一套做法。

| Skill | 来源 |
|-------|------|
| agent-reach | https://github.com/Panniantong/Agent-Reach |
| khazix-writer | https://github.com/KKKKhazix/khazix-skills （目录 `khazix-writer`） |
| humanizer-zh | https://github.com/op7418/humanizer-zh |
| hand-drawn | https://github.com/threerocks/hand-drawn-styles （安装后的目录名是 `hand-drawn`） |

## Progress checklist

```
- [ ] 1. Topic: agent-reach 收敛主选题；角度未说死则确认后再写
- [ ] 2. Write: 读 khazix-writer；用其口吻写概述与页下说明草稿（须有第一人称/口语），并定能总结全文的短标题
- [ ] 3. Boards: 先写开头概述，再分镜；灰色幽默；每页一到三句，不能写成资讯通报
- [ ] 4. Style: 按内容走 hand-drawn 选型；未指定则给 2–3 候选或经授权自动采用
- [ ] 5. Humanize: humanizer-zh 改概述和图下说明（仍遵守 khazix 禁区）
- [ ] 6. Draw: 选定画风后 hand-drawn 出图；全文同一编号
- [ ] 7. Save: wechat/articles/<slug>.md（概述在第一图之前）
- [ ] 8. Render: wenyan render → wechat/preview/
- [ ] 9. Publish: 仅当用户说推到草稿箱、wechat/env 已填、封面存在
```

## Workflow

1. **选题** — 读 `agent-reach`，按它的规则做全网调研。收敛成一个主选题：角度、读者读完能带走什么、可核对来源。可用 khazix-writer 的 HKR（Happy / Knowledge / Resonance）粗检选题质量；明显不及格则先跟用户调方向。用户已经把角度说死，或说了「直接写」，就按该角度写。否则给出主选题和两个备选，确认后再写。

2. **写作** — 读 `khazix-writer`（含需要时的 `references/`）。用它的价值观、风格内核、绝对禁区、推荐口语和开头手法，写出：
   - 能总结全文主结论的短标题
   - 开头三到六句概述（具体场景切入 + 至少一次「我」的判断或不确定）
   - 各页图下说明草稿（每页一到三句，带口语，不是通稿）

   事实以第 1 步 agent-reach 素材为准，不编造亲身经历；没有真实细节就按 khazix 规则坦诚写「还没亲自试过」，禁止「比如有一次…」式假场景。跳过 khazix-writer 里面向完整长文的排版/发布设想。成稿形态仍是本 skill 的漫画，不输出一篇卡兹克长文正文。若草稿读起来像资讯摘要，整段重写，不得带着通稿口吻进入分镜。

3. **分镜** — 把第 2 步的文字压进连环画。先别国/主流脉络，再一页接到国内。每一页是一格漫画，页下一到三句说明。

- 概述放在第一格漫画之前，交代事件、对照和读者能带走什么，语气仍是聊天不是播报
- 笑点来自规则拧在一起，不来自丑化人物
- 说明不把图里或概述里已经写清的字再讲一遍，只补图上放不下的事实或一句判断
- 必须交代出处时写进那一句，不另附来源表
- 文末不要 `-end-`，不要「文 | 技能101」或其他落款
- 不因 khazix 的「文化升维 / 长文收尾」在漫画文末加人生道理段

4. **画风** — 读 `hand-drawn` 的协议和选型说明（`PROTOCOL.md` / `docs/style-selection.md`，或 `scripts/render_prompt.py --catalog`）。**不固定 1 号**；按这篇内容的用途和视觉意图选画风。

- 用户当场指定了编号或名字 → 直接采用，并执行该画风自己的合同（含 3.1/19/20/22 的锚点与验收）
- 用户说「按内容选」「按推荐」「别问我」→ 给出首选及简短理由后直接采用，成稿备注里写明编号
- 其余情况 → 提供有依据的 2–3 个候选（匹配理由 + 重要限制），停下等用户选；依据不足则展示 hand-drawn 完整菜单
- 需求与候选配方冲突时先说明，不静默换风、不混多个画风
- **一篇全文只用一个画风编号**；多页连续故事不得中途换号

内容向选型提示（编辑建议，真源仍以 hand-drawn 配方为准）：

| 内容 | 优先比较 |
|------|----------|
| 多步规则、年龄表、流程对照 | 1、4 |
| 产品/Agent 怎么用、单一接触动作 | 18、20 |
| 家庭或情绪故事 | 10、3.1 |
| 中国制度、历史脉络感 | 8、22 |
| 轻松吐槽切口 | 5 |

5. **去 AI 味** — 读 `humanizer-zh`。只改开头概述和页下说明。不动 frontmatter、图、命令、链接和事实。改完后再扫一遍 khazix-writer 绝对禁区（套话、冒号破折号双引号、踩雷词）。不另附改写评分。

6. **画** — 画风已定后，用 `hand-drawn` 的 `scripts/render_prompt.py` 出 prompt。不手改配方，不把业务项目的线条、配色或纸面规则写进 prompt。

- 分镜只写人物、动作、道具和要写进格子里的原句。格子里的字短，一句事实
- 图存 `wechat/images/<slug>-01.jpg` 起，按阅读顺序编号
- 封面用第一页，存 `wechat/covers/<slug>.jpg`。裁成封面时保住格子和字
- 中文必须清楚可读。错字就用同一条 prompt 重画

7. **保存** — `wechat/articles/<slug>.md`。frontmatter 之后先是概述，再是图和页下说明。不要把未压缩的长文草稿贴回来。

```yaml
---
title: <能总结全文主结论的短标题>
author: 马瑙斯
cover: ../covers/<slug>.jpg
---

<两到四句总结性概述>

![...](../images/<slug>-01.jpg)

<页下说明>
```

8. **排版** — 只用 wenyan，不调用 gzh-design：

```bash
wenyan render -f wechat/articles/<slug>.md -t default > wechat/preview/<slug>.html
```

9. **发布** — 三条同时满足才执行：用户明确说「推到草稿箱」、`wechat/env` 已填、封面文件 `wechat/covers/<slug>.jpg` 存在。

```bash
wenyan publish -f wechat/articles/<slug>.md --env-file wechat/env
```

缺封面只保留预览，不发布。

## Hard rules

- 成稿是漫画，大部分是图；灰色幽默；文字必须能听出 khazix，禁止资讯通报腔
- 文字写作只走 khazix-writer，不调用 wechat-article-writer
- 概述与页下说明优先活人感；短句不等于删掉「我」和口语
- 标题能总结全文；正文开头先概述，再出第一格漫画
- 画风按内容走 hand-drawn 选型，不默认锁死 1 号；一篇只用一个编号
- 图只走 hand-drawn，不在本 skill 里维护第二套画风
- 未授权不 `wenyan publish`；密钥不进文稿
- 排版和发布只用 wenyan，不调用 gzh-design
- 成稿无 `-end-`，无文末来源列表，无卡兹克式长文升华收尾段
- 先讲别国/主流脉络，再接到国内情况；给文化程度一般的读者看
- 用户说马瑙斯、WeWrite 或 wechat-pipeline 时，走 wechat-pipeline，不用本 skill
