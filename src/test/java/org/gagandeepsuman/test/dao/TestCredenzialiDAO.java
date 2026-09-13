package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.CredenzialiDAO;
import org.gagandeepsuman.entity.EntityCredenziali;

//testing
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCredenzialiDAO {
    private EntityCredenziali c;
    private CredenzialiDAO cDAO;
    private int idCredenziali;
    private EntityCredenziali cAggiornato;

    @BeforeEach
    void setUp(){
        c = new EntityCredenziali();
        cDAO = new CredenzialiDAO();
    }
    @Test
        // inserimento
    void inserisciCredenziali(){
        // 1 arrange
        c.setUsername("gagansuma");
        c.setPassword("password");
        c.setClienteId(1);
        // 2 act
        cAggiornato = cDAO.salvaCredenziali(c);
        // 3 assert
        assertNotNull(cAggiornato, "cliente must be saved in DB for testing!");
    }
    @Test
    void trovaCliente(){
        // 1 arrange
        idCredenziali = 102;
        // 2 act
        c = cDAO.trovaCredenziali(idCredenziali);
        // 3 assert
        assertNotNull(c, "cliente must be found in DB for testing");
    }
    @Test
    void aggiornaCliente(){
        // 1 arrange
        c = cDAO.trovaCredenziali(102);
        c.setUsername("gagansuman");
        c.setPassword("drowssap");
        // 2 act
        cAggiornato = cDAO.aggiornaCredenziali(c);
        // 3 assert
        assertNotNull(cAggiornato, "cliente must be UPDATED in DB for testing");
    }
}
