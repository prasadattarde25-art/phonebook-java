package phonebook_backend.service;

import phonebook_backend.model.Contact;
import phonebook_backend.repository.ContactRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // =====================================================
    // GET CONTACTS - SEARCH + PAGINATION
    // =====================================================

    public Page<Contact> getContacts(
            String search,
            Pageable pageable) {

        if (search == null || search.trim().isEmpty()) {
            return contactRepository.findAll(pageable);
        }

        return contactRepository.findByNameContainingIgnoreCase(
                search.trim(),
                pageable
        );
    }

    // =====================================================
    // GET SINGLE CONTACT
    // =====================================================

    public Contact getContactById(Long id) {

        return contactRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Contact not found")
                );
    }

    // =====================================================
    // CREATE CONTACT
    // =====================================================

    public Contact createContact(Contact contact) {

        // Validate name
        if (contact.getName() == null ||
                contact.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Name is required"
            );
        }

        // Validate phone number
        if (contact.getPhoneNumber() == null ||
                contact.getPhoneNumber().trim().isEmpty()) {

            throw new RuntimeException(
                    "Phone number is required"
            );
        }

        // Clean values
        contact.setName(
                contact.getName().trim()
        );

        contact.setPhoneNumber(
                contact.getPhoneNumber().trim()
        );

        if (contact.getEmail() != null) {
            contact.setEmail(
                    contact.getEmail().trim()
            );
        }

        if (contact.getAddress() != null) {
            contact.setAddress(
                    contact.getAddress().trim()
            );
        }

        // Check duplicate phone
        if (contactRepository
                .findByPhoneNumber(
                        contact.getPhoneNumber()
                )
                .isPresent()) {

            throw new RuntimeException(
                    "Phone number already exists"
            );
        }

        // Check duplicate email
        if (contact.getEmail() != null &&
                !contact.getEmail().isEmpty() &&
                contactRepository
                        .findByEmail(contact.getEmail())
                        .isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        return contactRepository.save(contact);
    }

    // =====================================================
    // UPDATE CONTACT
    // =====================================================

    public Contact updateContact(
            Long id,
            Contact updatedContact) {

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Contact not found")
                );

        // Validate name
        if (updatedContact.getName() == null ||
                updatedContact.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Name is required"
            );
        }

        // Validate phone
        if (updatedContact.getPhoneNumber() == null ||
                updatedContact.getPhoneNumber().trim().isEmpty()) {

            throw new RuntimeException(
                    "Phone number is required"
            );
        }

        // Clean values
        String phoneNumber =
                updatedContact.getPhoneNumber().trim();

        String name =
                updatedContact.getName().trim();

        String email =
                updatedContact.getEmail();

        String address =
                updatedContact.getAddress();

        if (email != null) {
            email = email.trim();
        }

        if (address != null) {
            address = address.trim();
        }

        // =================================================
        // DUPLICATE PHONE CHECK
        // =================================================

        contactRepository
                .findByPhoneNumber(phoneNumber)
                .ifPresent(existing -> {

                    if (!existing.getId().equals(id)) {

                        throw new RuntimeException(
                                "Phone number already exists"
                        );
                    }
                });

        // =================================================
        // DUPLICATE EMAIL CHECK
        // =================================================

        if (email != null && !email.isEmpty()) {

            final String finalEmail = email;

            contactRepository
                    .findByEmail(finalEmail)
                    .ifPresent(existing -> {

                        if (!existing.getId().equals(id)) {

                            throw new RuntimeException(
                                    "Email already exists"
                            );
                        }
                    });
        }

        // =================================================
        // UPDATE VALUES
        // =================================================

        contact.setName(name);

        contact.setPhoneNumber(phoneNumber);

        contact.setEmail(
                email == null || email.isEmpty()
                        ? null
                        : email
        );

        contact.setAddress(
                address == null || address.isEmpty()
                        ? null
                        : address
        );

        return contactRepository.save(contact);
    }

    // =====================================================
    // DELETE CONTACT
    // =====================================================

    public void deleteContact(Long id) {

        if (!contactRepository.existsById(id)) {

            throw new RuntimeException(
                    "Contact not found"
            );
        }

        contactRepository.deleteById(id);
    }
}