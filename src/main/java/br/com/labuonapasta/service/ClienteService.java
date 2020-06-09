package br.com.labuonapasta.service;

import br.com.labuonapasta.banco.ClienteDao;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Cliente;
import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Operacoes gerais com Cliente
 */
public class ClienteService implements Serializable {

	private static final long serialVersionUID = 5639484524115688302L;

	private final ClienteDao clienteDao;

	@Inject
	public ClienteService(ClienteDao clienteDao) {
		this.clienteDao = clienteDao;
	}

	public List<Cliente> selecionarTodosClientes() {
		return clienteDao.selectAll();
	}

	@Transactional
	public Cliente inserir(Cliente cliente) throws RegistroExistenteException {
		Cliente cliente1 = null;

		try {
			cliente1 = clienteDao.procurarPorTelefone(cliente.getTelefone1());
		} catch (NoResultException ignored) {
		}

		Cliente cliente2 = null;

		if (!Objects.isNull(cliente.getTelefone2()) && !cliente.getTelefone2().isEmpty())
			try {
				cliente2 = clienteDao.procurarPorTelefone(cliente.getTelefone2());
			} catch (NoResultException ignored) {
			}

		if (!Objects.isNull(cliente1))
			throw new RegistroExistenteException("Tel. Principal já cadastrado para outro cliente");

		if (!Objects.isNull(cliente2))
			throw new RegistroExistenteException("Tel. Secundário já cadastrado para outro cliente");

		cliente.setDataCriacao(new Date());

		return clienteDao.create(cliente);
	}

	/**
	 * Efetuar alterações no usuário.
	 *
	 * @param cliente Com as informações para serem alteradas.
	 */
	@Transactional
	public void alterar(Cliente cliente) {
		Cliente cliente1 = null;

		try {
			cliente1 = clienteDao.procurarPorTelefone(cliente.getTelefone1());
		} catch (NoResultException ignored) {

		}

		Cliente cliente2 = null;

		if (!Objects.isNull(cliente.getTelefone2()) && !cliente.getTelefone2().isEmpty())
			try {
				cliente2 = clienteDao.procurarPorTelefone(cliente.getTelefone2());
			} catch (NoResultException ignored) {

			}

		if (!Objects.isNull(cliente1) && !cliente1.getClieId().equals(cliente.getClieId()))
			throw new RegistroExistenteException("Tel. Principal já cadastrado para outro cliente");

		if (!Objects.isNull(cliente2) && !cliente2.getClieId().equals(cliente.getClieId()))
			throw new RegistroExistenteException("Tel. Secundário já cadastrado para outro cliente");

		clienteDao.update(cliente);
	}

	/**
	 * Excluir o usuário definitivamente.
	 *
	 * @param cliente Que será excluido.
	 * @throws NoResultException quando o cliente não existir mais na base.
	 */
	@Transactional
	public void excluir(Cliente cliente) throws NoResultException {
		cliente = clienteDao.getById(cliente.getClieId());

		if (!Objects.isNull(cliente)) {
			clienteDao.delete(cliente);
			clienteDao.getEntityManager().flush();
		} else {
			throw new NoResultException("Cliente não está mais disponível para exclusão");
		}
	}

}
