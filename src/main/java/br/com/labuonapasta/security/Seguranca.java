package br.com.labuonapasta.security;

import br.com.labuonapasta.modelo.AcessoEnum;
import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.util.SpringUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class Seguranca implements Serializable {

    private static final long serialVersionUID = 7814991737483953002L;

    private Usuario usuario;

    public Seguranca() {
        usuario = SpringUtil.getUsuarioLogado();
    }

    public boolean isPerfilAdministrador() {
        try {
            return usuario.getTipoAcesso().equals(AcessoEnum.ADMINISTRADOR);
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isPerfilCadastro() {
        try {
            return isPerfilAdministrador() || usuario.getTipoAcesso().equals(AcessoEnum.CADASTRO);
        } catch (Exception ex) {
            return false;
        }
    }

}
