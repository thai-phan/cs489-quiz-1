package org.example;

import java.util.Objects;

public class PhoneNumber {
    private String number;
    private String label;

    public PhoneNumber(String number, String label) {
        setNumber(number);
        setLabel(label);
    }

    public String getNumber() {
        return number;
    }

    public String getLabel() {
        return label;
    }

    public void setNumber(String number) {
        this.number = normalizeRequired(number, "number");
    }

    public void setLabel(String label) {
        this.label = normalizeOptional(label);
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
        return label.isEmpty() ? number : label + ": " + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneNumber that)) {
            return false;
        }
        return Objects.equals(number, that.number) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, label);
    }
}

