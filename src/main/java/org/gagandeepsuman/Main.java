package org.gagandeepsuman;

import org.gagandeepsuman.boundary.BoundaryCliente;
import org.gagandeepsuman.boundary.BoundaryGestore;
import org.gagandeepsuman.boundary.ImpiegatoSegreteria;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Scanner scanner = null;
    void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println("Hello and welcome!");
        mostraMenu();
//        for (int i = 1; i <= 5; i++) {
//            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
//            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
//            IO.println("i = " + i);
//        }
    }
    public Main(){
        this.scanner = new Scanner(System.in);
    }

    private void mostraMenu() {
        int scelta = -1;
        do {
            System.out.println("\n=== MENU CLIENTE ===");
            System.out.println("1. Menu Cliente");
            System.out.println("2. Menu Gestore");
            System.out.println("3. Menu Segreteria");
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

    public void gestisciScelta(int scelta) {
        switch (scelta) {
            case 1 -> menuCliente();
            case 2 -> menuGestore();
            case 3 -> menuSegreteria();
            // case 4 -> annullareIscrizione();
            case 0 -> System.out.println("Ritorno al menu principale...");
            default -> System.out.println("Opzione non valida.");
        }
    }

    private void menuSegreteria() {
        ImpiegatoSegreteria iC = new ImpiegatoSegreteria();
        iC.mostraMenuImpiegatoSegreteria();
    }

    private void menuGestore() {
        BoundaryGestore bG = new BoundaryGestore();
        bG.mostraMenuGestore();
    }

    private void menuCliente() {
        BoundaryCliente bC = new BoundaryCliente();
        bC.mostraMenu();
    }

}
