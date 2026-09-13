/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 * 
 * This is an automatic generated file. It will be regenerated every time 
 * you generate persistence class.
 * 
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: 
 * License Type: Evaluation
 */
package org.gagandeepsuman.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;
@Entity
@Table(name="corso")
public class EntityCorso implements Serializable {
	public EntityCorso() {
	}
	public EntityCorso(String linguaCorso, String livelloCorso, int numeroMassimoPartecipanti, BigDecimal costo, int numIscritti) {
		this.linguaCorso = linguaCorso;
		this.livelloCorso = livelloCorso;
		this.numeroMassimoPartecipanti = numeroMassimoPartecipanti;
		this.costo = costo;
		this.numIscritti = numIscritti;
	}
	public EntityCorso(String linguaCorso, String livelloCorso, int numeroMassimoPartecipanti, BigDecimal costo, int idDocente, int numIscritti) {
		this.linguaCorso = linguaCorso;
		this.livelloCorso = livelloCorso;
		this.numeroMassimoPartecipanti = numeroMassimoPartecipanti;
		this.costo = costo;
		this.FKidDocente = idDocente;
		this.numIscritti = numIscritti;
	}
	public EntityCorso(String linguaCorso, String livelloCorso, int numeroMassimoPartecipanti, BigDecimal costo) {
		this.linguaCorso = linguaCorso;
		this.livelloCorso = livelloCorso;
		this.numeroMassimoPartecipanti = numeroMassimoPartecipanti;
		this.costo = costo;
		this.numIscritti = 0;
	}

	
	@Column(name="idcorso", length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYCORSO_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYCORSO_ID_GENERATOR")
	private int ID;
	
	@Column(name="linguaCorso", nullable=true, length=255)
	private String linguaCorso;
	
	@Column(name="livelloCorso", nullable=true, length=255)
	private String livelloCorso;
	
	@Column(name="numeromaxpartecipanti", nullable=false, length=10)
	private int numeroMassimoPartecipanti;
	
	@Column(name="costo")
	private BigDecimal costo;

	@Column(name = "docenteiddocente")
	private int FKidDocente;
	
	@Column(name="numeroiscritti", nullable=false)
	private int numIscritti = 0;

	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setLinguaCorso(String value) {
		this.linguaCorso = value;
	}
	
	public String getLinguaCorso() {
		return linguaCorso;
	}
	
	public void setLivelloCorso(String value) {
		this.livelloCorso = value;
	}
	
	public String getLivelloCorso() {
		return livelloCorso;
	}
	
	public void setNumeroMassimoPartecipanti(int value) {
		this.numeroMassimoPartecipanti = value;
	}
	
	public int getNumeroMassimoPartecipanti() {
		return numeroMassimoPartecipanti;
	}
	
	public void setCosto(java.math.BigDecimal value) {
		this.costo = value;
	}
	
	public java.math.BigDecimal getCosto() {
		return costo;
	}
	
	public void setNumIscritti(int value) {
		this.numIscritti = value;
	}
	
	public int getNumIscritti() {
		return numIscritti;
	}
	
	public int verificaDisponbilitàPosto() {
		//TODO: Implement Method
		throw new UnsupportedOperationException();
	}
	
	public String toString() {
		return String.valueOf(getID());
	}

    public int getFKidDocente() {
        return FKidDocente;
    }

    public void setFKidDocente(int FKidDocente) {
        this.FKidDocente = FKidDocente;
    }
}
