<script setup>
import { computed, onMounted, ref } from 'vue'
import { api } from './api'

const activeView = ref('overview')
const toast = ref('')
const loading = ref(false)
const dataSource = ref('演示数据')

const navItems = [
  { key: 'overview', icon: '⌂', label: '今日概览' },
  { key: 'menu', icon: '▦', label: '菜单与库存' },
  { key: 'orders', icon: '□', label: '订单处理' },
  { key: 'feedback', icon: '♡', label: '反馈记录' }
]

const foods = ref([
  { id: 1, name: '照烧鸡腿饭', category: '主食', description: '热乎的鸡腿配时蔬', price: 18, stock: 24, available: true, imageUrl: '🍱' },
  { id: 2, name: '番茄鸡蛋面', category: '面食', description: '适合赶课时快速取餐', price: 12, stock: 18, available: true, imageUrl: '🍜' },
  { id: 3, name: '冰柠檬茶', category: '饮品', description: '少糖可选', price: 6, stock: 36, available: true, imageUrl: '🍋' },
  { id: 4, name: '紫菜蛋花汤', category: '汤品', description: '午餐搭配汤品', price: 5, stock: 12, available: true, imageUrl: '🥣' }
])

const orders = ref([
  { id: 1001, orderNo: 'CF07281201A8', studentName: '林同学', pickupSlot: '11:40-12:00', foodName: '照烧鸡腿饭', quantity: 1, totalAmount: 18, status: 'PREPARING', createdAt: '2026-07-28 11:21' },
  { id: 1002, orderNo: 'CF07281158B2', studentName: '陈同学', pickupSlot: '12:00-12:20', foodName: '番茄鸡蛋面', quantity: 2, totalAmount: 24, status: 'READY', createdAt: '2026-07-28 11:08' },
  { id: 1003, orderNo: 'CF07281144C9', studentName: '周同学', pickupSlot: '12:20-12:40', foodName: '冰柠檬茶', quantity: 1, totalAmount: 6, status: 'COMPLETED', createdAt: '2026-07-28 10:54' },
  { id: 1004, orderNo: 'CF07281131D4', studentName: '赵同学', pickupSlot: '12:00-12:20', foodName: '紫菜蛋花汤', quantity: 1, totalAmount: 5, status: 'CANCELLED', createdAt: '2026-07-28 10:41' }
])

const statusMap = {
  PREPARING: ['制作中', 'status-warm'],
  READY: ['待取餐', 'status-green'],
  COMPLETED: ['已完成', 'status-muted'],
  CANCELLED: ['已取消', 'status-red']
}

const todayRevenue = computed(() => orders.value.filter(item => item.status !== 'CANCELLED').reduce((sum, item) => sum + item.totalAmount, 0))
const activeOrders = computed(() => orders.value.filter(item => ['PREPARING', 'READY'].includes(item.status)).length)
const lowStockFoods = computed(() => foods.value.filter(item => item.stock <= 12).length)

function showToast(message) {
  toast.value = message
  window.clearTimeout(showToast.timer)
  showToast.timer = window.setTimeout(() => { toast.value = '' }, 2600)
}

function statusText(status) {
  return statusMap[status]?.[0] || status
}

function statusClass(status) {
  return statusMap[status]?.[1] || 'status-muted'
}

async function loadData() {
  loading.value = true
  try {
    const [foodResponse, orderResponse] = await Promise.all([api.foods(), api.orders()])
    if (Array.isArray(foodResponse) && foodResponse.length) foods.value = foodResponse
    // Keep the demo orders when the backend is healthy but has no seeded orders yet.
    if (Array.isArray(orderResponse) && orderResponse.length) orders.value = orderResponse
    dataSource.value = '后端接口'
  } catch {
    dataSource.value = '演示数据'
  } finally {
    loading.value = false
  }
}

async function adjustStock(food) {
  const nextStock = window.prompt(`调整「${food.name}」库存`, String(food.stock))
  if (nextStock === null) return
  const value = Number(nextStock)
  if (!Number.isInteger(value) || value < 0) {
    showToast('库存必须是大于等于 0 的整数')
    return
  }
  try {
    const updated = await api.updateStock(food.id, value)
    Object.assign(food, updated)
    dataSource.value = '后端接口'
    showToast('库存已更新')
  } catch {
    food.stock = value
    food.available = value > 0
    showToast('当前使用演示数据，已在页面中更新')
  }
}

