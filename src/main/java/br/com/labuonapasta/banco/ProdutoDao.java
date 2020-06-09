package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.Produto;
import br.com.labuonapasta.modelo.ProdutoEnum;

import java.util.List;

/**
 * Classe para persistência do tipo produto.
 */
public class ProdutoDao extends GenericDao<Produto> {

    private static final long serialVersionUID = -7336677421469191321L;

    public Produto procurarPorNome(String nome) {
        return getEntityManager()
                .createQuery("SELECT p FROM Produto p WHERE p.nome = :nome", Produto.class)
                .setParameter("nome", nome).getSingleResult();
    }

    public List<Produto> selecionarPorTipo(ProdutoEnum tipo) {
        return getEntityManager()
                .createQuery("SELECT p FROM Produto p WHERE p.tipo = :tipo ORDER BY p.nome", Produto.class)
                .setParameter("tipo", tipo.getCodigo()).getResultList();
    }

    public List<Produto> selecionarPorNome(String nome) {
        return getEntityManager()
                .createQuery("SELECT p FROM Produto p WHERE p.nome like :nome", Produto.class)
                .setParameter("nome", nome + "%")
                .getResultList();
    }
}
