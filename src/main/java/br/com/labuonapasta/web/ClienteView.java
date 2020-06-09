package br.com.labuonapasta.web;

import br.com.labuonapasta.banco.ClienteDao;
import br.com.labuonapasta.excessao.CepInvalidoException;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Cliente;
import br.com.labuonapasta.modelo.Endereco;
import br.com.labuonapasta.service.ClienteService;
import br.com.labuonapasta.util.ConsultarCep;

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
public class ClienteView implements Serializable {

    private static final long serialVersionUID = -2324343265249809991L;

    private List<Cliente> clientes;
    private List<Cliente> filteredClientes;
    private Cliente selectedCliente;
    private Cliente novoCliente;

    private final FacesContext facesContext;
    private final ClienteDao clienteDao;
    private final ClienteService clienteService;

    @Inject
    public ClienteView(FacesContext facesContext, ClienteDao clienteDao, ClienteService clienteService) {
        this.facesContext = facesContext;
        this.clienteDao = clienteDao;
        this.clienteService = clienteService;
    }

    @PostConstruct
    public void init() {
        this.clientes = clienteService.selecionarTodosClientes();

        this.novoCliente = new Cliente();
        this.selectedCliente = new Cliente();
    }

    public void incluirCliente() {
        try {
            getClientes().add(clienteService.inserir(novoCliente));
            novoCliente = new Cliente();
            Messages.addInfo("msgGeral", "Cliente incluído com sucesso.");
        } catch (RegistroExistenteException ex) {
            facesContext.validationFailed();
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void alterarCliente() {
        try {
            clienteService.alterar(selectedCliente);
            clientes.remove(selectedCliente);
            clientes.add(selectedCliente);
            Messages.addInfo("msgGeral", "Cliente alterado com sucesso.");
        } catch (RegistroExistenteException ex) {
            facesContext.validationFailed();
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void excluirCliente() {
        try {
            clienteService.excluir(selectedCliente);
            clientes.remove(selectedCliente);
            Messages.addInfo("msgGeral", "Cliente excluído com sucesso.");
        } catch (NoResultException ex) {
            Messages.addError("msgGeral", ex.getMessage());
        } catch (PersistenceException ex) {
            Messages.addError("msgGeral",
                    "Cliente não pode ser excluído, pois provavalmente está associado com algum pedido.");
        }
    }
    
    public void obterCep(String operacao) {
		try {
			Endereco endereco = null;
			
			if (operacao == "inclusao") {				
				endereco = novoCliente.getEnderecos().get(0);
			} else {
				endereco = selectedCliente.getEnderecos().get(0);
			}
			
			Endereco enderecoConsultado = ConsultarCep.consultarCep(endereco.getCep());
			
			endereco.setCep(enderecoConsultado.getCep());
			endereco.setLogradouro(enderecoConsultado.getLogradouro().toUpperCase());
			endereco.setBairro(enderecoConsultado.getBairro().toUpperCase());
			endereco.setLocalidade(enderecoConsultado.getLocalidade().toUpperCase());
			endereco.setUf(enderecoConsultado.getUf().toUpperCase());
			endereco.setComplemento(enderecoConsultado.getComplemento().toUpperCase());
		} catch (CepInvalidoException e) {			
			Messages.addGlobalError(e.getMessage());
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao obter o CEP, inserir as informações manualmente.");
			e.printStackTrace();
		}
    }

    /**
     * Atualizar o cliente conforme informações do banco.
     */
    public void refreshCliente() {
        int idx = clientes.indexOf(selectedCliente);
        clientes.set(idx, clienteDao.getById(selectedCliente.getClieId()));
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Cliente> getFilteredClientes() {
        return filteredClientes;
    }

    public void setFilteredClientes(List<Cliente> filteredClientes) {
        this.filteredClientes = filteredClientes;
    }

    public Cliente getSelectedCliente() {
        return selectedCliente;
    }

    public void setSelectedCliente(Cliente cliente) {
    	// Esse acesso é pra resolver o problema do lazy inicialization.
    	cliente = clienteDao.getById(cliente.getClieId());
    	cliente.getEnderecos();
    	
        this.selectedCliente = cliente;
    }

    public Cliente getNovoCliente() {
        return novoCliente;
    }

    public void setNovoCliente(Cliente novoCliente) {
        this.novoCliente = novoCliente;
    }

}