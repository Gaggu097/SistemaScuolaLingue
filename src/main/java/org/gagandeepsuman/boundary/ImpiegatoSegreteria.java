package org.gagandeepsuman.boundary;

import org.gagandeepsuman.service.IGestioneScuolaService;
import org.gagandeepsuman.dao.IscrizioneDAO;
import org.gagandeepsuman.dao.DAOFactory;
import org.gagandeepsuman.ui.UserInterface;
import org.gagandeepsuman.ui.ConsoleUserInterface;
import org.gagandeepsuman.util.ValidationService;

import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class ImpiegatoSegreteria {
    private final IGestioneScuolaService controlImpiegato;
    private final UserInterface ui;

    public ImpiegatoSegreteria(UserInterface ui) {
        this.controlImpiegato = DAOFactory.getGestioneScuolaController();
        this.ui = ui != null ? ui : new ConsoleUserInterface();
    }

    // Costruttore di comodità per backward compatibility
    public ImpiegatoSegreteria() {
        this(new ConsoleUserInterface());
    }

    public void main(){
        mostraMenuImpiegatoSegreteria();
    }

    public void mostraMenuImpiegatoSegreteria(){
        int scelta = -1;
        do {
            ui.displayMessage("\n=== MENU Segreteria ===");
            ui.displayMessage("1. * Registrare Pagamento *");
            ui.displayMessage("2. * Annullare Iscrizione *");
            ui.displayMessage("3. Registrare Rimborso");
            ui.displayMessage("4. Annullare Lezione");
            ui.displayMessage("5. * Consultare Iscritti *");
            ui.displayMessage("6. * Consultare Statistiche Corsi *");
            ui.displayMessage("0. Torna al menu principale");
            ui.displayMessage("Seleziona un'opzione: ");

            String input = ui.getInput("");
            try {
                scelta = parseInt(input);
                gestisciSceltaImpiegato(scelta);
            } catch (NumberFormatException e) {
                ui.displayError("Inserisci un numero valido.");
            }
        } while (scelta != 0);
    }
    void gestisciSceltaImpiegato(int scelta){
        switch (scelta) {
            case 1 -> registrarePagamento();
            case 2 -> annullareIscrizione();
            case 3 -> registrareRimborso();
            case 4 -> annullareLezione();
            case 5 -> consultareIscritti();
            case 6 -> consultareStatisticheCorsi();
            case 0 -> ui.displayMessage("Ritorno al menu principale...");
            default -> ui.displayError("Opzione non valida.");
        }
    }

    public void registrarePagamento() {
        ui.displayMessage("\n--- Registrare Pagamento ---");
        int idIscrizione;
        while(true){
            String idInput = ui.getInput("ID Iscrizione di cui si vuole registrare il pagamento: ");
            if (ValidationService.isNotEmpty(idInput, "ID iscrizione") && ValidationService.isOnlyDigits(idInput, "ID iscrizione")) {
                idIscrizione = parseInt(idInput);
                if (ValidationService.isPositive(idIscrizione, "ID iscrizione") && controlImpiegato.checkIdIscrizione(idIscrizione)) {
                    break;
                } else if (!controlImpiegato.checkIdIscrizione(idIscrizione)) {
                    ui.displayError("Errore: ID iscrizione non esistente.");
                }
            } else {
                // Error messages handled by ValidationService for empty/not digits
                if (idInput.isEmpty()) {
                    ui.displayError("Errore: il campo ID iscrizione non può essere vuoto.");
                } else {
                    ui.displayError("Errore: ID iscrizione contiene caratteri non validi.");
                }
            }
        }
        controlImpiegato.registrarePagamento(idIscrizione);
        // throw new UnsupportedOperationException();
    }

    public void annullareIscrizione() {
        ui.displayMessage("\n--- ANNULLA ISCRIZIONE ---");
        int idCliente;
        while(true){
            String idInput = ui.getInput("Inserisci ID Cliente: ");
            if (ValidationService.isNotEmpty(idInput, "ID cliente") && ValidationService.isOnlyDigits(idInput, "ID cliente")) {
                idCliente = parseInt(idInput);
                if (controlImpiegato.checkIdCliente(idCliente)) {
                    break;
                } else {
                    ui.displayError("Errore: ID cliente non valido o non esistente.");
                }
            } else {
                // Error messages handled by ValidationService for empty/not digits
                if (idInput.isEmpty()) {
                    ui.displayError("Errore: il campo ID cliente non può essere vuoto.");
                } else {
                    ui.displayError("Errore: ID cliente contiene caratteri non validi.");
                }
            }
        }

        int idIscrizione;
        while(true){
            String idIscrizioneInput = ui.getInput("Inserisci ID Iscrizione: ");
            if (ValidationService.isNotEmpty(idIscrizioneInput, "ID iscrizione") && ValidationService.isOnlyDigits(idIscrizioneInput, "ID iscrizione")) {
                idIscrizione = parseInt(idIscrizioneInput);
                if (controlImpiegato.checkIdIscrizione(idIscrizione)) {
                    break;
                } else {
                    ui.displayError("Errore: ID iscrizione non valido o non esistente.");
                }
            } else {
                // Error messages handled by ValidationService for empty/not digits
                if (idIscrizioneInput.isEmpty()) {
                    ui.displayError("Errore: il campo ID iscrizione non può essere vuoto.");
                } else {
                    ui.displayError("Errore: ID iscrizione contiene caratteri non validi.");
                }
            }
        }
        boolean esito = controlImpiegato.annullareIscrizione(idCliente, idIscrizione);
        if (esito) {
            ui.displayMessage("[Impiegato] Iscrizione annullata con successo!");
        } else {
            ui.displayError("[Impiegato] Errore durante l'annullamento dell'iscrizione.");
        }
    }

    public void registrareRimborso() {
        ui.displayMessage("\n--- REGISTRA RIMBORSO ---");
        int idIscrizione;
        while(true){
            String idInput = ui.getInput("Inserisci ID Iscrizione: ");
            if (ValidationService.isNotEmpty(idInput, "ID iscrizione") && ValidationService.isOnlyDigits(idInput, "ID iscrizione")) {
                idIscrizione = parseInt(idInput);
                if (controlImpiegato.checkIdIscrizione(idIscrizione)) {
                    break;
                } else {
                    ui.displayError("Errore: ID iscrizione non valido o non esistente.");
                }
            } else {
                // Error messages handled by ValidationService for empty/not digits
                if (idInput.isEmpty()) {
                    ui.displayError("Errore: il campo ID iscrizione non può essere vuoto.");
                } else {
                    ui.displayError("Errore: ID iscrizione contiene caratteri non validi.");
                }
            }
        }
        controlImpiegato.registrareRimborso(idIscrizione);
        // Note: The controller method already shows success/error messages
    }

    public void annullareLezione() {
        ui.displayMessage("\n--- ANNULLA LEZIONE ---");
        int idLezione;
        while(true){
            String idInput = ui.getInput("Inserisci ID Lezione: ");
            if (ValidationService.isNotEmpty(idInput, "ID lezione") && ValidationService.isOnlyDigits(idInput, "ID lezione")) {
                idLezione = parseInt(idInput);
                // Note: We don't have a direct check method for lezione in the controller
                // We'll let the controller handle validation and just call the method
                break;
            } else {
                // Error messages handled by ValidationService for empty/not digits
                if (idInput.isEmpty()) {
                    ui.displayError("Errore: il campo ID lezione non può essere vuoto.");
                } else {
                    ui.displayError("Errore: ID lezione contiene caratteri non validi.");
                }
            }
        }
        controlImpiegato.annullareLezione(idLezione);
        // Note: The controller method already shows success/error messages
    }

    public void consultareIscritti() {
        controlImpiegato.consultareIscritti();
    }

    public void consultareStatisticheCorsi() {
        controlImpiegato.consultareStatisticheCorsi();
    }

}