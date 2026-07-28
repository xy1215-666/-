<script setup>
import { ref } from 'vue'

const orders = ref([
  { id: 'CF07281201A8', name: '照烧鸡腿饭', slot: '12:00 - 12:20', status: '制作中', statusClass: 'warm', price: 18, icon: '🍱' },
  { id: 'CF07281158B2', name: '番茄鸡蛋面 × 2', slot: '昨天 12:20 - 12:40', status: '已完成', statusClass: 'done', price: 24, icon: '🍜' }
])

function cancelOrder(order) {
  uni.showModal({ title: '取消这份预约？', content: '如果餐品已经开始制作，可能无法取消。', confirmText: '仍要取消', success: ({ confirm }) => {
    if (confirm) {
      order.status = '已取消'
      order.statusClass = 'cancelled'
      uni.showToast({ title: '已提交取消申请', icon: 'none' })
    }
  } })
}
</script>

<template>
  <view class="page"><view class="header"><text class="title">我的订单</text><text class="tip">记得按照时间段来取餐哦</text></view><view class="order-list"><view v-for="order in orders" :key="order.id" class="order-card"><view class="order-top"><text class="order-id">{{ order.id }}</text><text :class="['status', order.statusClass]">{{ order.status }}</text></view><view class="order-main"><text class="dish">{{ order.icon }}</text><view><text class="name">{{ order.name }}</text><text class="slot">取餐时间：{{ order.slot }}</text></view><text class="price">¥{{ order.price }}</text></view><view class="order-actions"><text class="reorder">再来一份</text><button v-if="order.status === '制作中'" class="cancel" @click="cancelOrder(order)">取消预约</button><button v-else class="detail">查看详情</button></view></view></view><view v-if="!orders.length" class="empty">还没有订单，去点一份喜欢的餐品吧</view></view>
</template>

<style scoped>
.page { min-height: 100vh; padding: 46rpx 30rpx; background: #f7f8f5; }.header .title { display: block; font-size: 44rpx; font-weight: 700; color: #21352d; }.tip { display: block; color: #9aa7a0; font-size: 23rpx; margin-top: 12rpx; }.order-list { display: grid; gap: 20rpx; margin-top: 40rpx; }.order-card { background: #fff; border-radius: 24rpx; padding: 26rpx; box-shadow: 0 10rpx 30rpx #214b3910; }.order-top, .order-main, .order-actions { display: flex; align-items: center; justify-content: space-between; }.order-id { color: #9aa7a0; font-size: 20rpx; }.status { font-size: 22rpx; }.status.warm { color: #ce963a; }.status.done { color: #3f9b75; }.status.cancelled { color: #b7bfba; }.order-main { justify-content: initial; gap: 20rpx; padding: 28rpx 0 24rpx; border-bottom: 1rpx solid #edf1ee; }.dish { width: 96rpx; height: 96rpx; border-radius: 18rpx; display: grid; place-items: center; background: #eef6f1; font-size: 50rpx; }.name, .slot { display: block; }.name { color: #31473c; font-size: 29rpx; font-weight: 600; }.slot { color: #9aa7a0; font-size: 22rpx; margin-top: 13rpx; }.order-main .price { margin-left: auto; color: #e78d47; font-size: 30rpx; font-weight: 700; }.order-actions { padding-top: 22rpx; }.reorder { color: #9aa7a0; font-size: 22rpx; }.cancel, .detail { margin: 0; padding: 10rpx 22rpx; line-height: 1.5; border-radius: 30rpx; font-size: 21rpx; background: #f2f7f3; color: #32866b; }.cancel::after, .detail::after { border: 0; }.empty { text-align: center; color: #9aa7a0; padding-top: 180rpx; font-size: 25rpx; }
</style>
