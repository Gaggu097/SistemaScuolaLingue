package org.gagandeepsuman.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.gagandeepsuman.service.IGestioneScuolaService;
import org.gagandeepsuman.dao.CorsoDAO;
import org.gagandeepsuman.dao.DocenteDAO;
import org.gagandeepsuman.dao.ClienteDAO;
import org.gagandeepsuman.dao.IscrizioneDAO;
import org.gagandeepsuman.dao.ClasseDAO;
import org.gagandeepsuman.dao.PagamentoDAO;
import org.gagandeepsuman.dao.CredenzialiDAO;
import org.gagandeepsuman.entity.EntityIscrizione;
import org.gagandeepsuman.entity.EntityCorso;
import org.gagandeepsuman.entity.EntityDocente;
import org.gagandeepsuman.entity.EntityCliente;
import org.gagandeepsuman.entity.EntityClasse;
import org.gagandeepsuman.entity.EntityPagamento;
import org.gagandeepsuman.entity.EntityCredenziali;
import org.gagandeepsuman.entity.EntityImpiegatoSegreteria;
import org.gagandeepsuman.security.SessionManager;
import org.gagandeepsuman.security.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Main web controller for the School Management System.
 * Provides web interface endpoints that mirror the console menu functionality.
 */
@Controller
public class WebController {

    @Autowired
    private IGestioneScuolaService gestioneScuolaService;

    @Autowired
    private CorsoDAO corsoDAO;

    @Autowired
    private DocenteDAO docenteDAO;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private IscrizioneDAO iscrizioneDAO;

    @Autowired
    private ClasseDAO classeDAO;

    @Autowired
    private PagamentoDAO pagamentoDAO;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Displays the main menu page.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/")
    public String showMainMenu(Model model) {
        model.addAttribute("title", "Sistema Scuola Lingue - Menu Principale");
        return "mainMenu";
    }

    /**
     * Displays the Cliente menu page.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/cliente")
    public String showClienteMenu(Model model) {
        model.addAttribute("title", "Menu Cliente");
        return "clienteMenu";
    }

    /**
     * Displays the catalog of available courses.
     * Enhancement: Show enroll buttons only for courses user is not already enrolled in.
     *
     * @param model The Spring MVC model (contains username and userRole from AuthInterceptor)
     * @param session The HTTP session
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/cliente/corsi")
    public String showCorsi(Model model, HttpSession session) {
        model.addAttribute("title", "Catalogo Corsi");
        try {
            // Get all courses
            List<EntityCorso> corsi = corsoDAO.trovaTuttiCorsi();

            // Prepare data for display
            List<Map<String, Object>> corsiData = new ArrayList<>();
            List<Integer> enrolledCourseIds = new ArrayList<>();

            // Check if user is authenticated as cliente to get their enrollments
            String username = (String) session.getAttribute(SessionManager.USER_SESSION_ATTRIBUTE);
            String userRole = (String) session.getAttribute(SessionManager.USER_ROLE_ATTRIBUTE);

            if (username != null && "CLIENTE".equals(userRole)) {
                // Get cliente credentials to find the cliente ID
                EntityCredenziali credenziali = authenticationService.getCredentialsByUsername(username);
                if (credenziali != null) {
                    int idCliente = credenziali.getClienteId();
                    // We'll check enrollment status for each course individually below
                }
            }

            if (corsi != null) {
                for (EntityCorso corso : corsi) {
                    EntityDocente docente = docenteDAO.trovaDocente(corso.getFKidDocente());

                    Map<String, Object> corsoInfo = new HashMap<>();
                    corsoInfo.put("id", corso.getID());
                    corsoInfo.put("linguaCorso", corso.getLinguaCorso());
                    corsoInfo.put("livelloCorso", corso.getLivelloCorso());
                    corsoInfo.put("costo", corso.getCosto());
                    corsoInfo.put("numeroMassimoPartecipanti", corso.getNumeroMassimoPartecipanti());
                    // Calculate available spots (this would need to check current enrollments)
                    corsoInfo.put("disponibili", corso.verificaDisponbilitaPosto());
                    corsoInfo.put("docenteCognome", docente != null ? docente.getCognome() : "N/A");
                    corsoInfo.put("docenteNome", docente != null ? docente.getNome() : "N/A");
                    // Add enrollment status for the current user
                    boolean alreadyEnrolled = false;
                    if (username != null && "CLIENTE".equals(userRole)) {
                        // Get cliente credentials to find the cliente ID
                        EntityCredenziali credenziali = authenticationService.getCredentialsByUsername(username);
                        if (credenziali != null) {
                            int idCliente = credenziali.getClienteId();
                            // Check if the user is already enrolled in this specific course
                            EntityIscrizione existingIscrizione = iscrizioneDAO.findByIds(idCliente, corso.getID());
                            alreadyEnrolled = (existingIscrizione != null && existingIscrizione.getDeleted_at() == null);
                        }
                    }
                    corsoInfo.put("alreadyEnrolled", alreadyEnrolled);

                    corsiData.add(corsoInfo);
                }
            }
            model.addAttribute("corsi", corsiData);
        } catch (Exception e) {
            model.addAttribute("corsi", new ArrayList<>());
            model.addAttribute("message", "Errore nel caricamento dei corsi: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "clienteCorsi";
    }

    /**
     * Displays the new client registration form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/cliente/registra")
    public String showRegistraForm(Model model) {
        model.addAttribute("title", "Registrazione Nuovo Cliente");
        return "clienteRegistraForm";
    }

    /**
     * Handles new client registration form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to cliente menu
     */
    @PostMapping("/cliente/registra")
    public String registrareCliente(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("dataNascita") String dataNascitaStr,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            LocalDate dataNascita = LocalDate.parse(dataNascitaStr);
            boolean success = gestioneScuolaService.registrazioneCliente(
                    nome, cognome, dataNascita, email, telefono);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "Registrazione completata con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante la registrazione del cliente.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore nei dati forniti: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/cliente";
    }

