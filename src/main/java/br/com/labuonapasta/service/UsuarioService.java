package br.com.labuonapasta.service;

import br.com.labuonapasta.banco.UsuarioDao;
import br.com.labuonapasta.excessao.RegistroExistenteException;
import br.com.labuonapasta.modelo.Usuario;
import br.com.labuonapasta.util.Transactional;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Operacoes gerais com Usuário
 */
public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UsuarioDao usuarioDao;

    @Inject
    public UsuarioService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public List<Usuario> selecionarTodosUsuarios() {
        return usuarioDao.selectAll();
    }

    /**
     * Incluir novo usuário.
     *
     * @param novoUsuario a ser incluído.
     * @return o usuário que foi incluído com a informação de ID atualizada.
     * @throws RegistroExistenteException quando o usuário já existe na base.
     */
    @Transactional
    public Usuario inserir(Usuario novoUsuario) throws RegistroExistenteException {
        try {
            usuarioDao.procurarPorLoginAtivo(novoUsuario.getLogin());
            throw new RegistroExistenteException("Usuário já cadastrado!");
        } catch (NoResultException ex) {
            return usuarioDao.create(novoUsuario);
        }
    }

    /**
     * Efetuar alterações no usuário.
     *
     * @param usuario Com as informações para serem alteradas.
     */
    @Transactional
    public void alterar(Usuario usuario) {
        usuarioDao.update(usuario);
    }

    /**
     * Excluir o usuário definitivamente.
     *
     * @param usuario Que será excluido.
     * @throws NoResultException quando o usuário não existir mais na base para ser excluído.
     */
    @Transactional
    public void excluir(Usuario usuario) throws NoResultException {
        usuario = usuarioDao.getById(usuario.getUserId());

        if (!Objects.isNull(usuario)) {
            usuarioDao.delete(usuario);
            usuarioDao.getEntityManager().flush();
        } else {
            throw new NoResultException("Usuário não está mais disponível para exclusão");
        }
    }
}
