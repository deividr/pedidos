package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.*;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Classe para persistência do tipo Pedido.
 */
public class PedidoDao extends GenericDao<Pedido> {

    private static final long serialVersionUID = 823844487557468167L;

    @Override
    public List<Pedido> selectAll() {
        TypedQuery<Pedido> query = getEntityManager()
                .createQuery("FROM Pedido p JOIN FETCH p.cliente", Pedido.class);
        return query.getResultList();
    }

    public int obterUltimoNumeroPedido(Date dataInicio, Date dataFim) throws NoResultException {
        Integer numeroPedido = (Integer) getEntityManager()
                .createQuery("SELECT MAX(p.numeroPedido) FROM Pedido p " +
                        "WHERE p.dataRetirada >= :dataInicio and p.dataRetirada <= :dataFim")
                .setParameter("dataInicio", dataInicio)
                .setParameter("dataFim", dataFim)
                .getSingleResult();

        if (Objects.isNull(numeroPedido))
            throw new NoResultException("Não existe pedidos para o período informado.");

        return numeroPedido;
    }

    public List<Pedido> filtrarLista(PedidoFilter pedidoFilter) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Pedido> query = builder.createQuery(Pedido.class);
        Root<Pedido> fromPedido = query.from(Pedido.class);

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(pedidoFilter.getDataInicial())) {
            predicates.add(builder.greaterThanOrEqualTo(fromPedido.<Date>get(Pedido_.dataRetirada),
                    pedidoFilter.getDataInicial()));
        }

        if (Objects.nonNull(pedidoFilter.getDataFinal())) {
            predicates.add(builder.lessThanOrEqualTo(
                    fromPedido.<Date>get(Pedido_.dataRetirada), pedidoFilter.getDataFinal()));
        }

        if (Objects.nonNull(pedidoFilter.getGeladeira())
                && !pedidoFilter.getGeladeira().isEmpty()
                && !pedidoFilter.getGeladeira().equals("")) {
            predicates.add(builder.equal(
                    fromPedido.get(Pedido_.geladeira), pedidoFilter.getGeladeira()));
        }

        if (Objects.nonNull(pedidoFilter.getProduto())) {
            CriteriaQuery<Integer> queryItem = builder.createQuery(Integer.class);
            Root<ItemPedido> fromItemPedido = queryItem.from(ItemPedido.class);
            queryItem.select(fromItemPedido.get(ItemPedido_.pedido).get(Pedido_.pedId));
            queryItem.where(builder.equal(
                    fromItemPedido.get(ItemPedido_.produto), pedidoFilter.getProduto()));
            queryItem.groupBy(fromItemPedido.get(ItemPedido_.pedido).get(Pedido_.pedId));
            TypedQuery<Integer> typedQueryPedidos = getEntityManager().createQuery(queryItem);
            List<Integer> pedidos = typedQueryPedidos.getResultList();

            if (!pedidos.isEmpty()) {
                predicates.add(fromPedido.get(Pedido_.pedId).in(pedidos));
            } else {
                predicates.add(fromPedido.get(Pedido_.pedId).in(999999999));
            }
        }

        query.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Pedido> pedidos = getEntityManager().createQuery(query);

        return pedidos.getResultList();
    }
}
