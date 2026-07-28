import http from 'k6/http'
import { check, sleep } from 'k6'

export const options = {
  vus: 2,
  duration: '10s',
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<800']
  }
}

export default function () {
  const baseUrl = __ENV.BASE_URL || 'http://localhost:8080'
  const foods = http.get(`${baseUrl}/api/foods`)
  check(foods, { 'foods status is 200': (response) => response.status === 200 })
  sleep(1)
}

