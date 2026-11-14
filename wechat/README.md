# 马瑙斯 · 公众号流水线

账号：**马瑙斯**。内容线：**AI 实操 / 复盘 / 专题 / 对比**（不默认追日更热点）。

## 工具

- **WeWrite**：选题 / 写作 / 审稿（`.cursor/skills/wewrite*`；编排见 `wechat-pipeline`）
- **wenyan-cli**：排版与草稿箱（`wenyan render` / `wenyan publish`）

建议把 `Python312\Scripts` 加入 PATH。

## 路径

| 用途 | 路径 |
|------|------|
| 成稿 | `wechat/articles/*.md` |
| 预览 | `wechat/preview/` |
| 封面 | `wechat/covers/` |
| 凭证 | `wechat/env`（勿提交） |

## 用法

```
用 wechat-pipeline 写一篇公众号
```

Agent 会给 3～5 个常青向候选（怎么用 / 踩坑 / 专题 / 对比），你确认后再写长文。

```bash
wenyan render -f wechat/articles/<slug>.md > wechat/preview/<slug>.html
wenyan publish -f wechat/articles/<slug>.md --env-file wechat/env
```

未说「推到草稿箱」时不 publish。
