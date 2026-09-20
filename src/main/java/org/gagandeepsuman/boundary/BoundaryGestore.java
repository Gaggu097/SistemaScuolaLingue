package org.gagandeepsuman.boundary;

import org.gagandeepsuman.service.IGestioneScuolaService;
import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.dao.DAOFactory;
import org.gagandeepsuman.entity.EntityDocente;
import org.gagandeepsuman.entity.EntityCorso;
import org.gagandeepsuman.ui.UserInterface;
import org.gagandeepsuman.ui.ConsoleUserInterface;
import org.gagandeepsuman.util.ValidationService;

import java.math.BigDecimal;

import static java.lang.Integer.parseInt;

public class BoundaryGestore{

    private final IGestioneScuolaService controlGestore;
    private final UserInterface ui;

    public BoundaryGestore(UserInterface ui) {
        this.controlGestore = DAOFactory.getGestioneScuolaController();
        this.ui = ui != null ? ui : new ConsoleUserInterface();
    }

    // Costruttore di comodità per backward compatibility
    public BoundaryGestore() {
        this(new ConsoleUserInterface());
    }

    public void main(){
        mostraMenuGestore();;
    }

    public void mostraMenuGestore() {
        int scelta = -1;
        do {
            ui.displayMessage("\n=== MENU Gestore ===");
            ui.displayMessage("1. Gestione Catalogo Corsi");
            ui.displayMessage("2. Gestione Impiegato");
            ui.displayMessage("3. Organizzare Classi");
            ui.displayMessage("0. Torna al menu principale");
            ui.displayMessage("Seleziona un'opzione: ");

            String input = ui.getInput("");
            try {
                scelta = parseInt(input);
                gestisciSceltaPrincipale(scelta);
            } catch (NumberFormatException e) {
                ui.displayError("Inserisci un numero valido.");
            }
        } while (scelta != 0);
    }

    public void mostraMenuGestioneImpiegati() {
        int scelta = -1;
        do {
            ui.displayMessage("\n=== MENU Gestore ===");
            ui.displayMessage("1. Aggiungi Docente");
            ui.displayMessage("2. Trova Docente");
            ui.displayMessage("3. Aggiorna Docente");
            ui.displayMessage("4. Elimina Docente");
            ui.displayMessage("0. Torna al menu principale");
            ui.displayMessage("Seleziona un'opzione: ");

            String input = ui.getInput("");
            try {
                scelta = parseInt(input);
                gestisciSceltaMenuImpiegati(scelta);
            } catch (NumberFormatException e) {
                ui.displayError("Inserisci un numero valido.");
            }
        } while (scelta != 0);
    }

    public void mostraMenuGestCorsi() {
        int scelta = -1;
        do {
            ui.displayMessage("\n=== MENU Gestore ===");
            ui.displayMessage("1. Visualizzare Catalogo Corsi");
            ui.displayMessage("2. Aggiungere Corso");
            ui.displayMessage("3. Aggiornare Corso");
            ui.displayMessage("4. Eliminare Corso");
            ui.displayMessage("0. Torna al menu principale");
            ui.displayMessage("Seleziona un'opzione: ");

            String input = ui.getInput("");
            try {
                scelta = parseInt(input);
                gestisciSceltaGestioneCatalogo(scelta);
            } catch (NumberFormatException e) {
                ui.displayError("Inserisci un numero valido.");
            }
        } while (scelta != 0);
    }

    private void gestisciSceltaPrincipale(int scelta) {
        switch (scelta) {
            case 1 -> gestireCatalogoCorsi();
            case 2 -> gestireImpiegato();
            case 3 -> organizzareClassi();
            case 0 -> ui.displayMessage("Ritorno al menu principale...");
            default -> ui.displayError("Opzione non valida.");
        }
    }
    private void gestisciSceltaGestioneCatalogo(int scelta) {
        switch (scelta) {
            case 1 -> visualizzareCatalogoCorsi();
            case 2 -> aggiungereCorso();
            case 3 -> aggiornareCorso();
            case 4 -> eliminareCorso();
            case 0 -> ui.displayMessage("Ritorno al menu principale...");
            default -> ui.displayError("Opzione non valida.");
        }
    }
    private void gestisciSceltaMenuImpiegati(int scelta) {
        switch (scelta) {
            case 1 -> aggiungereDocente();
            case 2 -> visualizzareDocenti();
            case 3 -> aggiornareDocente();
            case 4 -> eliminareDocente();
            case 0 -> ui.displayMessage("Ritorno al menu principale...");
            default -> ui.displayError("Opzione non valida.");
        }
    }

