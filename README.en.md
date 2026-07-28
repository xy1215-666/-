# Campus Food Reservation & Pickup Quality Engineering Project

This is a practical quality-engineering project for QA and test-development roles. Students reserve meals and pickup slots through an Android app, while merchants and administrators manage menus, inventory, and orders through a Vue 3 web console. The main goal is to connect functional testing, API automation, database verification, Web/App automation, performance testing, and CI/CD quality gates into one traceable workflow.

## Project focus

- Mobile app: uni-app + Vue 3 for students;
- Web console: Vue 3 + Vite with an Element Plus style system for merchants and admins;
- Backend: Spring Boot + Spring Data JPA with a layered service design;
- Data: MySQL, with Redis and RabbitMQ extension points;
- API automation: Python + pytest + requests;
- Web automation: Playwright;
- Mobile automation: Appium + pytest;
- Performance testing: JMeter/k6;
- CI/CD: GitHub Actions.

## Real-world scenarios

Students have limited lunch time and need a reliable way to reserve meals and pickup slots. Merchants need to prepare orders by time slot and handle sold-out items, delayed preparation, and cancellations with clear feedback. The project focuses on scenarios such as:

- Pickup-slot capacity control;
- Two users competing for the last item;
- Repeated payment clicks;
- Payment success followed by a client-side network interruption;
- Restoring inventory after order cancellation;
- Merchant-initiated sold-out and inventory-restoration flows;
- Backgrounding the app, weak networks, offline recovery, and reloads;
- Merchant/admin data isolation.

## Repository layout

```text
campus-food-quality-platform/
├── backend/                 # Spring Boot backend
├── web-admin/               # Vue 3 web console
├── mobile-app/              # uni-app Vue 3 Android app
├── tests/
│   ├── api/                 # pytest API automation
│   ├── web/                 # Playwright web automation
│   ├── mobile/              # Appium mobile automation
│   ├── performance/         # k6/JMeter scripts
│   └── database/            # SQL checks and test data
├── docs/                    # Requirements, test plan, architecture notes
├── .github/workflows/       # CI/CD pipelines
└── docker-compose.yml       # MySQL, Redis, RabbitMQ
```

## Local setup

### 1. Start infrastructure

```bash
docker compose up -d mysql redis rabbitmq
```

### 2. Start the backend

```bash
cd backend
mvn spring-boot:run
```

The default development profile uses H2 and serves on `http://localhost:8080`. To use MySQL, activate the `mysql` profile and set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

### 3. Start the web console

```bash
cd web-admin
npm install
npm run dev
```

Open `http://localhost:5173`.

### 4. Run the mobile app

Build an Android APK with HBuilderX or the uni-app CLI, then install it on an Android emulator. The current App uses local demo data to keep the reservation interaction easy to run; API wiring is a follow-up iteration. Point the mobile smoke tests at the real package through `APP_PATH`.

## Test execution

### API automation

```bash
cd tests/api
python -m venv .venv
\.venv\Scripts\activate
pip install -r requirements.txt
pytest -m smoke --base-url http://localhost:8080
pytest --base-url http://localhost:8080 --alluredir=allure-results
```

### Web automation

```bash
cd tests/web
npm install
npx playwright install chromium
npm test
```

### Mobile automation

```bash
cd tests/mobile
pip install -r requirements.txt
pytest -m smoke
```

Start an Appium server and Android emulator first. Set `APP_PATH` or use an already-installed test package.

### Performance testing

```bash
k6 run tests/performance/k6-smoke.js
```

The included script is a low-load baseline. Adjust concurrency for real load testing and record actual response time, throughput, and error-rate measurements.

## CI/CD quality gates

GitHub Actions runs:

1. Backend unit tests;
2. Web build;
3. App H5 build;
4. API smoke tests;
5. Web automation;
6. Pipeline blocking when critical checks fail.

Mobile automation and performance testing are kept as manual/nightly jobs because they require an Android emulator or a dedicated runner.

## Current status

The repository contains a runnable MVP UI, Spring Boot core APIs, API/database/performance/App/Web test scripts, and a CI configuration. Mobile and performance scripts require a real device or dedicated runner. Test counts, pass rates, and performance numbers must be measured by real execution before being added to a resume; no fabricated metrics are included here.
