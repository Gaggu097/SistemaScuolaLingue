package org.gagandeepsuman.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.gagandeepsuman.service.IGestioneScuolaService;
import org.gagandeepsuman.dao.DAOFactory;
import org.gagandeepsuman.ui.UserInterface;
import org.gagandeepsuman.ui.ConsoleUserInterface;
import org.gagandeepsuman.util.ValidationService;

import static java.lang.Integer.parseInt;


public class BoundaryCliente {
    private final IGestioneScuolaService controlCliente;
    private final UserInterface ui;

    // Costruttore principale con iniezione di dipendenza
    public BoundaryCliente(UserInterface ui) {
        this.controlCliente = DAOFactory.getGestioneScuolaController();
        this.ui = ui != null ? ui : new ConsoleUserInterface();
    }

    // Costruttore di comodità per backward compatibility
    public BoundaryCliente() {
        this(new ConsoleUserInterface());
    }

    void main(){
        mostraMenu();
    }

    public void mostraMenu() {
        int scelta = -1;
        do {
            ui.displayMessage("\n=== MENU CLIENTE ===");
            ui.displayMessage("1. Visualizza Catalogo Corsi");
            ui.displayMessage("2. Registra Nuovo Cliente");
            ui.displayMessage("3. Iscriviti a un Corso");
            ui.displayMessage("4. Annulla Iscrizione");
            ui.displayMessage("0. Torna al menu principale");
            ui.displayMessage("Seleziona un'opzione: ");

            String input = ui.getInput("");
            try {
                scelta = Integer.parseInt(input);
                gestisciScelta(scelta);
            } catch (NumberFormatException e) {
                ui.displayError("Inserisci un numero valido.");
            }
        } while (scelta != 0);
    }

    private void gestisciScelta(int scelta) {
        switch (scelta) {
            case 1 -> visualizzareCatalogo();
            case 2 -> registrareCliente();
            case 3 -> iscriversiAlCorso();
            case 4 -> annullareIscrizione();
            case 0 -> ui.displayMessage("Ritorno al menu principale...");
            default -> ui.displayError("Opzione non valida.");
        }
    }

    public void visualizzareCatalogo() {
        controlCliente.visualizzareCatalogo();
    }

    public void registrareCliente() {
        ui.displayMessage("\n--- REGISTRAZIONE NUOVO CLIENTE ---");
        String nome;
        while(true){
            nome = ui.getInput("Nome: ");
            if (ValidationService.isNotEmpty(nome, "nome") && ValidationService.isValidLength(nome, 50, "nome")) break;
            // Error messages are handled by ValidationService
        }
        String cognome;
        while(true){
            cognome = ui.getInput("Cognome: ");
            if (ValidationService.isNotEmpty(cognome, "cognome") && ValidationService.isValidLength(cognome, 50, "cognome")) break;
            // Error messages are handled by ValidationService
        }

        String email;
        while(true){
            email = ui.getInput("Email (formato <...@...[.]...>): ");
            if (ValidationService.isNotEmpty(email, "email") && ValidationService.isValidLength(email, 100, "email") && ValidationService.isValidEmail(email, "email")) break;
            // Error messages are handled by ValidationService
        }
        String telefono;
        while(true){
            telefono = ui.getInput("Telefono (formato <+39.....>): ");
            if (ValidationService.isNotEmpty(telefono, "telefono") && ValidationService.isValidLength(telefono, 20, "telefono") && ValidationService.isValidPhone(telefono, "telefono")) break;
            // Error messages are handled by ValidationService
        }

        LocalDate dataNascita = null;
        while (dataNascita == null) {
            String dateInput = ui.getInput("Data di Nascita formato <ANNO-MESE-GIORNO>: ");
            if (ValidationService.isValidDate(dateInput, "data di nascita")) {
                try {
                    // LocalDate.parse expects standard ISO format (YYYY-MM-DD) by default
                    dataNascita = LocalDate.parse(dateInput.trim());
                } catch (DateTimeParseException e) {
                    ui.displayError("Errore: Formato data non valido. Usa AAAA-MM-GG.");
                }
            }
            // Error messages are handled by ValidationService
        }

        boolean esito = controlCliente.registrazioneCliente(nome, cognome, dataNascita, email, telefono);
        if (esito) {
            ui.displayMessage("\nRegistrazione completata con successo!");
        } else {
            ui.displayError("\nErrore durante la registrazione del cliente.");
        }
    }

