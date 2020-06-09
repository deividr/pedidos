package br.com.labuonapasta.web;

import br.com.labuonapasta.banco.ProdutoDao;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Produto;
import br.com.labuonapasta.modelo.ProdutoEnum;
import br.com.labuonapasta.modelo.UnidadeEnum;
import br.com.labuonapasta.service.ProdutoService;
import org.omnifaces.util.Messages;

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
public class ProdutoView implements Serializable {

    private static final long serialVersionUID = -130600272900575180L;

    private List<Produto> produtos;
    private List<Produto> filteredProdutos;
    private Produto selectedProduto;
    private Produto novoProduto;

    private final FacesContext facesContext;
    private final ProdutoDao produtoDao;
    private final ProdutoService produtoService;

    @Inject
    public ProdutoView(FacesContext facesContext, ProdutoDao produtoDao, ProdutoService produtoService) {
        this.facesContext = facesContext;
        this.produtoDao = produtoDao;
        this.produtoService = produtoService;
    }

    @PostConstruct
    public void init() {
        this.produtos = produtoService.selecionarTodosProdutos();
        this.novoProduto = new Produto();
        this.selectedProduto = new Produto();
    }

    public void incluirProduto() {
        try {
            novoProduto.setAtivo(true);
            getProdutos().add(produtoService.inserir(novoProduto));
            novoProduto = new Produto();
            Messages.addInfo("msgGeral", "Produto incluído com sucesso.");
        } catch (RegistroExistenteException ex) {
            facesContext.validationFailed();
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void alterarProduto() {
        try {
            produtoService.alterar(selectedProduto);
            produtos.remove(selectedProduto);
            produtos.add(selectedProduto);
            Messages.addInfo("msgGeral", "Produto alterado com sucesso.");
        } catch (RegistroExistenteException ex) {
            facesContext.validationFailed();
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void excluirProduto() {
        try {
            produtoService.excluir(selectedProduto);
            produtos.remove(selectedProduto);
            Messages.addInfo("msgGeral", "Produto excluído com sucesso.");
        } catch (NoResultException ex) {
            Messages.addError("msgGeral", ex.getMessage());
        } catch (PersistenceException ex) {
            Messages.addError("msgGeral",
                            "Produto não pode ser excluído, pois provavalmente está associado com algum pedido.");
        }
    }

    /**
     * Atualizar o produto conforme informações do banco.
     */
    public void refreshProduto() {
        int idx = produtos.indexOf(selectedProduto);
        produtos.set(idx, produtoDao.getById(selectedProduto.getProdId()));
    }

    public List<UnidadeEnum> getTiposDeUnidade() {
        return Arrays.asList(UnidadeEnum.values());
    }

    public List<ProdutoEnum> getTiposDeProduto() {
        return Arrays.asList(ProdutoEnum.values());
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Produto> getFilteredProdutos() {
        return filteredProdutos;
    }

    public void setFilteredProdutos(List<Produto> filteredProdutos) {
        this.filteredProdutos = filteredProdutos;
    }

    public Produto getSelectedProduto() {
        return selectedProduto;
    }

    public void setSelectedProduto(Produto produto) {
        this.selectedProduto = produto;
    }

    public Produto getNovoProduto() {
        return novoProduto;
    }

    public void setNovoProduto(Produto novoProduto) {
        this.novoProduto = novoProduto;
    }

}