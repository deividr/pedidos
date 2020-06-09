package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.Funcionario;
import br.com.labuonapasta.modelo.Funcionario_;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Classe para persistência do tipo Funcionário.
 */
public class FuncionarioDao extends GenericDao<Funcionario> {

    private static final long serialVersionUID = 2944013104300417020L;

    /**
     * Filtrar os funcinários que correspondem com o filtro passado.
     *
     * @param nome que será usado para filtrar os Funcionários correspondentes.
     * @return Lista de todos os funcionários que correspondem a informação passada.
     */
    public List<Funcionario> selecionarPorNome(String nome) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Funcionario> query = builder.createQuery(Funcionario.class);

        Root<Funcionario> from = query.from(Funcionario.class);

        query.where(builder.like(from.get(Funcionario_.nome), nome + "%"));

        TypedQuery<Funcionario> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getResultList();
    }
}
