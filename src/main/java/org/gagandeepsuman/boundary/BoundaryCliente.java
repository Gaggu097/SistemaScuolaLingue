package org.gagandeepsuman.boundary;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.gagandeepsuman.controller.GestioneScuolaController;
import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.entity.EntityCorso;
import org.gagandeepsuman.entity.EntityDocente;

public class BoundaryCliente {
	private final GestioneScuolaController controlCliente;
	private final Scanner scanner;
	void main(){
		mostraMenu();
	}

	public BoundaryCliente() {
		this.controlCliente = new GestioneScuolaController();
		this.scanner = new Scanner(System.in);
	}

	public void mostraMenu() {
		int scelta = -1;
		do {
			System.out.println("\n=== MENU CLIENTE ===");
			System.out.println("1. Visualizza Catalogo Corsi");
			System.out.println("2. Registra Nuovo Cliente");
			System.out.println("3. Iscriviti a un Corso");
			System.out.println("4. Annulla Iscrizione");
			System.out.println("0. Torna al menu principale");
			System.out.print("Seleziona un'opzione: ");

			if (scanner.hasNextInt()) {
				scelta = scanner.nextInt();
				scanner.nextLine(); // Consuma il carattere newline
				gestisciScelta(scelta);
			} else {
				System.out.println("Inserisci un numero valido.");
				scanner.nextLine();
			}
		} while (scelta != 0);
	}

	private void gestisciScelta(int scelta) {
		switch (scelta) {
			case 1 -> visualizzareCatalogo();
			case 2 -> registrareCliente();
			case 3 -> iscriversiAlCorso();
			case 4 -> annullareIscrizione();
			case 0 -> System.out.println("Ritorno al menu principale...");
			default -> System.out.println("Opzione non valida.");
		}
	}

	public void visualizzareCatalogo() {
		List<EntityCorso> corsi = controlCliente.visualizzareCatalogo();

		if (corsi == null || corsi.isEmpty()) {
			System.out.println("Nessun corso attualmente disponibile.");
		} else {
			for (EntityCorso c : corsi) {
				DocenteDAO dDAO = new DocenteDAO();
				EntityDocente prof = dDAO.trovaDocente(c.getFKidDocente());
				System.out.printf("ID: %d | Lingua Corso: %s |Livello Corso: %s |Costo: €%.2f | Posti Max: %d | Nome Docente Corso: %s | Cognome Docente Corso: %s%n",
						c.getID(), c.getLinguaCorso(), c.getLivelloCorso(), c.getCosto(), c.getNumeroMassimoPartecipanti(), prof.getCognome(), prof.getCognome());
			}
		}
	}

	public void registrareCliente() {
		System.out.println("\n--- REGISTRAZIONE NUOVO CLIENTE ---");
		System.out.print("Nome: ");
		String nome = scanner.nextLine();
		System.out.print("Cognome: ");
		String cognome = scanner.nextLine();
		System.out.print("Email: formato <<...@...[.]...>");
		String email = scanner.nextLine();
		System.out.print("Telefono: ");
		String telefono = scanner.nextLine();
		System.out.print("Data di Nascita formato <<ANNO,mese,GIORNO>: ");
		LocalDate dataNascita = LocalDate.parse(scanner.nextLine());

		boolean esito = controlCliente.registrazioneCliente(nome, cognome, dataNascita, email, telefono);
		if (esito) {
			System.out.println("Registrazione completata con successo!");
		} else {
			System.err.println("åErrore durante la registrazione del cliente.");
		}
	}

	public void iscriversiAlCorso() {
		System.out.println("\n--- ISCRIZIONE AL CORSO ---");
		System.out.print("Inserisci Lingua Corso: ");
		String linguaCorso = scanner.nextLine();
		System.out.print("Inserisci Livello Corso: ");
		String livelloCorso = scanner.nextLine();
		System.out.print("Inserisci idCliente: ");
		int idCliente = scanner.nextInt();

		boolean esito = controlCliente.iscriversiAlCorso(linguaCorso, livelloCorso, idCliente);
		if (esito) {
			System.out.println("Iscrizione effettuata con successo!");
		}
		else{
			throw new UnsupportedOperationException();
		}
	}

	public void annullareIscrizione() {
		System.out.println("\n--- ANNULLAMENTO ISCRIZIONE ---");
		System.out.print("Inserisci ID Cliente: ");
		int idCliente = scanner.nextInt();
		System.out.print("Inserisci ID Corso: ");
		int idCorso = scanner.nextInt();
		scanner.nextLine();

		boolean esito = controlCliente.annullareIscrizione(idCliente, idCorso);
		if (esito) {
			System.out.println("Iscrizione annullata con successo!");
		} else {
			System.err.println("Errore durante l'annullamento dell'iscrizione.");
		}
	}

}
