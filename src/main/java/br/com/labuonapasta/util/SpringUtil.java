package br.com.labuonapasta.util;

import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.security.UsuarioSistema;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringUtil {
    public static Usuario getUsuarioLogado() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsuarioSistema usuarioSistema = (UsuarioSistema) securityContext.getAuthentication()
                .getPrincipal();
        return usuarioSistema.getUsuario();
    }
}
