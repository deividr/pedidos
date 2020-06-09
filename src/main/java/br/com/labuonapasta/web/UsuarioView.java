package br.com.labuonapasta.web;

import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.AcessoEnum;
import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.security.UsuarioSistema;
import br.com.labuonapasta.service.UsuarioService;
import org.omnifaces.util.Messages;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class UsuarioView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Usuario> usuarios;
    private List<Usuario> filteredUsuarios;
    private Usuario selectedUsuario;
    private Usuario novoUsuario;
    private Usuario usuarioLogado;
    private String senhaAtual;
    private String senhaNova;
    private String confirmaSenha;

    private final FacesContext facesContext;
    private final UsuarioService usuarioService;

    @Inject
    public UsuarioView(FacesContext facesContext, UsuarioService usuarioService) {
        this.facesContext = facesContext;
        this.usuarioService = usuarioService;
    }

    @PostConstruct
    public void init() {
        this.usuarios = usuarioService.selecionarTodosUsuarios();
        this.novoUsuario = new Usuario();
        this.selectedUsuario = new Usuario();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsuarioSistema usuario = (UsuarioSistema) securityContext.getAuthentication()
                .getPrincipal();

        this.usuarioLogado = usuario.getUsuario();
    }

    public void incluirUsuario() {
        //Se as senhas digitas conferem, faz a inclusão do usuário.
        if (validarSenha(confirmaSenha, novoUsuario.getSenha())) {
            try {
                novoUsuario.setAtivo(true);
                getUsuarios().add(usuarioService.inserir(novoUsuario));
                novoUsuario = new Usuario();
                Messages.addInfo("msgGeral","Usuário incluído com sucesso.");
            } catch (RegistroExistenteException ex) {
                facesContext.validationFailed();
                Messages.addGlobalError(ex.getMessage());
            }
        } else {
            facesContext.validationFailed();
            Messages.addGlobalError("Senhas não conferem.");
        }

    }

    public void alterarUsuario() {
        usuarioService.alterar(selectedUsuario);
        usuarios.remove(selectedUsuario);
        usuarios.add(selectedUsuario);
        Messages.addInfo("msgGeral", "Usuário alterado com sucesso.");
    }

    public void excluirUsuario() {
        try {
            usuarioService.excluir(selectedUsuario);
            usuarios.remove(selectedUsuario);
            Messages.addInfo("msgGeral", "Usuário excluído com sucesso.");
        } catch (NoResultException ex) {
            Messages.addInfo("msgGeral", ex.getMessage());
        } catch (PersistenceException ex) {
            Messages.addInfo("msgGeral", "Usuário não pode ser excluído.");
        }
    }

    public void alterarSenha() {
        if (!validarSenha(getSenhaAtual(), usuarioLogado.getSenha())) {
            facesContext.validationFailed();
            Messages.addGlobalError("Senha atual inválida.");
        } else if (!getSenhaNova().equals(getConfirmaSenha())) {
            facesContext.validationFailed();
            Messages.addGlobalError("Novas senhas não conferem.");
        } else if (getSenhaAtual().equals(getSenhaNova())) {
            facesContext.validationFailed();
            Messages.addGlobalError("Senha nova é igual a atual.");
        } else {
            usuarioLogado.setSenha(getSenhaNova());
            usuarioService.alterar(usuarioLogado);
            Messages.addGlobalInfo("Senha alterada com sucesso.");
            senhaAtual = "";
            senhaNova = "";
            confirmaSenha = "";
        }
    }

    private boolean validarSenha(String rawPassword, String encondedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encondedPassword);
    }

    public List<AcessoEnum> getTiposDeAcesso() {
        return Arrays.asList(AcessoEnum.values());
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Usuario> getFilteredUsuarios() {
        return filteredUsuarios;
    }

    public void setFilteredUsuarios(List<Usuario> filteredUsuarios) {
        this.filteredUsuarios = filteredUsuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario usuario) {
        this.selectedUsuario = usuario;
    }

    public Usuario getNovoUsuario() {
        return novoUsuario;
    }

    public void setNovoUsuario(Usuario novoUsuario) {
        this.novoUsuario = novoUsuario;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

}