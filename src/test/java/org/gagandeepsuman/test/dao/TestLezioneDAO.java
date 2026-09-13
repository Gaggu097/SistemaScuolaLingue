package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.dao.LezioneDAO;
import org.gagandeepsuman.entity.EntityDocente;
import org.gagandeepsuman.entity.EntityLezione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.entity.EntityDocente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestLezioneDAO {
    private EntityLezione d;
    private LezioneDAO dDAO;
    private EntityLezione dAggiornato;
    private int idLezione;
    @BeforeEach
    void setUp(){
        d = new EntityLezione();
        dDAO = new LezioneDAO();
        dAggiornato = new EntityLezione();
    }
    @Test
    void inserisciLezione(){
        // 1 arrange
        d.setDataLezione(LocalDate.of(2026,10,29));
        d.setOrarioInizio(LocalTime.of(16,0));
        d.setFKidCalendario(1);
        // 2 act
        dAggiornato = dDAO.salvaLezione(d);
        // 3 assert
        assertNotNull(dAggiornato, "Lezione must be saved to database for testing");
    }
    @Test
    void trovaLezione(){

        // 1 arrange
        idLezione = 1;

        // 2 act
        dAggiornato = dDAO.trovaLezione(idLezione);
        // 3 assert
        assertNotNull(dAggiornato, "Docente must be found on database for testing");
        System.out.print("Lezione Trovata:" + dAggiornato);
    }

    @Test
    void aggiornaLezione(){
        // 1 arranLezionege
        idLezione = 1;
        // 2 act
        d = dDAO.trovaLezione(idLezione);
        d.setOrarioInizio(LocalTime.of(10,0));
        dAggiornato = dDAO.aggiornaLezione(d);
        // 3 assert
        assertNotNull(dAggiornato, "Lezione must be updated on database for testing");
        System.out.print("Lezione Trovata:" + dAggiornato);
    }
}