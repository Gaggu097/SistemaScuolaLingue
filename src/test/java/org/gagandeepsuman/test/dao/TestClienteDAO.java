package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.ClienteDAO;
import org.gagandeepsuman.entity.EntityClasse;
import org.gagandeepsuman.entity.EntityCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClienteDAO {
    private EntityCliente c;
    private ClienteDAO cDAO;
    private int idCliente;
    private EntityCliente cAggiornato;

    @BeforeEach
    void setUp(){
        c = new EntityCliente();
        cDAO = new ClienteDAO();
    }
    @Test // inserimento
    void inserisciCliente(){
        // 1 arrange
        c.setNome("Gagandep");
        c.setCognome("Suman");
        c.setDataNascita(LocalDate.of(1997, 7, 12));
        c.setNumeroTelefono("3333333333");
        c.setEmail("gagan@email.com");
        // 2 act
        cDAO.salvaCliente(c);
        // 3 assert
        assertNotNull(c, "cliente must be saved in DB for testing!");
    }
    @Test
    void trovaCliente(){
        // 1 arrange
        idCliente = 1;
        // 2 act
        c = cDAO.trovaCliente(idCliente);
        // 3 assert
        assertNotNull(c, "cliente must be found in DB for testing");
    }
    @Test
    void aggiornaCliente(){
        // 1 arrange
        c = cDAO.trovaCliente(1);
        c.setNome("Gagandeep");

        // 2 act
        cAggiornato = cDAO.aggiornaCliente(c);
        // 3 assert
        assertNotNull(cAggiornato, "cliente must be UPDATED in DB for testing");
    }

    @Test
    void eliminaCliente(){
        // 1 arrange
        idCliente = 52;
        // 2 act
        boolean esito = cDAO.eliminaClientePerId(idCliente);
        // 3 assert
        assertTrue(esito, "cliente must be deleted in DB for testing");
    }
}
