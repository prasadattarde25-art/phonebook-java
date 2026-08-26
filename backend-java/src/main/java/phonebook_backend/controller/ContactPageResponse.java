package phonebook_backend.controller;

import phonebook_backend.model.Contact;

import java.util.List;

public record ContactPageResponse(
        List<Contact> items,
        long total,
        int page,
        int limit,
        int pages
) {
}