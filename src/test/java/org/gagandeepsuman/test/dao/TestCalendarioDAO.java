package org.gagandeepsuman.test.dao;
import org.gagandeepsuman.dao.CalendarioDAO;
import org.gagandeepsuman.entity.EntityCalendarioLezioni;

// per testing
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCalendarioDAO {
    private EntityCalendarioLezioni c;
    private CalendarioDAO cDAO;

    @BeforeEach
    void setUp() {
        c = new EntityCalendarioLezioni();
        cDAO = new CalendarioDAO();
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

    /*
{
    public static void main(String[] args) {

        CalendarioDAO cDAO = new CalendarioDAO();

        // 1. Istanzia un nuovo calendario
        EntityCalendarioLezioni c = new EntityCalendarioLezioni();
        c.setDataInizio(LocalDate.of(2026,10,26));
        c.setCorsoIdCorso(152);

        System.out.println("Salvataggio del calendario nel database PostgreSQL...");

        // 2. Chiamata al DAO
        EntityCalendarioLezioni cSalvato = cDAO.salvaCalendario(c);

        // 3. Esito
        if (cSalvato != null && cSalvato.getID() > 0) {
            System.out.println("###########Calendario salvato con successo###########");
            System.out.println("ID Generato: " + cSalvato.getID());
            System.out.println("dataInizio: " + cSalvato.getDataInizio());
            System.out.println("idCorso: " + cSalvato.getCorsoIdCorso());
        } else {
            System.err.println("❌ Errore durante il salvataggio del corso.");
        }
    }
}
    */
