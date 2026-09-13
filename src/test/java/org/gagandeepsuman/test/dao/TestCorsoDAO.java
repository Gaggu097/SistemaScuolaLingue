package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.CorsoDAO;
import org.gagandeepsuman.entity.EntityCorso;

import java.math.BigDecimal;

public class TestCorsoDAO {
    public static void main(String[] args) {
        CorsoDAO corsoDAO = new CorsoDAO();

        // 1. Istanzia un nuovo corso (String per BigDecimal)
        EntityCorso corso = new EntityCorso();
        corso.setLinguaCorso("Punjabi");
        corso.setLivelloCorso("A1");
        corso.setCosto(new BigDecimal("120.00"));
        corso.setNumeroMassimoPartecipanti(30);
        corso.setFKidDocente(1);

        System.out.println("Salvataggio del corso nel database PostgreSQL...");

        // 2. Chiamata al DAO
        EntityCorso corsoSalvato = corsoDAO.salvaCorso(corso);

        // 3. Esito
        if (corsoSalvato != null && corsoSalvato.getID() > 0) {
            System.out.println("###########Corso salvato con successo###########");
            System.out.println("ID Generato: " + corsoSalvato.getID());
            System.out.println("Nome: " + corsoSalvato.getLinguaCorso());
            System.out.println("Costo: €" + corsoSalvato.getCosto());
        } else {
            System.err.println("❌ Errore durante il salvataggio del corso.");
        }
    }
}