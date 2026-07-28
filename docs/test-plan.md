# Test Plan

## Scope

The first version covers student App flows, merchant/admin Web flows, Spring Boot REST APIs, MySQL data checks, and the most important order/inventory business rules.

## Test layers

| Layer | Main purpose | Tooling |
| --- | --- | --- |
| Functional | Validate user journeys and business rules | Manual cases, screenshots, defect log |
| API | Fast, repeatable service verification | pytest, requests, JSON assertions |
| Database | Verify state changes and consistency | MySQL SQL checks |
| Web UI | Verify merchant/admin critical paths | Playwright |
| Mobile UI | Verify Android critical paths | Appium, pytest |
| Performance | Establish a small reproducible baseline | k6/JMeter |
| CI | Run release checks consistently | GitHub Actions |

## Entry criteria

- Backend is reachable;
- Seed data exists;
- If a permission module is enabled, test accounts and roles are available;
- API base URL is configured;
- Android emulator is available for mobile tests.

## Exit criteria

- No open blocker/critical defects for the release scope;
- API smoke suite passes;
- Core App and Web flows pass;
- Database checks pass for order/inventory changes;
- A test report is archived for the executed scope.

## Important cases

- The last item is ordered concurrently (planned integration scenario);
- Payment succeeds while the client loses network (planned payment-module scenario);
- Cancelling an order restores inventory exactly once;
- A merchant cannot access another merchant's orders (planned permission scenario);
- A full pickup slot is rejected with a useful message (planned capacity scenario);
- Repeated submit clicks do not create duplicate orders (planned idempotency scenario).
