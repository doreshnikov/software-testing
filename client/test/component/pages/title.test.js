import {shallowMount} from '@vue/test-utils'
import TitlePreview from '@/components/pages/title/TitlePreview'
import TitlePage from '@/components/pages/title/TitlePage'

import VueRouter from 'vue-router'
import common from '../../common'

describe('title-preview', () => {
    it('renders info', () => {
        const props = {title: common.testTitle}
        const wrapper = shallowMount(TitlePreview, {propsData: props})
        expect(wrapper.find('.title-preview-name').text()).toBe(props.title.name)
        expect(wrapper.find('.title-preview-cover').attributes()['src']).toBe(props.title.image)
        expect(wrapper.find('.title-preview-description').text()).toBe(props.title.description)
        expect(wrapper.find('.title-preview-chapters').text()).toContain(`${props.title.chapters}`)
    })
})

describe('title-page', () => {
    let props
    let data
    beforeEach(() => {
        props = {
            id: 1
        }
        data = () => {
            return {
                isLoaded: false,
                title: common.testTitle
            }
        }
    })

    it('renders progress spinner', () => {
        const wrapper = shallowMount(TitlePage, {data: data, propsData: props})
        expect(wrapper.find('md-progress-spinner-stub').element).toBeDefined()
    })
    it('renders title-preview.vue', async () => {
        const wrapper = shallowMount(TitlePage, {data: data, propsData: props})
        wrapper.setData({isLoaded: true})
        await wrapper.vm.$nextTick()
        expect(wrapper.find('md-progress-spinner-stub').element).toBeUndefined()
        expect(wrapper.find('title-preview-stub').element).toBeDefined()
    })
    it('fails on invalid id', async () => {
        props.id = NaN
        const spy = jest.spyOn(TitlePage.methods, 'goHome')
        const wrapper = shallowMount(TitlePage, {
            data: data,
            propsData: props,
            store: common.store({}, {}, {'alerts/setFetchError': () => null}),
            router: new VueRouter()
        })
        await wrapper.vm.$nextTick()
        expect(spy).toBeCalled()
    })
})