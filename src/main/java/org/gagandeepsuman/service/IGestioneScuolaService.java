package org.gagandeepsuman.service;

import java.math.BigDecimal;
import org.gagandeepsuman.entity.EntityImpiegatoSegreteria;


public interface IGestioneScuolaService {
    String generaPasswordTemporanea();
    String generaUsernameUnico(String nome, String cognome);
    void visualizzareCatalogo();
    boolean registrazioneCliente(String nome, String cognome, java.time.LocalDate dataNascita, String email, String telefono);
    boolean iscriversiAlCorso(String linguaCorso, String livelloCorso, int idCliente);
    boolean iscriversiAlCorsoTest(String linguaCorso, String livelloCorso, int idCliente);
    boolean annullareIscrizione(int idCliente, int idIscrizione);
    void registrarePagamento(int idIscrizione);
    void registrareRimborso(int idIscrizione);
    void consultareIscritti();
    void consultareStatisticheIscritti();
    boolean aprireIscrizioni();
    boolean chiusuraIscrizioni();
    boolean isIscrizioniAperte();
    void visualizzareDocenti();
    void aggiungiDocente(String nome, String cognome);
    boolean aggiungiCorso(String linguaCorso, String livelloCorso, int numerMaxPartecipanti, BigDecimal costo, int idDocente);
    boolean aggiornaCorso(int idCorso);
    boolean checkIdIscrizione(int idIscrizione);
    boolean checkIdCliente(int idCliente);
    boolean checkIdCorso(int idCorso);
    void inviarePromemoriaPagamento();
    void inviarePromemoriaIscrizioni();

    // Docente management
    boolean eliminaDocente(int idDocente);
    boolean aggiornaDocente(int idDocente, String nome, String cognome);

    // Course and class management
    boolean eliminaCorso(int idCorso);
    void visualizzareClassi();
    boolean organizzareClasse(int idCorso, int capienza);

    // Statistics
    void consultareStatisticheCorsi();

    // Lesson management
    void annullareLezione(int idLezione);

    // Impiegato Segreteria management
    boolean aggiungiImpiegatoSegreteria(String nome, String cognome, String username, String password);
    java.util.List<EntityImpiegatoSegreteria> elencoImpiegatiSegreteria();
    boolean aggiornaImpiegatoSegreteria(int id, String nome, String cognome, String username, String password);
    boolean eliminaImpiegatoSegreteria(int id);
}