<script setup>
import { computed, ref } from 'vue'

const categories = ['推荐', '主食', '面食', '饮品', '汤品']
const selectedCategory = ref('推荐')
const cart = ref([])
const showCart = ref(false)

const foods = ref([
  { id: 1, category: '主食', name: '照烧鸡腿饭', detail: '热乎的鸡腿配时蔬', price: 18, image: '🍱', tag: '今日推荐' },
  { id: 2, category: '面食', name: '番茄鸡蛋面', detail: '适合赶课时快速取餐', price: 12, image: '🍜', tag: '少盐可选' },
  { id: 3, category: '饮品', name: '冰柠檬茶', detail: '少糖可选', price: 6, image: '🍋', tag: '清爽' },
  { id: 4, category: '汤品', name: '紫菜蛋花汤', detail: '午餐搭配汤品', price: 5, image: '🥣', tag: '暖胃' }
])

const visibleFoods = computed(() => selectedCategory.value === '推荐' ? foods.value : foods.value.filter(item => item.category === selectedCategory.value))
const cartCount = computed(() => cart.value.reduce((sum, item) => sum + item.count, 0))
const cartTotal = computed(() => cart.value.reduce((sum, item) => sum + item.price * item.count, 0))

function addFood(food) {
  const current = cart.value.find(item => item.id === food.id)
  if (current) current.count += 1
  else cart.value.push({ ...food, count: 1 })
  uni.showToast({ title: '已加入餐篮', icon: 'none' })
}

function submitOrder() {
  if (!cart.value.length) return
  uni.showModal({ title: '确认预约', content: `共 ${cartCount.value} 件餐品，取餐时间为 12:00 - 12:20`, confirmText: '确认下单', success: ({ confirm }) => {
    if (confirm) {
      cart.value = []
      showCart.value = false
      uni.showToast({ title: '预约成功', icon: 'success' })
    }
  } })
}
</script>

<template>
  <view class="page">
    <view class="hero">
      <view class="hero-top"><view><text class="hello">周一 · 7月28日</text><text class="title">今天也要好好吃饭</text></view><view class="bell">♧<view class="dot"></view></view></view>
      <view class="pickup-card"><view class="pickup-icon">⌁</view><view><text class="pickup-label">午餐取餐时间</text><text class="pickup-time">12:00 - 12:20</text></view><text class="change">更改 ›</text></view>
    </view>

    <view class="content">
      <scroll-view class="category-scroll" scroll-x><view class="categories"><text v-for="item in categories" :key="item" :class="['category', { selected: selectedCategory === item }]" @click="selectedCategory = item">{{ item }}</text></view></scroll-view>
      <view class="section-title"><view><text>今天想吃点什么？</text><text class="sub">现做餐品，提前预约更省时间</text></view><text class="cart-button" @click="showCart = true">餐篮 <text v-if="cartCount" class="cart-count">{{ cartCount }}</text></text></view>

      <view class="food-list"><view v-for="food in visibleFoods" :key="food.id" class="food-card"><view class="food-image">{{ food.image }}</view><view class="food-info"><view class="food-name-row"><text class="food-name">{{ food.name }}</text><text class="food-tag">{{ food.tag }}</text></view><text class="food-detail">{{ food.detail }}</text><view class="food-bottom"><text class="price">¥{{ food.price }}<text class="per"> / 份</text></text><button class="add-button" @click="addFood(food)">＋</button></view></view></view></view>
      <view class="hint">今天的餐品会根据备餐情况更新，售罄后我们会及时告诉你</view>
    </view>

    <view v-if="showCart" class="sheet-mask" @click="showCart = false"><view class="cart-sheet" @click.stop><view class="sheet-head"><text>你的餐篮</text><text class="close" @click="showCart = false">×</text></view><view v-if="cart.length" class="cart-list"><view v-for="item in cart" :key="item.id" class="cart-row"><text>{{ item.image }} {{ item.name }}</text><text>×{{ item.count }}</text><text>¥{{ item.price * item.count }}</text></view><view class="cart-total"><text>合计</text><text>¥{{ cartTotal }}</text></view><button class="confirm-button" @click="submitOrder">确认预约</button></view><view v-else class="empty-cart">餐篮还是空的，先去挑一份喜欢的餐品吧</view></view></view>
  </view>
</template>

