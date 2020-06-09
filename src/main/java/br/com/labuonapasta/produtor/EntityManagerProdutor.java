package br.com.labuonapasta.produtor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

/**
 * Produtor para objetos referentes ao Entity Manager.
 */
@ApplicationScoped
public class EntityManagerProdutor {

	private static EntityManagerFactory factory;

	public EntityManagerProdutor() {
		factory = Persistence.createEntityManagerFactory("labuonapasta");
	}

	@Produces
	@RequestScoped
	// Esse metódo antes estava retornando uma EntityManager, mas estava dando
	// problema no unwrap no momento de emitir o relatório.
	public Session getEntityManager() {
		return (Session) factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes Session manager) {
		manager.close();
	}

}
