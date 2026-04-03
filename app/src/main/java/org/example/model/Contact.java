package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Contact {
    private String firstName;
    private String lastName;
    private String company;
    private String jobTitle;
    private final List<PhoneNumber> phoneNumbers;
    private final List<EmailAddress> emailAddresses;
    private transient boolean deleted;

    public Contact() {
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        this.firstName = "";
        this.lastName = "";
        this.company = "";
        this.jobTitle = "";
        this.deleted = false;
    }

    public Contact(String firstName, String lastName, String company, String jobTitle) {
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        update(firstName, lastName, company, jobTitle);
    }

    public Contact create() {
        deleted = false;
        return this;
    }

    public void update(String firstName, String lastName, String company, String jobTitle) {
        this.firstName = normalizeOptional(firstName);
        this.lastName = normalizeOptional(lastName);
        this.company = normalizeOptional(company);
        this.jobTitle = normalizeOptional(jobTitle);
        deleted = false;
    }

    public void delete() {
        deleted = true;
    }

    public void addPhoneNumber(String number, String label) {
        PhoneNumber phoneNumber = new PhoneNumber(number, label);
        if (!phoneNumbers.contains(phoneNumber)) {
            phoneNumbers.add(phoneNumber);
        }
    }

    public void addEmailAddress(String address, String label) {
        EmailAddress emailAddress = new EmailAddress(address, label);
        if (!emailAddresses.contains(emailAddress)) {
            emailAddresses.add(emailAddress);
        }
    }

    public void merge(Contact other) {
        if (other == null || other == this) {
            return;
        }

        if (isBlank(firstName) && !isBlank(other.firstName)) {
            firstName = other.firstName;
        }
        if (isBlank(lastName) && !isBlank(other.lastName)) {
            lastName = other.lastName;
        }
        if (isBlank(company) && !isBlank(other.company)) {
            company = other.company;
        }
        if (isBlank(jobTitle) && !isBlank(other.jobTitle)) {
            jobTitle = other.jobTitle;
        }

        for (PhoneNumber phoneNumber : other.phoneNumbers) {
            if (!phoneNumbers.contains(phoneNumber)) {
                phoneNumbers.add(new PhoneNumber(phoneNumber.getNumber(), phoneNumber.getLabel()));
            }
        }
        for (EmailAddress emailAddress : other.emailAddresses) {
            if (!emailAddresses.contains(emailAddress)) {
                emailAddresses.add(new EmailAddress(emailAddress.getAddress(), emailAddress.getLabel()));
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return Collections.unmodifiableList(phoneNumbers);
    }

    public List<EmailAddress> getEmailAddresses() {
        return Collections.unmodifiableList(emailAddresses);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean matches(String query) {
        String normalizedQuery = normalizeOptional(query).toLowerCase();
        if (normalizedQuery.isEmpty()) {
            return true;
        }

        return containsIgnoreCase(firstName, normalizedQuery)
                || containsIgnoreCase(lastName, normalizedQuery)
                || containsIgnoreCase(company, normalizedQuery)
                || containsIgnoreCase(jobTitle, normalizedQuery)
                || phoneNumbers.stream().anyMatch(phoneNumber -> containsIgnoreCase(phoneNumber.getNumber(), normalizedQuery)
                || containsIgnoreCase(phoneNumber.getLabel(), normalizedQuery))
                || emailAddresses.stream().anyMatch(emailAddress -> containsIgnoreCase(emailAddress.getAddress(), normalizedQuery)
                || containsIgnoreCase(emailAddress.getLabel(), normalizedQuery));
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", phoneNumbers=" + phoneNumbers +
                ", emailAddresses=" + emailAddresses +
                '}';
    }


    private static String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean isBlank(String value) {
        return normalizeOptional(value).isEmpty();
    }

    private static boolean containsIgnoreCase(String value, String query) {
        return !isBlank(value) && value.toLowerCase().contains(query);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact contact)) {
            return false;
        }
        return deleted == contact.deleted
                && Objects.equals(firstName, contact.firstName)
                && Objects.equals(lastName, contact.lastName)
                && Objects.equals(company, contact.company)
                && Objects.equals(jobTitle, contact.jobTitle)
                && Objects.equals(phoneNumbers, contact.phoneNumbers)
                && Objects.equals(emailAddresses, contact.emailAddresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, company, jobTitle, phoneNumbers, emailAddresses, deleted);
    }
}