<style scoped>
.page { min-height: 100vh; background: #f7f8f5; }.hero { background: #1d7a63; padding: 48rpx 36rpx 36rpx; color: #fff; border-radius: 0 0 38rpx 38rpx; }.hero-top { display: flex; justify-content: space-between; align-items: flex-start; }.hello, .title { display: block; }.hello { font-size: 24rpx; opacity: .72; margin-bottom: 10rpx; }.title { font-size: 42rpx; font-weight: 700; letter-spacing: 1rpx; }.bell { position: relative; font-size: 42rpx; opacity: .92; }.dot { width: 12rpx; height: 12rpx; background: #f7c76f; border-radius: 50%; position: absolute; right: -2rpx; top: 4rpx; border: 3rpx solid #1d7a63; }.pickup-card { margin-top: 34rpx; display: flex; align-items: center; gap: 20rpx; background: #ffffff16; border: 1rpx solid #ffffff30; border-radius: 22rpx; padding: 22rpx; }.pickup-icon { width: 68rpx; height: 68rpx; display: grid; place-items: center; background: #ffffff1f; border-radius: 18rpx; font-size: 36rpx; }.pickup-label, .pickup-time { display: block; }.pickup-label { font-size: 22rpx; opacity: .7; }.pickup-time { margin-top: 8rpx; font-size: 30rpx; font-weight: 600; }.change { margin-left: auto; font-size: 24rpx; opacity: .86; }.content { padding: 30rpx 30rpx 60rpx; }.category-scroll { white-space: nowrap; }.categories { display: inline-flex; gap: 44rpx; }.category { color: #9aa7a0; font-size: 27rpx; padding-bottom: 14rpx; }.category.selected { color: #1d7a63; font-weight: 700; border-bottom: 5rpx solid #1d7a63; }.section-title { display: flex; justify-content: space-between; align-items: end; margin: 38rpx 0 24rpx; }.section-title > view > text { display: block; font-size: 34rpx; font-weight: 700; }.section-title .sub { color: #9aa7a0; font-size: 22rpx; font-weight: 400; margin-top: 8rpx; }.cart-button { color: #1d7a63; font-size: 25rpx; }.cart-count { display: inline-grid; place-items: center; width: 34rpx; height: 34rpx; border-radius: 50%; background: #f2ad54; color: #fff; font-size: 20rpx; margin-left: 5rpx; }.food-list { display: grid; gap: 20rpx; }.food-card { display: flex; gap: 22rpx; background: #fff; border-radius: 24rpx; padding: 22rpx; box-shadow: 0 10rpx 30rpx #214b3910; }.food-image { width: 150rpx; height: 150rpx; display: grid; place-items: center; flex: 0 0 150rpx; border-radius: 20rpx; background: #eef6f1; font-size: 72rpx; }.food-info { flex: 1; min-width: 0; padding: 4rpx 0; }.food-name-row { display: flex; align-items: center; gap: 10rpx; }.food-name { color: #21352d; font-size: 30rpx; font-weight: 600; }.food-tag { background: #fff2d4; color: #b17c27; font-size: 18rpx; padding: 5rpx 8rpx; border-radius: 7rpx; }.food-detail { display: block; color: #9aa7a0; font-size: 22rpx; margin-top: 14rpx; }.food-bottom { display: flex; align-items: center; justify-content: space-between; margin-top: 30rpx; }.price { color: #e78d47; font-size: 35rpx; font-weight: 700; }.per { font-size: 20rpx; font-weight: 400; color: #b4bab4; }.add-button { margin: 0; padding: 0; width: 58rpx; height: 58rpx; line-height: 54rpx; border-radius: 50%; color: #fff; background: #1d7a63; font-size: 36rpx; }.add-button::after { border: 0; }.hint { text-align: center; color: #b1bab5; font-size: 20rpx; padding: 38rpx 20rpx; }.sheet-mask { position: fixed; inset: 0; background: #21352d55; z-index: 10; display: flex; align-items: flex-end; }.cart-sheet { width: 100%; min-height: 300rpx; background: #fff; border-radius: 30rpx 30rpx 0 0; padding: 30rpx; }.sheet-head { display: flex; justify-content: space-between; font-size: 34rpx; font-weight: 700; }.close { color: #9aa7a0; font-size: 45rpx; font-weight: 300; }.cart-list { margin-top: 30rpx; }.cart-row { display: flex; justify-content: space-between; padding: 20rpx 0; border-bottom: 1rpx solid #edf1ee; color: #53655c; }.cart-total { display: flex; justify-content: space-between; margin: 28rpx 0; font-size: 31rpx; font-weight: 700; }.cart-total text:last-child { color: #e78d47; }.confirm-button { width: 100%; color: #fff; background: #1d7a63; border-radius: 48rpx; }.confirm-button::after { border: 0; }.empty-cart { text-align: center; color: #9aa7a0; padding: 80rpx 0; font-size: 25rpx; }
</style>
