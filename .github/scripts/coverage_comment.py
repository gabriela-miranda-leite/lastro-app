#!/usr/bin/env python3
import os
import re
import sys
import json
import urllib.request

MODULES = [
    ("core:common",  "core/common"),
    ("core:domain",  "core/domain"),
    ("core:network", "core/network"),
    ("core:ui",      "core/ui"),
    ("feature:auth", "feature/auth"),
]

REPORT_PATH = "build/reports/coverage/test/debug/report.xml"
MARKER = "<!-- coverage-report -->"


def parse_report(path):
    try:
        data = open(path).read()
    except FileNotFoundError:
        return None, []

    totals = re.findall(r'<counter type="LINE" missed="(\d+)" covered="(\d+)"', data)
    if not totals:
        return None, []
    missed, covered = int(totals[-1][0]), int(totals[-1][1])
    total = missed + covered
    pct = int(covered * 100 / total) if total else 0

    files = []
    for m in re.finditer(
        r'<sourcefile name="([^"]+)".*?<counter type="LINE" missed="(\d+)" covered="(\d+)"',
        data,
        re.DOTALL,
    ):
        name, fm, fc = m.group(1), int(m.group(2)), int(m.group(3))
        ft = fm + fc
        fp = int(fc * 100 / ft) if ft else 0
        files.append((name, fc, ft, fp))

    files.sort(key=lambda x: x[3])
    return (covered, total, pct), files


def badge(pct):
    if pct >= 80:
        return "🟢"
    if pct >= 50:
        return "🟡"
    return "🔴"


def build_comment(workspace):
    lines = [MARKER, "## Coverage Report\n"]

    for label, rel_path in MODULES:
        full_path = os.path.join(workspace, rel_path, REPORT_PATH)
        summary, files = parse_report(full_path)

        if summary is None:
            lines.append(f"<details>")
            lines.append(f"<summary>⬜ {label} — sem relatório</summary>\n</details>\n")
            continue

        covered, total, pct = summary
        icon = badge(pct)
        lines.append(f"<details>")
        lines.append(f"<summary>{icon} <b>{label}</b> — {pct}% ({covered}/{total} lines)</summary>\n")
        lines.append("| Arquivo | Cobertura | Linhas |")
        lines.append("|---|---|---|")

        for name, fc, ft, fp in files:
            if ft == 0:
                continue
            bar = badge(fp)
            lines.append(f"| `{name}` | {bar} {fp}% | {fc}/{ft} |")

        lines.append("\n</details>\n")

    return "\n".join(lines)


def post_comment(comment, token, repo, pr_number):
    url = f"https://api.github.com/repos/{repo}/issues/{pr_number}/comments"
    headers = {
        "Authorization": f"Bearer {token}",
        "Accept": "application/vnd.github+json",
        "X-GitHub-Api-Version": "2022-11-28",
        "Content-Type": "application/json",
    }

    list_req = urllib.request.Request(url, headers=headers)
    with urllib.request.urlopen(list_req) as r:
        existing = json.loads(r.read())

    existing_id = None
    for c in existing:
        if MARKER in c.get("body", ""):
            existing_id = c["id"]
            break

    body = json.dumps({"body": comment}).encode()
    if existing_id:
        patch_url = f"https://api.github.com/repos/{repo}/issues/comments/{existing_id}"
        req = urllib.request.Request(patch_url, data=body, headers=headers, method="PATCH")
    else:
        req = urllib.request.Request(url, data=body, headers=headers, method="POST")

    with urllib.request.urlopen(req) as r:
        print(f"Comment {'updated' if existing_id else 'posted'}: {r.status}")


if __name__ == "__main__":
    workspace = os.environ.get("GITHUB_WORKSPACE", ".")
    token     = os.environ["GITHUB_TOKEN"]
    repo      = os.environ["GITHUB_REPOSITORY"]
    pr_number = os.environ["PR_NUMBER"]

    comment = build_comment(workspace)
    print(comment)
    post_comment(comment, token, repo, pr_number)
