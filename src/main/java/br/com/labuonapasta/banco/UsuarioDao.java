package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.modelo.Usuario_;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Classe para persistência do tipo usuário.
 */
public class UsuarioDao extends GenericDao<Usuario> {

	private static final long serialVersionUID = 8446150194037221299L;

	public Usuario procurarPorLoginAtivo(String login) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
		Root<Usuario> from = query.from(Usuario.class);

		query.where(builder.and(builder.equal(from.get(Usuario_.login), login),
				builder.equal(from.get(Usuario_.ativo), true)));

		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(query);

		return typedQuery.getSingleResult();
	}

}