onMounted(loadData)
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-mark">食</div>
        <div>
          <strong>校园餐饮</strong>
          <span>运营工作台</span>
        </div>
      </div>

      <div class="workspace-label">WORKSPACE</div>
      <nav class="nav-list">
        <button v-for="item in navItems" :key="item.key" class="nav-item" :class="{ active: activeView === item.key }" @click="activeView = item.key">
          <span class="nav-icon">{{ item.icon }}</span>
          <span>{{ item.label }}</span>
        </button>
      </nav>

      <div class="sidebar-bottom">
        <div class="help-card">
          <span class="help-icon">?</span>
          <div>
            <strong>需要帮忙？</strong>
            <small>查看操作指引</small>
          </div>
        </div>
        <div class="profile-mini">
          <div class="avatar">王</div>
          <div><strong>王老师</strong><small>运营管理员</small></div>
          <span class="more">•••</span>
        </div>
      </div>
    </aside>

    <main class="main-content">
      <header class="topbar">
        <div>
          <p class="eyebrow">MONDAY, JULY 28, 2026</p>
          <h1>{{ navItems.find(item => item.key === activeView)?.label }}</h1>
        </div>
        <div class="top-actions">
          <span class="data-badge"><i></i>{{ dataSource }}</span>
          <button class="icon-button" title="刷新数据" :disabled="loading" @click="loadData">↻</button>
          <button class="notification" title="通知">♧<b>2</b></button>
        </div>
      </header>

      <section v-if="activeView === 'overview'" class="content-section">
        <div class="welcome-row">
          <div>
            <h2>午餐高峰前，一切准备得怎么样？</h2>
            <p>这里是今天的经营情况。先看一眼待处理订单，再去确认库存。</p>
          </div>
          <button class="primary-button" @click="activeView = 'orders'">查看待处理订单 <span>→</span></button>
        </div>

        <div class="stats-grid">
          <div class="stat-card accent-blue"><div class="stat-top"><span>今日订单</span><span class="stat-icon">↗</span></div><strong>{{ orders.length }}<small> 单</small></strong><p><em>+12%</em> 较昨日同时段</p></div>
          <div class="stat-card accent-green"><div class="stat-top"><span>待处理订单</span><span class="stat-icon">◷</span></div><strong>{{ activeOrders }}<small> 单</small></strong><p><em>请在取餐前完成备餐</em></p></div>
          <div class="stat-card accent-yellow"><div class="stat-top"><span>今日营业额</span><span class="stat-icon">¥</span></div><strong>¥{{ todayRevenue }}<small>.00</small></strong><p><em>+8.4%</em> 较昨日同时段</p></div>
          <div class="stat-card accent-purple"><div class="stat-top"><span>库存提醒</span><span class="stat-icon">!</span></div><strong>{{ lowStockFoods }}<small> 项</small></strong><p><em>建议午餐前补充</em></p></div>
        </div>

        <div class="grid-two">
          <section class="panel chart-panel">
            <div class="panel-heading"><div><h3>今日订单趋势</h3><p>过去 6 个时间段的订单量</p></div><button class="ghost-button">今天⌄</button></div>
            <div class="chart-area"><div class="chart-y"><span>40</span><span>30</span><span>20</span><span>10</span><span>0</span></div><div class="bars"><div v-for="bar in [35, 44, 58, 72, 93, 66]" :key="bar" class="bar-wrap"><div class="bar" :style="{ height: `${bar}%` }"><span>{{ Math.round(bar / 2) }}</span></div></div></div></div>
            <div class="chart-x"><span>08:00</span><span>09:00</span><span>10:00</span><span>11:00</span><span>12:00</span><span>13:00</span></div>
          </section>

          <section class="panel pickup-panel">
            <div class="panel-heading"><div><h3>取餐时间段</h3><p>午餐时段容量使用情况</p></div><button class="text-button">管理时间段 →</button></div>
            <div class="slot-row"><div><strong>11:40 - 12:00</strong><small>还可接单</small></div><div class="progress"><i style="width: 76%"></i></div><b>38/50</b></div>
            <div class="slot-row"><div><strong>12:00 - 12:20</strong><small>接近满额</small></div><div class="progress warning"><i style="width: 92%"></i></div><b>46/50</b></div>
            <div class="slot-row"><div><strong>12:20 - 12:40</strong><small>还可接单</small></div><div class="progress"><i style="width: 52%"></i></div><b>26/50</b></div>
            <div class="slot-row"><div><strong>12:40 - 13:00</strong><small>还可接单</small></div><div class="progress"><i style="width: 34%"></i></div><b>17/50</b></div>
          </section>
        </div>

        <section class="panel recent-panel">
          <div class="panel-heading"><div><h3>最近订单</h3><p>需要优先关注的订单</p></div><button class="text-button" @click="activeView = 'orders'">查看全部 →</button></div>
          <div class="table-wrap"><table><thead><tr><th>订单号</th><th>学生</th><th>餐品</th><th>取餐时间</th><th>金额</th><th>状态</th></tr></thead><tbody><tr v-for="order in orders.slice(0, 4)" :key="order.id"><td class="order-no">{{ order.orderNo }}</td><td>{{ order.studentName }}</td><td>{{ order.foodName }} ×{{ order.quantity }}</td><td>{{ order.pickupSlot }}</td><td>¥{{ order.totalAmount }}</td><td><span class="status-pill" :class="statusClass(order.status)"><i></i>{{ statusText(order.status) }}</span></td></tr></tbody></table></div>
        </section>
      </section>

      <section v-else-if="activeView === 'menu'" class="content-section">
        <div class="page-heading"><div><h2>菜单与库存</h2><p>餐品库存会在学生下单和取消时同步变化。</p></div><button class="primary-button" @click="showToast('新增餐品流程将在下一版本开放')">＋ 新增餐品</button></div>
        <section class="panel menu-panel"><div class="table-wrap"><table><thead><tr><th>餐品</th><th>分类</th><th>价格</th><th>库存</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="food in foods" :key="food.id"><td><div class="food-cell"><span>{{ food.imageUrl }}</span><div><strong>{{ food.name }}</strong><small>{{ food.description }}</small></div></div></td><td>{{ food.category }}</td><td>¥{{ food.price }}</td><td><strong :class="{ 'low-stock': food.stock <= 12 }">{{ food.stock }}</strong></td><td><span class="status-pill" :class="food.available ? 'status-green' : 'status-red'"><i></i>{{ food.available ? '销售中' : '已售罄' }}</span></td><td><button class="small-button" @click="adjustStock(food)">调整库存</button></td></tr></tbody></table></div></section>
      </section>

      <section v-else-if="activeView === 'orders'" class="content-section">
        <div class="page-heading"><div><h2>订单处理</h2><p>先处理制作中订单，再关注退款和异常订单。</p></div><button class="ghost-button" @click="loadData">刷新订单 ↻</button></div>
        <section class="panel menu-panel"><div class="table-wrap"><table><thead><tr><th>订单号</th><th>学生</th><th>餐品</th><th>取餐时间</th><th>金额</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="order in orders" :key="order.id"><td class="order-no">{{ order.orderNo }}</td><td>{{ order.studentName }}</td><td>{{ order.foodName }} ×{{ order.quantity }}</td><td>{{ order.pickupSlot }}</td><td>¥{{ order.totalAmount }}</td><td><span class="status-pill" :class="statusClass(order.status)"><i></i>{{ statusText(order.status) }}</span></td><td><button class="small-button" @click="showToast(`已打开订单 ${order.orderNo}`)">查看详情</button></td></tr></tbody></table></div></section>
      </section>

      <section v-else class="content-section">
        <div class="page-heading"><div><h2>反馈记录</h2><p>把学生的反馈留在流程里，方便商家复盘。</p></div></div>
        <section class="feedback-list"><article class="feedback-card"><span class="feedback-avatar">林</span><div><div class="feedback-head"><strong>林同学</strong><small>今天 10:18</small></div><p>12:00 的取餐时间快满了，能不能提醒我选 12:20？</p><span class="feedback-tag">取餐体验</span></div></article><article class="feedback-card"><span class="feedback-avatar blue">陈</span><div><div class="feedback-head"><strong>陈同学</strong><small>昨天 18:42</small></div><p>番茄鸡蛋面很好吃，希望可以加一个少盐选项。</p><span class="feedback-tag">餐品建议</span></div></article></section>
      </section>

      <div v-if="toast" class="toast">{{ toast }}</div>
    </main>
  </div>
</template>
