// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import Register from '../views/Register.vue'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import CreateGroup from '../views/CreateGroup.vue'
import AllGroups from '../views/AllGroups.vue'
import ChatRoom from '../views/ChatRoom.vue'

// 你后面还会有 Login、ChatRoom 等页面，可以提前写好

const routes = [
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/home',
        name: 'Home',
        component: Home
    },
    {
        path: '/group/create',
        name: 'CreateGroup',
        component: CreateGroup
    },
    {
        path: '/group/all',
        name: 'AllGroups',
        component: AllGroups
    },
    {
        path: '/chat/:groupId',
        name: 'ChatRoom',
        component: ChatRoom,
    },


    {
        path: '/',
        redirect: '/login',
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router