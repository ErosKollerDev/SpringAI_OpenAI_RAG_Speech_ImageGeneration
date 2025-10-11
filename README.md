# Spring AI — Learning Playground

A curated, hands-on collection of Spring Boot examples showcasing the most useful Spring AI features: from first chat calls to multi‑provider setup, prompt templates, advisors, RAG, MCP, testing, and a full observability stack (metrics, traces, dashboards).

This repository is organized as independent modules you can run and explore one by one. Each module is a minimal, focused example.


## At a glance
- Language/Stack: Java + Spring Boot + Spring AI
- LLMs: OpenAI (and pluggable others), local models via Ollama (optional in some modules)
- Data: In‑memory and H2, Qdrant for vector search (via Docker)
- Observability: Prometheus, Grafana, Jaeger, OpenTelemetry
- Orchestration: Docker Compose for infra services


## Modules
The project contains the following modules at the repository root:

1. 01_OpenAI — Your first Spring AI chat endpoint
   - A minimal controller exposing GET /api/chat?message=...
   - Uses Spring AI ChatClient with your configured provider (default OpenAI).

2. 02_OpenAI — Variation of the basics
   - Mirrors 01 with small iterative changes; useful to compare approaches and defaults.

3. 03_MultiAIProviders — Multiple providers
   - Shows how to switch providers (e.g., OpenAI, Ollama) via Spring configuration and profiles.

4. 04_MsgRoles — Messages and roles
   - Demonstrates user/system/assistant roles and how they affect responses.

5. 05_Templates — Prompt templates
   - Introduces prompt templating and resource‑backed system prompts.

6. 06_Advisors — Advisors and memory
   - Demonstrates advisors like SimpleLoggerAdvisor and message chat memory.
   - Great place to learn how to add cross‑cutting concerns (logging, policies, token audits).

7. 07_RAG — Retrieval Augmented Generation
   - Shows how to retrieve external knowledge and ground model responses.
   - Often paired with a vector store like Qdrant.

8. 08_mcpclient — MCP Client
   - Example of a Model Context Protocol (MCP) client interacting with external tools/servers.

9. 08_mcpserverremote — MCP Server (Remote)
   - A remote MCP server exposing capabilities consumable by MCP clients.

10. 08_mcpserverstdio — MCP Server (STDIO)
    - A STDIO‑based MCP server variant.

11. 09_unit_test — Testing AI features
    - Examples of testing patterns for Spring AI components.

12. 10_observability_metrics_monitoring_tracing — Observability
    - A complete example with metrics, tracing, memory, and RAG + web search.
    - Notable pieces:
      - Endpoints: Web search controller at GET /api/v1/web-search/web-search requires headers and query params.
      - Memory: JDBC ChatMemory with H2 file database chatmemory.* (see application.yaml).
      - Vector store: Qdrant collection erosvectorstore on localhost:6334.
      - OpenTelemetry: OTLP exporter to http://localhost:4317.
      - Actuator: exposes health, metrics, prometheus.
      - Prometheus and Grafana via Docker Compose for dashboards.


## Important files at the repo root
- compose.yaml — Spins up Qdrant, Prometheus, Grafana, and Jaeger.
- prometheus-config.yml — Scrape config pointing to your Spring Boot Actuator metrics.
- chatmemory.mv.db / chatmemory.trace.db — H2 files used by the observability module for chat memory.
- From+Java+Dev+to+AI+Engineer-+Spring+AI+Fast+Track.pdf — Slide deck/notes.


## Prerequisites
- Java 17+
- Maven 3.9+
- An AI provider API key (e.g., OpenAI) as environment variable OPENAI_API_KEY
- Docker Desktop or compatible engine (for Qdrant/Prometheus/Grafana/Jaeger)

Optional (when experimenting with local models):
- Ollama installed and running (if you enable the commented Ollama sections in application.yaml)


## Running a module
Every module is a standalone Spring Boot app. Example with 01_OpenAI:

- In a terminal at the repo root:
  - export OPENAI_API_KEY=your_key_here
  - cd 01_OpenAI
  - mvn spring-boot:run
- Then call: curl "http://localhost:8080/api/chat?message=Hello"

Ports may vary per module; if a module uses 8080 and it’s busy, change server.port in application properties or export SERVER_PORT.


## Observability stack (module 10)
This module shows how to add real metrics, logs, and traces to your AI application.

What’s included:
- Prometheus (metrics) at http://localhost:9090
- Grafana (dashboards) at http://localhost:3000 (default admin/admin on first run)
- Jaeger (traces) at http://localhost:16686
- Qdrant (vector DB) at http://localhost:6333, gRPC at 6334

How to start infra:
- docker compose up -d

Spring Boot app highlights:
- application.yaml configures:
  - Spring AI OpenAI API key via OPENAI_API_KEY
  - JDBC chat memory in H2 (file: ./chatmemory)
  - Vector store: Qdrant at localhost:6334, collection erosvectorstore
  - Actuator: health, metrics, prometheus
  - OpenTelemetry exporter to OTLP at localhost:4317 (Jaeger collector)
  - Log colorized pattern for easy terminal scanning

Key endpoints in this module:
- GET /actuator/health — service health
- GET /actuator/metrics — list of metrics
- GET /actuator/prometheus — Prometheus scrape endpoint
- GET /api/v1/web-search/web-search?message=... with header username: <your-id>
  - This calls a ChatClient wired with advisors and memory, and retrieves information through web search + RAG components.


## Docker Compose services
From compose.yaml:
- qdrant/qdrant:latest
  - Ports: 6333 (HTTP), 6334 (gRPC)
- prom/prometheus:v2.50.1
  - Port: 9090, mounts ./prometheus-config.yml
- grafana/grafana:11.0.0
  - Port: 3000, volume grafana-storage
- jaegertracing/all-in-one:1.35.0
  - Ports: 16686 (UI), 4317 (OTLP gRPC)

Start/stop:
- docker compose up -d
- docker compose down


## Environment variables
- OPENAI_API_KEY — your OpenAI key
- Optional: SPRING_PROFILES_ACTIVE to switch providers or configurations in modules that use profiles


## Tips and troubleshooting
- If you see 401/403 from provider: verify OPENAI_API_KEY is exported in the same shell that runs the module.
- If /actuator/prometheus is empty: ensure management.endpoints.web.exposure.include includes prometheus (module 10 does).
- If Qdrant isn’t reachable: docker compose up and confirm 6333/6334 are open, and vectorstore config matches.
- For traces not appearing in Jaeger: confirm OpenTelemetry exporter endpoint is http://localhost:4317 and the Jaeger container has COLLECTOR_OTLP_ENABLED=true (already set).


## Folder cheatsheet
- 01_.. to 10_.. — individual learning modules, each a Spring Boot app
- grafana-storage — Docker volume directory used by Grafana
- mcp_dir — assets/temp workspace for MCP samples


## Contributing / extending
- Add a new module by copying one of the existing ones and adjusting pom.xml and application properties.
- Keep examples minimal and focused: a single feature per module is ideal.


## License
This repository is intended for learning and demo purposes. Please review/choose the appropriate license for your needs if distributing.
