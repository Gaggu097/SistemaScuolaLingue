package org.gagandeepsuman.controller;

import org.gagandeepsuman.boundary.BoundaryTempo;
import org.gagandeepsuman.dao.*;
import org.gagandeepsuman.dao.DAOFactory;
import org.gagandeepsuman.entity.*;
import org.gagandeepsuman.service.IGestioneScuolaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.text.Normalizer;
import java.util.Locale;
import java.security.SecureRandom;

@Service
public class GestioneScuolaController implements IGestioneScuolaService {

    public String generaPasswordTemporanea() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }
        return password.toString();
    }


    public String generaUsernameUnico(String nome, String cognome) {
        // 1. Clean and normalize strings (remove accents, spaces, and special characters)
        String cleanNome = pulisciStringa(nome);
        String cleanCognome = pulisciStringa(cognome);

        // Base format: "mario.rossi"
        String baseUsername = cleanNome + "." + cleanCognome;
        String candidateUsername = baseUsername;
        int counter = 1;

        // 2. Check DB uniqueness via DAO and append counter if taken
        CredenzialiDAO credenzialiDAO = DAOFactory.getCredenzialiDAO();
        while (credenzialiDAO.esisteUsername(candidateUsername)) {
            candidateUsername = baseUsername + counter; // e.g., mario.rossi1, mario.rossi2
            counter++;
        }
        return candidateUsername;
    }


    private String pulisciStringa(String text) {
        if (text == null) return "";
        // Normalize unicode accents (e.g., "é" -> "e") and remove non-alphanumeric chars
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase(Locale.ROOT);
    }
    public void visualizzareCatalogo() {
        // dal database leggi tutti i corsi
        List<EntityCorso> E;
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        E = corsoDAO.trovaTuttiCorsi();
        // TODO - implement GestioneScuolaController.visulizzareCatalogo
        if (E == null) {
            System.out.println("[gsController] ]Nessun corso attualmente Trovato/disponibile.");
        } else {
            DocenteDAO dDAO = DAOFactory.getDocenteDAO();
            EntityDocente prof;
            for (EntityCorso c : E) {
                // leggi anche le generalità del docente del corso
                // DocenteDAO dDAO = new DocenteDAO();
                prof =  dDAO.trovaDocente(c.getFKidDocente());
                System.out.printf("\n##############################" +
                                "\nID: %d |" +
                                "\nLingua Corso: %s |" +
                                "\nLivello Corso: %s |" +
                                "\nCosto: €%.2f | " +
                                "\nPosti Max: %d | " +
                                "\nCognome Docente Corso: %s | " +
                                "\nNome Docente Corso: %s%n|" +
                                "##############################\n",
                        c.getID(), c.getLinguaCorso(), c.getLivelloCorso(),
                        c.getCosto(), c.getNumeroMassimoPartecipanti(),
                        prof.getCognome(), prof.getNome());
            }
        }
    }

    public boolean registrazioneCliente(String nome, String cognome, LocalDate dataNascita, String email, String telefono) {
        // crea Cliente
        EntityCliente eC = new EntityCliente();
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO(); // crea oggetto DAO
        int idCliente;

        eC.setNome(nome); eC.setCognome(cognome); eC.setEmail(email); // crea entità Cliente
        eC.setDataNascita(dataNascita); eC.setNumeroTelefono(telefono);



        EntityCliente clSalvato = clienteDAO.salvaCliente(eC); //salva Cliente su database
        if (clSalvato == null){
            System.err.print("[gsController] Errore salvataggio DB");
            return false;
        }
        idCliente = clSalvato.getID();
        if (idCliente > 0){
            // crea credenziali Cliente
            System.out.print("[gsController]Cliente salvato con successo!");

            // crea credenziali per l'utente
            EntityCredenziali eCr = new EntityCredenziali();
            CredenzialiDAO credenzialiDAO = DAOFactory.getCredenzialiDAO();

            // creazione Credenziali uniche
            eCr.setClienteId(idCliente);
            eCr.setUsername(generaUsernameUnico(nome, cognome));

            String plainPassword = generaPasswordTemporanea();
            String hashedPassword = org.gagandeepsuman.util.PasswordSecurity.hashPassword(plainPassword);
            eCr.setPassword(hashedPassword);

            // salva credenziali
            EntityCredenziali eCrSalvato = credenzialiDAO.salvaCredenziali(eCr);
            if (eCrSalvato==null){
                System.err.print("[gsController] Errore salvataggio Credenziali DB");
                return false;
            }
            else { // credenziali salvate
                System.out.print("[gsController]Credenziali salvate con successo");
                System.out.print("\n[gsController] IdCliente: " + clSalvato.getID()
                        +"\n[gsController] Username: " + eCrSalvato.getUsername()
                        + "\nPassword temporanea (Salvala, non sarà più recuperabile in chiaro!): " + plainPassword);
                return true;
            }
        }else{
            System.out.print("[gsController][ERRORE]Cliente non salvato!");
            return false;
        }
    }

    public boolean iscriversiAlCorso(String linguaCorso, String livelloCorso, int idCliente) {
        // TODO - implement GestioneScuolaController.iscriversiAlCorso
        EntityCorso eC ;
        EntityDocente eDoc ;
        int idDocente;
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        EntityCliente eCliente = DAOFactory.getClienteDAO().trovaCliente(idCliente);
        if (eCliente == null){
            System.out.println("[gsController]Cliente non trovato");
            return false;
        }
        else {
            System.out.println("[gsController]Cliente trovato");
        }

        eC = corsoDAO.trovaPerLinguaELivello(linguaCorso, livelloCorso);

        if ( eC == null){
            System.out.println("[gsController]Corso non Trovato!");
            return false; // Corso non trovato
        }
        // corso trovato
        idDocente = eC.getFKidDocente();
        if ( idDocente > -1){
            // id docente trovato da corso
            System.out.println("[gsController]Docente trovato");
        }
        else{
            System.out.println("[gsController]Errore, docente non trovato");
            return false;
        }
        DocenteDAO dDAO = DAOFactory.getDocenteDAO();
        eDoc = dDAO.trovaDocente(idDocente);

        System.out.printf("ID: %d | Lingua Corso: %s |Livello Corso: %s |Costo: €%.2f | Posti Max: %d | Nome Docente Corso: %s | Cognome Docente Corso: %s%n",
                eC.getID(), eC.getLinguaCorso(), eC.getLivelloCorso(), eC.getCosto(), eC.getNumeroMassimoPartecipanti(), eDoc.getCognome(), eDoc.getNome());

        // Assume confirmation is given for web
        // Skip scanner input
            if ( new BoundaryTempo().isIscrizioneAperta() ){
                // S
                System.out.println("\nIscrizioni Aperte\n!");

                if (eC.verificaDisponbilitaPosto() >= 1) {
                    // verifica iscrizioni aperte
                    System.out.println("[gsController]Procedo con l'iscrizione");
                    // crea iscrizione
                    EntityIscrizione eI = new EntityIscrizione();
                    // ANNO ACCADEMICO
                    int anno = LocalDate.now().getYear();
                    int idIscrizione ;
                    // DATA ISCRIZIONE
                    eI.setDataIscrizione(LocalDate.now());
                    // idCliente
                    eI.setFKidCliente(idCliente);
                    // idCorso
                    eI.setFKidCorso(eC.getID());

                    // crea oggetto DAO
                    IscrizioneDAO iDAO = DAOFactory.getIscrizioneDAO();

                    // Prepara il pagamento
                    EntityPagamento eP = new EntityPagamento();
                    eP.setStatoPagamento(false);
                    eP.setImporto(eC.getCosto());

                    // Esegue il salvataggio transazionale
                    boolean successo = iDAO.creaIscrizioneTransazionale(eI, eP, eC);
                    if (successo) {
                        System.out.println("[gsController] Iscrizione e pagamento creati con successo in un'unica transazione. IdIscrizione: " + eI.getID());
                        return true;
                    } else {
                        System.out.println("[gsController] Errore durante la creazione dell'iscrizione (Rollback effettuato).");
                        return false;
                    }
                } else{ // disponibilità < 1
                    System.out.println("\n[gsController]Iscrizione Annullata, NON ci sono posti disponibili per l'iscrizione al corso\n!");
                    return false;
                }
            }else {
                System.out.println("[BCliente]Iscrizioni Chiuse\nNon si può iscrivere al corso!!");
                return false;
            }
        // throw new UnsupportedOperationException();
    }

    public boolean iscriversiAlCorsoTest(String linguaCorso, String livelloCorso, int idCliente) {
        // TODO - implement GestioneScuolaController.iscriversiAlCorso
        EntityCorso eC ;
        EntityDocente eDoc ;
        int idDocente;
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        EntityCliente eCliente = DAOFactory.getClienteDAO().trovaCliente(idCliente);
        if (eCliente == null){
            System.out.println("[gsController]Cliente non trovato");
            return false;
        }
        else {
            System.out.println("[gsController]Cliente trovato");
        }

        eC = corsoDAO.trovaPerLinguaELivello(linguaCorso, livelloCorso);

        if ( eC == null){
            System.out.println("[gsController]Corso non Trovato!");
            return false; // Corso non trovato
        }
        // corso trovato
        idDocente = eC.getFKidDocente();
        if ( idDocente > -1){
            // id docente trovato da corso
            System.out.println("[gsController]Docente trovato");
        }
        else{
            System.out.println("[gsController]Errore, docente non trovato");
            return false;
        }
        DocenteDAO dDAO = DAOFactory.getDocenteDAO();
        eDoc = dDAO.trovaDocente(idDocente);

        System.out.printf("ID: %d | Lingua Corso: %s |Livello Corso: %s |Costo: €%.2f | Posti Max: %d | Nome Docente Corso: %s | Cognome Docente Corso: %s%n",
                eC.getID(), eC.getLinguaCorso(), eC.getLivelloCorso(), eC.getCosto(), eC.getNumeroMassimoPartecipanti(), eDoc.getCognome(), eDoc.getNome());

    //		String inputConfermaUtente;
    //		Scanner scanner = new Scanner(System.in);
    //		inputConfermaUtente = scanner.nextLine();
    //		if (inputConfermaUtente == null) {
    //			System.err.println("[gsController]Scegli tra valori validi Y o n");
    //			return false;
    //		}
            // conferma iscrizione da parte del cliente
            // se Y
    //		else if (inputConfermaUtente.equalsIgnoreCase("y"))
            { // caso Y o y
                if ( new BoundaryTempo().isIscrizioneAperta() ){
                    // S
                    System.out.println("\nIscrizioni Aperte\n!");

                    if (eC.verificaDisponbilitaPosto() >= 1) {
                        // verifica iscrizioni aperte
                        System.out.println("[gsController]Procedo con l'iscrizione");
                        // crea iscrizione
                        EntityIscrizione eI = new EntityIscrizione();
                        // ANNO ACCADEMICO
                        int anno = LocalDate.now().getYear();
                        int idIscrizione ;
                        // DATA ISCRIZIONE
                        eI.setDataIscrizione(LocalDate.now());
                        // idCliente
                        eI.setFKidCliente(idCliente);
                        // idCorso
                        eI.setFKidCorso(eC.getID());

                        // crea oggetto DAO
                        IscrizioneDAO iDAO = DAOFactory.getIscrizioneDAO();

                        // Prepara il pagamento
                        EntityPagamento eP = new EntityPagamento();
                        eP.setStatoPagamento(false);
                        eP.setImporto(eC.getCosto());

                        // Esegue il salvataggio transazionale
                        boolean successo = iDAO.creaIscrizioneTransazionale(eI, eP, eC);
                        if (successo) {
                            System.out.println("[gsController] Iscrizione e pagamento creati con successo in un'unica transazione. IdIscrizione: " + eI.getID());
                            return true;
                        } else {
                            System.out.println("[gsController] Errore durante la creazione dell'iscrizione (Rollback effettuato).");
                            return false;
                        }
                    } else{ // disponibilità < 1
                        System.out.println("\n[gsController]Iscrizione Annullata, NON ci sono posti disponibili per l'iscrizione al corso\n!");
                        //throw new UnsupportedOperationException("Nessuna Disponibilità per il corso con ID:"+ eC.getID());
                        return false;
                    }
                }else {
                    System.out.println("[BCliente]Iscrizioni Chiuse\nNon si può iscrivere al corso!!");
                    return false;
                }
            }
        //		else if(inputConfermaUtente.equalsIgnoreCase("n")) { // caso N o n
        //			// se n
        //			System.out.println("\n[BCliente]Iscrizione Annullata dal Cliente\n");
        //			return false;
        //		}
        //		else{
        //			System.out.println("\n[BCliente]Scelta Cliente non valida\n");
        //			return false;
        //		}
            // throw new UnsupportedOperationException();
        }

    public boolean annullareIscrizione(int idCliente, int idIscrizione) {
        // TODO - implement GestioneScuolaController.annullareIscrizione
        EntityCliente eCliente;
        ClienteDAO clienteDAO = DAOFactory.getClienteDAO();
        eCliente = clienteDAO.trovaCliente(idCliente);
        if (eCliente == null) {
            System.out.println("[BCliente]Cliente non trovato");
            return false;
        }
        EntityIscrizione iscrizione;
        IscrizioneDAO iscrizioneDAO = DAOFactory.getIscrizioneDAO();
        iscrizione = iscrizioneDAO.trovaIscrizione(idIscrizione);		// cerca l'iscrizione
        if (iscrizione == null) {
            System.out.println("[BCliente]Iscrizione non trovata");
            return false;
        }
        System.out.println("[BCliente]Iscrizione Trovata");
        // cancellazione "soft" dell'iscrizione
        if (iscrizione.getDeleted_at() != null) { // iscrizione è gà annullata
            System.out.println("[BCliente] Iscrizione è già annullata");
            return false;
        }else{											// iscrizione non è annullata
            iscrizione.setDeleted_at(OffsetDateTime.now());
            // aggiorna iscrizione -> cancellazione "soft"
            iscrizione = iscrizioneDAO.aggiornaIscrizione(iscrizione);
        }
        // controlla se l'iscrizione è stata annullata con successo.
        if ( iscrizione.getDeleted_at() == null){
            System.out.println("[BCliente]Iscrizione non annullata. c'è stato un ERRORE");
            return false;
        }else {
            System.out.println("[BCliente]Iscrizione Annullata con successo e salvata su DB");
            return true;
        }
    }

    public void registrarePagamento(int idIscrizione) {
        IscrizioneDAO iscrizioneDAO = DAOFactory.getIscrizioneDAO();
        // verifico che esiste il pagamento legato all'iscrizione e che l'iscrizione non sia annullata
        EntityIscrizione iscrizione = iscrizioneDAO.trovaIscrizione(idIscrizione);
        if ( iscrizione.getDeleted_at() == null ){ // iscrizione non annullata
            PagamentoDAO pagamentoDAO= DAOFactory.getPagamentoDAO();
            EntityPagamento pagamento = pagamentoDAO.trovaPagamentoPerIscrizione(idIscrizione);
            if (pagamento.getStatoPagamento()){
                System.out.println("[gsController] L'isccrizione risulta già PAGATA");
            }else{
                pagamento.setStatoPagamento(true);
                pagamento.setDataPagamento(LocalDate.now());
                pagamento = pagamentoDAO.aggiornaPagamento(pagamento);
                if (pagamento== null){ // salvo pagamento su database
                    System.err.println("[gsController] Errore durante salvataggio del Pagamento");
                }
                else {
                    System.out.println("[gsController] Pagamento SALVATO CON SUCCESSO NEL DB");
                }
            }
        }else{
            System.err.println("[gsController] Errore! Iscrizione Annullata non è possibile procedere col pagamento");
        }
    }

    public void registrareRimborso(int idIscrizione) {
        PagamentoDAO pagamentoDAO = DAOFactory.getPagamentoDAO();
        // verifico che esiste il pagamento legato all'iscrizione
        EntityPagamento pagamento = pagamentoDAO.trovaPagamentoPerIscrizione(idIscrizione);
        if (pagamento == null) {
            System.out.println("[gsController] Nessun pagamento trovato per l'iscrizione ID: " + idIscrizione);
            return;
        }
        // controlla se il pagamento è stato effettuato
        if (!pagamento.getStatoPagamento()){
            System.out.println("[gsController] Il pagamento per l'iscrizione ID: " + idIscrizione + " non è stato ancora effettuato.");
            return;
        }
        // procedi con il rimborso: riporta lo stato pagamento a false (non pagato)
        pagamento.setStatoPagamento(false);
        pagamento.setDataPagamento(null); // opzionale: rimuovi la data di pagamento
        pagamento = pagamentoDAO.aggiornaPagamento(pagamento);
        if (pagamento == null){
            System.err.println("[gsController] Errore durante il rimborso del pagamento");
        } else {
            System.out.println("[gsController] Rimborso effettuato con successo per l'iscrizione ID: " + idIscrizione);
        }
    }

    public void annullareLezione(int idLezione) {
        LezioneDAO lezioneDAO = DAOFactory.getLezioneDAO();
        EntityLezione lezione = lezioneDAO.trovaLezione(idLezione);
        if (lezione == null) {
            System.out.println("[gsController] Lezione non trovata con ID: " + idLezione);
            return;
        }
        lezioneDAO.eliminaLezione(lezione);
        System.out.println("[gsController] Lezione annullata con successo! ID Lezione: " + idLezione);
    }

    public void consultareStatisticheCorsi() {
        List<EntityCorso> corsi;
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        corsi = corsoDAO.trovaTuttiCorsi();
        if (corsi == null || corsi.isEmpty()) {
            System.out.println("[gsController] Nessun corso trovato.");
            return;
        }

        IscrizioneDAO iscrizioneDAO = DAOFactory.getIscrizioneDAO();
        PagamentoDAO pagamentoDAO = DAOFactory.getPagamentoDAO();

        System.out.println("\n=== STATISTICHE CORSI ===");
        System.out.printf("%-5s %-15s %-10s %-10s %-10s %-10s %-10s%n",
                          "ID", "Lingua", "Livello", "Iscritti", "Posti Tot.", "Disponibili", "Incasso");

        for (EntityCorso corso : corsi) {
            // Count active iscrizioni (not cancelled/deleted)
            List<EntityIscrizione> iscrizioni = iscrizioneDAO.trovaPerCorso(corso.getID());
            int iscrittiAttivi = 0;
            if (iscrizioni != null) {
                for (EntityIscrizione iscrizione : iscrizioni) {
                    // Check if iscrizione is not cancelled (deleted_at is null)
                    if (iscrizione.getDeleted_at() == null) {
                        iscrittiAttivi++;
                    }
                }
            }

            int postiTotali = corso.getNumeroMassimoPartecipanti();
            int disponibili = postiTotali - iscrittiAttivi;

            // Calculate incasso (amount paid)
            BigDecimal incasso = BigDecimal.ZERO;
            if (iscrizioni != null) {
                for (EntityIscrizione iscrizione : iscrizioni) {
                    // Only count payments for active iscrizioni
                    if (iscrizione.getDeleted_at() == null) {
                        EntityPagamento pagamento = pagamentoDAO.trovaPagamentoPerIscrizione(iscrizione.getID());
                        if (pagamento != null && pagamento.getStatoPagamento()) {
                            incasso = incasso.add(pagamento.getImporto());
                        }
                    }
                }
            }

            System.out.printf("%-5d %-15s %-10s %-10d %-10d %-10d €%-10.2f%n",
                    corso.getID(),
                    corso.getLinguaCorso(),
                    corso.getLivelloCorso(),
                    iscrittiAttivi,
                    postiTotali,
                    disponibili,
                    incasso);
        }
    }

    public void consultareIscritti() {
        // TODO - implement GestioneScuolaController.consultareIscritti
        // permette di consultare tutti gli iscritti al ( che non abbiano annullato l'iscrizione
        List<EntityIscrizione> iscrizioni;
        IscrizioneDAO iDAO = DAOFactory.getIscrizioneDAO();
        iscrizioni = iDAO.trovaTutti();
        if (iscrizioni == null){
            System.err.println("[gsController] Errore nessun iscrizione trovata");
        }else{
            EntityCliente Cliente;
            EntityCorso corso;
            EntityDocente docente;

            ClienteDAO clienteDAO = DAOFactory.getClienteDAO();
            CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
            DocenteDAO docenteDao = DAOFactory.getDocenteDAO();
            System.out.println("ID");
            for (EntityIscrizione i: iscrizioni){

                Cliente = clienteDAO.trovaCliente(i.getFKidCliente());
                corso = corsoDAO.trovaCorso(i.getFKidCorso());
                docente = docenteDao.trovaDocente(corso.getFKidDocente());
                System.out.printf("\n##############################" +
                                "\nIdIscrizione: %d | \nNome Iscritto: %s"+
                                "\nCognome Iscritto: %s\nLingua Corso: %s|\nLivello Corso: %s |" +
                                "\nCognome Docente: %s | \nNome Docente: %s|" +
                                "\n##############################\n",
                        i.getID(),
                        Cliente.getNome(),Cliente.getCognome(),
                        corso.getLinguaCorso(),
                        corso.getLivelloCorso(),
                        docente.getCognome(),
                        docente.getNome()
                        );

            }
        }



    }

    public void consultareStatisticheIscritti() {
        List<EntityIscrizione> iscrizioni;
        IscrizioneDAO iDAO = DAOFactory.getIscrizioneDAO();
        iscrizioni = iDAO.trovaTutti();
        if (iscrizioni == null){
            System.err.println("[gsController] Errore nessun iscrizione trovata");
            return;
        }

        int totaleIscrizioni = iscrizioni.size();
        int iscrizioniAttive = 0;
        int iscrizioniAnnullate = 0;

        // Payment statistics
        PagamentoDAO pDAO = DAOFactory.getPagamentoDAO();
        int pagamentiEffettuati = 0;
        int pagamentiNonEffettuati = 0;
        BigDecimal incassoTotale = BigDecimal.ZERO;

        // Course breakdown
        CorsoDAO cDAO = DAOFactory.getCorsoDAO();
        Map<EntityCorso, Integer> iscrizioniPerCorso = new HashMap<>();

        for (EntityIscrizione iscrizione : iscrizioni) {
            // Check if cancelled
            if (iscrizione.getDeleted_at() != null) {
                iscrizioniAnnullate++;
            } else {
                iscrizioniAttive++;
            }

            // Payment status
            EntityPagamento pagamento = pDAO.trovaPagamentoPerIscrizione(iscrizione.getID());
            if (pagamento != null && pagamento.getStatoPagamento()) {
                pagamentiEffettuati++;
                if (pagamento.getImporto() != null) {
                    incassoTotale = incassoTotale.add(pagamento.getImporto());
                }
            } else {
                pagamentiNonEffettuati++;
            }

            // Course breakdown
            EntityCorso corso = cDAO.trovaCorso(iscrizione.getFKidCorso());
            if (corso != null) {
                iscrizioniPerCorso.put(corso, iscrizioniPerCorso.getOrDefault(corso, 0) + 1);
            }
        }

        System.out.println("\n=== STATISTICHE ISCRIZIONI ===");
        System.out.println("Totale iscrizioni: " + totaleIscrizioni);
        System.out.println("Iscrizioni attive: " + iscrizioniAttive);
        System.out.println("Iscrizioni annullate: " + iscrizioniAnnullate);
        System.out.println("Pagamenti effettuati: " + pagamentiEffettuati);
        System.out.println("Pagamenti non effettuati: " + pagamentiNonEffettuati);
        System.out.println("Incasso totale: €" + incassoTotale);

        System.out.println("\n--- Iscrizioni per corso ---");
        for (Map.Entry<EntityCorso, Integer> entry : iscrizioniPerCorso.entrySet()) {
            EntityCorso corso = entry.getKey();
            int count = entry.getValue();
            System.out.println(corso.getLinguaCorso() + " " + corso.getLivelloCorso() +
                             " (ID: " + corso.getID() + "): " + count + " iscrizioni");
        }

        if (iscrizioniPerCorso.isEmpty()) {
            System.out.println("Nessun corso con iscrizioni trovato.");
        }
    }

    private static final String CONFIG_FILE = "sistema_config.properties";

    public boolean isIscrizioniAperte() {
        java.util.Properties props = new java.util.Properties();
        try (java.io.FileInputStream in = new java.io.FileInputStream(CONFIG_FILE)) {
            props.load(in);
            return Boolean.parseBoolean(props.getProperty("iscrizioni_aperte", "false"));
        } catch (java.io.IOException e) {
            return false; // Chiuso di default se non esiste il file
        }
    }

    private boolean impostaStatoIscrizioni(boolean stato) {
        java.util.Properties props = new java.util.Properties();
        try (java.io.FileInputStream in = new java.io.FileInputStream(CONFIG_FILE)) {
            props.load(in);
        } catch (java.io.IOException e) {
            // Ignora se il file non esiste ancora
        }
        props.setProperty("iscrizioni_aperte", String.valueOf(stato));
        try (java.io.FileOutputStream out = new java.io.FileOutputStream(CONFIG_FILE)) {
            props.store(out, "Configurazione Sistema Scuola Lingue");
            return true;
        } catch (java.io.IOException e) {
            System.err.println("[gsController] Errore salvataggio configurazione: " + e.getMessage());
            return false;
        }
    }

    public boolean aprireIscrizioni() {
        return impostaStatoIscrizioni(true);
    }

    public boolean chiusuraIscrizioni() {
        return impostaStatoIscrizioni(false);
    }

    public void inviarePromemoriaPagamento() {
        System.out.println("[gsController] Inizio invio promemoria pagamenti...");
        IscrizioneDAO iDAO = DAOFactory.getIscrizioneDAO();
        PagamentoDAO pDAO = DAOFactory.getPagamentoDAO();
        ClienteDAO cDAO = DAOFactory.getClienteDAO();
        CalendarioDAO calDAO = DAOFactory.getCalendarioDAO();
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();

        java.util.List<EntityIscrizione> iscrizioni = iDAO.trovaTutti();
        if (iscrizioni == null || iscrizioni.isEmpty()) {
            System.out.println("[gsController] Nessuna iscrizione trovata.");
            return;
        }

        LocalDate oggi = LocalDate.now();
        int countInviate = 0;

        for (EntityIscrizione iscrizione : iscrizioni) {
            EntityPagamento pagamento = pDAO.trovaPagamentoPerIscrizione(iscrizione.getID());
            if (pagamento != null && !pagamento.getStatoPagamento()) {
                // Il pagamento non è stato effettuato
                EntityCalendarioLezioni calendario = calDAO.trovaCalendarioPerCorso(iscrizione.getFKidCorso());
                if (calendario != null && calendario.getDataInizio() != null) {
                    LocalDate dataInizio = calendario.getDataInizio();
                    EntityCliente cliente = cDAO.trovaCliente(iscrizione.getFKidCliente());
                    EntityCorso corso = corsoDAO.trovaCorso(iscrizione.getFKidCorso());

                    if (cliente == null || corso == null) continue;

                    String email = cliente.getEmail();
                    String oggetto = null;
                    String messaggio = null;

                    // a) il giorno prima dell'inizio dei corsi
                    if (oggi.isEqual(dataInizio.minusDays(1))) {
                        oggetto = "Promemoria Pagamento: Domani inizia il corso!";
                        messaggio = "Gentile " + cliente.getNome() + ",\nti ricordiamo che domani inizierà il corso di " + corso.getLinguaCorso() + ". Non risulta ancora saldato il pagamento di €" + pagamento.getImporto() + ". Ti preghiamo di provvedere il prima possibile.";
                    }
                    // b) il giorno dopo la prima lezione
                    else if (oggi.isEqual(dataInizio.plusDays(1))) {
                        oggetto = "Sollecito Pagamento: Corso iniziato";
                        messaggio = "Gentile " + cliente.getNome() + ",\nieri è iniziata la prima lezione del corso di " + corso.getLinguaCorso() + ". Risultiamo ancora in attesa del tuo pagamento di €" + pagamento.getImporto() + ". Ti invitiamo a saldare al più presto per mantenere l'iscrizione attiva.";
                    }

                    if (oggetto != null && messaggio != null) {
                        boolean inviata = org.gagandeepsuman.util.EmailService.inviaEmail(email, oggetto, messaggio);
                        if (inviata) countInviate++;
                    }
                }
            }
        }
        System.out.println("[gsController] Fine invio promemoria pagamenti. Mail inviate totali: " + countInviate);
    }

    /**
     * Mappa di progressione: dato il livello attuale, restituisce il livello successivo.
     * Se il livello non è presente (es. C1), restituisce null → mail generale.
     */
    private static final java.util.Map<String, String> PROGRESSIONE_LIVELLI;
    static {
        PROGRESSIONE_LIVELLI = new java.util.LinkedHashMap<>();
        PROGRESSIONE_LIVELLI.put("A1", "A2");
        PROGRESSIONE_LIVELLI.put("A2", "B1");
        PROGRESSIONE_LIVELLI.put("B1", "B2");
        PROGRESSIONE_LIVELLI.put("B2", "C1");
        // C1 → nessun livello superiore → mail generale
    }

    public void inviarePromemoriaIscrizioni() {
        LocalDate oggi = LocalDate.now();
        int annoCorrente = oggi.getYear();
        // Anno scorso nel formato "2025-2026"
        String annoScorso = (annoCorrente - 1) + "-" + annoCorrente;

        System.out.println("[gsController] Avvio promemoria iscrizioni per ex-studenti anno " + annoScorso);

        IscrizioneDAO iDAO = DAOFactory.getIscrizioneDAO();
        ClienteDAO cDAO = DAOFactory.getClienteDAO();
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();

        // 1. Recupera tutte le iscrizioni non annullate dell'anno scorso
        java.util.List<EntityIscrizione> iscrizioniAnnoScorso = iDAO.trovaPerAnnoAccademico(annoScorso);
        if (iscrizioniAnnoScorso == null || iscrizioniAnnoScorso.isEmpty()) {
            System.out.println("[gsController] Nessuna iscrizione trovata per l'anno " + annoScorso + ". Nessuna mail inviata.");
            return;
        }

        // 2. Recupera tutti i corsi disponibili per la ricerca rapida
        java.util.List<EntityCorso> tuttiCorsi = corsoDAO.trovaTuttiCorsi();
        if (tuttiCorsi == null) tuttiCorsi = new java.util.ArrayList<>();

        // 3. Raggruppa le iscrizioni per cliente: idCliente -> lista di corsi seguiti l'anno scorso
        java.util.Map<Integer, java.util.List<EntityCorso>> corsiPerCliente = new java.util.LinkedHashMap<>();
        for (EntityIscrizione iscrizione : iscrizioniAnnoScorso) {
            EntityCorso corsoSeguito = corsoDAO.trovaCorso(iscrizione.getFKidCorso());
            if (corsoSeguito == null) continue;
            corsiPerCliente
                .computeIfAbsent(iscrizione.getFKidCliente(), k -> new java.util.ArrayList<>())
                .add(corsoSeguito);
        }

        int countInviate = 0;

        // 4. Per ogni cliente, determina cosa inviargli
        for (java.util.Map.Entry<Integer, java.util.List<EntityCorso>> entry : corsiPerCliente.entrySet()) {
            int idCliente = entry.getKey();
            java.util.List<EntityCorso> corsiSeguiti = entry.getValue();

            EntityCliente cliente = cDAO.trovaCliente(idCliente);
            if (cliente == null || cliente.getEmail() == null) continue;

            // Costruisce l'insieme dei corsi già completati (lingua+livello) per escluderli dalla mail generale
            java.util.Set<String> chiaveCorsiCompletati = new java.util.HashSet<>();
            for (EntityCorso c : corsiSeguiti) {
                chiaveCorsiCompletati.add(c.getLinguaCorso().toUpperCase() + "_" + c.getLivelloCorso().toUpperCase());
            }

            // 5. Per ogni corso seguito, determina la mail da inviare
            for (EntityCorso corsoSeguito : corsiSeguiti) {
                String lingua    = corsoSeguito.getLinguaCorso();
                String livello   = corsoSeguito.getLivelloCorso().toUpperCase();
                String livelloSuccessivo = PROGRESSIONE_LIVELLI.get(livello);

                if (livelloSuccessivo != null) {
                    // Caso A: esiste un livello superiore → cerca se il corso è disponibile
                    EntityCorso corsoSuccessivo = corsoDAO.trovaPerLinguaELivello(lingua, livelloSuccessivo);
                    if (corsoSuccessivo != null && corsoSuccessivo.verificaDisponbilitaPosto() >= 1) {
                        // Il corso di livello superiore esiste ed ha posti
                        String oggetto = "🎓 Iscrizioni Aperte: " + lingua + " " + livelloSuccessivo + " ti aspetta!";
                        String messaggio = "Gentile " + cliente.getNome() + " " + cliente.getCognome() + ",\n\n"
                                + "Hai completato il corso di " + lingua + " " + livello + " l'anno scorso — complimenti!\n\n"
                                + "Siamo lieti di comunicarti che il corso di livello superiore è ora disponibile:\n"
                                + "  Lingua: " + lingua + "\n"
                                + "  Livello: " + livelloSuccessivo + "\n"
                                + "  Posti disponibili: " + corsoSuccessivo.verificaDisponbilitaPosto() + "\n"
                                + "  Costo: €" + corsoSuccessivo.getCosto() + "\n\n"
                                + "Le iscrizioni sono aperte dal 1° settembre. Affrettati!\n\n"
                                + "Cordiali saluti,\nSistema Scuola Lingue";
                        boolean inviata = org.gagandeepsuman.util.EmailService.inviaEmail(cliente.getEmail(), oggetto, messaggio);
                        if (inviata) countInviate++;
                    } else {
                        System.out.println("[gsController] Corso " + lingua + " " + livelloSuccessivo + " non disponibile per il cliente " + idCliente + ". Mail non inviata.");
                    }

                } else {
                    // Caso B: livello C1 completato → mail generale con altri corsi disponibili
                    StringBuilder corsiDisponibili = new StringBuilder();
                    for (EntityCorso c : tuttiCorsi) {
                        String chiave = c.getLinguaCorso().toUpperCase() + "_" + c.getLivelloCorso().toUpperCase();
                        if (!chiaveCorsiCompletati.contains(chiave) && c.verificaDisponbilitaPosto() >= 1) {
                            corsiDisponibili.append("  - ")
                                    .append(c.getLinguaCorso()).append(" ").append(c.getLivelloCorso())
                                    .append(" (posti: ").append(c.verificaDisponbilitaPosto())
                                    .append(", costo: €").append(c.getCosto()).append(")\n");
                        }
                    }

                    if (corsiDisponibili.length() == 0) {
                        System.out.println("[gsController] Nessun altro corso disponibile per cliente C1 " + idCliente + ". Mail non inviata.");
                        continue;
                    }

                    String oggetto = "🌍 Hai completato il livello massimo! Scopri nuove lingue";
                    String messaggio = "Gentile " + cliente.getNome() + " " + cliente.getCognome() + ",\n\n"
                            + "Congratulazioni! Hai raggiunto il livello C1 in " + lingua + " — il massimo del nostro percorso!\n\n"
                            + "Ti proponiamo altri corsi disponibili quest'anno accademico:\n\n"
                            + corsiDisponibili.toString()
                            + "\nLe iscrizioni sono aperte dal 1° settembre.\n\n"
                            + "Cordiali saluti,\nSistema Scuola Lingue";
                    boolean inviata = org.gagandeepsuman.util.EmailService.inviaEmail(cliente.getEmail(), oggetto, messaggio);
                    if (inviata) countInviate++;
                }
            }
        }
        System.out.println("[gsController] Fine invio promemoria iscrizioni. Mail inviate totali: " + countInviate);
    }
    public boolean aggiungiCorso(String linguaCorso, String livelloCorso, int numerMaxPartecipanti,
                              BigDecimal costo, int idDocente) {
        EntityCorso entityCorso = new EntityCorso();
        // 1 aggiungi attributi
        entityCorso.setLinguaCorso(linguaCorso);
        entityCorso.setLivelloCorso(livelloCorso);
        entityCorso.setNumeroMassimoPartecipanti(numerMaxPartecipanti);
        entityCorso.setCosto(costo);
        entityCorso.setFKidDocente(idDocente);

        // 2 prepare per salvare
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        // 3 salva
        entityCorso = corsoDAO.salvaCorso(entityCorso);

        if (entityCorso == null) {
            System.err.println("[gsController] Corso non salvato");
            return false;
        }else {
            int idCorso = entityCorso.getID();
            System.out.println("[gsController] Corso aggiunto con successo con idCorso: "+idCorso);
            return true;
        }
    }

    public boolean aggiornaCorso(int idCorso) {
        // Get the existing course
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        EntityCorso corso = corsoDAO.trovaCorso(idCorso);

        if (corso == null) {
            System.out.println("[gsController] Corso non trovato con ID: " + idCorso);
            return false;
        }

        // Show current course details
        DocenteDAO dDAO = DAOFactory.getDocenteDAO();
        EntityDocente prof = dDAO.trovaDocente(corso.getFKidDocente());
        System.out.println("\n=== CORSO DA AGGIORNARE ===");
        System.out.printf("ID: %d%n", corso.getID());
        System.out.printf("Lingua Corso: %s%n", corso.getLinguaCorso());
        System.out.printf("Livello Corso: %s%n", corso.getLivelloCorso());
        System.out.printf("Costo: €%.2f%n", corso.getCosto());
        System.out.printf("Numero Massimo Partecipanti: %d%n", corso.getNumeroMassimoPartecipanti());
        System.out.printf("Docente: %s %s%n", prof.getNome(), prof.getCognome());

        Scanner scanner = new Scanner(System.in);
        boolean corsoModificato = false;

        // Ask what to update
        System.out.println("\nCosa desideri aggiornare?");
        System.out.println("1. Numero massimo partecipanti");
        System.out.println("2. Costo");
        System.out.println("3. Docente");
        System.out.println("0. Torna indietro senza modifiche");
        System.out.print("Seleziona un'opzione: ");

        if (scanner.hasNextInt()) {
            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (scelta) {
                case 1: // Aggiorna numero massimo partecipanti
                    System.out.print("Inserisci nuovo numero massimo partecipanti (attuale: " + corso.getNumeroMassimoPartecipanti() + "): ");
                    if (scanner.hasNextInt()) {
                        int nuovoNumero = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (nuovoNumero > 0) {
                            corso.setNumeroMassimoPartecipanti(nuovoNumero);
                            corsoModificato = true;
                            System.out.println("Numero massimo partecipanti aggiornato a: " + nuovoNumero);
                        } else {
                            System.out.println("Errore: il numero deve essere maggiore di 0");
                        }
                    } else {
                        System.out.println("Input non valido");
                        scanner.nextLine(); // Consume invalid input
                    }
                    break;

                case 2: // Aggiorna costo
                    System.out.print("Inserisci nuovo costo (attuale: €" + corso.getCosto() + "): ");
                    if (scanner.hasNextBigDecimal()) {
                        BigDecimal nuovoCosto = scanner.nextBigDecimal();
                        scanner.nextLine(); // Consume newline
                        if (nuovoCosto.compareTo(BigDecimal.ZERO) >= 0) {
                            corso.setCosto(nuovoCosto);
                            corsoModificato = true;
                            System.out.println("Costo aggiornato a: €" + nuovoCosto);
                        } else {
                            System.out.println("Errore: il costo non può essere negativo");
                        }
                    } else {
                        System.out.println("Input non valido");
                        scanner.nextLine(); // Consume invalid input
                    }
                    break;

                case 3: // Aggiorna docente
                    System.out.print("Inserisci nuovo ID docente (attuale: " + corso.getFKidDocente() + "): ");
                    if (scanner.hasNextInt()) {
                        int nuovoIdDocente = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        // Verify the docente exists
                        DocenteDAO docenteCheck = DAOFactory.getDocenteDAO();
                        EntityDocente docenteVerifica = docenteCheck.trovaDocente(nuovoIdDocente);
                        if (docenteVerifica != null) {
                            corso.setFKidDocente(nuovoIdDocente);
                            corsoModificato = true;
                            System.out.println("Docente aggiornato a: " + docenteVerifica.getNome() + " " + docenteVerifica.getCognome());
                        } else {
                            System.out.println("Errore: Docente con ID " + nuovoIdDocente + " non trovato");
                        }
                    } else {
                        System.out.println("Input non valido");
                        scanner.nextLine(); // Consume invalid input
                    }
                    break;

                case 0: // Torna indietro senza modifiche
                    System.out.println("Nessuna modifica effettuata.");
                    return true; // Consider this as success since user chose to exit

                default:
                    System.out.println("Opzione non valida.");
                    return false;
            }
        } else {
            System.out.println("Input non valido");
            scanner.nextLine(); // Consume invalid input
            return false;
        }

        // Save changes if any were made
        if (corsoModificato) {
            EntityCorso corsoAggiornato = corsoDAO.aggiornaCorso(corso);
            if (corsoAggiornato != null) {
                System.out.println("[gsController] Corso aggiornato con successo!");
                return true;
            } else {
                System.err.println("[gsController] Errore durante l'aggiornamento del corso");
                return false;
            }
        } else {
            System.out.println("Nessuna modifica da salvare.");
            return true; // No changes made, but not an error
        }
    }

    public void visualizzareDocenti() {
        List <EntityDocente> eD;
        DocenteDAO docenteDAO = DAOFactory.getDocenteDAO();
        eD = docenteDAO.trovaTuttiDocenti();
        for(EntityDocente e: eD){
            System.out.printf("\n##############################" +
                    "\nIdDocente: %d |" +
                    "\nNome Docente: %s"+
                    "\nCognome Docente: %s"+
                    "\n##############################\n", e.getID(), e.getNome(), e.getCognome());
        }
    }

    public void aggiungiDocente(String nome, String cognome) {
        // 1 arrange
        EntityDocente docente = new EntityDocente(nome, cognome);
        DocenteDAO dDao = DAOFactory.getDocenteDAO();
        System.out.println("\n--- Salvataggio NUOVO Docente in corso ------");
        // 2 act
        docente = dDao.salvaDocente(docente);
        // 3 check (no assert)
        if ( docente == null ){
            System.err.println("\n [gsController]--- ERRORE Salvataggio NUOVO Docente ------");
        }else{
            System.out.println("\n--- Salvataggio NUOVO Docente concluso ------");
        }
    }

    public boolean checkIdIscrizione(int idIscrizione) {
        return (DAOFactory.getIscrizioneDAO().trovaIscrizione(idIscrizione)) != null;
    }
    public boolean checkIdCliente(int idCliente) {
        return (DAOFactory.getClienteDAO().trovaCliente(idCliente)) != null;
    }

    public boolean eliminaDocente(int idDocente) {
        DocenteDAO docenteDAO = DAOFactory.getDocenteDAO();
        EntityDocente docente = docenteDAO.trovaDocente(idDocente);
        if (docente == null) {
            System.out.println("[gsController] Docente non trovato con ID: " + idDocente);
            return false;
        }
        docenteDAO.eliminaDocente(docente);
        System.out.println("[gsController] Docente eliminato con successo!");
        return true;
    }

    public boolean aggiornaDocente(int idDocente, String nome, String cognome) {
        DocenteDAO docenteDAO = DAOFactory.getDocenteDAO();
        EntityDocente docente = docenteDAO.trovaDocente(idDocente);
        if (docente == null) {
            System.out.println("[gsController] Docente non trovato con ID: " + idDocente);
            return false;
        }

        docente.setNome(nome);
        docente.setCognome(cognome);

        EntityDocente docenteAggiornato = docenteDAO.aggiornaDocente(docente);
        if (docenteAggiornato != null) {
            System.out.println("[gsController] Docente aggiornato con successo!");
            return true;
        } else {
            System.err.println("[gsController] Errore durante l'aggiornamento del docente");
            return false;
        }
    }

    public boolean eliminaCorso(int idCorso) {
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        EntityCorso corso = corsoDAO.trovaCorso(idCorso);
        if (corso == null) {
            System.out.println("[gsController] Corso non trovato con ID: " + idCorso);
            return false;
        }

        corsoDAO.eliminaCorso(corso);
        System.out.println("[gsController] Corso eliminato con successo!");
        return true;
    }

    public void visualizzareClassi() {
        List<EntityClasse> classi;
        ClasseDAO classeDAO = DAOFactory.getClasseDAO();
        classi = classeDAO.trovaClassi();
        if (classi == null || classi.isEmpty()) {
            System.out.println("[gsController] Nessuna classe trovata.");
        } else {
            CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
            EntityCorso corso;
            System.out.println("\n=== ELENCO CLASSI ===");
            for (EntityClasse classe : classi) {
                corso = corsoDAO.trovaCorso(classe.getFkIdCorso());
                if (corso != null) {
                    System.out.printf("ID Classe: %d | Corso: %s %s (ID: %d) | Capienza: %d%n",
                            classe.getID(),
                            corso.getLinguaCorso(), corso.getLivelloCorso(),
                            corso.getID(),
                            classe.getCapienza());
                } else {
                    System.out.printf("ID Classe: %d | Corso ID: %s (Corso non trovato) | Capienza: %d%n",
                            classe.getID(),
                            classe.getFkIdCorso(),
                            classe.getCapienza());
                }
            }
        }
    }

    public boolean organizzareClasse(int idCorso, int capienza) {
        // Verify the course exists
        CorsoDAO corsoDAO = DAOFactory.getCorsoDAO();
        EntityCorso corso = corsoDAO.trovaCorso(idCorso);
        if (corso == null) {
            System.out.println("[gsController] Corso non trovato con ID: " + idCorso);
            return false;
        }

        // Validate capacity
        if (capienza <= 0) {
            System.out.println("[gsController] Errore: la capienza deve essere maggiore di zero.");
            return false;
        }

        // Create and save the class
        EntityClasse classe = new EntityClasse(capienza, idCorso);
        ClasseDAO classeDAO = DAOFactory.getClasseDAO();
        EntityClasse classeSalvata = classeDAO.salvaClasse(classe);

        if (classeSalvata != null) {
            System.out.println("[gsController] Classe organizzata con successo! ID Classe: " + classeSalvata.getID());
            return true;
        } else {
            System.err.println("[gsController] Errore durante l'organizzazione della classe.");
            return false;
        }
    }

    public boolean checkIdCorso(int idCorso) {
        return (DAOFactory.getCorsoDAO().trovaCorso(idCorso)) != null;
    }
}