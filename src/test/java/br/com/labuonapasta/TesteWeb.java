package br.com.labuonapasta;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.jintegrity.helper.JPAHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.labuonapasta.banco.ClienteDao;
import br.com.labuonapasta.banco.PedidoDao;

public class TesteWeb {

	private static PedidoDao pedidoDao;
	private static ClienteDao clienteDao;
	private boolean commitarTransacao = false;

	@BeforeClass
	public static void iniciarDB() throws Exception {
		JPAHelper.entityManagerFactory("labuonapasta");

		pedidoDao = new PedidoDao();
		pedidoDao.setEntityManager(JPAHelper.currentEntityManager());

		clienteDao = new ClienteDao();
		clienteDao.setEntityManager(JPAHelper.currentEntityManager());
	}

	@Before
	public void iniciarTransacao() {
		// JPAHelper.currentEntityManager().getTransaction().begin();
		commitarTransacao = false;
	}

	@After
	public void finalizarTransacao() {
		// Se o método finalizou com sucesso efetua o commit, senão efetua o rollback.
		if (commitarTransacao) {
			JPAHelper.currentEntityManager().getTransaction().commit();
		} else {
			JPAHelper.currentEntityManager().getTransaction().rollback();
		}
	}

	@Test
	public void main() throws IOException, JAXBException {
	}
}
