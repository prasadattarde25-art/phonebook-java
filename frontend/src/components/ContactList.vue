<template>
  <div class="container">

    <!-- Top Bar -->
    <div class="top-bar">
      <h1>📱 Phonebook</h1>

      <button
        @click="logout"
        class="logout-btn"
      >
        Logout
      </button>
    </div>


    <!-- Add Contact Form -->
    <div class="add-contact">

      <h2>Add New Contact</h2>

      <form @submit.prevent="addContact">

        <div class="form-group">

          <input
            v-model="newContact.name"
            placeholder="Name"
            required
          />

          <input
            v-model="newContact.phone_number"
            placeholder="Phone Number"
            required
          />

          <input
            v-model="newContact.email"
            placeholder="Email"
            type="email"
          />

          <input
            v-model="newContact.address"
            placeholder="Address"
          />

          <button type="submit">
            Add Contact
          </button>

        </div>

      </form>

    </div>


    <!-- Contact List -->
    <div class="contact-list">

      <!-- Header + Search -->
      <div class="contact-header">

        <h2>Contact List</h2>

        <input
          v-model="searchQuery"
          @input="searchContacts"
          type="text"
          placeholder="🔍 Search contacts..."
          class="search-input"
        />

      </div>


      <!-- Loading -->
      <div
        v-if="loading"
        class="loading"
      >
        Loading...
      </div>


      <!-- Error -->
      <div
        v-if="error"
        class="error"
      >
        {{ error }}
      </div>


      <!-- Contact Table -->
      <table
        v-if="!loading && contacts.length > 0"
      >

        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Phone Number</th>
            <th>Email</th>
            <th>Address</th>
            <th>Action</th>
          </tr>
        </thead>


        <tbody>

          <tr
            v-for="contact in contacts"
            :key="contact.id"
          >

            <td>
              {{ contact.id }}
            </td>


            <td>

              <router-link
                :to="`/contact/${contact.id}`"
                class="contact-link"
              >
                {{ contact.name }}
              </router-link>

            </td>


            <td>
              {{ contact.phone_number }}
            </td>


            <td>
              {{ contact.email || '-' }}
            </td>


            <td>
              {{ contact.address || '-' }}
            </td>


            <td>

              <button
                @click="deleteContact(contact.id)"
                class="delete-btn"
              >
                Delete
              </button>

            </td>

          </tr>

        </tbody>

      </table>


      <!-- Empty State -->
      <p
        v-if="!loading && contacts.length === 0"
        class="empty-message"
      >
        No contacts found.
      </p>


      <!-- Pagination -->
      <div
        v-if="!loading && totalPages > 1"
        class="pagination"
      >

        <button
          @click="previousPage"
          :disabled="currentPage === 1"
        >
          ← Previous
        </button>


        <span>
          Page {{ currentPage }} of {{ totalPages }}
        </span>


        <button
          @click="nextPage"
          :disabled="currentPage === totalPages"
        >
          Next →
        </button>

      </div>

    </div>

  </div>
</template>


<script>

import api from '../services/api'


