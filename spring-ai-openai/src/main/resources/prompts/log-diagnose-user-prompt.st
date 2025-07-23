1.[Role & Context]
You are currently investigating error logs from a production environment. The goal is to identify root causes, assess the scope of impact, and provide remediation recommendations.

2.[Log Input]
Here is the error log content (provided as raw text):

<error_log>

3.[Analysis Process (Chain‑of‑Thought)]
Please think step by step, performing the following sub‑steps in order:
a) Summarize the log: extract primary error types, timestamps, module locations, and error messages.
b) Assess severity: distinguish between Error/Warn/Exception and evaluate impact on system stability and users.
c) Pattern recognition: check for recurring patterns, high‑frequency errors, and common exceptions.
d) Context correlation: look for hints related to deployment, configuration, or recent changes.
e) Historical case matching: if the log resembles a known past issue, link it to historical experiences.
f) Recommendations: include short‑term mitigations (e.g., service restart) and long‑term fixes (code patches, config updates, capacity expansion, etc.).

4.[Output Format]
Return the result in JSON format with the following structure:

{
  "summary": { "error_types": [...], "time_range": "...", "module": "..." },
  "severity": { "level": "Low/Medium/High/Critical", "rationale": "..." },
  "patterns": [ { "message": "...", "count": X } ],
  "context_clues": [ "...deployment info...", "...config changes..." ],
  "similar_cases": [ { "past_log_snippet": "...", "reference": "..." } ],
  "recommendations": { "short_term": [...], "long_term": [...] },
  "confidence": "High/Medium/Low",
  "verification_notes": [ "...any uncertainties or missing info..." ]
}
Respond only with valid JSON, no explanatory text