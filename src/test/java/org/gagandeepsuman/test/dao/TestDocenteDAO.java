package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.entity.EntityDocente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDocenteDAO {
    private EntityDocente d;
    private DocenteDAO dDAO;
    private EntityDocente dAggiornato;
    private int idDocente;
    @BeforeEach
    void setUp(){
        d = new EntityDocente();
        dDAO = new DocenteDAO();
        dAggiornato = new EntityDocente();
    }
    @Test
    void inserisciDocente(){
        // 1 arrange
        d.setCognome("Vittorni");
        d.setNome("Valeria");
        // 2 act
        dAggiornato = dDAO.salvaDocente(d);
        // 3 assert
        assertNotNull(dAggiornato, "Docente must be saved to database for testing");
    }
    @Test
    void trovaDocente(){

        // 1 arrange
        idDocente = 2;

        // 2 act
        dAggiornato = dDAO.trovaDocente(idDocente);
        // 3 assert
        assertNotNull(dAggiornato, "Docente must be found on database for testing");
        System.out.print("Docente Trovato:" + dAggiornato);
    }
    @Test
    void aggiornaDocente(){
        // 1 arrange
        idDocente = 2;
        // 2 act
        d = dDAO.trovaDocente(idDocente);
        d.setNome("Vittoriaa");
        dAggiornato = dDAO.aggiornaDocente(d);
        // 3 assert
        assertNotNull(dAggiornato, "Docente must be updated on database for testing");
        System.out.print("Docente Trovato:" + dAggiornato);
    }
}