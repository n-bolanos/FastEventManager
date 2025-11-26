import { createApp } from 'vue'
import { createPinia } from 'pinia'
import "./style.css"
import App from './views/tempApp.vue'
import router from './router/index.js' 

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.mount('#app')
