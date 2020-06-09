package br.com.labuonapasta.security;

import br.com.labuonapasta.banco.UsuarioDao;
import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.service.CDIServiceLocator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Obter o usuário do sistema
 */
public class AppUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            UsuarioDao usuarioDao = CDIServiceLocator.getBean(UsuarioDao.class);

            Usuario usuario = usuarioDao.procurarPorLoginAtivo(login);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            SimpleGrantedAuthority simpleGrantedAuthority =
                    new SimpleGrantedAuthority(usuario.getTipoAcesso().obterDescricao()
                            .toUpperCase());

            authorities.add(simpleGrantedAuthority);

            return new UsuarioSistema(usuario, authorities);
        } catch (NoResultException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

    }

}
