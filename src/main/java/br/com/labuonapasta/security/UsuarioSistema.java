package br.com.labuonapasta.security;

import br.com.labuonapasta.modelo.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Usuário para controle do Spring Security
 */
public class UsuarioSistema extends User {

    private static final long serialVersionUID = -5365627726596398636L;

    private Usuario usuario;

    public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getLogin(), usuario.getSenha(), authorities);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return usuario.getSenha();
    }
}
