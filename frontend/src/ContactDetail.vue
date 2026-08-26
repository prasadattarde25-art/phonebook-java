<template>
  <div class="container">

    <!-- Back Button -->
    <button @click="goBack" class="back-btn">
      ← Back to Contacts
    </button>

    <!-- Heading -->
    <h1>📱 Contact Details</h1>

    <!-- Loading -->
    <div v-if="loading" class="loading">
      Loading contact...
    </div>

    <!-- Error -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- Contact Details -->
    <div v-if="!loading && !error && contact" class="contact-card">

      <h2>{{ contact.name }}</h2>

      <div class="detail-row">
        <strong>Phone Number:</strong>
        <span>{{ contact.phone_number }}</span>
      </div>

      <div class="detail-row">
        <strong>Email:</strong>
        <span>{{ contact.email || '-' }}</span>
      </div>

      <div class="detail-row">
        <strong>Address:</strong>
        <span>{{ contact.address || '-' }}</span>
      </div>

      <div class="detail-row">
        <strong>ID:</strong>
        <span>{{ contact.id }}</span>
      </div>

      <!-- Update Button -->
      <button
        @click="toggleEdit"
        class="update-btn"
      >
        {{ isEditing ? 'Cancel Edit' : 'Update' }}
      </button>

      <!-- Edit Form -->
      <div v-if="isEditing" class="edit-form">

        <h3>Edit Contact</h3>

        <form @submit.prevent="updateContact">

          <div class="form-group">

            <input
              v-model="editForm.name"
              type="text"
              placeholder="Name"
              required
            />

            <input
              v-model="editForm.phone_number"
              type="text"
              placeholder="Phone Number"
              required
            />

            <input
              v-model="editForm.email"
              type="email"
              placeholder="Email"
            />

            <input
              v-model="editForm.address"
              type="text"
              placeholder="Address"
            />

          </div>

          <div class="button-group">

            <button
              type="submit"
              class="save-btn"
            >
              Save Changes
            </button>

            <button
              type="button"
              @click="toggleEdit"
              class="cancel-btn"
            >
              Cancel
            </button>

          </div>

        </form>

      </div>

    </div>

  </div>
</template>


<script>
import api from './services/api'

export default {

  name: 'ContactDetail',

  data() {
    return {
      contact: null,

      loading: true,

      error: null,

      isEditing: false,

      editForm: {
        name: '',
        phone_number: '',
        email: '',
        address: ''
      }
    }
  },


  mounted() {
    this.fetchContact()
  },


  methods: {

    // ==========================================
    // GET SINGLE CONTACT
    // ==========================================

    async fetchContact() {

      const id = this.$route.params.id

      console.log('📞 Contact ID:', id)

      try {

        this.loading = true
        this.error = null

        const response = await api.get(`/contacts/${id}`)

        console.log('✅ Contact response:', response.data)

        this.contact = response.data

        this.editForm = {
          name: response.data.name || '',
          phone_number: response.data.phone_number || '',
          email: response.data.email || '',
          address: response.data.address || ''
        }

      } catch (err) {

        console.error('❌ GET CONTACT ERROR:', err)

        console.error(
          'Status:',
          err.response?.status
        )

        console.error(
          'Response:',
          err.response?.data
        )

        if (err.response?.status === 401 ||
            err.response?.status === 403) {

          this.error =
            '❌ Session expired. Please login again.'

        } else if (err.response?.status === 404) {

          this.error =
            '❌ Contact not found.'

        } else {

          this.error =
            '❌ Failed to load contact.'
        }

      } finally {

        this.loading = false
      }
    },


    // ==========================================
    // BACK TO CONTACT LIST
    // ==========================================

    goBack() {

      this.$router.push('/')
    },


    // ==========================================
    // OPEN / CLOSE EDIT FORM
    // ==========================================

    toggleEdit() {

      this.isEditing = !this.isEditing

      if (!this.isEditing && this.contact) {

        this.editForm = {
          name: this.contact.name || '',
          phone_number: this.contact.phone_number || '',
          email: this.contact.email || '',
          address: this.contact.address || ''
        }
      }
    },


    // ==========================================
    // UPDATE CONTACT
    // ==========================================

    async updateContact() {

      try {

        if (!this.editForm.phone_number ||
            !this.editForm.phone_number.trim()) {

          alert('❌ Phone number is required.')

          return
        }

        const id = this.contact.id

        const payload = {

          name: this.editForm.name,

          phone_number:
            this.editForm.phone_number,

          email:
            this.editForm.email,

          address:
            this.editForm.address
        }

        console.log('📤 UPDATE ID:', id)

        console.log(
          '📤 UPDATE DATA:',
          payload
        )

        const response = await api.put(
          `/contacts/${id}`,
          payload
        )

        console.log(
          '✅ UPDATE RESPONSE:',
          response.data
        )

        this.contact = response.data

        this.editForm = {

          name:
            response.data.name || '',

          phone_number:
            response.data.phone_number || '',

          email:
            response.data.email || '',

          address:
            response.data.address || ''
        }

        this.isEditing = false

        alert(
          '✅ Contact updated successfully!'
        )

      } catch (err) {

        console.error(
          '❌ UPDATE CONTACT ERROR:',
          err
        )

        console.error(
          'Status:',
          err.response?.status
        )

        console.error(
          'Response:',
          err.response?.data
        )

        if (
          err.response?.status === 401 ||
          err.response?.status === 403
        ) {

          alert(
            '❌ Session expired or unauthorized.'
          )

        } else {

          const message =
            err.response?.data ||
            err.message ||
            'Unknown error'

          alert(
            '❌ Failed to update contact: ' +
            message
          )
        }
      }
    }

  }
}
</script>


