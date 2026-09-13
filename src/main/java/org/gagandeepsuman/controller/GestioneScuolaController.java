package org.gagandeepsuman.controller;

import org.gagandeepsuman.dao.*;
import org.gagandeepsuman.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.text.Normalizer;
import java.util.Locale;
import java.security.SecureRandom;

public class GestioneScuolaController {

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
		while (CredenzialiDAO.esisteUsername(candidateUsername)) {
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
	public List<EntityCorso> visualizzareCatalogo() {
		// TODO - implement GestioneScuolaController.visulizzareCatalogo
		throw new UnsupportedOperationException();
	}

	public boolean registrazioneCliente(String nome, String cognome, LocalDate dataNascita, String email, String telefono) {
		// TODO - implement GestioneScuolaController.registrazioneCliente
		// crea Cliente
		EntityCliente eC = new EntityCliente();
		eC.setNome(nome);
		eC.setCognome(cognome);
		eC.setEmail(email);
		eC.setDataNascita(dataNascita);
		eC.setNumeroTelefono(telefono);
		// crea oggetto DAO
		ClienteDAO clienteDAO = new ClienteDAO();
		//salva Cliente su database
		try {
			EntityCliente clAggiornato = clienteDAO.salvaCliente(eC);
			int idCliente = clAggiornato.getID();
			// crea credenziali Cliente
			EntityCredenziali eCr = new EntityCredenziali();
			CredenzialiDAO credenzialiDAO = new CredenzialiDAO();
			try{
				// creazione Credenziali uniche
				eCr.setClienteId(idCliente);
				eCr.setUsername(generaUsernameUnico(nome, cognome));
				eCr.setPassword(generaPasswordTemporanea());
				// salva credenziali
				EntityCredenziali eCrAggiornato = credenzialiDAO.salvaCredenziali(eCr);
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
			System.out.print("Registrazione Completata con successo!! ");
			return  true;
        }catch (Exception e){
			e.printStackTrace();
			return false;
		}
    }

	public boolean iscriversiAlCorso(String linguaCorso, String livelloCorso, int idCliente) {
		// TODO - implement GestioneScuolaController.iscriversiAlCorso
		EntityCorso eC = new EntityCorso();

		CorsoDAO corsoDAO = new CorsoDAO();
		eC = corsoDAO.trovaPerLinguaELivello(linguaCorso, livelloCorso);
		int idDocente = eC.getFKidDocente();

		EntityDocente eDoc = new EntityDocente();
		DocenteDAO dDAO = new DocenteDAO();
		eDoc = dDAO.trovaDocente(idDocente);
		// BigDecimal costo = eC.getCosto();
		//int idCorso = eC.getID();
		// linguaCorso = eC.getLinguaCorso();
		// livelloCorso = eC.getLivelloCorso();
		// String nomeDocente = eDoc.getNome();
		// String cognomeDocente = eDoc.getCognome();

		System.out.printf("ID: %d | Lingua Corso: %s |Livello Corso: %s |Costo: €%.2f | Posti Max: %d | Nome Docente Corso: %s | Cognome Docente Corso: %s%n",
				eC.getID(), eC.getLinguaCorso(), eC.getLivelloCorso(), eC.getCosto(), eC.getNumeroMassimoPartecipanti(), eDoc.getCognome(), eDoc.getNome());
		String inputConfermaUtente;
		Scanner scanner = new Scanner(System.in);
		inputConfermaUtente = scanner.nextLine();
		// conferma iscrizione da parte del cliente
		// se Y
		if (inputConfermaUtente.equals("Y")) {
			if (eC.verificaDisponbilitàPosto() >= 1) {
				// crea iscrizione
				EntityIscrizione eI = new EntityIscrizione();
				// ANNO ACCADEMICO
				int anno = LocalDate.now().getYear();
				// DATA ISCRIZIONE
				eI.setAnnoAccademico(String.format("%d-%d", anno, anno + 1));
				// salva iscrizione DAO
				eI.setDataIscrizione(LocalDate.now());
				// idCliente
				eI.setFKidCliente(idCliente);
				// idCorso
				eI.setFKidCorso(eC.getID());

				// crea oggetto DAO
				IscrizioneDAO iDAO = new IscrizioneDAO();
				// SALVA Iscrzione su database
				EntityIscrizione eIAggiornato = iDAO.creaIscrizione(eI);
				// ottieni Id Iscrizione
				int idIscrizione = eIAggiornato.getID();
				// crea pagamento
				EntityPagamento eP = new EntityPagamento();
				eP.setStatoPagamento(false);
				eP.setImporto(eC.getCosto());
				eP.setFkIdIscrizione(idIscrizione);
				// salva Pagamento
				PagamentoDAO pagamentoDAO = new PagamentoDAO();
				EntityPagamento ePAggiornato = pagamentoDAO.salvaPagamento(eP);

				// conferma iscrizione cliente
				System.out.print("Iscrizione confermata con idIscrizione: " + idIscrizione);
				return true;
			} else {
				System.out.println("\nIscrizione Annullata\nErrore1!");
			}
		} else {
			// se n
			System.out.println("\nIscrizione Annullata dal Cliente\n");
			return false;
		}
		throw new UnsupportedOperationException();
	}
	public boolean annullareIscrizione(int idCliente, int idCorso) {
		// TODO - implement GestioneScuolaController.annullareIscrizione
		throw new UnsupportedOperationException();
	}

	public void registrarePagamento() {
		// TODO - implement GestioneScuolaController.registrarePagamento
		throw new UnsupportedOperationException();
	}

	public void registrareRimborso() {
		// TODO - implement GestioneScuolaController.registrareRimborso
		throw new UnsupportedOperationException();
	}

	public void consultareIscritti() {
		// TODO - implement GestioneScuolaController.consultareIscritti
		throw new UnsupportedOperationException();
	}

	public void consultareStatisticheIscritti() {
		// TODO - implement GestioneScuolaController.consultareStatisticheIscritti
		throw new UnsupportedOperationException();
	}

	public void aprireIscrizioni() {
		// TODO - implement GestioneScuolaController.aprireIscrizioni
		throw new UnsupportedOperationException();
	}

	public void chiusuraIscrizioni() {
		// TODO - implement GestioneScuolaController.chiusuraIscrizioni
		throw new UnsupportedOperationException();
	}

	public void inviarePromemoriaPagamento() {
		// TODO - implement GestioneScuolaController.inviarePromemoriaPagamento
		throw new UnsupportedOperationException();
	}

	public void inviarePromemoriaIscrizioni() {
		// TODO - implement GestioneScuolaController.inviarePromemoriaIscrizioni
		throw new UnsupportedOperationException();
	}

}