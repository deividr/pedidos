package br.com.labuonapasta;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.jintegrity.core.JIntegrity;
import com.jintegrity.helper.JPAHelper;

import br.com.labuonapasta.banco.UsuarioDao;
import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.service.UsuarioService;

public class UsuarioServiceTest {

	private static JIntegrity helper = new JIntegrity();

	private static UsuarioService usuarioService;
	private static UsuarioDao usuarioDao;
	private boolean commitarTransacao = false;

	@BeforeClass
	public static void iniciarDB() throws Exception {
		JPAHelper.entityManagerFactory("labuonapasta_test");

		clean();
		helper.insert("Usuario", "Cliente", "Produto", "Pedido", "ItemPedido");

		usuarioDao = new UsuarioDao();
		usuarioDao.setEntityManager(JPAHelper.currentEntityManager());
		usuarioService = new UsuarioService(usuarioDao);

		JPAHelper.currentEntityManager().getTransaction().commit();
	}

	private static void clean() {
		helper.clean("ItemPedido");
		helper.clean("Pedido");
		helper.clean("Produto");
		helper.clean("Cliente");
		helper.clean("Usuario");
	}

	@Before
	public void iniciarTransacao() {
		JPAHelper.currentEntityManager().getTransaction().begin();
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

	// @Test
	public void testExcluir() {
		Usuario usuario = usuarioDao.procurarPorLoginAtivo("exclSucesso");
		usuarioService.excluir(usuario);
		commitarTransacao = true;
	}

	// @Test(expected = NoResultException.class)
	public void testExcluirInexistente() {
		Usuario usuario = new Usuario();
		usuario.setUserId(99);
		usuarioService.excluir(usuario);
	}

}
