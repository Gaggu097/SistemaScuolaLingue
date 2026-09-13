package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.entity.EntityDocente;

public class TestDocenteDAO {
    public static void main(String[] args) {
        DocenteDAO dDAO = new DocenteDAO();

        // 1. Istanzia un nuovo docente
        EntityDocente docente = new EntityDocente();
        docente.setNome("Mario");
        docente.setCognome("Rossi");

        System.out.println("Salvataggio del docente nel database PostgreSQL...");

        // 2. Chiamata al DAO
        EntityDocente docenteSalvato = dDAO.salvaDocente(docente);

        // 3. Esito
        if (docenteSalvato != null && docenteSalvato.getID() > 0) {
            System.out.println("###########Docente salvato con successo###########");
            System.out.println("ID Generato: " + docenteSalvato.getID());
            System.out.println("Nome: " + docenteSalvato.getNome());
            System.out.println("Cognome: " + docenteSalvato.getCognome());
        } else {
            System.err.println("❌ Errore durante il salvataggio del corso.");
        }
    }
}