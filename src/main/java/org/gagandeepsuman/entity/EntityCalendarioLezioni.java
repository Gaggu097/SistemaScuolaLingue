package org.gagandeepsuman.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="calendariolezioni")
public class EntityCalendarioLezioni implements Serializable {
	public EntityCalendarioLezioni() {
	}
	public EntityCalendarioLezioni(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}
	public EntityCalendarioLezioni(LocalDate dataInizio, int corsoIdCorso) {
		this.dataInizio = dataInizio;
		this.corsoIdCorso = corsoIdCorso;
	}

	
	@Column(name="idcalendariolezioni", length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYCALENDARIOLEZIONI_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYCALENDARIOLEZIONI_ID_GENERATOR")
	private int ID;
	
	@Column(name="datainizio")
	private LocalDate dataInizio;

	@Column(name="corsoidcorso", nullable = true)
	private int corsoIdCorso;

	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setDataInizio(LocalDate value) {
		this.dataInizio = value;
	}
	
	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public String toString() {
		return String.valueOf(getID());
	}

    public int getCorsoIdCorso() {
        return corsoIdCorso;
    }

    public void setCorsoIdCorso(int corsoIdCorso) {
        this.corsoIdCorso = corsoIdCorso;
    }
}
