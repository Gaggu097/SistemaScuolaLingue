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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table(name="iscrizione")
public class EntityIscrizione implements Serializable {
	public EntityIscrizione() {
	}
	public EntityIscrizione(LocalDate dataIscrizione, String annoAccademico) {
		this.dataIscrizione = dataIscrizione;
		this.annoAccademico = annoAccademico;
	}
	public EntityIscrizione(LocalDate dataIscrizione, String annoAccademico, int FKidCliente) {
		this.dataIscrizione = dataIscrizione;
		this.annoAccademico = annoAccademico;
		this.FKidCliente = FKidCliente;
	}
	public EntityIscrizione(LocalDate dataIscrizione, String annoAccademico, int FKidCliente, int FKidCorso) {
		this.dataIscrizione = dataIscrizione;
		this.annoAccademico = annoAccademico;
		this.FKidCliente = FKidCliente;
		this.FKidCorso = FKidCorso;
	}
	public EntityIscrizione(LocalDate dataIscrizione, String annoAccademico, int FKidCliente, int FKidCorso,
							int FKidClasse) {
		this.dataIscrizione = dataIscrizione;
		this.annoAccademico = annoAccademico;
		this.FKidCliente = FKidCliente;
		this.FKidCorso = FKidCorso;
		this.FKidClasse = FKidClasse;
	}
	public EntityIscrizione(LocalDate dataIscrizione, String annoAccademico, int FKidCliente, int FKidCorso,
							int FKidClasse, OffsetDateTime deletedAt) {
		this.dataIscrizione = dataIscrizione;
		this.annoAccademico = annoAccademico;
		this.FKidCliente = FKidCliente;
		this.FKidCorso = FKidCorso;
		this.FKidClasse = FKidClasse;
		this.deletedAt = deletedAt;
	}
	
	@Column(name="idiscrizione", nullable=false, length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYISCRIZIONE_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYISCRIZIONE_ID_GENERATOR")
	private int ID;
	
	@Column(name="dataiscrizione", nullable=true)
	private LocalDate dataIscrizione;
	
	@Column(name="annoaccademico", nullable=true, length=255)
	private String annoAccademico;

	@Column(name = "clienteidcliente")
	private int FKidCliente;
	@Column(name = "corsoidcorso")
	private int FKidCorso;
	@Column(name = "classeidclasse")
	private int FKidClasse;
	@Column(name = "deleted_at")
	private OffsetDateTime deletedAt;
	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setDataIscrizione(LocalDate value) {
		this.dataIscrizione = value;
	}
	
	public LocalDate getDataIscrizione() {
		return dataIscrizione;
	}
	
	public void setAnnoAccademico(String value) {
		this.annoAccademico = value;
	}
	
	public String getAnnoAccademico() {
		return annoAccademico;
	}
	
	public String toString() {
		return String.valueOf(getID());
	}

    public int getFKidCliente() {
        return FKidCliente;
    }

    public void setFKidCliente(int FKidCliente) {
        this.FKidCliente = FKidCliente;
    }

    public int getFKidCorso() {
        return FKidCorso;
    }

    public void setFKidCorso(int FKidCorso) {
        this.FKidCorso = FKidCorso;
    }

    public int getFKidClasse() {
        return FKidClasse;
    }

    public void setFKidClasse(int FKidClasse) {
        this.FKidClasse = FKidClasse;
    }

    public OffsetDateTime getDeleted_at() {
        return deletedAt;
    }

    public void setDeleted_at(OffsetDateTime deleted_at) {
        this.deletedAt = deleted_at;
    }
}
