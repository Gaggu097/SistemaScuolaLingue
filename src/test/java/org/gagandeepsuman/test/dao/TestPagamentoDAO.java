package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.PagamentoDAO;
import org.gagandeepsuman.entity.EntityPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestPagamentoDAO {
    private EntityPagamento d;
    private PagamentoDAO dDAO;
    private EntityPagamento dAggiornato;
    private int idPagamento;
    @BeforeEach
    void setUp(){
        d = new EntityPagamento();
        dDAO = new PagamentoDAO();
        dAggiornato = new EntityPagamento();
    }
    @Test
    void inserisciPagamento(){
        // 1 arrange
        d.setDataPagamento(LocalDate.of(2026,10,30));
        d.setImporto(new BigDecimal("120.00"));
        d.setFkIdIscrizione(2);
        // 2 act
        dAggiornato = dDAO.salvaPagamento(d);
        // 3 assert
        assertNotNull(dAggiornato, "Lezione must be saved to database for testing");
    }
    @Test
    void trovaPagamento(){

        // 1 arrange
        idPagamento = 1;

        // 2 act
        dAggiornato = dDAO.trovaPagamento(idPagamento);
        // 3 assert
        assertNotNull(dAggiornato, "Docente must be found on database for testing");
        System.out.print("Lezione Trovata:" + dAggiornato);
    }

    @Test
    void aggiornaPagamento(){
        // 1 arrange
        idPagamento = 1;
        // 2 act
        d = dDAO.trovaPagamento(idPagamento);
        d.setStatoPagamento(true);
        d.setDataPagamento(LocalDate.of(2026,10,29));
        dAggiornato = dDAO.aggiornaPagamento(d);
        // 3 assert
        assertNotNull(dAggiornato, "Lezione must be updated on database for testing");
        System.out.print("Lezione Trovata:" + dAggiornato);
    }
}