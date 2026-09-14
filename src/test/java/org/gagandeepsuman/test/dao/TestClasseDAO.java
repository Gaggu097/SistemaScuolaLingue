package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.ClasseDAO;
import org.gagandeepsuman.entity.EntityClasse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestClasseDAO {
    private ClasseDAO cDAO;
    private EntityClasse cAggiornata;
    private EntityClasse c;
    private int idClasse;
    @BeforeEach
    void setUp(){
        cDAO = new ClasseDAO();
        cAggiornata = new EntityClasse();
        c = new EntityClasse();
    }
    @Test
    void inserisciClasse(){
        // 1 arrange
        c.setCapienza(12);
        c.setFkIdCorso(252);
        // 2 act
        c = cDAO.salvaClasse(c);
        // 3 assert
        assertNotNull(c, "Classe must be saved to DB for testing");
    }
    @Test
    void trovaClasse(){
        // 1 arrange
        idClasse = 2;
        // 2 act
        c = cDAO.trovaClasse(idClasse);
        // 3 assert
        assertNotNull(c, "Classe must be found on DB for testing");
    }
    @Test
    void aggiornaClasse(){
        // 1 arrange
        idClasse = 2;
        c = cDAO.trovaClasse(idClasse);
        c.setCapienza(7);
        // 2 act
        cAggiornata = cDAO.aggiornaClasse(c);
        // 3 assert
        assertNotNull(cAggiornata, "Classe must be updated on DB for testing");
    }
    @Test
    void nonCancellaClasse(){
        // 1 arrange
        idClasse = 3567; // non esiste
        // 2 act
        boolean esito = cDAO.eliminaClassePerId(idClasse);
        // 3 assert
        assertFalse(esito, "Classe must not be deleted on database because it doesn't exist");
    }
    @Test
    void cancellaClasse(){
        // 1 arrange
        c.setCapienza(99);
        c.setFkIdCorso(252);
        c = cDAO.salvaClasse(c);
        assertNotNull(c, "Classe is created on database");
        // 2 act
        boolean esito = cDAO.eliminaClassePerId(c.getID());
        // 3 assert
        assertTrue(esito, "Classe must be deleted on database because it does exist");
    }


}
