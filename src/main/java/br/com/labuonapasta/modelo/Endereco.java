package br.com.labuonapasta.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "endereco")
@XmlRootElement(name = "xmlcep")
public class Endereco implements Serializable {

	private static final long serialVersionUID = 322358474680593633L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_endereco")
	private Long enderecoId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cd_cliente", foreignKey = @ForeignKey(name = "fk_endereco_cliente1"))
	private Cliente cliente;

	@NotNull
	@Column(name = "cd_cep")
	private String cep;

	@Column(name = "ds_logradouro")
	private String logradouro;

	@Column(name = "nr_logradouro")
	private int numero;

	@Column(name = "ds_bairro")
	private String bairro;

	@Column(name = "ds_cidade")
	private String localidade;

	@Column(name = "ds_uf")
	private String uf;

	@Column(name = "ds_complemento")
	private String complemento;
	
	@Column(name = "ds_distancia")
	private String distancia;
	
	public Endereco() {
		
	}

	public Long getEnderecoId() {
		return enderecoId;
	}

	public void setEnderecoId(Long enderecoId) {
		this.enderecoId = enderecoId;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getDistancia() {
		return this.distancia;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}
	
	public boolean isValid() {
		if (getLogradouro().isEmpty() 
				|| getNumero() == 0 
				|| getBairro().isEmpty() 
				|| getLocalidade().isEmpty() 
				|| getUf().isEmpty() ) {
			return false;			
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enderecoId == null) ? 0 : enderecoId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (enderecoId == null) {
			if (other.enderecoId != null)
				return false;
		} else if (!enderecoId.equals(other.enderecoId))
			return false;
		return true;
	}

}
