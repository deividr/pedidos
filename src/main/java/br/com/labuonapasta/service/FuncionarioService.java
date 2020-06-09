package br.com.labuonapasta.service;

import br.com.labuonapasta.banco.FuncionarioDao;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Funcionario;
import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Operações gerais com Funcionário
 */
public class FuncionarioService implements Serializable {

    private static final long serialVersionUID = 5063957853689787204L;

    private final FuncionarioDao funcionarioDao;

    @Inject
    public FuncionarioService(FuncionarioDao funcionarioDao) {
        this.funcionarioDao = funcionarioDao;
    }

    public List<Funcionario> selecionarTodosFuncionarios() {
        return funcionarioDao.selectAll();
    }

    /**
     * Efetuar a inclusão de Funcionario novo.
     *
     * @param funcionario que será incluído no sistema
     * @return Funcionario incluído no sistema.
     */
    @Transactional
    public Funcionario inserir(Funcionario funcionario) throws RegistroExistenteException {
        if (Objects.isNull(funcionarioDao.getById(funcionario.getCpf()))) {
            return funcionarioDao.create(funcionario);
        } else {
            throw new RegistroExistenteException("Funcionário já cadastrado com esse CPF.");
        }
    }

    /**
     * Efetuar alterações no funcionário.
     *
     * @param funcionario Com as informações para serem alteradas.
     */
    @Transactional
    public void alterar(Funcionario funcionario) {
        funcionarioDao.update(funcionario);
    }

    /**
     * Excluir o <code>Funcionario</> definitivamente.
     *
     * @param funcionario Que será excluido.
     * @throws NoResultException quando o Funcionario não existir mais na base.
     */
    @Transactional
    public void excluir(Funcionario funcionario) throws NoResultException {
        funcionario = funcionarioDao.getById(funcionario.getCpf());

        if (!Objects.isNull(funcionario)) {
            funcionarioDao.delete(funcionario);
            funcionarioDao.getEntityManager().flush();
        } else {
            throw new NoResultException("Funcionário não está mais disponível para exclusão");
        }
    }

}
