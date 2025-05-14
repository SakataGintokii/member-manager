<template>
  <div class="container mt-5">
    <h2 class="mb-4 text-center fw-bold">會員管理系統</h2>

    <!-- 新增 + 下載 -->
    <div class="d-flex gap-2 mb-3 align-items-center">
      <b-button variant="primary" @click="router.push('/add')">新增會員</b-button>

      <!-- 下載檔案表單 -->
      <b-form @submit.prevent="submitDownload" class="d-flex gap-2 align-items-center">
        <b-form-select v-model="selectedDownloadFile" :options="downloadFileList" class="w-auto" placeholder="請選擇檔案" />
        <b-button type="submit" variant="outline-success" :disabled="!selectedDownloadFile">下載</b-button>
      </b-form>
    </div>

    <!-- 上傳 + 查詢 -->
    <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
      <!-- 上傳檔案表單 -->
      <b-form @submit.prevent="uploadData" class="d-flex flex-row gap-2 align-items-center">
        <b-form-file v-model="selectedFile" accept="*/*" browse-text="選擇檔案" class="w-auto" />
        <b-button type="submit" variant="success">上傳</b-button>
      </b-form>

      <!-- 查詢欄 -->
      <b-form-input v-model="searchKeyword" placeholder="請輸入關鍵字查詢" class="w-25" />
    </div>

    <!-- 表格 -->
    <b-table :items="filteredMembers" :fields="fields" striped hover responsive="sm" thead-class="text-center">
      <template #cell(actions)="row">
        <div class="text-center">
          <b-button size="sm" variant="info" class="me-2" @click="editMember(row.item)">編輯</b-button>
          <b-button size="sm" variant="danger" @click="deleteMember(row.item)">刪除</b-button>
        </div>
      </template>
    </b-table>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

interface Member {
  memberId: number
  name: string
  email: string
  phone: string
}

const members = ref<Member[]>([])
const searchKeyword = ref('')
const selectedFile = ref<File | null>(null)
const selectedDownloadFile = ref('')
const downloadFileList = ref<string[]>([])

const fields = [
  { key: 'memberId', label: 'ID', thClass: 'text-center uniform-col', tdClass: 'text-center uniform-col' },
  { key: 'name', label: '姓名', thClass: 'text-center uniform-col name-offset', tdClass: 'text-center uniform-col name-offset' },
  { key: 'email', label: 'Email', thClass: 'text-center uniform-col email-offset', tdClass: 'text-center uniform-col email-offset' },
  { key: 'phone', label: '電話', thClass: 'text-center uniform-col', tdClass: 'text-center uniform-col' },
  { key: 'actions', label: '操作', thClass: 'text-center uniform-col', tdClass: 'text-center uniform-col' }
]

const filteredMembers = computed(() =>
  members.value.filter(member =>
    member.name.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    member.email.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    member.phone.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
)

const fetchMembers = async () => {
  const res = await axios.get<Member[]>('http://localhost:8080/members/getall')
  members.value = res.data
}

const fetchDownloadList = async () => {
  try {
    const res = await axios.get<string[]>('http://localhost:8080/file/list')
    console.log("下載清單", res.data)
    downloadFileList.value = res.data
  } catch (e) {
    alert('無法取得下載檔案清單')
    console.error(e)
  }
}

const uploadData = async () => {
  if (!selectedFile.value) return
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  try {
    await axios.post('http://localhost:8080/file/upload', formData)
    alert('上傳成功')
    await fetchMembers()
    await fetchDownloadList()
  } catch (e) {
    alert('上傳失敗')
    console.error(e)
  }
}

const submitDownload = async () => {
  const fileName = selectedDownloadFile.value
  try {
    const res = await axios.get<Blob>(`http://localhost:8080/file/download/${fileName}`, {
      responseType: 'blob'
    })
    const url = window.URL.createObjectURL(new Blob([res.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fileName)
    document.body.appendChild(link)
    link.click()
    alert('下載成功')
  } catch (e) {
    alert('下載失敗')
    console.error(e)
  }
}

const editMember = (member: Member) => {
  router.push({ path: '/edit', query: { id: member.memberId.toString() } })
}

const deleteMember = async (member: Member) => {
  await axios.delete(`http://localhost:8080/members/delete/${member.memberId}`)
  await fetchMembers()
}

onMounted(() => {
  fetchMembers()
  fetchDownloadList()
})
</script>

<style scoped>
.uniform-col {
  width: 160px;
}

.name-offset {
  padding-left: 30px;
}

.email-offset {
  padding-left: 30px;
}
</style>
