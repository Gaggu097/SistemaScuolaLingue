package org.gagandeepsuman.test.dao;
import org.gagandeepsuman.dao.CalendarioDAO;
import org.gagandeepsuman.entity.EntityCalendarioLezioni;

import java.time.LocalDate;

public class TestCalendarioDAO {
    public static void main(String[] args) {
        CalendarioDAO cDAO = new CalendarioDAO();

        // 1. Istanzia un nuovo docente
        EntityCalendarioLezioni c = new EntityCalendarioLezioni();
        c.setDataInizio(LocalDate.of(2026,10,26));

        System.out.println("Salvataggio del docente nel database PostgreSQL...");

        // 2. Chiamata al DAO
        EntityCalendarioLezioni docenteSalvato = cDAO.salvaCalendario(c);

        // 3. Esito
        if (docenteSalvato != null && docenteSalvato.getID() > 0) {
            System.out.println("###########Docente salvato con successo###########");
            System.out.println("ID Generato: " + docenteSalvato.getID());
            System.out.println("Nome: " + docenteSalvato.getDataInizio());
            System.out.println("Cognome: " + docenteSalvato.getCorsoIdCorso());
        } else {
            System.err.println("❌ Errore durante il salvataggio del corso.");
        }
    }
}