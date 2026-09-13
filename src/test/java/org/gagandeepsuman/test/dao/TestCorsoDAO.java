package org.gagandeepsuman.test.dao;

import jakarta.persistence.Id;
import org.gagandeepsuman.dao.CorsoDAO;
import org.gagandeepsuman.entity.EntityCorso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCorsoDAO {
    private EntityCorso eC;
    private CorsoDAO corsoDAO;
    private EntityCorso cAggiornato;
    private int IdCorso;
    @BeforeEach
    void setUp(){
        eC = new EntityCorso() ;
        corsoDAO = new CorsoDAO();
    }
    @Test
    void inserisciCorso(){
        // 1 arrange
        eC.setLinguaCorso("Inglse");
        eC.setLivelloCorso("A2");
        eC.setNumIscritti(30);
        eC.setFKidDocente(2);
        eC.setNumIscritti(0);
        eC.setCosto(new BigDecimal("89.00"));
        // 2 act
        cAggiornato = corsoDAO.salvaCorso(eC); // salva
        // 3 assert
        assertNotNull (cAggiornato, "Corso must be saved in DB for testing!");
    }

    @Test
    void trovaCorso(){
        // 1 arrange
        IdCorso = 252;
        // 2 act
        eC = corsoDAO.trovaCorso(IdCorso); // trova
        // 3 assert
        assertNotNull (eC, "Corso must be found in DB for testing!");
        System.out.print(eC.getID());
    }
    @Test
    void aggiornaCorso(){
        // 1 arrange
        IdCorso = 252;
        eC = corsoDAO.trovaCorso(IdCorso);
        // 2 act
        eC.setNumeroMassimoPartecipanti(30);
        eC.setCosto(new BigDecimal("99.00"));
        cAggiornato = corsoDAO.aggiornaCorso(eC); // aggiorna
        // 3 assert
        assertNotNull (cAggiornato, "Corso must be updated in DB for testing!");
        System.out.print(
                cAggiornato.getID() + ' ' +cAggiornato.getLinguaCorso() +
                    ' ' + cAggiornato.getLivelloCorso() +
                    ' '+ cAggiornato.getNumeroMassimoPartecipanti() +
                    ' ' + cAggiornato.getCosto() +
                    ' ' + cAggiornato.getFKidDocente() +
                    ' ' +cAggiornato.getNumIscritti()
        );
    }
    @Test
    void eliminaCorso(){
        // 1 arrange
        IdCorso = 302;
        eC = corsoDAO.trovaCorso(IdCorso);
        // 2 act
        boolean esito = corsoDAO.eliminaCorsoPerId(IdCorso); // cancella
        // 3 assert
        assertNotNull (esito, "Corso must be updated in DB for testing!");
    }
}