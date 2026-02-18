import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import LoginView from '../../views/LoginView.vue'

const router = createRouter({
  history: createMemoryHistory(),
  routes: [{ path: '/login', component: LoginView }],
})

describe('LoginView', () => {
  it('renders the AI Chatbot heading', async () => {
    router.push('/login')
    await router.isReady()
    const wrapper = mount(LoginView, { global: { plugins: [router] } })
    expect(wrapper.find('h1').text()).toBe('AI Chatbot')
  })

  it('renders Google sign-in disclaimer text', () => {
    const wrapper = mount(LoginView, { global: { plugins: [router] } })
    expect(wrapper.find('.disclaimer').text()).toContain('Google')
  })
})
