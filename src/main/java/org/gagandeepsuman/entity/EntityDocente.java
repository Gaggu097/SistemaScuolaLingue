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
import jakarta.persistence.*;
@Entity

@Table(name="docente")
public class EntityDocente implements Serializable {
	public EntityDocente() {
	}
	public EntityDocente(String nome, String cognome) {
		this.nome =nome;
		this.cognome = cognome;
	}
	public EntityDocente(int ID, String nome, String cognome) {
		this.ID = ID;
		this.nome = nome;
		this.cognome = cognome;
	}
	
	@Column(name="iddocente", nullable=false, length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYDOCENTE_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYDOCENTE_ID_GENERATOR")
	private int ID;
	
	@Column(name="nome", nullable=true, length=255)
	private String nome;
	
	@Column(name="cognome", nullable=true, length=255)
	private String cognome;
	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setNome(String value) {
		this.nome = value;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setCognome(String value) {
		this.cognome = value;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String toString() {
		return String.valueOf(getID());
	}
	
}
