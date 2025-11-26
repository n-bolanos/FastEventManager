import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/main.vue'
import Attendance from '../views/attendance.vue'

const routes = [
{ path: '/attendance/:id', name: 'attendance', component: Attendance },
{ path: '/', name: 'home', component: Home },
{ path: '/:pathMatch(.*)*', component: Home}
  
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
