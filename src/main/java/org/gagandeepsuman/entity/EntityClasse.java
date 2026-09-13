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

@Table(name="classe")
public class EntityClasse implements Serializable {
	public EntityClasse() {
	}
	public EntityClasse(int capienza, int fkIdCorso) {
		this.capienza = capienza;
		this.fkIdCorso = fkIdCorso;
	}
	public EntityClasse(int capienza) {
		this.capienza = capienza;

	}
	
	@Column(name="idclasse", length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYCLASSE_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYCLASSE_ID_GENERATOR")
	private int ID;
	
	@Column(name="capienza", length=10)
	private int capienza = 15;
	@Column(name = "corsoidcorso")
	private int fkIdCorso;
	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setCapienza(int value) {
		this.capienza = value;
	}
	
	public int getCapienza() {
		return capienza;
	}

    public int getFkIdCorso() {
        return fkIdCorso;
    }

    public void setFkIdCorso(int fkIdCorso) {
        this.fkIdCorso = fkIdCorso;
    }
	public String toString() {
		return String.valueOf(getID());
	}
}
