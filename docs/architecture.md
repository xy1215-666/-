# Architecture Notes

## Why a modular monolith first?

The project uses Spring Boot modules rather than immediately splitting into many deployable services. This keeps the code understandable while preserving boundaries between users, menus, orders, inventory, pickup slots, payments, and feedback. Redis, RabbitMQ, and containerized deployment are extension points for later iterations.

## Quality workflow

```text
Requirement -> Test analysis -> Manual cases -> API/UI/App automation
    -> Database checks -> Performance baseline -> CI quality gate
```

## Quality risks to track

- Concurrent inventory deduction;
- Duplicate order/payment requests;
- Order cancellation and inventory restoration;
- Payment callback idempotency;
- Pickup-slot capacity limits;
- Merchant/admin permission isolation;
- Weak-network recovery on mobile;
- Inconsistent status between App, Web, and backend.

