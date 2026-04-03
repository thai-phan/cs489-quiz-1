package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.Contact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ContactManager contactManager;
    private final Scanner scanner;

    public App() {
        this(new ContactManager(), new Scanner(System.in), true);
    }


    App(ContactManager contactManager, Scanner scanner, boolean seedSampleData) {
        this.contactManager = contactManager;
        this.scanner = scanner;
        if (seedSampleData) {
            seedSampleContacts();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }

    public void run() {
        System.out.println("Contacts CLI");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Select an option: ");
            if (choice == null) {
                break;
            }
            switch (choice) {
                case "1" -> listContacts();
                case "2" -> createContact();
                case "3" -> updateContact();
                case "4" -> deleteContact();
                case "5" -> searchContacts();
                case "6" -> addPhoneNumber();
                case "7" -> addEmailAddress();
                case "8" -> mergeContacts();
                case "9" -> running = false;
                default -> System.out.println("Unknown option. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("1) List contacts");
        System.out.println("2) Create contact");
        System.out.println("3) Update contact");
        System.out.println("4) Delete contact");
        System.out.println("5) Search contacts");
        System.out.println("6) Add phone number");
        System.out.println("7) Add email address");
        System.out.println("8) Merge contacts");
        System.out.println("9) Exit");
    }

    private void seedSampleContacts() {
        Contact david = contactManager.createContact("David", "Sanger", "Argos LLC", "Sales Manager");
        david.addPhoneNumber("240-133-0011", "Home");
        david.addPhoneNumber("240-112-0123", "Mobile");
        david.addEmailAddress("dave.sang@gmail.com", "Home");
        david.addEmailAddress("dsanger@argos.com", "Work");

        contactManager.createContact("Carlos", "Jimenez", "Zappos", "Director");

        Contact ali = contactManager.createContact("Ali", "Gafar", "BMI Services", "HR Manager");
        ali.addPhoneNumber("412-116-9988", "Work");
        ali.addEmailAddress("ali@bmi.com", "Work");
    }

    private void listContacts() {
        printContacts(contactManager.getContacts());
    }

    private void createContact() {
        try {
            String firstName = readRequiredLine("First name: ");
            if (firstName == null) {
                return;
            }
            String lastName = readRequiredLine("Last name: ");
            if (lastName == null) {
                return;
            }
            String company = readLine("Company: ");
            if (company == null) {
                return;
            }
            String jobTitle = readLine("Job title: ");
            if (jobTitle == null) {
                return;
            }
            Contact contact = contactManager.createContact(firstName, lastName, company, jobTitle);
            System.out.println(toJson(contact));
        } catch (IllegalArgumentException ex) {
            System.out.println("Could not create contact: " + ex.getMessage());
        }
    }

    private void updateContact() {
        if (contactManager.getContacts().isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        int index = readContactIndex("Contact number to update: ");
        if (index < 0) {
            return;
        }
        try {
            String firstName = readRequiredLine("First name: ");
            if (firstName == null) {
                return;
            }
            String lastName = readRequiredLine("Last name: ");
            if (lastName == null) {
                return;
            }
            String company = readLine("Company: ");
            if (company == null) {
                return;
            }
            String jobTitle = readLine("Job title: ");
            if (jobTitle == null) {
                return;
            }
            contactManager.updateContact(index, firstName, lastName, company, jobTitle);
            System.out.println(toJson(contactManager.getContact(index)));
        } catch (IllegalArgumentException ex) {
            System.out.println("Could not update contact: " + ex.getMessage());
        }
    }

    private void deleteContact() {
        if (contactManager.getContacts().isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        int index = readContactIndex("Contact number to delete: ");
        if (index < 0) {
            return;
        }
        Contact contact = contactManager.getContact(index);
        contactManager.deleteContact(index);
        System.out.println(toJson(contact));
    }

    private void searchContacts() {
        String query = readLine("Search query: ");
        List<Contact> results = contactManager.search(query);
        printContacts(results);
    }

    private void addPhoneNumber() {
        if (contactManager.getContacts().isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        int index = readContactIndex("Contact number: ");
        if (index < 0) {
            return;
        }
        try {
            String number = readRequiredLine("Phone number: ");
            if (number == null) {
                return;
            }
            String label = readLine("Phone label: ");
            if (label == null) {
                return;
            }
            contactManager.addPhoneNumber(index, number, label);
            System.out.println(toJson(contactManager.getContact(index)));
        } catch (IllegalArgumentException ex) {
            System.out.println("Could not add phone number: " + ex.getMessage());
        }
    }

    private void addEmailAddress() {
        if (contactManager.getContacts().isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        int index = readContactIndex("Contact number: ");
        if (index < 0) {
            return;
        }
        try {
            String address = readRequiredLine("Email address: ");
            if (address == null) {
                return;
            }
            String label = readLine("Email label: ");
            if (label == null) {
                return;
            }
            contactManager.addEmailAddress(index, address, label);
            System.out.println(toJson(contactManager.getContact(index)));
        } catch (IllegalArgumentException ex) {
            System.out.println("Could not add email address: " + ex.getMessage());
        }
    }

    private void mergeContacts() {
        if (contactManager.getContacts().size() < 2) {
            System.out.println("At least two contacts are required to merge.");
            return;
        }
        int targetIndex = readContactIndex("Target contact number: ");
        if (targetIndex < 0) {
            return;
        }
        int sourceIndex = readContactIndex("Source contact number: ");
        if (sourceIndex < 0) {
            return;
        }
        try {
            contactManager.mergeContacts(targetIndex, sourceIndex);
            System.out.println("Merge complete.");
            listContacts();
        } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
            System.out.println("Could not merge contacts: " + ex.getMessage());
        }
    }

    private void printContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        List<Contact> sortedContacts = new ArrayList<>(contacts);
        sortedContacts.sort(Comparator.comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER));

        System.out.println(toJson(sortedContacts));
    }

    private String toJson(Object value) {
        return GSON.toJson(value);
    }

    private int readContactIndex(String prompt) {
        while (true) {
            String input = readRequiredLine(prompt);
            if (input == null) {
                return -1;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    private String readRequiredLine(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (input == null) {
                return null;
            }
            if (!input.isBlank()) {
                return input;
            }
            System.out.println("Value must not be blank.");
        }
    }
}
