package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.*;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe para persistência do tipo ItemPedido.
 */
public class ItemPedidoDao extends GenericDao<ItemPedidoDao> {

    private static final long serialVersionUID = 6941272360440482242L;

    public List<ProdutoQuantidade> listarProdutoQuantidade(Date dataInicio, Date dataFim) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        // Obter listas dos Pedidos que estão entre o período informado.
        CriteriaQuery<Pedido> queryPedido = builder.createQuery(Pedido.class);
        Root<Pedido> fromPedido = queryPedido.from(Pedido.class);

		queryPedido.where(builder.between(
                fromPedido.<Date>get(Pedido_.dataRetirada), dataInicio, dataFim));

        TypedQuery<Pedido> typedQueryPedido = getEntityManager().createQuery(queryPedido);
        List<Pedido> pedidos = typedQueryPedido.getResultList();

        List<ProdutoQuantidade> produtosQuantidades = new ArrayList<>();

        if (!pedidos.isEmpty()) {

            //Obter a sumarização dos produtos em todos os pedidos.
            CriteriaQuery<ProdutoQuantidade> query = builder.createQuery(ProdutoQuantidade.class);
            Root<ItemPedido> from = query.from(ItemPedido.class);

            query.select(builder.construct(ProdutoQuantidade.class,
                    from.get(ItemPedido_.produto),
                    builder.sum(from.get(ItemPedido_.qtde))));

            query.where(from.get(ItemPedido_.pedido).in(pedidos));

            query.groupBy(from.get(ItemPedido_.produto));

            TypedQuery<ProdutoQuantidade> typedQuery = getEntityManager().createQuery(query);

            produtosQuantidades = typedQuery.getResultList();
        }

        return produtosQuantidades;
    }

    public void excluirItensPedido(Pedido pedido) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<ItemPedido> query = builder.createCriteriaDelete(ItemPedido.class);

        Root<ItemPedido> from = query.from(ItemPedido.class);

        query.where(builder.equal(from.get(ItemPedido_.pedido), pedido.getPedId()));

        getEntityManager().createQuery(query).executeUpdate();
    }
}