    private void eliminareDocente() {
        ui.displayMessage("\n--- ELIMINA DOCENTE ---");

        // Show available docenti
        controlGestore.visualizzareDocenti();

        // Ask for docente ID to delete
        String idInput = ui.getInput("\nInserisci ID del docente da eliminare: ");
        try {
            int idDocente = parseInt(idInput);
            // Verify docente exists
            EntityDocente docente = DAOFactory.getDocenteDAO().trovaDocente(idDocente);
            if (docente == null) {
                ui.displayError("[bGestore] Errore: ID docente non esistente.");
                return;
            }

            // Confirm deletion
            ui.displayMessage("Sei sicuro di voler eliminare il docente: " + docente.getNome() + " " + docente.getCognome() + "? (S/N)");
            String conferma = ui.getInput("").toUpperCase();
            if (conferma.equals("S")) {
                boolean esito = controlGestore.eliminaDocente(idDocente);
                if (esito) {
                    ui.displayMessage("[bGestore] Docente eliminato con successo!");
                } else {
                    ui.displayError("[bGestore] Errore durante l'eliminazione del docente.");
                }
            } else {
                ui.displayMessage("Eliminazione annullata.");
            }
        } catch (NumberFormatException e) {
            ui.displayError("Errore: ID docente non valido");
        }
    }

    private void aggiornareDocente() {
        ui.displayMessage("\n--- AGGIORNA DOCENTE ---");

        // Show available docenti
        controlGestore.visualizzareDocenti();

        // Ask for docente ID to update
        String idInput = ui.getInput("\nInserisci ID del docente da aggiornare: ");
        try {
            int idDocente = parseInt(idInput);
            // Verify docente exists
            EntityDocente docente = DAOFactory.getDocenteDAO().trovaDocente(idDocente);
            if (docente == null) {
                ui.displayError("[bGestore] Errore: ID docente non esistente.");
                return;
            }

            // Get new nome
            String nome;
            while(true){
                nome = ui.getInput("Nuovo nome docente (attuale: " + docente.getNome() + "): ");
                if (ValidationService.isNotEmpty(nome, "nome docente") && ValidationService.isValidLength(nome, 30, "nome docente")) break;
                // Error messages are handled by ValidationService
            }

            // Get new cognome
            String cognome;
            while(true){
                cognome = ui.getInput("Nuovo cognome docente (attuale: " + docente.getCognome() + "): ");
                if (ValidationService.isNotEmpty(cognome, "cognome docente") && ValidationService.isValidLength(cognome, 10, "cognome docente")) break;
                // Error messages are handled by ValidationService
            }

            boolean esito = controlGestore.aggiornaDocente(idDocente, nome, cognome);
            if (esito) {
                ui.displayMessage("[bGestore] Docente aggiornato con successo!");
            } else {
                ui.displayError("[bGestore] Errore durante l'aggiornamento del docente.");
            }
        } catch (NumberFormatException e) {
            ui.displayError("Errore: ID docente non valido");
        }
    }

    private void aggiungereDocente() {
        ui.displayMessage("\n--- Aggiunta NUOVO Docente ---");
        String nome;
        while(true){
            nome = ui.getInput("Nome Docente: ");
            if (ValidationService.isNotEmpty(nome, "nome docente") && ValidationService.isValidLength(nome, 30, "nome docente")) break;
            // Error messages are handled by ValidationService
        }
        String cognome;
        while(true){
            cognome = ui.getInput("Cognome Docente: ");
            if (ValidationService.isNotEmpty(cognome, "cognome docente") && ValidationService.isValidLength(cognome, 10, "cognome docente")) break;
            // Error messages are handled by ValidationService
        }
        controlGestore.aggiungiDocente(nome, cognome);

    }

    private void visualizzareDocenti() {
        controlGestore.visualizzareDocenti();
    }

