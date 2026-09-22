package org.gagandeepsuman.dao;

import org.gagandeepsuman.controller.GestioneScuolaController;
import org.gagandeepsuman.service.IGestioneScuolaService;

public class DAOFactory {

    private DAOFactory() { }

    public static ClienteDAO getClienteDAO() {
        return new ClienteDAO();
    }

    public static CorsoDAO getCorsoDAO() {
        return new CorsoDAO();
    }

    public static DocenteDAO getDocenteDAO() {
        return new DocenteDAO();
    }

    public static IscrizioneDAO getIscrizioneDAO() {
        return new IscrizioneDAO();
    }

    public static PagamentoDAO getPagamentoDAO() {
        return new PagamentoDAO();
    }

    public static LezioneDAO getLezioneDAO() {
        return new LezioneDAO();
    }

    public static ClasseDAO getClasseDAO() {
        return new ClasseDAO();
    }

    public static CalendarioDAO getCalendarioDAO() {
        return new CalendarioDAO();
    }

    public static CredenzialiDAO getCredenzialiDAO() {
        return new CredenzialiDAO();
    }

    public static ImpiegatoSegreteriaDAO getImpiegatoSegreteriaDAO() {
        return new ImpiegatoSegreteriaDAO();
    }

    public static GenericDAO getGenericDAO(Class clazz) {
        // fallback
        try {
            return (GenericDAO) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IGestioneScuolaService getGestioneScuolaController() {
        return new GestioneScuolaController();
    }
}