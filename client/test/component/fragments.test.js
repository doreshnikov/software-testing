import {shallowMount} from '@vue/test-utils'
import AspectRatioContainer from '@/components/fragments/AspectRatioContainer'
import Header from '@/components/fragments/Header'

describe('aspect-ratio-container', () => {
    it('renders slot', () => {
        const wrapper = shallowMount(AspectRatioContainer, {
            slots: {default: '<div>Content</div>'},
            propsData: {ratio: 1.4}
        })
        expect(wrapper.props().ratio).toBe(1.4)
        expect(wrapper.find('.aspect-ratio-data > div').text()).toBe('Content')
    })
})

describe('header', () => {
    let computed
    beforeEach(() => {
        computed = {
            fetchError: () => null,
            isLoggedIn: () => computed.login() !== null,
            login: () => null,
        }
    })

    it('renders tabs and no login label', () => {
        const wrapper = shallowMount(Header, {computed: computed})
        expect(wrapper.findAll('md-tab-stub').length).toBe(3)
        expect(wrapper.find('#login').element).toBeUndefined()
    })
    it('renders login label for a user', () => {
        computed.login = () => 'doreshnikov'
        const wrapper = shallowMount(Header, {computed: computed})
        expect(wrapper.findAll('md-tab-stub').length).toBe(1)
        expect(wrapper.find('.header-login').text()).toContain('doreshnikov')
    })
    it('renders error button on error', () => {
        computed.fetchError = () => 'errormessage'
        const wrapper = shallowMount(Header, {computed: computed})
        expect(wrapper.find('.header-error').text()).toBe('Error occurred')
    })
    it('opens error dialog', async () => {
        computed.fetchError = () => 'errormessage'
        const wrapper = shallowMount(Header, {computed: computed})
        wrapper.find('.header-error').trigger('click')
        await wrapper.vm.$nextTick()
        expect(wrapper.find('md-dialog-stub').text()).toContain('errormessage')
    })
})