    private void eliminareCorso() {
        ui.displayMessage("\n--- ELIMINA CORSO ---");

        // Show available courses
        controlGestore.visualizzareCatalogo();

        // Ask for course ID to delete
        String idInput = ui.getInput("\nInserisci ID del corso da eliminare: ");
        try {
            int idCorso = parseInt(idInput);
            // Verify corso exists
            EntityCorso corso = DAOFactory.getCorsoDAO().trovaCorso(idCorso);
            if (corso == null) {
                ui.displayError("[bGestore] Errore: ID corso non esistente.");
                return;
            }

            // Show course details for confirmation
            DocenteDAO dDAO = DAOFactory.getDocenteDAO();
            EntityDocente prof = dDAO.trovaDocente(corso.getFKidDocente());
            ui.displayMessage("\nCorso selezionato:");
            ui.displayMessage("ID: " + corso.getID());
            ui.displayMessage("Lingua: " + corso.getLinguaCorso());
            ui.displayMessage("Livello: " + corso.getLivelloCorso());
            ui.displayMessage("Costo: €" + corso.getCosto());
            ui.displayMessage("Posti massima: " + corso.getNumeroMassimoPartecipanti());
            ui.displayMessage("Docente: " + prof.getNome() + " " + prof.getCognome());

            // Confirm deletion
            ui.displayMessage("\nSei sicuro di voler eliminare questo corso? (S/N)");
            String conferma = ui.getInput("").toUpperCase();
            if (conferma.equals("S")) {
                boolean esito = controlGestore.eliminaCorso(idCorso);
                if (esito) {
                    ui.displayMessage("[bGestore] Corso eliminato con successo!");
                } else {
                    ui.displayError("[bGestore] Errore durante l'eliminazione del corso.");
                }
            } else {
                ui.displayMessage("Eliminazione annullata.");
            }
        } catch (NumberFormatException e) {
            ui.displayError("Errore: ID corso non valido");
        }
    }

    private void aggiornareCorso() {
        ui.displayMessage("\n--- AGGIORNAMENTO CORSO ---");

        // Show available courses
        controlGestore.visualizzareCatalogo();

        // Ask for course ID to update
        String idInput = ui.getInput("\nInserisci ID del corso da aggiornare: ");
        try {
            int idCorso = parseInt(idInput);
            // Update the course
            boolean esito = controlGestore.aggiornaCorso(idCorso);
            if (esito) {
                ui.displayMessage("[bGestore] Corso aggiornato con successo!");
            } else {
                ui.displayError("[bGestore] Errore durante l'aggiornamento del corso.");
            }
        } catch (NumberFormatException e) {
            ui.displayError("Errore: ID corso non valido");
        }
    }

    public void aggiungereCorso() {
        ui.displayMessage("\n--- Aggiunta NUOVO Corso ---");
        String linguaCorso;
        while(true){
            linguaCorso = ui.getInput("Lingua Corso: ");
            if (ValidationService.isNotEmpty(linguaCorso, "lingua corso") && ValidationService.isValidLength(linguaCorso, 30, "lingua corso")) break;
            // Error messages are handled by ValidationService
        }
        String livelloCorso;
        while(true){
            livelloCorso = ui.getInput("Livello Corso: ");
            if (ValidationService.isNotEmpty(livelloCorso, "livello corso") && ValidationService.isValidLength(livelloCorso, 10, "livello corso")) break;
            // Error messages are handled by ValidationService
        }

        int numerMaxPartecipanti;
        while(true){
            String numInput = ui.getInput("Numero partecipanti (intero tra 10 e 750): ");
            if (ValidationService.isNotEmpty(numInput, "numero partecipanti") && ValidationService.isOnlyDigits(numInput, "numero partecipanti")) {
                numerMaxPartecipanti = parseInt(numInput);
                if (ValidationService.isInRange(numerMaxPartecipanti, 10, 750, "numero partecipanti")) {
                    break;
                }
            }
            // Error messages are handled by ValidationService
        }

        BigDecimal costo;
        while(true){
            String costoInput = ui.getInput("Costo (tra [50 e 500]€ <30.00>): ");
            if (ValidationService.isNotEmpty(costoInput, "costo") && ValidationService.isValidLength(costoInput, 10, "costo")) { // Length check for reasonable input size
                try {
                    costo = new BigDecimal(costoInput);
                    if (ValidationService.isInRange(costo, new BigDecimal("50"), new BigDecimal("500"), "costo")) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    ui.displayError("Errore: valore non valido per il costo.");
                }
            }
            // Error messages are handled by ValidationService
        }

        int idDocente;
        while(true){
            String idInput = ui.getInput("idDocente valido: ");
            if (ValidationService.isNotEmpty(idInput, "ID docente") && ValidationService.isOnlyDigits(idInput, "ID docente")) {
                idDocente = parseInt(idInput);
                if (ValidationService.isPositive(idDocente, "ID docente")) {
                    EntityDocente eD = DAOFactory.getDocenteDAO().trovaDocente(idDocente);
                    if (eD != null) {
                        break;
                    } else {
                        ui.displayError("Errore: ID docente non esistente.");
                    }
                } else {
                    ui.displayError("Errore: ID docente deve essere maggiore di zero.");
                }
            } else {
                // Error messages handled by ValidationService for empty/not digits
                if (idInput.isEmpty()) {
                    ui.displayError("Errore: il campo ID docente non può essere vuoto.");
                } else {
                    ui.displayError("Errore: ID docente contiene caratteri non validi.");
                }
            }
        }

        boolean esito = controlGestore.aggiungiCorso(linguaCorso, livelloCorso, numerMaxPartecipanti, costo, idDocente);
        if (esito) {
            ui.displayMessage("[bGestore]Registrazione completata con successo!");
        } else {
            ui.displayError("[bGestore]Errore durante la registrazione del cliente.");
        }
    }

