package phonebook_backend.controller;

import phonebook_backend.model.Contact;
import phonebook_backend.service.ContactService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // --------------------------------------------------
    // GET contacts - Search + Pagination
    // --------------------------------------------------

    @GetMapping
    public ResponseEntity<?> getContacts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        if (page < 1) {
            return ResponseEntity.badRequest()
                    .body("Page must be greater than 0");
        }

        if (limit < 1 || limit > 100) {
            return ResponseEntity.badRequest()
                    .body("Limit must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Contact> result =
                contactService.getContacts(search, pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("items", result.getContent());
        response.put("total", result.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("pages", result.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // GET single contact
    // --------------------------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> getContact(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(
                    contactService.getContactById(id)
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }

    // --------------------------------------------------
    // POST - Create contact
    // --------------------------------------------------

    @PostMapping
    public ResponseEntity<?> createContact(
            @RequestBody Contact contact) {

        try {
            return ResponseEntity.ok(
                    contactService.createContact(contact)
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // --------------------------------------------------
    // PUT - Update contact
    // --------------------------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(
            @PathVariable Long id,
            @RequestBody Contact contact) {

        try {
            return ResponseEntity.ok(
                    contactService.updateContact(id, contact)
            );

        } catch (RuntimeException e) {

            if (e.getMessage().equals("Contact not found")) {
                return ResponseEntity
                        .status(404)
                        .body(e.getMessage());
            }

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // --------------------------------------------------
    // DELETE - Delete contact
    // --------------------------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(
            @PathVariable Long id) {

        try {

            contactService.deleteContact(id);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Contact deleted successfully"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(404)
                    .body(e.getMessage());
        }
    }
}