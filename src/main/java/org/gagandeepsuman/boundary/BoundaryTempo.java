package org.gagandeepsuman.boundary;

import org.gagandeepsuman.service.IGestioneScuolaService;
import org.gagandeepsuman.dao.DAOFactory;
import org.gagandeepsuman.ui.UserInterface;
import org.gagandeepsuman.ui.ConsoleUserInterface;

public class BoundaryTempo {

    private final IGestioneScuolaService controller;
    private final UserInterface ui;

    public BoundaryTempo(UserInterface ui) {
        this.controller = DAOFactory.getGestioneScuolaController();
        this.ui = ui != null ? ui : new ConsoleUserInterface();
    }

    // Costruttore di comodità per backward compatibility
    public BoundaryTempo() {
        this(new ConsoleUserInterface());
    }

    public boolean aprireIscrizioniCorsi() {
        boolean aperte = controller.aprireIscrizioni();
        if (aperte) {
            ui.displayMessage("[BoundaryTempo] Iscrizioni ai corsi aperte con successo.");
        } else {
            ui.displayError("[BoundaryTempo] Errore durante l'apertura delle iscrizioni.");
        }
        return aperte;
    }

    public boolean isIscrizioneAperta() {
        return controller.isIscrizioniAperte();
    }

    public void chiusuraIscrizioniCorsi() {
        boolean chiuse = controller.chiusuraIscrizioni();
        if (chiuse) {
            ui.displayMessage("[BoundaryTempo] Iscrizioni ai corsi chiuse con successo.");
        } else {
            ui.displayError("[BoundaryTempo] Errore durante la chiusura delle iscrizioni.");
        }
    }

    public void inviarePromemoriaPagamento() {
        ui.displayMessage("[BoundaryTempo] Avvio processo invio promemoria pagamenti in sospeso...");
        controller.inviarePromemoriaPagamento();
    }

    public void inviarePromemoriaIscrizioni() {
        ui.displayMessage("[BoundaryTempo] Avvio processo invio promemoria apertura iscrizioni...");
        controller.inviarePromemoriaIscrizioni();
    }

}