import { Dialog, Notify, Quasar } from 'quasar'
import App from './App.vue'
import './style.scss'
import 'quasar/dist/quasar.css'
import 'quasar/src/css/index.sass'
import '@quasar/extras/material-icons/material-icons.css'

import { createApp } from 'vue'

const app = createApp(App)

app.use(Quasar, {
  plugins: { Notify, Dialog },
})
app.mount('#app')
