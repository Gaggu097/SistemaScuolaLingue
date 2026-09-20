package org.gagandeepsuman.ui;

/**
 * Astrazione dell'interfaccia utente per consentire diversi tipi di UI
 * (console, GUI, web) senza modificare la logica di business.
 */
public interface UserInterface {
    /**
     * Ottiene una stringa di input dall'utente con il messaggio specificato.
     *
     * @param prompt il messaggio da visualizzare all'utente
     * @return la stringa di input fornita dall'utente
     */
    String getInput(String prompt);

    /**
     * Ottiene un intero di input dall'utente con il messaggio specificato.
     * Continua a chiedere finché l'utente non fornisce un intero valido.
     *
     * @param prompt il messaggio da visualizzare all'utente
     * @return l'intero di input fornito dall'utente
     */
    int getIntInput(String prompt);

    /**
     * Ottiene un BigDecimal di input dall'utente con il messaggio specificato.
     * Continua a chiedere finché l'utente non fornisce un valore decimale valido.
     *
     * @param prompt il messaggio da visualizzare all'utente
     * @return il valore BigDecimal di input fornito dall'utente
     */
    java.math.BigDecimal getBigDecimalInput(String prompt);

    /**
     * Visualizza un messaggio informativo all'utente.
     *
     * @param message il messaggio da visualizzare
     */
    void displayMessage(String message);

    /**
     * Visualizza un messaggio di errore all'utente.
     *
     * @param message il messaggio di errore da visualizzare
     */
    void displayError(String message);

    /**
     * Visualizza un messaggio e attende che l'utente prema invio per continuare.
     * Utilizzato per pause informative.
     *
     * @param message il messaggio da visualizzare
     */
    void displayMessageAndWait(String message);
}