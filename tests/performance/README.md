# Performance test notes

The first performance target is the backend API, not the UI. Use a small baseline first, then increase concurrency gradually.

Suggested scenarios:

1. `GET /api/foods`: menu browsing;
2. `POST /api/orders`: peak-time order submission;
3. `PUT /api/admin/foods/{id}/stock`: merchant inventory updates;
4. Concurrent ordering of the last few items;
5. Repeated reads during a pickup-slot peak.

Record actual throughput, average/P95 latency, error rate, CPU, memory, database connections, and inventory correctness. Do not copy sample values into a resume without a real run.

