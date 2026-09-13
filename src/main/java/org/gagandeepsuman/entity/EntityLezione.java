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

@Entity
@Table(name="lezione")
public class EntityLezione implements Serializable {
	public EntityLezione() {
	}
	public EntityLezione(LocalDate dataLezione, LocalTime orarioInizio) {
		this.dataLezione = dataLezione;
		this.orarioInizio = orarioInizio;
	}
	public EntityLezione(LocalDate dataLezione, LocalTime orarioInizio, int FKidCalendario) {
		this.dataLezione = dataLezione;
		this.orarioInizio = orarioInizio;
		this.FKidCalendario = FKidCalendario;
	}
	
	@Column(name="idlezione", nullable=false, length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYLEZIONE_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYLEZIONE_ID_GENERATOR")
	private int ID;
	
	@Column(name="datalezione", nullable=true)
	private LocalDate dataLezione;
	
	@Column(name="orarioinizio", nullable=true)
	private LocalTime orarioInizio;
	@Column(name = "calendariolezionidcalendariolezioni")
	private int FKidCalendario;
	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setDataLezione(LocalDate value) {
		this.dataLezione = value;
	}
	
	public LocalDate getDataLezione() {
		return dataLezione;
	}
	
	public void setOrarioInizio(LocalTime value) {
		this.orarioInizio = value;
	}
	
	public LocalTime getOrarioInizio() {
		return orarioInizio;
	}
	


    public int getFKidCalendario() {
        return FKidCalendario;
    }

    public void setFKidCalendario(int FKidCalendario) {
        this.FKidCalendario = FKidCalendario;
    }
	public String toString() {
		return String.valueOf(getID());
	}
}