export default {

  data() {

    return {

      // Contacts
      contacts: [],


      // Loading / Error
      loading: false,
      error: null,


      // Search
      searchQuery: '',


      // Pagination
      currentPage: 1,
      totalPages: 1,
      pageSize: 10,


      // New Contact
      newContact: {

        name: '',

        phone_number: '',

        email: '',

        address: ''

      }

    }

  },


  mounted() {

    this.fetchContacts()

  },


  methods: {


    // ========================================
    // LOGOUT
    // ========================================

    logout() {

      console.log('🚪 Logging out...')

      localStorage.removeItem('token')

      localStorage.removeItem('username')

      this.$router.push('/login')

    },


    // ========================================
    // FETCH CONTACTS
    // ========================================

    async fetchContacts() {

      this.loading = true

      this.error = null


      try {

        // Check token
        const token =
          localStorage.getItem('token')


        if (!token) {

          this.error =
            'Please login first'

          this.$router.push('/login')

          return

        }


        // JWT is automatically attached
        // by api.js interceptor

        const response =
          await api.get(
            '/contacts',
            {
              params: {

                search:
                  this.searchQuery || undefined,

                page:
                  this.currentPage,

                limit:
                  this.pageSize

              }
            }
          )


        console.log(
          '✅ Contacts response:',
          response.data
        )


        this.contacts =
          response.data.items || []


        this.totalPages =
          response.data.pages || 1


      } catch (err) {

        console.error(
          '❌ FETCH CONTACTS ERROR:',
          err
        )


        console.error(
          'STATUS:',
          err.response?.status
        )


        console.error(
          'RESPONSE:',
          err.response?.data
        )


        if (

          err.response?.status === 401 ||

          err.response?.status === 403

        ) {

          this.error =
            'Session expired or unauthorized. Please login again.'

          return

        }


        this.error =
          'Failed to load contacts'

      } finally {

        this.loading = false

      }

    },


    // ========================================
    // SEARCH
    // ========================================

    async searchContacts() {

      this.currentPage = 1

      await this.fetchContacts()

    },


    // ========================================
    // NEXT PAGE
    // ========================================

    async nextPage() {

      if (
        this.currentPage <
        this.totalPages
      ) {

        this.currentPage++

        await this.fetchContacts()

      }

    },


    // ========================================
    // PREVIOUS PAGE
    // ========================================

    async previousPage() {

      if (
        this.currentPage > 1
      ) {

        this.currentPage--

        await this.fetchContacts()

      }

    },


    // ========================================
    // ADD CONTACT
    // ========================================

    async addContact() {

      try {

        const token =
          localStorage.getItem('token')


        if (!token) {

          alert(
            '❌ Please login first'
          )

          this.$router.push('/login')

          return

        }


        const payload = {

          name:
            this.newContact.name,

          phone_number:
            this.newContact.phone_number,

          email:
            this.newContact.email || null,

          address:
            this.newContact.address || null

        }


        console.log(
          '📤 ADD CONTACT PAYLOAD:',
          payload
        )


        // JWT automatically attached
        const response =
          await api.post(
            '/contacts',
            payload
          )


        console.log(
          '✅ CONTACT ADDED:',
          response.data
        )


        alert(
          '✅ Contact added successfully!'
        )


        // Reset form
        this.newContact = {

          name: '',

          phone_number: '',

          email: '',

          address: ''

        }


        this.currentPage = 1


        await this.fetchContacts()


      } catch (err) {

        console.error(
          '❌ ADD CONTACT ERROR:',
          err
        )


        console.error(
          'STATUS:',
          err.response?.status
        )


        console.error(
          'RESPONSE:',
          err.response?.data
        )


        if (

          err.response?.status === 401 ||

          err.response?.status === 403

        ) {

          alert(
            '❌ Session expired or unauthorized. Please login again.'
          )

          return

        }


        if (err.response?.data) {

          const message =

            typeof err.response.data ===
            'string'

              ? err.response.data

              : err.response.data.message ||

                err.response.data.error ||

                'Failed to add contact'


          alert(
            '❌ Failed to add contact: ' +
            message
          )

        } else {

          alert(
            '❌ Failed to add contact: ' +
            err.message
          )

        }

      }

    },


    // ========================================
    // DELETE CONTACT
    // ========================================

    async deleteContact(id) {

      const confirmed =
        confirm(
          'Are you sure you want to delete this contact?'
        )


      if (!confirmed) {

        return

      }


      try {

        const token =
          localStorage.getItem('token')


        if (!token) {

          alert(
            '❌ Please login first'
          )

          this.$router.push('/login')

          return

        }


        // JWT automatically attached
        await api.delete(
          `/contacts/${id}`
        )


        alert(
          '✅ Contact deleted successfully!'
        )


        await this.fetchContacts()


        // If current page becomes empty,
        // move to previous page

        if (

          this.contacts.length === 0 &&

          this.currentPage > 1

        ) {

          this.currentPage--

          await this.fetchContacts()

        }


      } catch (err) {

        console.error(
          '❌ DELETE CONTACT ERROR:',
          err
        )


        console.error(
          'STATUS:',
          err.response?.status
        )


        console.error(
          'RESPONSE:',
          err.response?.data
        )


        if (

          err.response?.status === 401 ||

          err.response?.status === 403

        ) {

          alert(
            '❌ Session expired or unauthorized. Please login again.'
          )

          return

        }


        if (err.response?.data) {

          const message =

            typeof err.response.data ===
            'string'

              ? err.response.data

              : err.response.data.message ||

                err.response.data.error ||

                'Failed to delete contact'


          alert(
            '❌ Failed to delete contact: ' +
            message
          )

        } else {

          alert(
            '❌ Failed to delete contact'
          )

        }

      }

    }

  }

}

</script>


<style scoped>

.container {

  max-width: 1200px;

  margin: 50px auto;

  padding: 20px;

  background: white;

  border-radius: 10px;

  box-shadow:
    0 2px 10px
    rgba(0, 0, 0, 0.1);

}


/* ========================================
   TOP BAR
======================================== */

.top-bar {

  display: flex;

  justify-content:
    space-between;

  align-items:
    center;

  margin-bottom:
    30px;

}


.top-bar h1 {

  margin: 0;

  color: #333;

}


/* ========================================
   LOGOUT BUTTON
======================================== */

