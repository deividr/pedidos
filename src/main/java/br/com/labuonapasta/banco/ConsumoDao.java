package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.Consumo;
import br.com.labuonapasta.modelo.ConsumoFilter;
import br.com.labuonapasta.modelo.Consumo_;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe para persistência do tipo Consumo.
 */
public class ConsumoDao extends GenericDao<Consumo> {

    private static final long serialVersionUID = -4265272741390855157L;

    public List<Consumo> filtrarLista(ConsumoFilter consumoFilter) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Consumo> query = builder.createQuery(Consumo.class);
        Root<Consumo> fromConsumo = query.from(Consumo.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.greaterThanOrEqualTo(fromConsumo.<Date>get(Consumo_.dataConsumo),
                consumoFilter.getDataInicial()));

        predicates.add(builder.lessThanOrEqualTo(fromConsumo.<Date>get(Consumo_.dataConsumo),
                consumoFilter.getDataFinal()));

        predicates.add(builder.equal(fromConsumo.get(Consumo_.funcionario), consumoFilter.getFuncionario()));

        query.where(predicates.toArray(new Predicate[]{}));

        TypedQuery<Consumo> consumos = getEntityManager().createQuery(query);

        return consumos.getResultList();
    }
}
