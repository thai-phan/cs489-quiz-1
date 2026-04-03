package org.example;

import org.example.model.Contact;
import org.example.model.EmailAddress;
import org.example.model.PhoneNumber;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {

    @Test
    void appPrintsSeedContactsAsJson() {
        ContactManager manager = new ContactManager();
        App app = new App(manager, new Scanner("1\n9\n"), true);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            app.run();
        } finally {
            System.setOut(originalOut);
        }

        String output = buffer.toString();
        assertTrue(output.contains("\"firstName\": \"David\""));
        assertTrue(output.contains("\"emails\"") || output.contains("\"emailAddresses\""));
        assertFalse(output.contains("deleted"));
        assertTrue(output.indexOf("\"lastName\": \"Gafar\"")
                < output.indexOf("\"lastName\": \"Jimenez\""));
        assertTrue(output.indexOf("\"lastName\": \"Jimenez\"")
                < output.indexOf("\"lastName\": \"Sanger\""));
    }

    @Test
    void appSeedsSampleContactsOnStartup() {
        ContactManager manager = new ContactManager();
        new App(manager, new Scanner(""), true);

        assertEquals(3, manager.getContacts().size());
        assertEquals("David", manager.getContact(1).getFirstName());
        assertEquals(2, manager.getContact(1).getPhoneNumbers().size());
        assertEquals(2, manager.getContact(1).getEmailAddresses().size());
        assertEquals("Carlos", manager.getContact(2).getFirstName());
        assertEquals("Ali", manager.getContact(3).getFirstName());
    }

    @Test
    void contactRejectsBlankRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("", "mobile"));
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress("not-an-email", "work"));
    }

    @Test
    void managerCreatesSearchesAndDeletesContacts() {
        ContactManager manager = new ContactManager();
        manager.createContact("Jane", "Doe", "Acme", "Engineer");
        manager.createContact("John", "Smith", "Beta", "Manager");
        manager.addPhoneNumber(1, "555-1234", "mobile");
        manager.addEmailAddress(1, "jane@example.com", "work");

        List<Contact> searchResults = manager.search("jane");
        assertEquals(1, searchResults.size());
        Contact searchResult = searchResults.getFirst();
        assertEquals("Jane", searchResult.getFirstName());
        assertEquals(1, searchResult.getPhoneNumbers().size());
        assertEquals(1, searchResult.getEmailAddresses().size());

        manager.deleteContact(2);
        assertEquals(1, manager.getContacts().size());
        assertEquals("Jane", manager.getContact(1).getFirstName());
    }

    @Test
    void mergeCombinesUniquePhoneNumbersAndEmails() {
        ContactManager manager = new ContactManager();
        manager.createContact("Jane", "Doe", "", "");
        manager.createContact("", "", "Acme", "Engineer");

        manager.addPhoneNumber(1, "555-1234", "mobile");
        manager.addEmailAddress(1, "jane@example.com", "work");
        manager.addPhoneNumber(2, "555-1234", "mobile");
        manager.addPhoneNumber(2, "555-9999", "home");
        manager.addEmailAddress(2, "jane@example.com", "work");
        manager.addEmailAddress(2, "jane.doe@example.com", "personal");

        manager.mergeContacts(1, 2);

        Contact merged = manager.getContact(1);
        assertEquals("Jane", merged.getFirstName());
        assertEquals("Doe", merged.getLastName());
        assertEquals("Acme", merged.getCompany());
        assertEquals("Engineer", merged.getJobTitle());
        assertEquals(2, merged.getPhoneNumbers().size());
        assertEquals(2, merged.getEmailAddresses().size());
        assertEquals(1, manager.getContacts().size());
    }

    @Test
    void contactMatchesAcrossNestedValues() {
        Contact contact = new Contact("Jane", "Doe", "Acme", "Engineer");
        contact.addPhoneNumber("555-1234", "mobile");
        contact.addEmailAddress("jane@example.com", "work");

        assertTrue(contact.matches("acme"));
        assertTrue(contact.matches("555-1234"));
        assertTrue(contact.matches("work"));
        assertFalse(contact.matches("missing"));
    }
}