    /**
     * Displays the course enrollment form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/cliente/iscriviti")
    public String showIscrivitiForm(Model model) {
        model.addAttribute("title", "Iscriviti a un Corso");
        return "clienteIscrivitiForm";
    }

    /**
     * Handles course enrollment form submission.
     * Requires authentication - extracts idCliente from authenticated user session.
     * Enhancement: Prevents duplicate enrollments in the same course.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @param session The HTTP session
     * @return Redirect to cliente menu
     */
    @PostMapping("/cliente/iscriviti")
    public String iscriversiAlCorso(
            @RequestParam("linguaCorso") String linguaCorso,
            @RequestParam("livelloCorso") String livelloCorso,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Extract idCliente from authenticated user session
        String username = (String) session.getAttribute(SessionManager.USER_SESSION_ATTRIBUTE);
        if (username == null) {
            redirectAttributes.addFlashAttribute("message", "Devi effettuare il login per iscriverti a un corso.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/login";
        }

        // Get cliente credentials to find the cliente ID
        EntityCredenziali credenziali = authenticationService.getCredentialsByUsername(username);
        int idCliente = (credenziali != null) ? credenziali.getClienteId() : 0;

        if (idCliente <= 0) {
            redirectAttributes.addFlashAttribute("message", "Errore nell'identificazione del cliente.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/login";
        }

        try {
            // Check if user is already enrolled in a course with the same language and level
            boolean alreadyEnrolled = false;
            EntityCorso matchingCorso = corsoDAO.trovaPerLinguaELivello(linguaCorso, livelloCorso);
            if (matchingCorso != null) {
                // Check if the user is already enrolled in this specific course
                EntityIscrizione existingIscrizione = iscrizioneDAO.findByIds(idCliente, matchingCorso.getID());
                if (existingIscrizione != null && existingIscrizione.getDeleted_at() == null) {
                    alreadyEnrolled = true;
                }
            }

            if (alreadyEnrolled) {
                redirectAttributes.addFlashAttribute("message", "Sei già iscritto a un corso con questa lingua e livello!");
                redirectAttributes.addFlashAttribute("messageType", "warning");
                return "redirect:/cliente/corsi";
            }

            boolean success = gestioneScuolaService.iscriversiAlCorso(
                    linguaCorso, livelloCorso, idCliente);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "Iscrizione effettuata con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Iscrizione non effettuata!");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'iscrizione: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/cliente";
    }

    /**
     * Displays the enrollment cancellation form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/cliente/annulla")
    public String showAnnullaForm(Model model) {
        model.addAttribute("title", "Annulla Iscrizione");
        return "clienteAnnullaForm";
    }

    /**
     * Handles enrollment cancellation form submission.
     * Requires authentication - extracts idCliente from authenticated user session.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @param session The HTTP session
     * @return Redirect to cliente menu
     */
    @PostMapping("/cliente/annulla")
    public String annullareIscrizione(
            @RequestParam("idIscrizione") int idIscrizione,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // Extract idCliente from authenticated user session
        String username = (String) session.getAttribute(SessionManager.USER_SESSION_ATTRIBUTE);
        if (username == null) {
            redirectAttributes.addFlashAttribute("message", "Devi effettuare il login per annullare un'iscrizione.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/login";
        }

        // Get cliente credentials to find the cliente ID
        EntityCredenziali credenziali = authenticationService.getCredentialsByUsername(username);
        int idCliente = (credenziali != null) ? credenziali.getClienteId() : 0;

        if (idCliente <= 0) {
            redirectAttributes.addFlashAttribute("message", "Errore nell'identificazione del cliente.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/login";
        }

        try {
            boolean success = gestioneScuolaService.annullareIscrizione(idCliente, idIscrizione);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "Iscrizione annullata con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'annullamento dell'iscrizione.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'operazione: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/cliente";
    }

    /**
     * Displays the Gestore menu page.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore")
    public String showGestoreMenu(Model model) {
        model.addAttribute("title", "Menu Gestore");
        // In a full implementation, we would delegate to BoundaryGestore methods
        return "gestoreMenu";
    }

    /**
     * Displays the catalog of available courses for Gestore.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/corsi")
    public String showGestoreCorsi(Model model) {
        model.addAttribute("title", "Catalogo Corsi - Gestore");
        try {
            // Get all courses
            List<EntityCorso> corsi = corsoDAO.trovaTuttiCorsi();

            // Prepare data for display
            List<Map<String, Object>> corsiData = new ArrayList<>();
            if (corsi != null) {
                for (EntityCorso corso : corsi) {
                    EntityDocente docente = docenteDAO.trovaDocente(corso.getFKidDocente());

                    Map<String, Object> corsoInfo = new HashMap<>();
                    corsoInfo.put("id", corso.getID());
                    corsoInfo.put("linguaCorso", corso.getLinguaCorso());
                    corsoInfo.put("livelloCorso", corso.getLivelloCorso());
                    corsoInfo.put("costo", corso.getCosto());
                    corsoInfo.put("numeroMassimoPartecipanti", corso.getNumeroMassimoPartecipanti());
                    // Calculate available spots (this would need to check current enrollments)
                    corsoInfo.put("disponibili", corso.verificaDisponbilitaPosto());
                    corsoInfo.put("docenteCognome", docente != null ? docente.getCognome() : "N/A");
                    corsoInfo.put("docenteNome", docente != null ? docente.getNome() : "N/A");

                    corsiData.add(corsoInfo);
                }
            }
            model.addAttribute("corsi", corsiData);
        } catch (Exception e) {
            model.addAttribute("corsi", new ArrayList<>());
            model.addAttribute("message", "Errore nel caricamento dei corsi: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreCorsi";
    }

    /**
     * Displays the new course form for Gestore.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/corsi/aggiungi")
    public String showGestoreCorsoAggiungiForm(Model model) {
        model.addAttribute("title", "Aggiungi Nuovo Corso - Gestore");
        return "gestoreCorsoAggiungiForm";
    }

    /**
     * Handles new course form submission for Gestore.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to gestore corsi list
     */
    @PostMapping("/gestore/corsi/aggiungi")
    public String gestoreAggiungiCorso(
            @RequestParam("linguaCorso") String linguaCorso,
            @RequestParam("livelloCorso") String livelloCorso,
            @RequestParam("numerMaxPartecipanti") int numerMaxPartecipanti,
            @RequestParam("costo") BigDecimal costo,
            @RequestParam("idDocente") int idDocente,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate input
            if (linguaCorso == null || linguaCorso.isEmpty() ||
                livelloCorso == null || livelloCorso.isEmpty() ||
                numerMaxPartecipanti <= 0 || costo == null ||
                idDocente <= 0) {
                redirectAttributes.addFlashAttribute("message", "Tutti i campi sono obbligatori e devono essere validi.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/gestore/corsi/aggiungi";
            }

            // Verify docente exists
            EntityDocente docente = docenteDAO.trovaDocente(idDocente);
            if (docente == null) {
                redirectAttributes.addFlashAttribute("message", "ID docente non valido.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/gestore/corsi/aggiungi";
            }

            EntityCorso corso = new EntityCorso();
            corso.setLinguaCorso(linguaCorso);
            corso.setLivelloCorso(livelloCorso);
            corso.setNumeroMassimoPartecipanti(numerMaxPartecipanti);
            corso.setCosto(costo);
            corso.setFKidDocente(idDocente);

            boolean success = corsoDAO.salvaCorso(corso) != null;
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Corso aggiunto con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'aggiunta del corso.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore nei dati forniti: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/corsi";
    }

    /**
     * Displays the update course form for Gestore.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/corsi/{id}/aggiorna")
    public String showGestoreCorsoAggiornaForm(@PathVariable int id, Model model) {
        EntityCorso corso = corsoDAO.trovaCorso(id);
        if (corso == null) {
            model.addAttribute("message", "Corso non trovato.");
            model.addAttribute("messageType", "error");
            return "redirect:/gestore/corsi";
        }

        model.addAttribute("title", "Aggiorna Corso - Gestore");
        model.addAttribute("corso", corso);
        // Get list of docenti for the dropdown
        List<EntityDocente> docenti = docenteDAO.trovaTuttiDocenti();
        model.addAttribute("docenti", docenti);
        return "gestoreCorsoAggiornaForm";
    }

    /**
     * Handles update course form submission for Gestore.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to gestore corsi list
     */
    @PostMapping("/gestore/corsi/{id}/aggiorna")
    public String gestoreAggiornaCorso(@PathVariable int id,
            @RequestParam("linguaCorso") String linguaCorso,
            @RequestParam("livelloCorso") String livelloCorso,
            @RequestParam("numerMaxPartecipanti") int numerMaxPartecipanti,
            @RequestParam("costo") BigDecimal costo,
            @RequestParam("idDocente") int idDocente,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // Validate input
            if (linguaCorso == null || linguaCorso.isEmpty() ||
                livelloCorso == null || livelloCorso.isEmpty() ||
                numerMaxPartecipanti <= 0 || costo == null ||
                idDocente <= 0) {
                redirectAttributes.addFlashAttribute("message", "Tutti i campi sono obbligatori e devono essere validi.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/gestore/corsi/" + id + "/aggiorna";
            }

            // Verify corso exists
            EntityCorso corso = corsoDAO.trovaCorso(id);
            if (corso == null) {
                redirectAttributes.addFlashAttribute("message", "Corso non trovato.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/gestore/corsi";
            }

            // Verify docente exists
            EntityDocente docente = docenteDAO.trovaDocente(idDocente);
            if (docente == null) {
                redirectAttributes.addFlashAttribute("message", "ID docente non valido.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/gestore/corsi/" + id + "/aggiorna";
            }

            corso.setLinguaCorso(linguaCorso);
            corso.setLivelloCorso(livelloCorso);
            corso.setNumeroMassimoPartecipanti(numerMaxPartecipanti);
            corso.setCosto(costo);
            corso.setFKidDocente(idDocente);

            boolean success = corsoDAO.aggiornaCorso(corso) != null;
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Corso aggiornato con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'aggiornamento del corso.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore nei dati forniti: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/corsi";
    }

    /**
     * Displays the delete course confirmation for Gestore.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/corsi/{id}/elimina")
    public String showGestoreCorsoEliminaConfirm(@PathVariable int id, Model model) {
        EntityCorso corso = corsoDAO.trovaCorso(id);
        if (corso == null) {
            model.addAttribute("message", "Corso non trovato.");
            model.addAttribute("messageType", "error");
            return "redirect:/gestore/corsi";
        }

        model.addAttribute("title", "Elimina Corso - Conferma");
        model.addAttribute("corso", corso);
        // Get docente info for display
        EntityDocente docente = docenteDAO.trovaDocente(corso.getFKidDocente());
        model.addAttribute("docenteNome", docente != null ? docente.getNome() : "N/A");
        model.addAttribute("docenteCognome", docente != null ? docente.getCognome() : "N/A");
        return "gestoreCorsoEliminaConfirm";
    }

    /**
     * Handles delete course submission for Gestore.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to gestore corsi list
     */
    @PostMapping("/gestore/corsi/{id}/elimina")
    public String gestoreEliminaCorso(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            // Verify corso exists
            EntityCorso corso = corsoDAO.trovaCorso(id);
            if (corso == null) {
                redirectAttributes.addFlashAttribute("message", "Corso non trovato.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/gestore/corsi";
            }

            boolean success = corsoDAO.eliminaCorsoPerId(id);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Corso eliminato con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'eliminazione del corso.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'operazione: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/corsi";
    }

    /**
     * Displays the Segreteria menu page.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria")
    public String showSegreteriaMenu(Model model) {
        model.addAttribute("title", "Menu Segreteria");
        // In a full implementation, we would delegate to ImpiegatoSegreteria methods
        return "segreteriaMenu";
    }

    /**
     * Displays the register payment form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria/registrare-pagamento")
    public String showRegistrarePagamentoForm(Model model) {
        model.addAttribute("title", "Registrare Pagamento");
        return "segreteriaRegistrarePagamentoForm";
    }

    /**
     * Handles register payment form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to segreteria menu
     */
    @PostMapping("/segreteria/registrare-pagamento")
    public String registrarePagamento(
            @RequestParam("idIscrizione") int idIscrizione,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.registrarePagamento(idIscrizione);
            redirectAttributes.addFlashAttribute("message", "Pagamento registrato con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante la registrazione del pagamento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/segreteria";
    }

    /**
     * Displays the cancel inscription form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria/annullare-iscrizione")
    public String showAnnullareIscrizioneForm(Model model) {
        model.addAttribute("title", "Annullare Iscrizione");
        return "segreteriaAnnullareIscrizioneForm";
    }

    /**
     * Handles cancel inscription form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to segreteria menu
     */
    @PostMapping("/segreteria/annullare-iscrizione")
    public String annullareIscrizioneSegreteria(
            @RequestParam("idCliente") int idCliente,
            @RequestParam("idIscrizione") int idIscrizione,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            boolean success = gestioneScuolaService.annullareIscrizione(idCliente, idIscrizione);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Iscrizione annullata con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'annullamento dell'iscrizione.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'operazione: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/segreteria";
    }

    /**
     * Displays the register refund form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria/registrare-rimborso")
    public String showRegistrareRimborsoForm(Model model) {
        model.addAttribute("title", "Registrare Rimborso");
        return "segreteriaRegistrareRimborsoForm";
    }

    /**
     * Handles register refund form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to segreteria menu
     */
    @PostMapping("/segreteria/registrare-rimborso")
    public String registrareRimborso(
            @RequestParam("idIscrizione") int idIscrizione,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.registrareRimborso(idIscrizione);
            redirectAttributes.addFlashAttribute("message", "Rimborso effettuato con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante il rimborso: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/segreteria";
    }

    /**
     * Displays the cancel lesson form.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria/annullare-lezione")
    public String showAnnullareLezioneForm(Model model) {
        model.addAttribute("title", "Annullare Lezione");
        return "segreteriaAnnullareLezioneForm";
    }

    /**
     * Handles cancel lesson form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to segreteria menu
     */
    @PostMapping("/segreteria/annullare-lezione")
    public String annullareLezione(
            @RequestParam("idLezione") int idLezione,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.annullareLezione(idLezione);
            redirectAttributes.addFlashAttribute("message", "Lezione annullata con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'annullamento della lezione: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/segreteria";
    }

    /**
     * Displays the list of active inscriptions.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria/consultare-iscritti")
    public String consultareIscritti(Model model) {
        model.addAttribute("title", "Consultare Iscritti");
        try {
            // Get all inscriptions
            List<EntityIscrizione> iscrizioni = iscrizioneDAO.trovaTutti();

            // Prepare data for display (only active inscriptions)
            List<Map<String, Object>> iscrizioniData = new ArrayList<>();
            if (iscrizioni != null) {
                for (EntityIscrizione iscrizione : iscrizioni) {
                    // Only show active inscriptions (not cancelled)
                    if (iscrizione.getDeleted_at() == null) {
                        EntityCliente cliente = clienteDAO.trovaCliente(iscrizione.getFKidCliente());
                        EntityCorso corso = corsoDAO.trovaCorso(iscrizione.getFKidCorso());
                        EntityDocente docente = corso != null ? docenteDAO.trovaDocente(corso.getFKidDocente()) : null;

                        Map<String, Object> iscrizioneInfo = new HashMap<>();
                        iscrizioneInfo.put("id", iscrizione.getID());
                        iscrizioneInfo.put("dataIscrizione", iscrizione.getDataIscrizione());
                        iscrizioneInfo.put("clienteNome", cliente != null ? cliente.getNome() : "N/A");
                        iscrizioneInfo.put("clienteCognome", cliente != null ? cliente.getCognome() : "N/A");
                        iscrizioneInfo.put("corsoLingua", corso != null ? corso.getLinguaCorso() : "N/A");
                        iscrizioneInfo.put("corsoLivello", corso != null ? corso.getLivelloCorso() : "N/A");
                        iscrizioneInfo.put("docenteNome", docente != null ? docente.getNome() : "N/A");
                        iscrizioneInfo.put("docenteCognome", docente != null ? docente.getCognome() : "N/A");

                        iscrizioniData.add(iscrizioneInfo);
                    }
                }
            }
            model.addAttribute("iscrizioni", iscrizioniData);
        } catch (Exception e) {
            model.addAttribute("iscrizioni", new ArrayList<>());
            model.addAttribute("message", "Errore nel caricamento delle iscrizioni: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "segreteriaConsultareIscritti";
    }

    /**
     * Displays the course statistics.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/segreteria/consultare-statistiche-corsi")
    public String consultareStatisticheCorsi(Model model) {
        model.addAttribute("title", "Consultare Statistiche Corsi");
        try {
            List<EntityCorso> corsi = corsoDAO.findAll(EntityCorso.class);
            if (corsi == null || corsi.isEmpty()) {
                model.addAttribute("message", "Nessun corso trovato.");
                model.addAttribute("messageType", "info");
                model.addAttribute("statisticheCorso", new ArrayList<>());
                return "segreteriaConsultareStatisticheCorsi";
            }

            List<Map<String, Object>> statisticheLista = new ArrayList<>();

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

                Map<String, Object> stats = new HashMap<>();
                stats.put("id", corso.getID());
                stats.put("lingua", corso.getLinguaCorso());
                stats.put("livello", corso.getLivelloCorso());
                stats.put("iscritti", iscrittiAttivi);
                stats.put("postiTotali", postiTotali);
                stats.put("disponibili", disponibili);
                stats.put("incasso", incasso);
                statisticheLista.add(stats);
            }

            model.addAttribute("statisticheCorso", statisticheLista);
            model.addAttribute("message", "Statistiche corsi calcolate con successo.");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Errore durante il calcolo delle statistiche: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "segreteriaConsultareStatisticheCorsi";
    }

    // ==================== GESTORE - EMPLOYEE MANAGEMENT (DOCENTI) ====================

    /**
     * Displays the list of all docenti.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/docenti")
    public String gestoreDocenti(Model model) {
        model.addAttribute("title", "Gestione Docenti");
        try {
            List<EntityDocente> docenti = docenteDAO.findAll(EntityDocente.class);
            // Prepare data for display (email and telefono not available in entity)
            List<Map<String, Object>> docentiData = new ArrayList<>();
            if (docenti != null) {
                for (EntityDocente docente : docenti) {
                    Map<String, Object> docenteInfo = new HashMap<>();
                    docenteInfo.put("id", docente.getID());
                    docenteInfo.put("nome", docente.getNome());
                    docenteInfo.put("cognome", docente.getCognome());
                    // Email and telefono fields not available in EntityDocente entity
                    docenteInfo.put("email", "N/A");
                    docenteInfo.put("telefono", "N/A");
                    docentiData.add(docenteInfo);
                }
            }
            model.addAttribute("docenti", docentiData);
            model.addAttribute("message", "Lista docenti caricata con successo.");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Errore durante il caricamento dei docenti: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreDocenti";
    }

    /**
     * Displays the form to add a new docente.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/docenti/aggiungi")
    public String showAggiungiDocenteForm(Model model) {
        model.addAttribute("title", "Aggiungi Nuovo Docente");
        return "gestoreDocentiAggiungiForm";
    }

    /**
     * Handles add docente form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to docenti list
     */
    @PostMapping("/gestore/docenti/aggiungi")
    public String aggiungiDocente(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.aggiungiDocente(nome, cognome);
            redirectAttributes.addFlashAttribute("message", "Docente aggiunto con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'aggiunta del docente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/docenti";
    }

    /**
     * Displays the form to update an existing docente.
     *
     * @param idDocente The ID of the docente to update
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/docenti/{id}/aggiorna")
    public String showAggiornaDocenteForm(@PathVariable("id") int idDocente, Model model) {
        model.addAttribute("title", "Aggiorna Docente");
        // In a full implementation, we would fetch the docente data to pre-populate the form
        // For now, we'll pass the ID to the form
        model.addAttribute("idDocente", idDocente);
        return "gestoreDocentiAggiornaForm";
    }

    /**
     * Handles update docente form submission.
     *
     * @param idDocente The ID of the docente to update
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to docenti list
     */
    @PostMapping("/gestore/docenti/{id}/aggiorna")
    public String aggiornaDocente(
            @PathVariable("id") int idDocente,
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.aggiornaDocente(idDocente, nome, cognome);
            redirectAttributes.addFlashAttribute("message", "Docente aggiornato con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'aggiornamento del docente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/docenti";
    }

    /**
     * Displays the confirmation page for deleting a docente.
     *
     * @param idDocente The ID of the docente to delete
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/docenti/{id}/elimina")
    public String showEliminaDocenteConfirm(@PathVariable("id") int idDocente, Model model) {
        model.addAttribute("title", "Elimina Docente");
        model.addAttribute("idDocente", idDocente);
        return "gestoreDocentiEliminaConfirm";
    }

    /**
     * Handles docente deletion.
     *
     * @param idDocente The ID of the docente to delete
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to docenti list
     */
    @PostMapping("/gestore/docenti/{id}/elimina")
    public String eliminaDocente(
            @PathVariable("id") int idDocente,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.eliminaDocente(idDocente);
            redirectAttributes.addFlashAttribute("message", "Docente eliminato con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'eliminazione del docente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/docenti";
    }

    // ==================== GESTORE - IMPIEGATO SEGRETERIA MANAGEMENT ====================

    /**
     * Displays the list of all impiegati segreteria.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/impiegati")
    public String gestoreImpiegati(Model model) {
        model.addAttribute("title", "Gestione Impiegati Segreteria");
        try {
            List<EntityImpiegatoSegreteria> impiegati = gestioneScuolaService.elencoImpiegatiSegreteria();
            // Prepare data for display (password not shown for security)
            List<Map<String, Object>> impiegatiData = new ArrayList<>();
            if (impiegati != null) {
                for (EntityImpiegatoSegreteria impiegato : impiegati) {
                    Map<String, Object> impiegatoInfo = new HashMap<>();
                    impiegatoInfo.put("id", impiegato.getId());
                    impiegatoInfo.put("nome", impiegato.getNome());
                    impiegatoInfo.put("cognome", impiegato.getCognome());
                    impiegatoInfo.put("username", impiegato.getUsername());
                    // Password not shown for security reasons
                    impiegatoInfo.put("password", "******");
                    impiegatiData.add(impiegatoInfo);
                }
            }
            model.addAttribute("impiegati", impiegatiData);
            model.addAttribute("message", "Lista impiegati segreteria caricata con successo.");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("impiegati", new ArrayList<>());
            model.addAttribute("message", "Errore durante il caricamento degli impiegati segreteria: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreImpiegati";
    }

    /**
     * Displays the form to add a new impiegato segreteria.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/impiegati/aggiungi")
    public String showAggiungiImpiegatoSegreteriaForm(Model model) {
        model.addAttribute("title", "Aggiungi Nuovo Impiegato Segreteria");
        return "gestoreImpiegatiAggiungiForm";
    }

    /**
     * Handles add impiegato segreteria form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to impiegati list
     */
    @PostMapping("/gestore/impiegati/aggiungi")
    public String aggiungiImpiegatoSegreteria(
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            boolean success = gestioneScuolaService.aggiungiImpiegatoSegreteria(nome, cognome, username, password);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Impiegato segreteria aggiunto con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'aggiunta dell'impiegato segreteria.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore nei dati forniti: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/impiegati";
    }

    /**
     * Displays the form to update an existing impiegato segreteria.
     *
     * @param id The ID of the impiegato segreteria to update
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/impiegati/{id}/aggiorna")
    public String showAggiornaImpiegatoSegreteriaForm(@PathVariable int id, Model model) {
        EntityImpiegatoSegreteria impiegato = gestioneScuolaService.elencoImpiegatiSegreteria().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
        if (impiegato == null) {
            model.addAttribute("message", "Impiegato segreteria non trovato.");
            model.addAttribute("messageType", "error");
            return "redirect:/gestore/impiegati";
        }

        model.addAttribute("title", "Aggiorna Impiegato Segreteria");
        model.addAttribute("impiegato", impiegato);
        return "gestoreImpiegatiAggiornaForm";
    }

    /**
     * Handles update impiegato segreteria form submission.
     *
     * @param id The ID of the impiegato segreteria to update
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to impiegati list
     */
    @PostMapping("/gestore/impiegati/{id}/aggiorna")
    public String aggiornaImpiegatoSegreteria(@PathVariable int id,
            @RequestParam("nome") String nome,
            @RequestParam("cognome") String cognome,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            boolean success = gestioneScuolaService.aggiornaImpiegatoSegreteria(id, nome, cognome, username, password);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Impiegato segreteria aggiornato con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'aggiornamento dell'impiegato segreteria.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore nei dati forniti: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/impiegati";
    }

    /**
     * Displays the confirmation page for deleting an impiegato segreteria.
     *
     * @param id The ID of the impiegato segreteria to delete
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/impiegati/{id}/elimina")
    public String showEliminaImpiegatoSegreteriaConfirm(@PathVariable int id, Model model) {
        EntityImpiegatoSegreteria impiegato = gestioneScuolaService.elencoImpiegatiSegreteria().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
        if (impiegato == null) {
            model.addAttribute("message", "Impiegato segreteria non trovato.");
            model.addAttribute("messageType", "error");
            return "redirect:/gestore/impiegati";
        }

        model.addAttribute("title", "Elimina Impiegato Segreteria - Conferma");
        model.addAttribute("impiegato", impiegato);
        return "gestoreImpiegatiEliminaConfirm";
    }

    /**
     * Handles impiegato segreteria deletion.
     *
     * @param id The ID of the impiegato segreteria to delete
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to impiegati list
     */
    @PostMapping("/gestore/impiegati/{id}/elimina")
    public String eliminaImpiegatoSegreteria(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            boolean success = gestioneScuolaService.eliminaImpiegatoSegreteria(id);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Impiegato segreteria eliminato con successo!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "Errore durante l'eliminazione dell'impiegato segreteria.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'operazione: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/impiegati";
    }

    // ==================== GESTORE - CLASS ORGANIZATION (CLASSI) ====================

    /**
     * Displays the list of all classes.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/classi")
    public String gestoreClassi(Model model) {
        model.addAttribute("title", "Gestione Classi");
        try {
            List<EntityClasse> classi = classeDAO.findAll(EntityClasse.class);
            // Prepare data for display (need to fetch related course and docente info)
            List<Map<String, Object>> classiData = new ArrayList<>();
            if (classi != null) {
                for (EntityClasse classe : classi) {
                    Map<String, Object> classeInfo = new HashMap<>();
                    classeInfo.put("id", classe.getID());
                    classeInfo.put("capienza", classe.getCapienza());

                    // Get course info using fkIdCorso
                    EntityCorso corso = corsoDAO.trovaCorso(classe.getFkIdCorso());
                    if (corso != null) {
                        // Combine language and level for course display
                        String corsoDisplay = corso.getLinguaCorso() + " " + corso.getLivelloCorso();
                        classeInfo.put("corso", corsoDisplay);
                        // Get docente info from the course
                        EntityDocente docente = docenteDAO.trovaDocente(corso.getFKidDocente());
                        if (docente != null) {
                            String docenteDisplay = docente.getNome() + " " + docente.getCognome();
                            classeInfo.put("docente", docenteDisplay);
                        } else {
                            classeInfo.put("docente", "N/A");
                        }
                    } else {
                        classeInfo.put("corso", "Corso non trovato (ID: " + classe.getFkIdCorso() + ")");
                        classeInfo.put("docente", "N/A");
                    }

                    // Count active iscrizioni for this classe (not cancelled)
                    List<EntityIscrizione> iscrizioni = iscrizioneDAO.trovaPerClasse(classe.getID());
                    int iscrittiAttivi = 0;
                    if (iscrizioni != null) {
                        for (EntityIscrizione iscrizione : iscrizioni) {
                            // Only count if not cancelled (deleted_at is null)
                            if (iscrizione.getDeleted_at() == null) {
                                iscrittiAttivi++;
                            }
                        }
                    }
                    classeInfo.put("iscritti", iscrittiAttivi);

                    classiData.add(classeInfo);
                }
            }
            model.addAttribute("classi", classiData);
            model.addAttribute("message", "Lista classi caricata con successo.");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Errore durante il caricamento delle classi: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreClassi";
    }

    /**
     * Displays the form to organize a new class.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/classi/organizza")
    public String showOrganizzaClasseForm(Model model) {
        model.addAttribute("title", "Organizza Nuova Classe");
        try {
            // Get all courses for the dropdown
            List<EntityCorso> corsi = corsoDAO.trovaTuttiCorsi();
            model.addAttribute("corsi", corsi);
        } catch (Exception e) {
            model.addAttribute("corsi", new ArrayList<>());
            model.addAttribute("message", "Errore nel caricamento dei corsi: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreClassiOrganizzaForm";
    }

    /**
     * Handles organize new class form submission.
     *
     * @param model The Spring MVC model
     * @param redirectAttributes Attributes for redirect with flash messages
     * @return Redirect to classi list
     */
    @PostMapping("/gestore/classi/organizza")
    public String organizzareClasse(
            @RequestParam("idCorso") int idCorso,
            @RequestParam("capienza") int capienza,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            gestioneScuolaService.organizzareClasse(idCorso, capienza);
            redirectAttributes.addFlashAttribute("message", "Classe organizzata con successo!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante l'organizzazione della classe: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/gestore/classi";
    }

    /**
     * Displays the list of active inscriptions for Gestore.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/consultare-iscritti")
    public String gestoreConsultareIscritti(Model model) {
        model.addAttribute("title", "Consultare Iscritti - Gestore");
        try {
            // Get all inscriptions
            List<EntityIscrizione> iscrizioni = iscrizioneDAO.trovaTutti();

            // Prepare data for display (only active inscriptions)
            List<Map<String, Object>> iscrizioniData = new ArrayList<>();
            if (iscrizioni != null) {
                for (EntityIscrizione iscrizione : iscrizioni) {
                    // Only show active inscriptions (not cancelled)
                    if (iscrizione.getDeleted_at() == null) {
                        EntityCliente cliente = clienteDAO.trovaCliente(iscrizione.getFKidCliente());
                        EntityCorso corso = corsoDAO.trovaCorso(iscrizione.getFKidCorso());
                        EntityDocente docente = corso != null ? docenteDAO.trovaDocente(corso.getFKidDocente()) : null;

                        Map<String, Object> iscrizioneInfo = new HashMap<>();
                        iscrizioneInfo.put("id", iscrizione.getID());
                        iscrizioneInfo.put("dataIscrizione", iscrizione.getDataIscrizione());
                        iscrizioneInfo.put("clienteNome", cliente != null ? cliente.getNome() : "N/A");
                        iscrizioneInfo.put("clienteCognome", cliente != null ? cliente.getCognome() : "N/A");
                        iscrizioneInfo.put("corsoLingua", corso != null ? corso.getLinguaCorso() : "N/A");
                        iscrizioneInfo.put("corsoLivello", corso != null ? corso.getLivelloCorso() : "N/A");
                        iscrizioneInfo.put("docenteNome", docente != null ? docente.getNome() : "N/A");
                        iscrizioneInfo.put("docenteCognome", docente != null ? docente.getCognome() : "N/A");

                        iscrizioniData.add(iscrizioneInfo);
                    }
                }
            }
            model.addAttribute("iscrizioni", iscrizioniData);
        } catch (Exception e) {
            model.addAttribute("iscrizioni", new ArrayList<>());
            model.addAttribute("message", "Errore nel caricamento delle iscrizioni: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreConsultareIscritti";
    }

    /**
     * Displays the course statistics for Gestore.
     *
     * @param model The Spring MVC model
     * @return The name of the Thymeleaf template to render
     */
    @GetMapping("/gestore/consultare-statistiche-corsi")
    public String gestoreConsultareStatisticheCorsi(Model model) {
        model.addAttribute("title", "Consultare Statistiche Corsi - Gestore");
        try {
            List<EntityCorso> corsi = corsoDAO.findAll(EntityCorso.class);
            if (corsi == null || corsi.isEmpty()) {
                model.addAttribute("message", "Nessun corso trovato.");
                model.addAttribute("messageType", "info");
                model.addAttribute("statisticheCorso", new ArrayList<>());
                return "gestoreConsultareStatisticheCorsi";
            }

            List<Map<String, Object>> statisticheLista = new ArrayList<>();

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

                Map<String, Object> stats = new HashMap<>();
                stats.put("id", corso.getID());
                stats.put("lingua", corso.getLinguaCorso());
                stats.put("livello", corso.getLivelloCorso());
                stats.put("iscritti", iscrittiAttivi);
                stats.put("postiTotali", postiTotali);
                stats.put("disponibili", disponibili);
                stats.put("incasso", incasso);
                statisticheLista.add(stats);
            }

            model.addAttribute("statisticheCorso", statisticheLista);
            model.addAttribute("message", "Statistiche corsi calcolate con successo.");
            model.addAttribute("messageType", "success");
        } catch (Exception e) {
            model.addAttribute("message", "Errore durante il calcolo delle statistiche: " + e.getMessage());
            model.addAttribute("messageType", "error");
        }
        return "gestoreConsultareStatisticheCorsi";
    }

    /**
     * Debug endpoint to list all clients.
     *
     * @return List of clients as JSON
     */
    @GetMapping("/debug/clienti")
    @ResponseBody
    public List<EntityCliente> debugClienti() {
        return clienteDAO.findAll(EntityCliente.class);
    }

    /**
     * Debug endpoint to list all courses.
     *
     * @return List of courses as JSON
     */
    @GetMapping("/debug/corsi")
    @ResponseBody
    public List<EntityCorso> debugCorsi() {
        return corsoDAO.findAll(EntityCorso.class);
    }

    /**
     * Debug endpoint to list all inscriptions.
     *
     * @return List of inscriptions as JSON
     */
    @GetMapping("/debug/iscrizioni")
    @ResponseBody
    public List<EntityIscrizione> debugIscrizioni() {
        return iscrizioneDAO.findAll(EntityIscrizione.class);
    }
}