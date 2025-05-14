<template>
  <div class="container mt-5">

    <h2 class="mb-4 text-center fw-bold">新增會員</h2>

    <b-form @submit.prevent="handleSubmit" class="w-50 mx-auto">

      <b-form-group label="姓名" label-for="name">
        <b-form-input id="name" v-model="member.name" placeholder="請輸入姓名" required />
      </b-form-group>

      <b-form-group label="Email" label-for="email">
        <b-form-input id="email" v-model="member.email" type="email" placeholder="請輸入 Email" required />
      </b-form-group>

      <b-form-group label="電話" label-for="phone">
        <b-form-input id="phone" v-model="member.phone" type="tel" placeholder="請輸入電話" :state="isPhoneValid" required />
        <b-form-invalid-feedback>
          電話號碼必須是 09 開頭的 10 位數字
        </b-form-invalid-feedback>
      </b-form-group>

      <div class="text-center mt-4">
        <b-button type="submit" variant="primary">提交</b-button>
      </div>
    </b-form>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

//  定義會員欄位
const member = ref({
  name: '',
  email: '',
  phone: ''
})

const phonePattern = /^09\d{8}$/
const isPhoneValid = computed(() => phonePattern.test(member.value.phone))

//  提交表單（含前端驗證）
const handleSubmit = async () => {
  if (!isPhoneValid.value) {
    alert('請輸入有效的手機號碼(09 開頭，共 10 碼)')
    return
  }

  try {
    await axios.post('http://localhost:8080/members/create', member.value, {
      params: { user: 'admin' }
    })
    alert('新增成功！')
    router.push('/')
  } catch (error) {
    alert('新增失敗，請檢查資料')
    console.error(error)
  }
}
</script>