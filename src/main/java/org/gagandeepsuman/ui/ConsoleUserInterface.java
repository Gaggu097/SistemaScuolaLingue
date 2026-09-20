package org.gagandeepsuman.ui;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Implementazione console dell'interfaccia utente.
 * Utilizza Scanner per l'input e System.out per l'output.
 * Questa è l'implementazione predefinita che mantiene il comportamento originale dell'applicazione.
 */
public class ConsoleUserInterface implements UserInterface {
    private final Scanner scanner;

    public ConsoleUserInterface() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    @Override
    public int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                displayError("Inserisci un numero intero valido.");
            }
        }
    }

    @Override
    public java.math.BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                displayError("Inserisci un numero decimale valido.");
            }
        }
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String message) {
        System.err.println(message);
    }

    @Override
    public void displayMessageAndWait(String message) {
        displayMessage(message);
        System.out.print("Premi INVIO per continuare...");
        scanner.nextLine(); // Attende che l'utente prema INVIO
    }
}