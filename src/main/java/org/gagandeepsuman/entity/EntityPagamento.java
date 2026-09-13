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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.persistence.*;
/**
 * Entity
 */
@Entity

@Table(name="pagamento")
public class EntityPagamento implements Serializable {
	public EntityPagamento() {
	}
	public EntityPagamento(BigDecimal importo, boolean statoPagamento) {
		this.importo = importo;
		this.statoPagamento = statoPagamento;
	}
	public EntityPagamento(BigDecimal importo, boolean statoPagamento, LocalDate dataPagamento) {
		this.importo = importo;
		this.statoPagamento = statoPagamento;
		this.dataPagamento = dataPagamento;
	}
	public EntityPagamento(BigDecimal importo, boolean statoPagamento, LocalDate dataPagamento, int fkIdIscrizione) {
		this.importo = importo;
		this.statoPagamento = statoPagamento;
		this.dataPagamento = dataPagamento;
		this.fkIdIscrizione = fkIdIscrizione;
	}

	
	@Column(name="idpagamento", nullable=false, length=10)
	@Id	
	@GeneratedValue(generator="DOMAINCLASSES_ENTITYPAGAMENTO_ID_GENERATOR")	
	@org.hibernate.annotations.GenericGenerator(name="DOMAINCLASSES_ENTITYPAGAMENTO_ID_GENERATOR")
	private int ID;
	
	@Column(name="importo", nullable=true, precision=19, scale=0)
	private BigDecimal importo;
	
	@Column(name="statopagamento", nullable=false)
	private boolean statoPagamento;
	
	@Column(name="datapagamento", nullable=true)
	private LocalDate dataPagamento;
	@Column(name="iscrizioneidiscrizione")
	private int fkIdIscrizione;
	
	private void setID(int value) {
		this.ID = value;
	}
	
	public int getID() {
		return ID;
	}
	
	public int getORMID() {
		return getID();
	}
	
	public void setImporto(java.math.BigDecimal value) {
		this.importo = value;
	}
	
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	
	public void setStatoPagamento(boolean value) {
		this.statoPagamento = value;
	}
	
	public boolean getStatoPagamento() {
		return statoPagamento;
	}
	
	public void setDataPagamento(LocalDate value) {
		this.dataPagamento = value;
	}
	
	public LocalDate getDataPagamento() {
		return dataPagamento;
	}
	

    public int getFkIdIscrizione() {
        return fkIdIscrizione;
    }

    public void setFkIdIscrizione(int fkIdIscrizione) {
        this.fkIdIscrizione = fkIdIscrizione;
    }
	public String toString() {
		return String.valueOf(getID());
	}
}
