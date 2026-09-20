package org.gagandeepsuman.test.dao;
import org.gagandeepsuman.boundary.BoundaryCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFunzionale {
    private String linguaCorso;
    private String livelloCorso;
    private String idCliente;
    @BeforeEach
    void setUp(){
        linguaCorso = null;
        livelloCorso = null;
    }

    @Test //1
    void Test1(){
        // tutti input validi
        // 1 arrange
        linguaCorso = "Inglese";
        livelloCorso = "C2";
        idCliente= "402";
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(1, esito);
    }
    @Test
    void Test2(){
        linguaCorso = "aaaaaaaaaa aaaaaaaaaaaaaaa aaaaaa"; // ERRORE
        livelloCorso = "C2";
        idCliente= "402";
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(3,esito); // valore atteso se lingua Corso ha lunghezza > 30
    }

    @Test
    void Test3(){
        linguaCorso = "%&£"; // ERRORE
        livelloCorso = "C2";
        idCliente= "402";
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(4,esito);
        // valore atteso se lingua Corso non deve avere caratteri %&£ speciali
    }
    @Test
    void Test4(){
        linguaCorso = "Inglese";
        livelloCorso = "aaaaaaaaaaa"; // ERRORE livello corso con più di 10 caratteri
        idCliente= "402";
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(5,esito);
        // valore atteso se livello Corso ha deve avere più di 10 caratteri
    }
    @Test
    void Test5(){
        linguaCorso = "Inglese";
        livelloCorso = "%&£"; // ERROR
        idCliente= "402";
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(6,esito);
        // valore atteso se livello Corso ha caratteri %&£ speciali
    }

    @Test
    void Test6(){
        // non testabile per adesso bisogna cambiare il numero di iscritti
        // al corso o un corso dove non c'è disponibilità
        // ##corso 452 (Inglese, C2) cambiato in (Punjabi, A1)##
        linguaCorso = "Punjabi"; // cambiato da, questo corso 452 (Inglese, C2) ha posti disponibili.
        livelloCorso = "A1"; // cambiato da, questo corso 452 ha posti disponibili.
        idCliente= "402";
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act

        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(7,esito);
        // valore atteso se Corso non ha posti disponibili per l'iscrizione
    }
    @Test
    void Test7(){
        // id cliente non trovato
        linguaCorso = "Inglese";
        livelloCorso = "C2";
        idCliente= "14"; // cliente non esiste nel database
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(8,esito);
        // valore atteso se il cliente non esiste nel database
    }

    @Test
    void Test8(){
        // id cliente con caratteri non validi
        linguaCorso = "Inglese";
        livelloCorso = "C2";
        idCliente= "“&5£”"; // cliente non esiste nel database
        BoundaryCliente bc = new BoundaryCliente();
        // 2 act
        int esito = bc.iscriversiAlCorsoTest(linguaCorso,livelloCorso,idCliente);
        // 3 assert
        assertEquals(9,esito);
        // valore atteso se il cliente è una string che contiene caratteri speciali ( non intero )
    }


}
