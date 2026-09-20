package org.gagandeepsuman.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Centralized validation service for common input validations.
 * This service helps eliminate duplicated validation logic across boundary classes.
 */
public class ValidationService {

    /**
     * Validates that a string is not empty or null.
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isNotEmpty(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        return true;
    }

    /**
     * Validates that a string length does not exceed the maximum.
     *
     * @param input the string to validate
     * @param maxLength the maximum allowed length
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isValidLength(String input, int maxLength, String fieldName) {
        if (input == null) {
            System.out.println("Errore: il campo " + fieldName + " non può essere nullo.");
            return false;
        }
        if (input.length() > maxLength) {
            System.out.println("Errore: il campo " + fieldName + " non può superare i " + maxLength + " caratteri.");
            return false;
        }
        return true;
    }

    /**
     * Validates that a string contains only letters.
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isOnlyLetters(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        if (!input.matches("[a-zA-Z]+")) {
            System.out.println("Errore: il campo " + fieldName + " deve contenere solo lettere.");
            return false;
        }
        return true;
    }

    /**
     * Validates that a string contains only letters and numbers.
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isOnlyLettersOrNumbers(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        if (!input.matches("[a-zA-Z0-9]+")) {
            System.out.println("Errore: il campo " + fieldName + " deve contenere solo lettere e numeri.");
            return false;
        }
        return true;
    }

    /**
     * Validates that a string contains only digits.
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isOnlyDigits(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        if (!input.matches("[0-9]+")) {
            System.out.println("Errore: il campo " + fieldName + " deve contenere solo numeri.");
            return false;
        }
        return true;
    }

    /**
     * Validates an email format.
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        if (!input.matches(".*@.*\\..*")) {
            System.out.println("Errore: formato email non valido per il campo " + fieldName + ".");
            return false;
        }
        return true;
    }

    /**
     * Validates a phone number format (allows optional leading + and digits only).
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        if (!input.matches("^\\+?[0-9]+$")) {
            System.out.println("Errore: formato telefono non valido per il campo " + fieldName + ". Utilizzare solo numeri e un opzionale '+' iniziale.");
            return false;
        }
        return true;
    }

    /**
     * Validates that an integer is positive.
     *
     * @param value the integer to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isPositive(int value, String fieldName) {
        if (value <= 0) {
            System.out.println("Errore: il campo " + fieldName + " deve essere maggiore di zero.");
            return false;
        }
        return true;
    }

    /**
     * Validates that an integer is within a specified range (inclusive).
     *
     * @param value the integer to validate
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isInRange(int value, int min, int max, String fieldName) {
        if (value < min || value > max) {
            System.out.println("Errore: il campo " + fieldName + " deve essere compreso tra " + min + " e " + max + ".");
            return false;
        }
        return true;
    }

    /**
     * Validates that a BigDecimal is within a specified range (inclusive).
     *
     * @param value the BigDecimal to validate
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isInRange(BigDecimal value, BigDecimal min, BigDecimal max, String fieldName) {
        if (value == null) {
            System.out.println("Errore: il campo " + fieldName + " non può essere nullo.");
            return false;
        }
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            System.out.println("Errore: il campo " + fieldName + " deve essere compreso tra " + min + " e " + max + ".");
            return false;
        }
        return true;
    }

    /**
     * Validates a date string in ISO format (YYYY-MM-DD).
     *
     * @param input the string to validate
     * @param fieldName the name of the field (for error messages)
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String input, String fieldName) {
        if (input == null || input.isEmpty()) {
            System.out.println("Errore: il campo " + fieldName + " non può essere vuoto.");
            return false;
        }
        try {
            LocalDate.parse(input.trim());
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Errore: formato data non valido per il campo " + fieldName + ". Utilizzare il formato AAAA-MM-GG.");
            return false;
        }
    }
}