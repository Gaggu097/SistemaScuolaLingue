/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 * 
 * This is an automatic generated file. It will be regenerated every time 
 * you generate persistence class.
 * 
 * Modifying its content may cause the program not work, or your work may be lost.
 */

/**
 * Licensee: 
 * License Type: Evaluation
 */
package org.gagandeepsuman.entity;
import java.time.LocalDate;

import java.io.Serializable;
import jakarta.persistence.*;
@Entity
@Table(name="cliente")
public class EntityCliente implements Serializable {
	public EntityCliente() {
	}
	public EntityCliente(String nome, String cognome, LocalDate dataNascita, String email,
						 String numeroTelefono) {
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.email = email;
		this.numeroTelefono = numeroTelefono;
	}
	public EntityCliente(String nome, String cognome, LocalDate dataNascita, String email) {
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.email = email;
	}

	@Column(name="idcliente", nullable=false, length=10)
	@Id
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYCLIENTE_ID_GENERATOR")
	//@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYCLIENTE_ID_GENERATOR")
	private int ID;

	@Column(name="nome", nullable=true, length=255)
	private String nome;

	@Column(name="cognome", nullable=true, length=255)
	private String cognome;

	@Column(name="datanascita", nullable=true)
	private LocalDate dataNascita;

	@Column(name="email", nullable=true, length=255)
	private String email;

	@Column(name="numerotelefono", nullable=true, length=255)
	private String numeroTelefono;

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

	public void setDataNascita(LocalDate value) {
		this.dataNascita = value;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	public String getEmail() {
		return email;
	}

	public void setNumeroTelefono(String value) {
		this.numeroTelefono = value;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public String toString() {
		return String.valueOf(getID());
	}

}
