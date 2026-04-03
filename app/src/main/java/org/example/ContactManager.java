package org.example;

import org.example.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactManager {
    private final List<Contact> contacts = new ArrayList<>();

    public List<Contact> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    public Contact createContact(String firstName, String lastName, String company, String jobTitle) {
        Contact contact = new Contact(firstName, lastName, company, jobTitle).create();
        contacts.add(contact);
        return contact;
    }

    public void updateContact(int index, String firstName, String lastName, String company, String jobTitle) {
        getContact(index).update(firstName, lastName, company, jobTitle);
    }

    public void deleteContact(int index) {
        Contact contact = getContact(index);
        contact.delete();
        contacts.remove(contact);
    }

    public void addPhoneNumber(int index, String number, String label) {
        getContact(index).addPhoneNumber(number, label);
    }

    public void addEmailAddress(int index, String address, String label) {
        getContact(index).addEmailAddress(address, label);
    }

    public void mergeContacts(int targetIndex, int sourceIndex) {
        if (targetIndex == sourceIndex) {
            throw new IllegalArgumentException("Cannot merge a contact with itself");
        }

        Contact target = getContact(targetIndex);
        Contact source = getContact(sourceIndex);
        target.merge(source);
        source.delete();
        contacts.remove(source);
    }

    public List<Contact> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getContacts();
        }

        String normalized = query.trim();
        return contacts.stream()
                .filter(contact -> !contact.isDeleted())
                .filter(contact -> contact.matches(normalized))
                .toList();
    }

    public Contact getContact(int index) {
        if (index < 1 || index > contacts.size()) {
            throw new IndexOutOfBoundsException("Contact index out of range: " + index);
        }
        return contacts.get(index - 1);
    }
}

