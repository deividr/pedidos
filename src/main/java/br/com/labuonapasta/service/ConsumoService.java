package br.com.labuonapasta.service;

import br.com.labuonapasta.banco.ConsumoDao;
import br.com.labuonapasta.modelo.Consumo;
import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.Objects;

/**
 * Operacoes gerais com Consumo
 */
public class ConsumoService implements Serializable {

	private static final long serialVersionUID = 1234771854504362156L;

	private final ConsumoDao consumoDao;

	@Inject
	public ConsumoService(ConsumoDao consumoDao) {
		this.consumoDao = consumoDao;
	}

	/**
	 * Incluir novo consumo.
	 *
	 * @param novoConsumo a ser incluído.
	 * @return Retorna o consumo com o a informação de ID preenchido.
	 */
	@Transactional
	public Consumo inserir(Consumo novoConsumo) {
		return consumoDao.create(novoConsumo);
	}

	/**
	 * Efetuar alterações no consumo.
	 *
	 * @param consumo Com as informações para serem alteradas.
	 */
	@Transactional
	public void alterar(Consumo consumo) {
		consumoDao.update(consumo);
	}

	/**
	 * Excluir o consumo definitivamente.
	 *
	 * @param consumo Que será excluido
	 * @throws NoResultException quando o consumo não existir mais na base.
	 */
	@Transactional
	public void excluir(Consumo consumo) throws NoResultException {
		consumo = consumoDao.getById(consumo.getConsumoId());

		if (!Objects.isNull(consumo)) {
			consumoDao.delete(consumo);
			consumoDao.getEntityManager().flush();
		} else {
			throw new NoResultException("Consumo não está mais disponível para exclusão");
		}
	}
}
