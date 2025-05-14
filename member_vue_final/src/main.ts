import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap/dist/js/bootstrap.bundle.js'

import {
    BButton,
    BForm,
    BFormGroup,
    BFormInput,
    BFormFile,
    BFormSelect,
    BTable
} from 'bootstrap-vue-next'

const app = createApp(App)
app.use(router)

app.component('BButton', BButton)
app.component('BForm', BForm)
app.component('BFormGroup', BFormGroup)
app.component('BFormInput', BFormInput)
app.component('BFormFile', BFormFile)
app.component('BFormSelect', BFormSelect as any)
app.component('BTable', BTable as any)

app.mount('#app')