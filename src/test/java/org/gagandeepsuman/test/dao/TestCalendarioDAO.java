package org.gagandeepsuman.test.dao;
import org.gagandeepsuman.dao.CalendarioDAO;
import org.gagandeepsuman.entity.EntityCalendarioLezioni;

// per testing
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCalendarioDAO {
    private EntityCalendarioLezioni c;
    private CalendarioDAO cDAO;

    @BeforeEach
    void setUp() {
        c = new EntityCalendarioLezioni();
        cDAO = new CalendarioDAO();
    }

    @Test
    void trovaCalendario() {
        // 1 arrange 2act
        c = cDAO.trovaCalendario(1);
        // assert
        assertEquals(1, c.getID(), "calendario must exist in DB for testing");
    }

    @Test
    void cancellaCalendario() {
        // 1 arrange
        c = cDAO.trovaCalendario(52);
        assertNotNull(c, "calendario must exist in DB for testing");
        // 2 act
        boolean cCancellato = cDAO.eliminaCalendarioPerId(52);
        // 3 assert
        assertTrue(cCancellato, "calendario must be deleted from DB for testing");
    }
}