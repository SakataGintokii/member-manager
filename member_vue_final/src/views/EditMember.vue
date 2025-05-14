<template>
  <div class="container mt-5">
    
    <h2 class="mb-4 text-center fw-bold">編輯會員</h2>
 
    <b-form @submit.prevent="handleSubmit" class="w-50 mx-auto">
      
      <b-form-group label="姓名" label-for="name">
        <b-form-input id="name" v-model="member.name" placeholder="請輸入姓名" required />
      </b-form-group>
  
      <b-form-group label="Email" label-for="email">
        <b-form-input id="email" v-model="member.email" type="email" placeholder="請輸入 Email" required />
      </b-form-group>

      <b-form-group label="電話" label-for="phone">
        <b-form-input
          id="phone"
          v-model="member.phone"
          type="tel"
          placeholder="請輸入電話"
          :state="isPhoneValid"
          required
        />
        <b-form-invalid-feedback>
          電話格式錯誤，必須是 09 開頭的 10 位數字
        </b-form-invalid-feedback>
      </b-form-group>

      <div class="text-center mt-4">
        <b-button type="submit" variant="primary">提交修改</b-button>
      </div>
    </b-form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

// 型別定義
interface MemberPayload {
  name: string
  email: string
  phone: string
}

// 路由工具：取得網址參數與跳轉功能
const router = useRouter()
const route = useRoute()

// 響應式變數：會員資料
const member = ref<MemberPayload>({
  name: '',
  email: '',
  phone: ''
})


const phonePattern = /^09\d{8}$/
const isPhoneValid = computed(() => phonePattern.test(member.value.phone))

// 頁面掛載時 → 根據 URL 中的 id 載入該會員資料
onMounted(async () => {
  const id = route.query.id
  if (!id) {
    alert('無效的會員 ID')
    router.push('/')
    return
  }

  try {
    const res = await axios.get<MemberPayload>(`http://localhost:8080/members/get/${id}`)
    member.value = res.data
  } catch (err) {
    alert('查詢會員資料失敗')
    console.error(err)
    router.push('/')
  }
})

// 提交修改表單
const handleSubmit = async () => {
  if (!isPhoneValid.value) {
    alert('請輸入有效的手機號碼(09 開頭，共 10 碼)')
    return
  }

  const id = route.query.id
  try {
    await axios.put(`http://localhost:8080/members/update/${id}`, member.value, {
      params: { user: 'admin' }
    })

    alert('修改成功！')
    router.push('/')
  } catch (error) {
    alert('修改失敗，請檢查資料')
    console.error(error)
  }
}
</script>