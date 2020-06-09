package br.com.labuonapasta.modelo;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_usuario")
    private int userId;

    @NotBlank
    @Size(min = 3, max = 15)
    @Column(name = "nm_login")
    private String login;

    @NotBlank
    @Size(min = 3, max = 40)
    @Column(name = "nm_usuario")
    private String nomeCompleto;

    @NotBlank
    @Column(name = "ds_senha")
    private String senha;

    @Column(name = "cd_ativo")
    private boolean ativo;

    @NotNull
    @Column(name = "st_acesso")
    private AcessoEnum tipoAcesso;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto.toUpperCase();
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = new BCryptPasswordEncoder().encode(senha);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public AcessoEnum getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(AcessoEnum tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    @Override
    public String toString() {
        return "Usuario " + getNomeCompleto() + " possui o login " + getLogin()
                + ". Esse usuario tem permissao de " + getTipoAcesso().obterDescricao()
                + ". Ele atualmente esta " + (isAtivo() ? "ativo." : "inativo.");
    }

    @Override
    public boolean equals(Object o) {
        //Se o objeto recebido for diferente de null e a sua classe for um Usuario verifica
        //igualdade no codigo do usuario.
        if (o != null && o.getClass() == this.getClass()) {
            Usuario user = (Usuario) o;
            if (this.getUserId().equals(user.getUserId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getLogin().hashCode();
    }

}