    public void iscriversiAlCorso() {

        ui.displayMessage("\n--- ISCRIZIONE AL CORSO ---");

        String linguaCorso;
        while(true){
            linguaCorso = ui.getInput("Inserisci Lingua Corso: ");
            if (ValidationService.isNotEmpty(linguaCorso, "lingua corso") && ValidationService.isValidLength(linguaCorso, 30, "lingua corso")) break;
            // Error messages are handled by ValidationService
        }
        String livelloCorso;
        while(true){
            livelloCorso = ui.getInput("Inserisci Livello Corso: ");
            if (ValidationService.isNotEmpty(livelloCorso, "livello corso") && ValidationService.isValidLength(livelloCorso, 10, "livello corso")) break;
            // Error messages are handled by ValidationService
        }

        int idCliente;
        while(true){
            String idInput = ui.getInput("Inserisci Id Cliente: ");
            if (ValidationService.isNotEmpty(idInput, "ID cliente") && ValidationService.isOnlyDigits(idInput, "ID cliente")) {
                idCliente = parseInt(idInput);
                if (ValidationService.isPositive(idCliente, "ID cliente")) {
                    break;
                }
            }
            // Error messages are handled by ValidationService
        }

        boolean esito = controlCliente.iscriversiAlCorso(linguaCorso, livelloCorso, idCliente);
        if (esito) {
            ui.displayMessage("Iscrizione effettuata con successo!");
        }
        else{
            ui.displayError("\nIscrizione non effettuata!");
            // throw new UnsupportedOperationException();
        }
    }

    public int iscriversiAlCorsoTest(String linguaCorso, String livelloCorso, String idCliente) {
        int stato = 1;

        if (linguaCorso.length() > 30) {
            stato+= 2;
            ui.displayError("Errore: linguaCorso deve contenere massimo 30 caratteri");
            return stato;
        }
        else if(!ValidationService.isOnlyLetters(linguaCorso, "lingua corso")){
            ui.displayError("Errore: linguaCorso contiene caratteri non validi");
            stato +=3;
            return stato;
        }

        if(livelloCorso.length() > 10){
            stato += 4;
            ui.displayError("Errore: il livello Corso non deve contenere più di 10 caratteri");
            return stato;
        }
        else if(!ValidationService.isOnlyLettersOrNumbers(livelloCorso, "livello corso")){
            ui.displayError("Errore: il livello corso contiene caratteri non validi");
            stato += 5;
            return stato;
        }

        // controllo su cliente

        // int cliente = parseInt(idCliente);
        if(ValidationService.isOnlyDigits(idCliente, "ID cliente") && (DAOFactory.getClienteDAO().trovaCliente(parseInt(idCliente))) == null){
            // se id cliente contiene solo numeri [0-9] e non viene trovato
            stato += 7;
            ui.displayMessage("ID cliente non trovato.");
            return stato;
        }
        else if(!ValidationService.isOnlyDigits(idCliente, "ID cliente")){
            // se id cliente non contiene solo numeri [0-9]
            ui.displayError("ID cliente contiene caratteri non validi.");
            stato += 8;
            return stato;
        }


        boolean esito = false;
        int cliente = parseInt(idCliente);
        ui.displayMessage("Stato: "+stato);
        // tutti input validi?
        if ( stato == 1){
            ui.displayMessage("Stato: "+stato);
            esito = controlCliente.iscriversiAlCorsoTest(linguaCorso, livelloCorso, cliente);
            return stato;
        }else{
            ui.displayMessage("Stato: " + stato );
            return stato;
        }
        //		if (esito) {
        //			System.out.println("Iscrizione effettuata con successo!");
        //		}
        //		else{
        //			System.out.println("\nIscrizione non effettuata!");
        //			// throw new UnsupportedOperationException();
        //		}
    }

    public void annullareIscrizione() {

        ui.displayMessage("\n--- ANNULLAMENTO ISCRIZIONE ---");
        int idCliente;
        while(true){
            String idInput = ui.getInput("Inserisci ID Cliente: ");
            if (ValidationService.isNotEmpty(idInput, "ID cliente") && ValidationService.isOnlyDigits(idInput, "ID cliente")) {
                idCliente = parseInt(idInput);
                if (controlCliente.checkIdCliente(idCliente)) {
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
                if (controlCliente.checkIdIscrizione(idIscrizione)) {
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
        boolean esito = controlCliente.annullareIscrizione(idCliente, idIscrizione);
        if (esito) {
            ui.displayMessage("[Cliente] Iscrizione annullata con successo!");
        } else {
            ui.displayError("[Cliente] Errore durante l'annullamento dell'iscrizione.");
        }
    }

}