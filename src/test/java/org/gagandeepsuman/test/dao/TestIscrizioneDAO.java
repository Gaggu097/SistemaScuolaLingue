package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.IscrizioneDAO;
import org.gagandeepsuman.entity.EntityIscrizione;
import java.time.LocalDate;
// testing
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;




public class TestIscrizioneDAO {
    private EntityIscrizione c;
    private IscrizioneDAO cDAO;
    private int idIsc;
    private EntityIscrizione cAggiornato;

    @BeforeEach
    void setUp(){
        c = new EntityIscrizione();
        cDAO = new IscrizioneDAO();
    }
    @Test
        // inserimento
    void inserisciCredenziali(){
        // 1 arrange
        c.setDataIscrizione(LocalDate.of(2026,9,13));
        c.setAnnoAccademico("2026-2027");
        c.setFKidCliente(1);
        c.setFKidCorso(152);
        c.setFKidClasse(1);
        // 2 act
        cAggiornato = cDAO.creaIscrizione(c);
        // 3 assert
        assertNotNull(cAggiornato, "Iscrizione must be saved in DB for testing!");
    }
    @Test
    void trovaCliente(){
        // 1 arrange
        idIsc = 2;
        // 2 act
        c = cDAO.trovaIscrizione(idIsc);
        // 3 assert
        assertNotNull(c, "cliente must be found in DB for testing");
    }
    @Test
    void aggiornaCliente(){
        // 1 arrange
        c = cDAO.trovaIscrizione(2);
        c.setDataIscrizione(LocalDate.of(2026,9,14));
        // 2 act
        cAggiornato = cDAO.aggiornaIscrizione(c);
        // 3 assert
        assertNotNull(cAggiornato, "cliente must be UPDATED in DB for testing");
    }
}

