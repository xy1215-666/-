# 校园餐饮预约与自提平台质量工程项目

这是一个面向测试工程师/测试开发岗位的可运行练习项目：学生通过 Android App 预约餐品，商家和管理员通过 Vue3 Web 后台处理菜单、库存和订单。项目重点不是堆砌业务，而是把功能测试、接口自动化、数据库校验、Web/App 自动化、性能测试和 CI/CD 质量门禁串成一条可追溯的质量流程。

## 项目定位

- App 端：uni-app + Vue3，面向学生用户；
- Web 端：Vue3 + Vite + Element Plus 风格组件，面向商家/管理员；
- 后端：Spring Boot + Spring Data JPA 分层；
- 数据：MySQL，预留 Redis、RabbitMQ 扩展；
- 接口自动化：Python + pytest + requests；
- Web 自动化：Playwright；
- 移动端自动化：Appium + pytest；
- 性能测试：JMeter/k6；
- CI/CD：GitHub Actions。

## 真实业务场景

学生午休时间有限，需要提前预约餐品和取餐时间；商家需要按时间段准备订单，并在售罄、延迟备餐、取消订单等情况下给出清晰反馈。项目重点覆盖这些真实场景：

- 取餐时间段容量控制；
- 最后一份餐品被多人同时购买；
- 重复点击支付；
- 支付成功但客户端网络中断；
- 订单取消后的库存恢复；
- 商家临时售罄和取消订单后的库存回补；
- App 切后台、弱网、断网和重新加载；
- 商家和管理员的数据权限隔离。

## 目录结构

```text
campus-food-quality-platform/
├── backend/                 # Spring Boot 后端
├── web-admin/               # Vue3 Web 管理端
├── mobile-app/              # uni-app Vue3 Android App 端
├── tests/
│   ├── api/                 # pytest 接口自动化
│   ├── web/                 # Playwright Web 自动化
│   ├── mobile/              # Appium 移动端自动化
│   ├── performance/         # k6/JMeter 性能脚本
│   └── database/            # SQL 校验和测试数据
├── docs/                    # 需求、测试计划、架构说明
├── .github/workflows/       # CI/CD 流水线
└── docker-compose.yml       # MySQL、Redis、RabbitMQ
```

## 本地启动

### 1. 启动基础设施

```bash
docker compose up -d mysql redis rabbitmq
```

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

默认开发环境使用 H2，访问 `http://localhost:8080`。连接 MySQL 时可使用 `mysql` profile，并配置 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD`。

### 3. 启动 Web 管理端

```bash
cd web-admin
npm install
npm run dev
```

访问 `http://localhost:5173`。

### 4. 启动 App 端

使用 HBuilderX 或 uni-app CLI 打包 Android APK，再使用 Android Emulator 安装。当前 App 先用本地演示数据跑通预约交互，接口联调作为后续迭代；移动端自动化脚本通过 `APP_PATH` 指向实际安装包。

## 测试执行

### 接口自动化

```bash
cd tests/api
python -m venv .venv
\.venv\Scripts\activate
pip install -r requirements.txt
pytest -m smoke --base-url http://localhost:8080
pytest --base-url http://localhost:8080 --alluredir=allure-results
```

### Web 自动化

```bash
cd tests/web
npm install
npx playwright install chromium
npm test
```

### 移动端自动化

```bash
cd tests/mobile
pip install -r requirements.txt
pytest -m smoke
```

执行前需要启动 Appium Server、Android Emulator，并设置 `APP_PATH` 或连接已安装的测试包。

### 性能测试

```bash
k6 run tests/performance/k6-smoke.js
```

性能脚本默认只做小流量基线验证，正式压测前应根据机器配置调整并发量，并记录真实的响应时间、吞吐量和错误率。

## CI/CD 质量门禁

GitHub Actions 会执行：

1. 后端单元测试；
2. Web 构建；
3. App H5 构建；
4. 接口冒烟测试；
5. Web 自动化测试；
6. 核心用例失败时阻断流水线。

移动端自动化和性能测试需要 Android Emulator 或独立 Runner，默认保留为手动/夜间任务，避免普通提交被环境问题阻断。

## 当前状态

项目包含可运行的 MVP 页面、Spring Boot 核心接口、接口/数据库/性能/App/Web 测试脚本和 CI 配置。移动端和性能脚本需要真实设备或独立 Runner 执行；具体测试数量、通过率、性能指标只有在真实执行后才能写入项目简历，不在 README 中虚构数据。
