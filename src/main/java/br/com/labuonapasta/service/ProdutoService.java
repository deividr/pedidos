package br.com.labuonapasta.service;

import br.com.labuonapasta.banco.ProdutoDao;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Produto;
import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Operacoes gerais com Produto
 */
public class ProdutoService implements Serializable {

    private static final long serialVersionUID = 718256753169428285L;

    private final ProdutoDao produtoDao;

    @Inject
    public ProdutoService(ProdutoDao produtoDao) {
        this.produtoDao = produtoDao;
    }

    /**
     * Retorna uma lista de todos os produtos cadastrados no sistema.
     *
     * @return List com todos os produtos.
     */
    public List<Produto> selecionarTodosProdutos() {
        return produtoDao.selectAll();
    }

    /**
     * Incluir novo produto.
     *
     * @param novoProduto a ser incluído.
     * @return novoProduto retorna o produto com o a informação de ID preenchido.
     * @throws RegistroExistenteException quando o produto já existir na base.
     */
    @Transactional
    public Produto inserir(Produto novoProduto) throws RegistroExistenteException {
        try {
            produtoDao.procurarPorNome(novoProduto.getNome());
            throw new RegistroExistenteException("Produto já cadastrado.");
        } catch (NoResultException ex) {
            return produtoDao.create(novoProduto);
        }
    }

    /**
     * Efetuar alterações no produto.
     *
     * @param produto com as informações para serem alteradas.
     */
    @Transactional
    public void alterar(Produto produto) {
        Produto prod2 = produtoDao.getById(produto.getProdId());
        if (Objects.isNull(prod2) || Objects.equals(produto, prod2)) {
            produtoDao.update(produto);
        } else {
            throw new RegistroExistenteException("Nome do produto já cadastrado para outro.");
        }
    }

    /**
     * Excluir o produto definitivamente.
     *
     * @param produto Que será excluido
     * @throws NoResultException quando o produto não existir mais na base.
     */
    @Transactional
    public void excluir(Produto produto) throws NoResultException {
        produto = produtoDao.getById(produto.getProdId());

        if (!Objects.isNull(produto)) {
            produtoDao.delete(produto);
            produtoDao.getEntityManager().flush();
        } else {
            throw new NoResultException("Produto não está mais disponível para exclusão");
        }
    }
}
