import {
  describe,
  it,
  expect,
  vi,
  beforeEach,
  afterEach
} from 'vitest'

import { mount } from '@vue/test-utils'

import ContactList from '../components/ContactList.vue'
import api from '../services/api'


// ==================================================
// MOCK API SERVICE
// ==================================================

vi.mock('../services/api', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn()
  }
}))


// ==================================================
// TEST DATA
// ==================================================

const contactsResponse = {
  data: {
    items: [
      {
        id: 1,
        name: 'Rahul Sharma',
        phone_number: '9876543210',
        email: 'rahul@example.com',
        address: 'Mumbai'
      },
      {
        id: 2,
        name: 'Amit Patil',
        phone_number: '9876543222',
        email: 'amit@example.com',
        address: 'Pune'
      }
    ],
    total: 2,
    page: 1,
    limit: 10,
    pages: 1
  }
}


// ==================================================
// MOUNT HELPER
// ==================================================

const mountContactList = () => {
  return mount(ContactList, {
    global: {
      mocks: {
        $router: {
          push: vi.fn()
        },

        $route: {
          params: {}
        }
      },

      stubs: {
        RouterLink: true
      }
    }
  })
}


// ==================================================
// TEST SUITE
// ==================================================

describe('ContactList.vue', () => {


  // ==================================================
  // BEFORE EACH
  // ==================================================

  beforeEach(() => {

    vi.clearAllMocks()

    // Fake logged-in user
    localStorage.setItem(
      'token',
      'test-jwt-token'
    )

    localStorage.setItem(
      'username',
      'test-user'
    )


    // Mock browser functions
    global.alert = vi.fn()

    global.confirm = vi.fn(() => true)


    // Default GET response
    api.get.mockResolvedValue(
      contactsResponse
    )


    // Default POST response
    api.post.mockResolvedValue({
      data: {
        id: 3,
        name: 'New Contact',
        phone_number: '8888888888',
        email: 'new@example.com',
        address: 'Mumbai'
      }
    })


    // Default DELETE response
    api.delete.mockResolvedValue({
      data: {
        message: 'Contact deleted successfully'
      }
    })

  })


  // ==================================================
  // AFTER EACH
  // ==================================================

  afterEach(() => {

    localStorage.clear()

    vi.restoreAllMocks()

  })


  // ==================================================
  // 1. COMPONENT RENDER
  // ==================================================

  it('renders Phonebook title', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 0)
    )

    expect(
      wrapper.text()
    ).toContain('Phonebook')

  })


  // ==================================================
  // 2. LOAD CONTACTS
  // ==================================================

  it('loads contacts from API', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 50)
    )

    expect(
      api.get
    ).toHaveBeenCalled()

    // Verify component received contact data
    expect(
      wrapper.vm.contacts.length
    ).toBe(2)

    expect(
      wrapper.vm.contacts[0].name
    ).toBe('Rahul Sharma')

    expect(
      wrapper.vm.contacts[1].name
    ).toBe('Amit Patil')

  })


  // ==================================================
  // 3. SEARCH
  // ==================================================

  it('searches contacts', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 50)
    )

    const searchInput =
      wrapper.find('.search-input')

    expect(
      searchInput.exists()
    ).toBe(true)


    await searchInput.setValue('Rahul')

    await searchInput.trigger('input')


    // Give async search request time to execute
    await new Promise(resolve =>
      setTimeout(resolve, 500)
    )


    expect(
      api.get
    ).toHaveBeenCalled()

  })


  // ==================================================
  // 4. NEXT PAGE
  // ==================================================

  it('moves to next page', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 50)
    )


    wrapper.vm.currentPage = 1

    wrapper.vm.totalPages = 2

    await wrapper.vm.$nextTick()


    const nextButton =
      wrapper
        .findAll('.pagination button')
        .find(button =>
          button.text().includes('Next')
        )


    expect(
      nextButton
    ).toBeTruthy()


    await nextButton.trigger('click')


    expect(
      wrapper.vm.currentPage
    ).toBe(2)

  })


  // ==================================================
  // 5. PREVIOUS PAGE
  // ==================================================

  it('moves to previous page', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 50)
    )


    wrapper.vm.currentPage = 2

    wrapper.vm.totalPages = 2

    await wrapper.vm.$nextTick()


    const previousButton =
      wrapper
        .findAll('.pagination button')
        .find(button =>
          button.text().includes('Previous')
        )


    expect(
      previousButton
    ).toBeTruthy()


    await previousButton.trigger('click')


    expect(
      wrapper.vm.currentPage
    ).toBe(1)

  })


  // ==================================================
  // 6. ADD CONTACT
  // ==================================================

  it('adds a new contact', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 50)
    )


    wrapper.vm.newContact = {
      name: 'New Contact',
      phone_number: '8888888888',
      email: 'new@example.com',
      address: 'Mumbai'
    }


    await wrapper.vm.addContact()


    expect(
      api.post
    ).toHaveBeenCalled()


    // Verify POST was made to contacts endpoint
    expect(
      api.post.mock.calls[0][0]
    ).toBe('/contacts')


  })


  // ==================================================
  // 7. DELETE CONTACT
  // ==================================================

  it('deletes a contact', async () => {

    const wrapper = mountContactList()

    await new Promise(resolve =>
      setTimeout(resolve, 50)
    )


    await wrapper.vm.deleteContact(1)


    expect(
      global.confirm
    ).toHaveBeenCalled()


    expect(
      api.delete
    ).toHaveBeenCalled()


    // Verify correct contact ID
    expect(
      api.delete.mock.calls[0][0]
    ).toBe('/contacts/1')

  })

})