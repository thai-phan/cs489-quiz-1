package org.example.model;

import java.util.Objects;

public class EmailAddress {
    private String address;
    private String label;

    public EmailAddress(String address, String label) {
        setAddress(address);
        setLabel(label);
    }

    public String getAddress() {
        return address;
    }

    public String getLabel() {
        return label;
    }

    public void setAddress(String address) {
        this.address = normalizeEmail(address);
    }

    public void setLabel(String label) {
        this.label = normalizeOptional(label);
    }

    private static String normalizeEmail(String value) {
        String normalized = normalizeRequired(value, "address");
        if (!normalized.contains("@")) {
            throw new IllegalArgumentException("address must contain '@'");
        }
        return normalized;
    }

    private static String normalizeRequired(String value, String fieldName) {
        String normalized = normalizeOptional(value);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return normalized;
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public String toString() {
        return label.isEmpty() ? address : label + ": " + address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailAddress that)) {
            return false;
        }
        return Objects.equals(address, that.address) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, label);
    }
}

