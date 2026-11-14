# 🔥 Hot Topics Monitor — Agent Skill

A self-contained AI Agent skill that fetches real-time trending topics from **10 global sources** — with **zero AI API calls**. YOU (the AI agent) are the intelligence engine.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Node.js](https://img.shields.io/badge/Node.js-%3E%3D18-green.svg)](https://nodejs.org/)

---

## What Is This?

An **Agent Skill** (for GitHub Copilot, Claude, Cursor, etc.) that gives your AI assistant the ability to:

- **Check what's trending** across Google Trends, Reddit, HackerNews, GitHub, HuggingFace, DuckDuckGo, Bing News, V2EX, Bilibili, and Twitter/X
- **Search for a keyword** across multiple platforms simultaneously
- **Analyze and report** on trends with structured importance scoring

### The Key Insight: Agent = AI Engine

```
User asks "What's trending in AI?" 
  → Agent reads SKILL.md (analysis framework)
  → Agent runs fetch-trends.ts (pure HTTP data fetching)
  → Agent classifies, translates, ranks, and summarizes the data itself
  → Agent outputs a structured report
```

The scripts make **zero external AI API calls**. They only fetch raw data via HTTP. All intelligence — classification, translation, importance scoring, quality filtering, and report generation — is performed by the AI agent itself using the analysis framework in [SKILL.md](SKILL.md).

**Benefits:**
- 🔑 **Zero API keys required** (except Twitter, which is optional)
- 💰 **Zero extra AI costs** — analysis runs on the model you're already paying for
- 🧠 **Quality scales with your model** — use Opus, get Opus-level analysis
- 📦 **Self-contained** — no database, no server, no configuration

## Quick Start

### Prerequisites

- Node.js ≥ 18
- `cheerio` package (for HTML/XML parsing)

### Installation

#### Option A: As a Copilot/Agent Skill

Clone into your project or Copilot skills directory:

```bash
# Into your project
git clone https://github.com/jacob-lou/hot-topics-monitor-skill.git skills/hot-topics-monitor

# Or into Copilot user skills
git clone https://github.com/jacob-lou/hot-topics-monitor-skill.git ~/.copilot/skills/hot-topics-monitor
```

Install the dependency:

```bash
npm install cheerio
```

#### Option B: Standalone

```bash
git clone https://github.com/jacob-lou/hot-topics-monitor-skill.git
cd hot-topics-monitor-skill
npm install
```

### Usage

All scripts output JSON to stdout. Run from the skill root directory:

```bash
# Fetch trends from all 10 sources
npx tsx scripts/fetch-trends.ts

# Pick specific sources
npx tsx scripts/fetch-trends.ts --sources hackernews,reddit,github

# Limit items per source
npx tsx scripts/fetch-trends.ts --sources hackernews --limit 5

# Search a keyword across sources
npx tsx scripts/search-keyword.ts "GPT-5"

# Search specific sources
npx tsx scripts/search-keyword.ts "DeepSeek" --sources reddit,hackernews,duckduckgo
```

> **Twitter/X** requires a `TWITTER_API_KEY` environment variable (from [twitterapi.io](https://twitterapi.io)). Without it, Twitter is silently skipped.

## Data Sources

| Source | Method | Content | Rate Limit |
|--------|--------|---------|------------|
| **Google Trends** | RSS Feed | Trending keywords + traffic estimates | Free / No auth |
| **Reddit** | JSON API | Hot posts from 11 AI subreddits | Free / No auth |
| **HackerNews** | Algolia API | Top stories by score | Free / No auth |
| **GitHub Trending** | HTML scraping | Daily trending repositories | Free / No auth |
| **HuggingFace** | REST API | Trending models + daily papers | Free / No auth |
| **DuckDuckGo** | Autocomplete API | Search suggestions + instant answers | Free / No auth |
| **Bing News** | RSS Feed | Technology news articles | Free / No auth |
| **V2EX** | REST API | Chinese tech community hot topics | Free / No auth |
| **Bilibili** | Web API | Hot searches + tech zone videos | Free / No auth |
| **Twitter/X** | twitterapi.io | AI-related trending tweets | Requires API key |

## How the Skill Works

The [SKILL.md](SKILL.md) file is the brain of this skill. It provides:

### 1. Workflow Decision Tree

The agent matches user intent to the right action:

| User Intent | Agent Action |
|-------------|-------------|
| "What's trending?" / "AI news?" | Run `fetch-trends.ts` → Analyze → Report |
| "Search for GPT-5" / "Track DeepSeek" | Run `search-keyword.ts` → Analyze → Report |
| "Add a new data source" | Read `references/extending.md` → Guide development |
| "How does the architecture work?" | Read `references/architecture.md` → Explain |

### 2. Classification Framework

Built-in keyword library (60+ terms) for categorizing content as AI/Tech vs General:

- **Models**: GPT, Claude, Gemini, LLaMA, Mistral, DeepSeek, Midjourney, etc.
- **Concepts**: LLM, Transformer, RAG, RLHF, Fine-tuning, Prompt Engineering, etc.
- **Tools**: PyTorch, TensorFlow, LangChain, HuggingFace, vLLM, etc.
- **Companies**: OpenAI, Anthropic, Google AI, Meta AI, xAI, etc.

### 3. Importance Scoring

Four-factor formula (0–100):

| Factor | Weight | Method |
|--------|--------|--------|
| Popularity | 35% | Score percentile within source |
| Engagement | 25% | Comments/replies percentile |
| Cross-source | 25% | +25 per additional source reporting same topic |
| Timeliness | 15% | 24h=100, 2-7d=60, 7-30d=30, >30d=10 |

### 4. Quality Filtering

Rules to identify and flag low-quality content:
- Clickbait (ALL CAPS, excessive punctuation)
- Ads/promotions (discount codes, affiliate links)
- Duplicates (titles with <30% edit distance)
- Stale content (>30 days old)

### 5. Report Template

Structured Markdown output with sections for high-attention items (3+ cross-source), noteworthy trends, domain breakdowns, and data overview tables.

## Project Structure

```
.
├── SKILL.md                    # Main entry: triggers + analysis framework
├── README.md                   # This file
├── package.json                # Dependencies (cheerio only)
├── scripts/
│   ├── fetch-trends.ts         # Multi-source trend fetching CLI
│   ├── search-keyword.ts       # Cross-source keyword search CLI
│   └── lib/
│       ├── types.ts            # TypeScript interfaces
│       ├── utils.ts            # HTTP helpers, arg parsing
│       └── sources/            # 10 source fetch functions
│           ├── google.ts
│           ├── reddit.ts
│           ├── hackernews.ts
│           ├── duckduckgo.ts
│           ├── github.ts
│           ├── huggingface.ts
│           ├── v2ex.ts
│           ├── bingnews.ts
│           ├── bilibili.ts
│           └── twitter.ts
└── references/                 # Context docs for the agent
    ├── architecture.md         # Full system architecture
    ├── data-sources.md         # Detailed source documentation
    └── extending.md            # How to add new sources
```

## Output Format

### fetch-trends.ts

```json
{
  "fetchedAt": "2026-04-02T12:00:00.000Z",
  "sources": ["hackernews", "reddit"],
  "totalItems": 80,
  "results": [
    {
      "source": "hackernews",
      "items": [
        {
          "title": "Show HN: I built a real-time AI news tracker",
          "url": "https://news.ycombinator.com/item?id=...",
          "source": "hackernews",
          "score": 342,
          "externalId": "hn-12345",
          "publishedAt": "2026-04-02T10:00:00.000Z",
          "extra": "{\"num_comments\":89}"
        }
      ],
      "count": 40,
      "error": null
    }
  ]
}
```

### search-keyword.ts

```json
{
  "keyword": "DeepSeek",
  "searchedAt": "2026-04-02T12:00:00.000Z",
  "totalResults": 21,
  "results": [
    {
      "title": "DeepSeek R2 benchmarks show surprising results",
      "url": "https://reddit.com/r/LocalLLaMA/...",
      "source": "reddit",
      "score": 1500,
      "snippet": "New benchmarks show DeepSeek R2 competing with..."
    }
  ]
}
```

## Extending

Want to add a new data source? See [references/extending.md](references/extending.md) for a step-by-step guide with a ProductHunt example.

## Related

This skill was extracted from [AI News Sentinel](https://github.com/jacob-lou/AI-News-Sentinel) — a full-stack real-time hot topics aggregation system with Express backend, SQLite database, WebSocket push, and web dashboard.

## License

[MIT](LICENSE) © jacob lou