.logout-btn {

  padding:
    10px 22px;

  background:
    #dc3545;

  color:
    white;

  border:
    none;

  border-radius:
    6px;

  cursor:
    pointer;

  font-size:
    14px;

  font-weight:
    600;

  transition:
    background 0.2s;

}


.logout-btn:hover {

  background:
    #c82333;

}


.logout-btn:active {

  transform:
    scale(0.98);

}


/* ========================================
   HEADINGS
======================================== */

h2 {

  color:
    #555;

  margin-bottom:
    15px;

}


/* ========================================
   ADD CONTACT
======================================== */

.add-contact {

  background:
    #f8f9fa;

  padding:
    20px;

  border-radius:
    8px;

  margin-bottom:
    30px;

}


.form-group {

  display:
    grid;

  grid-template-columns:
    1fr 1fr 1fr 1fr auto;

  gap:
    10px;

}


.form-group input {

  padding:
    10px;

  border:
    1px solid #ddd;

  border-radius:
    5px;

  font-size:
    14px;

}


.form-group button {

  padding:
    10px 20px;

  background:
    #28a745;

  color:
    white;

  border:
    none;

  border-radius:
    5px;

  cursor:
    pointer;

  font-size:
    14px;

}


.form-group button:hover {

  background:
    #218838;

}


/* ========================================
   CONTACT LIST
======================================== */

.contact-list {

  margin-top:
    20px;

}


.contact-header {

  display:
    flex;

  justify-content:
    space-between;

  align-items:
    center;

  margin-bottom:
    15px;

}


.contact-header h2 {

  margin-bottom:
    0;

}


.search-input {

  width:
    300px;

  padding:
    10px 14px;

  border:
    1px solid #ddd;

  border-radius:
    6px;

  font-size:
    14px;

  outline:
    none;

}


.search-input:focus {

  border-color:
    #007bff;

}


/* ========================================
   TABLE
======================================== */

table {

  width:
    100%;

  border-collapse:
    collapse;

  background:
    white;

}


thead {

  background:
    #007bff;

  color:
    white;

}


th,
td {

  padding:
    12px;

  text-align:
    left;

  border-bottom:
    1px solid #ddd;

}


tbody tr:hover {

  background:
    #f1f3f5;

}


/* ========================================
   CONTACT LINK
======================================== */

.contact-link {

  color:
    #007bff;

  text-decoration:
    none;

  font-weight:
    bold;

}


.contact-link:hover {

  text-decoration:
    underline;

}


/* ========================================
   DELETE BUTTON
======================================== */

.delete-btn {

  padding:
    5px 15px;

  background:
    #dc3545;

  color:
    white;

  border:
    none;

  border-radius:
    4px;

  cursor:
    pointer;

}


.delete-btn:hover {

  background:
    #c82333;

}


/* ========================================
   PAGINATION
======================================== */

.pagination {

  display:
    flex;

  justify-content:
    center;

  align-items:
    center;

  gap:
    20px;

  margin-top:
    25px;

}


.pagination button {

  padding:
    8px 16px;

  border:
    none;

  border-radius:
    5px;

  background:
    #007bff;

  color:
    white;

  cursor:
    pointer;

  font-size:
    14px;

}


.pagination button:hover:not(:disabled) {

  background:
    #0056b3;

}


.pagination button:disabled {

  background:
    #ccc;

  cursor:
    not-allowed;

}


.pagination span {

  font-weight:
    bold;

  color:
    #555;

}


/* ========================================
   LOADING
======================================== */

.loading {

  text-align:
    center;

  padding:
    20px;

  color:
    #007bff;

}


/* ========================================
   ERROR
======================================== */

.error {

  color:
    red;

  padding:
    10px;

  background:
    #f8d7da;

  border-radius:
    5px;

  margin:
    10px 0;

}


/* ========================================
   EMPTY STATE
======================================== */

.empty-message {

  text-align:
    center;

  padding:
    30px;

  color:
    #777;

}


/* ========================================
   RESPONSIVE
======================================== */

@media (max-width: 900px) {

  .form-group {

    grid-template-columns:
      1fr 1fr;

  }


  .contact-header {

    flex-direction:
      column;

    align-items:
      stretch;

    gap:
      10px;

  }


  .search-input {

    width:
      100%;

    box-sizing:
      border-box;

  }


  table {

    font-size:
      13px;

  }

}


@media (max-width: 600px) {

  .container {

    margin:
      20px 10px;

    padding:
      15px;

  }


  .top-bar {

    flex-direction:
      column;

    gap:
      15px;

  }


  .top-bar h1 {

    text-align:
      center;

  }


  .logout-btn {

    width:
      100%;

  }


  .form-group {

    grid-template-columns:
      1fr;

  }


  table {

    display:
      block;

    overflow-x:
      auto;

    white-space:
      nowrap;

  }


  .pagination {

    gap:
      10px;

  }

}

</style>