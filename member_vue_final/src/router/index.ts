import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import MemberManager from '../views/MemberManager.vue'

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Home',
        component: MemberManager
    },
    {
        path: '/add',
        name: 'AddMember',
        component: () => import('../views/AddMember.vue')
    },
    {
        path: '/edit',
        name: 'EditMember',
        component: () => import('../views/EditMember.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router