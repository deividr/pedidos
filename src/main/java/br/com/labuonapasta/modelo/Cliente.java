package br.com.labuonapasta.modelo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Classe que representa o Cliente da aplicação.
 */
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 5084364566310857500L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_cliente")
    private int clieId;

    @Size(min = 3, max = 40)
    @Column(name = "nm_cliente")
    private String nome;

    @NotBlank
    @Column(name = "nr_telefone1")
    private String telefone1;

    @Column(name = "nr_telefone2")
    private String telefone2;

    @Email
    @Column(name = "ds_email")
    private String email;
    
    @NotNull
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;

    @Temporal(TemporalType.DATE)
    @Column(name = "dt_criacao")
    private Date dataCriacao;
    
    public Cliente() {
    	this.enderecos = new ArrayList<Endereco>();
    }
    
    public void addEndereco(Endereco endereco) {
    	endereco.setCliente(this);
    	getEnderecos().add(endereco);
    }
    
    public void removeEndereco(Endereco endereco) {
    	getEnderecos().remove(endereco);
    	endereco.setCliente(null);
    }
    
    public void removeEndereco(int enderecoId) {
    	removeEndereco(getEnderecos().get(enderecoId));
    }

    public Integer getClieId() {
        return clieId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public List<Endereco> getEnderecos() {
    	// Tem clientes que podem estar na base sem nenhum endereço cadastrado.
    	if (enderecos.isEmpty()) {
    		Endereco endereco = new Endereco();
    		endereco.setCliente(this);
    		enderecos.add(endereco);
    	}
    	
        return enderecos;
    }

    public void setEnderecos(List<Endereco> endereco) {
        this.enderecos = endereco;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        if (Objects.isNull(telefone2)) return "";

        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        if (telefone2.equals(""))
            this.telefone2 = null;
        else
            this.telefone2 = telefone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "O cliente " + getNome() + " é cliente desde " + getDataCriacao();
    }

    @Override
    public boolean equals(Object o) {
        //Se o objeto recebido for diferente de null e a sua classe for um Produto verifica
        //igualdade no codigo do usuario.
        if (o != null && o.getClass() == this.getClass()) {
            Cliente prod = (Cliente) o;
            if (Objects.equals(prod.getClieId(), this.getClieId())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getNome().hashCode();
    }

}
