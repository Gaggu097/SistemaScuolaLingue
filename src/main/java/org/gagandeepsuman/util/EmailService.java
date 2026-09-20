package org.gagandeepsuman.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.List;
import java.util.Arrays;

public class EmailService {

    // Lista dei domini considerati "validi" (veri)
    private static final List<String> DOMINI_VALIDI = Arrays.asList(
            "gmail.com", "icloud.com", "yahoo.com", "hotmail.com", "outlook.com", "live.com", "libero.it", "virgilio.it"
    );

    // Nome del file di configurazione (stesso usato per iscrizioni_aperte)
    private static final String CONFIG_FILE = "sistema_config.properties";

    /**
     * Carica la configurazione SMTP dal file di configurazione.
     * Se il file non esiste o mancano proprietà, usa valori di default ragionevoli.
     */
    private static void loadSmtpConfig(Properties props) {
        try (java.io.FileInputStream in = new java.io.FileInputStream(CONFIG_FILE)) {
            props.load(in);
        } catch (java.io.IOException e) {
            // Ignora se il file non esiste ancora - useremo i valori di default
        }
    }

    public static boolean inviaEmail(String destinatario, String oggetto, String messaggio) {
        if (!isDominioValido(destinatario)) {
            System.out.println("[EmailService] Invio ANNULLATO. Il dominio dell'email '" + destinatario + "' non è tra quelli considerati 'veri'.");
            return false;
        }

        Properties props = new Properties();
        // Carica configurazione esistente (se presenti)
        loadSmtpConfig(props);

        // Configurazione SMTP con valori di default sovrascrivibili da file di configurazione
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", props.getProperty("smtp_host", "smtp.gmail.com"));
        props.put("mail.smtp.port", props.getProperty("smtp_port", "587"));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String user = props.getProperty("smtp_user");
                String pass = props.getProperty("smtp_pass");
                // Se le credenziali non sono configurate, restituisce null che fallirà l'autenticazione
                // Questo è intenzionale per forzare la configurazione
                return (user != null && pass != null) ? new PasswordAuthentication(user, pass) : null;
            }
        });

        // Controlla se le credenziali SMTP sono configurate
        if (props.getProperty("smtp_user") == null || props.getProperty("smtp_pass") == null) {
            System.err.println("[EmailService] Credenziali SMTP non configurate. Impostare smtp_user e smtp_pass in " + CONFIG_FILE);
            return false;
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("smtp_user"), "Sistema Scuola Lingue"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(oggetto);
            message.setText(messaggio);

            System.out.println("[EmailService] Invio mail in corso a: " + destinatario + " ...");

            // Questa istruzione esegue L'INVIO REALE sul server SMTP
            Transport.send(message);

            System.out.println("[EmailService] MAIL INVIATA CON SUCCESSO su rete reale!");
            return true;

        } catch (Exception e) {
            System.err.println("[EmailService] Errore durante l'invio della mail. Errore: " + e.getMessage());
            return false;
        }
    }

    private static boolean isDominioValido(String email) {
        if (email == null || !email.contains("@")) return false;
        String dominio = email.substring(email.lastIndexOf("@") + 1).toLowerCase();
        return DOMINI_VALIDI.contains(dominio);
    }
}