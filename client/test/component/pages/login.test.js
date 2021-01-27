import VueRouter from 'vue-router'
import {mount, shallowMount} from '@vue/test-utils'
import LoginPage from '@/components/pages/login/LoginPage'
import common from '../../common'

describe('login-page', () => {
    const credentials = {
        login: 'doreshnikov',
        password: 'secret'
    }

    it('responds to inputs', async () => {
        const wrapper = mount(LoginPage)
        wrapper.find('.login-page-login').setValue(credentials.login)
        wrapper.find('.login-page-password').setValue(credentials.password)
        await wrapper.vm.$nextTick()
        expect(wrapper.vm.formData).toEqual(credentials)
    })
    it('renders error', async () => {
        const wrapper = shallowMount(LoginPage)
        wrapper.setData({error: 'errormessage'})
        await wrapper.vm.$nextTick()
        expect(wrapper.find('.md-error').text()).toBe('errormessage')
    })
    it('handles auth correctly', async () => {
        const router = new VueRouter()
        const replaceSpy = jest.spyOn(router, 'replace')
        const loginAction = jest.fn(() => Promise.resolve())
        const wrapper = shallowMount(LoginPage, {
            store: common.store({}, {}, {}, {
                'user/login': loginAction
            }),
            router: router
        })
        await wrapper.vm.authenticate()
        expect(loginAction).toBeCalled()
        expect(replaceSpy).toBeCalledWith('/')
    })
})