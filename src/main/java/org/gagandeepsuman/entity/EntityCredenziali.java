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

@Table(name="credenziali")
public class EntityCredenziali implements Serializable {
	public EntityCredenziali() {
	}
	public EntityCredenziali(String username, String password, int clienteId) {
		this.username = username;
		this.password = password;
		this.clienteId = clienteId;
	}
	public EntityCredenziali(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Column(name="idcredenziali", nullable=false, length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYCREDENZIALI_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYCREDENZIALI_ID_GENERATOR")
	private int ID;
	
	@Column(name="username", nullable=true, length=255)
	private String username;
	
	@Column(name="password", nullable=true, length=255)
	private String password;
	@Column(name = "clienteidcliente")
	private int clienteId;
	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setUsername(String value) {
		this.username = value;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String value) {
		this.password = value;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String toString() {
		return String.valueOf(getID());
	}

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
}
