package phonebook_backend.repository;

import phonebook_backend.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Optional<Contact> findByPhoneNumber(String phoneNumber);

    Optional<Contact> findByEmail(String email);
}