    private void visualizzareCatalogoCorsi() {
        controlGestore.visualizzareCatalogo();
    }

    public void gestireCatalogoCorsi () {
        mostraMenuGestCorsi();
    }

    public void gestireImpiegato () {
        mostraMenuGestioneImpiegati();
    }

    public void organizzareClassi () {
        int scelta = -1;
        do {
            ui.displayMessage("\n=== ORGANIZZARE CLASSI ===");
            ui.displayMessage("1. Visualizza classi esistenti");
            ui.displayMessage("2. Organizza nuova classe");
            ui.displayMessage("0. Torna al menu precedente");
            ui.displayMessage("Seleziona un'opzione: ");

            String input = ui.getInput("");
            try {
                scelta = parseInt(input);
                gestisciSceltaOrganizzareClassi(scelta);
            } catch (NumberFormatException e) {
                ui.displayError("Inserisci un numero valido.");
            }
        } while (scelta != 0);
    }

    private void gestisciSceltaOrganizzareClassi(int scelta) {
        switch (scelta) {
            case 1 -> visualizzareClassiExistenti();
            case 2 -> organizzareNuovaClasse();
            case 0 -> ui.displayMessage("Ritorno al menu precedente...");
            default -> ui.displayError("Opzione non valida.");
        }
    }

    private void visualizzareClassiExistenti() {
        controlGestore.visualizzareClassi();
    }

    private void organizzareNuovaClasse() {
        ui.displayMessage("\n--- ORGANIZZA NUOVA CLASSE ---");

        // Show available courses
        controlGestore.visualizzareCatalogo();

        // Ask for course ID
        String idInput = ui.getInput("\nInserisci ID del corso per cui organizzare la classe: ");
        try {
            int idCorso = parseInt(idInput);
            // Verify corso exists
            EntityCorso corso = DAOFactory.getCorsoDAO().trovaCorso(idCorso);
            if (corso == null) {
                ui.displayError("[bGestore] Errore: ID corso non esistente.");
                return;
            }

            // Ask for capacity
            String capInput = ui.getInput("Inserisci capienza della classe (numero di posti): ");
            try {
                int capienza = parseInt(capInput);
                if (capienza <= 0) {
                    ui.displayError("Errore: la capienza deve essere maggiore di zero.");
                    return;
                }

                boolean esito = controlGestore.organizzareClasse(idCorso, capienza);
                if (esito) {
                    ui.displayMessage("[bGestore] Classe organizzata con successo!");
                } else {
                    ui.displayError("[bGestore] Errore durante l'organizzazione della classe.");
                }
            } catch (NumberFormatException e) {
                ui.displayError("Errore: capienza non valida");
            }
        } catch (NumberFormatException e) {
            ui.displayError("Errore: ID corso non valido");
        }
    }

}