package org.gagandeepsuman.test.dao;

import org.gagandeepsuman.dao.ClasseDAO;
import org.gagandeepsuman.entity.EntityClasse;

public class TestClasseDAO {
    public static void main(String[] args) {
        ClasseDAO cDAO = new ClasseDAO();

        // 1. Istanzia un nuovo calendario
        EntityClasse c = new EntityClasse();
        c.setCapienza(16);
        c.setFkIdCorso(152);

        System.out.println("Salvataggio della classe nel database PostgreSQL...");

        // 2. Chiamata al DAO
        EntityClasse cSalvato = cDAO.salvaClasse(c);

        // 3. Esito
        if (cSalvato != null && cSalvato.getID() > 0) {
            System.out.println("###########Calendario salvato con successo###########");
            System.out.println("ID Generato: " + cSalvato.getID());
            System.out.println("capienza: " + cSalvato.getCapienza());
            System.out.println("FKidCorso: " + cSalvato.getFkIdCorso());
        } else {
            System.err.println("❌ Errore durante il salvataggio della classe.");
        }
    }
}
