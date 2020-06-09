package br.com.labuonapasta.banco;

import br.com.labuonapasta.modelo.Cliente;

/**
 * Classe para persistência do tipo usuário.
 */
public class ClienteDao extends GenericDao<Cliente> {

    private static final long serialVersionUID = 7317494544731299558L;

    public Cliente procurarPorTelefone(String telefone) {
        return getEntityManager()
                .createQuery("SELECT c FROM Cliente c WHERE c.telefone1 = :telefone1 "
                        + "OR c.telefone2 = :telefone2", Cliente.class)
                .setParameter("telefone1", telefone)
                .setParameter("telefone2", telefone)
                .getSingleResult();
    }

}
