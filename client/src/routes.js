import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import HomePage from '@/components/pages/home/HomePage'
import LoginPage from '@/components/pages/login/LoginPage'
import RegisterPage from '@/components/pages/register/RegisterPage'
import NewTitlePage from '@/components/pages/title/NewTitlePage'
import TitlePage from '@/components/pages/title/TitlePage'

export const routes = [
    {path: '/', component: HomePage},
    {path: '/login', component: LoginPage},
    {path: '/register', component: RegisterPage},
    {path: '/title/add', component: NewTitlePage},
    {
        path: '/title/:id',
        component: TitlePage,
        name: 'title',
        props: route => ({id: Number(route.params.id)})
    }
]

export default new VueRouter({
    routes: routes
})