<style scoped>

.container {
  max-width: 800px;
  margin: 50px auto;
  padding: 30px;

  background: #ffffff;

  border-radius: 10px;

  box-shadow:
    0 2px 10px
    rgba(0, 0, 0, 0.1);
}


h1 {
  text-align: center;

  color: #333;

  margin-bottom: 30px;
}


.back-btn {
  background: #333;

  color: white;

  padding: 10px 18px;

  border: none;

  border-radius: 5px;

  cursor: pointer;

  margin-bottom: 20px;

  font-size: 15px;
}


.back-btn:hover {
  background: #555;
}


.contact-card {
  background: white;

  padding: 30px;

  border-radius: 8px;
}


.contact-card h2 {
  text-align: center;

  color: #333;

  margin-bottom: 30px;

  font-size: 26px;
}


.detail-row {
  display: flex;

  padding: 15px;

  border-bottom:
    1px solid #ddd;

  font-size: 17px;
}


.detail-row strong {
  width: 180px;

  color: #333;
}


.detail-row span {
  color: #555;
}


.update-btn {
  display: block;

  margin: 30px auto 0;

  padding: 12px 30px;

  background: #007bff;

  color: white;

  border: none;

  border-radius: 5px;

  cursor: pointer;

  font-size: 16px;
}


.update-btn:hover {
  background: #0056b3;
}


.edit-form {
  margin-top: 30px;

  padding: 25px;

  background: #f8f9fa;

  border-radius: 8px;
}


.edit-form h3 {
  text-align: center;

  margin-bottom: 20px;

  color: #333;

  font-size: 22px;
}


.form-group {
  display: grid;

  grid-template-columns:
    1fr 1fr;

  gap: 12px;

  margin-bottom: 20px;
}


.form-group input {
  padding: 12px;

  border:
    1px solid #ddd;

  border-radius: 5px;

  font-size: 16px;

  box-sizing: border-box;
}


.form-group input:focus {
  outline: none;

  border-color: #007bff;
}


.button-group {
  display: flex;

  gap: 10px;

  justify-content: center;
}


.save-btn {
  padding: 10px 30px;

  background: #28a745;

  color: white;

  border: none;

  border-radius: 5px;

  cursor: pointer;

  font-size: 15px;
}


.save-btn:hover {
  background: #218838;
}


.cancel-btn {
  padding: 10px 30px;

  background: #dc3545;

  color: white;

  border: none;

  border-radius: 5px;

  cursor: pointer;

  font-size: 15px;
}


.cancel-btn:hover {
  background: #c82333;
}


.loading {
  text-align: center;

  padding: 40px;

  font-size: 18px;

  color: #666;
}


.error {
  text-align: center;

  padding: 20px;

  font-size: 18px;

  color: #dc3545;

  background: #f8d7da;

  border-radius: 5px;
}


@media (max-width: 600px) {

  .container {
    margin: 20px;

    padding: 20px;
  }

  .form-group {
    grid-template-columns: 1fr;
  }

  .detail-row {
    flex-direction: column;

    gap: 5px;
  }

  .detail-row strong {
    width: auto;
  }

}

</style>