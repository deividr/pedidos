package br.com.labuonapasta.web;

import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Funcionario;
import br.com.labuonapasta.service.FuncionarioService;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class FuncionarioView implements Serializable {

    private static final long serialVersionUID = -3829457571237874153L;

    private List<Funcionario> funcionarios;

    private List<Funcionario> filteredFuncionarios;

    private Funcionario selectedFuncionario;

    private Funcionario novoFuncionario;

    private final FacesContext facesContext;

    private final FuncionarioService funcionarioService;

    @Inject
    public FuncionarioView(FacesContext facesContext, FuncionarioService funcionarioService) {
        this.facesContext = facesContext;
        this.funcionarioService = funcionarioService;
    }

    @PostConstruct
    public void init() {
        this.funcionarios = funcionarioService.selecionarTodosFuncionarios();
        this.novoFuncionario = new Funcionario();
        this.selectedFuncionario = new Funcionario();
    }

    public void incluirFuncionario() {
        try {
            getFuncionarios().add(funcionarioService.inserir(novoFuncionario));
            novoFuncionario = new Funcionario();
            Messages.addInfo("msgGeral", "Funcionário incluído com sucesso.");
        } catch (RegistroExistenteException ex) {
            facesContext.validationFailed();
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void alterarFuncionario() {
        try {
            funcionarioService.alterar(selectedFuncionario);
            funcionarios.remove(selectedFuncionario);
            funcionarios.add(selectedFuncionario);
            Messages.addInfo("msgGeral", "Funcionário alterado com sucesso.");
        } catch (RegistroExistenteException ex) {
            facesContext.validationFailed();
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void excluirFuncionario() {
        try {
            funcionarioService.excluir(selectedFuncionario);
            funcionarios.remove(selectedFuncionario);
            Messages.addInfo("msgGeral", "Funcionário excluído com sucesso.");
        } catch (NoResultException ex) {
            Messages.addError("msgGeral", ex.getMessage());
        } catch (PersistenceException ex) {
            Messages.addError("msgGeral", "Funcionário não pode ser excluído, pois provavalmente" +
                    " está associado com algum pedido.");
        }
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public List<Funcionario> getFilteredFuncionarios() {
        return filteredFuncionarios;
    }

    public void setFilteredFuncionarios(List<Funcionario> filteredFuncionarios) {
        this.filteredFuncionarios = filteredFuncionarios;
    }

    public Funcionario getSelectedFuncionario() {
        return selectedFuncionario;
    }

    public void setSelectedFuncionario(Funcionario usuario) {
        this.selectedFuncionario = usuario;
    }

    public Funcionario getNovoFuncionario() {
        return novoFuncionario;
    }

    public void setNovoFuncionario(Funcionario novoFuncionario) {
        this.novoFuncionario = novoFuncionario;
    }

}