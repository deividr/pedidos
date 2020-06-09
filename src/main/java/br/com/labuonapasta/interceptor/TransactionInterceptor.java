package br.com.labuonapasta.interceptor;

import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;

@Interceptor
@Transactional
public class TransactionInterceptor implements Serializable {

	private static final long serialVersionUID = 5186145792118715614L;

	@Inject
	private EntityManager manager;

	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {
		EntityTransaction transaction = manager.getTransaction();

		boolean criador = false;

		try {
			if (!transaction.isActive()) {
				// Truque para fazer o rollback no que já passou, senão, um futuro commit,
				// confirmaria até mesmo operações sem transações.
				transaction.begin();
				transaction.rollback();

				// Agora sim inicia a transação.
				transaction.begin();

				criador = true;
			}
			return context.proceed();
		} catch (Exception ex) {
			if (transaction != null && criador) {
				transaction.rollback();
			}
			throw ex;
		} finally {
			if (transaction != null && transaction.isActive() && criador) {
				transaction.commit();
			}
		}
	}
}
