import { createApp } from 'vue'
import { createPinia } from 'pinia'
import "./style.css"
import Toast, { POSITION } from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import App from './App.vue'
import router from './router/index.js' 

const app = createApp(App)

app.use(Toast, {
  position: POSITION.BOTTOM_CENTER,
  timeout: 4000,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 60,
})
app.use(createPinia())
app.use(router)
app.mount('#app')
