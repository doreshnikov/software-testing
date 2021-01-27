import {mount, shallowMount} from '@vue/test-utils'
import TitleCard from '@/components/pages/home/TitleCard'
import HomePage from '@/components/pages/home/HomePage'

import common from '../../common'

describe('title-card', () => {
    let computed
    let props = {
        title: common.testTitle
    }
    beforeEach(() => {
        computed = {
            isLoggedIn: () => false
        }
    })

    it('renders info', () => {
        const wrapper = shallowMount(TitleCard, {computed: computed, propsData: props})
        expect(wrapper.find('.title-card-name').text()).toBe(props.title.name)
        expect(wrapper.find('.title-card-cover').attributes()['src']).toBe(props.title.image)
    })
    it('renders delete button for a user', () => {
        computed.isLoggedIn = () => true
        const wrapper = shallowMount(TitleCard, {computed: computed, propsData: props})
        expect(wrapper.findAll('.title-card-action').length).toBe(2)
    })
})

describe('home-page', () => {
    let titles = [1, 2, 3].map(i => ({id: i}))
    let loadTitles
    let computed
    let store
    beforeEach(() => {
        computed = {
            isLoggedIn: () => false,
            titles: () => titles
        }
        loadTitles = jest.fn()
        store = common.store({}, {}, {}, {'titles/loadTitles': loadTitles})
    })

    it('renders exact amount of titles', () => {
        const wrapper = shallowMount(HomePage, {computed: computed, store: store})
        expect(loadTitles).toHaveBeenCalledTimes(1)
        expect(wrapper.find('new-title-card-stub').element).toBeUndefined()
        expect(wrapper.findAll('title-card-stub').length).toBe(titles.length)
    })
    it('renders new title card for a user', () => {
        computed.isLoggedIn = () => true
        const wrapper = shallowMount(HomePage, {computed: computed, store: store})
        expect(loadTitles).toHaveBeenCalledTimes(1)
        expect(wrapper.find('new-title-card-stub').element).toBeDefined()
    })
    it('handles refresh button', async () => {
        const wrapper = mount(HomePage, {computed: computed, store: store, stubs: ['title-card']})
        wrapper.find('.home-page-refresh').trigger('click')
        await wrapper.vm.$nextTick()
        expect(loadTitles).toHaveBeenCalledTimes(2)
    })
})