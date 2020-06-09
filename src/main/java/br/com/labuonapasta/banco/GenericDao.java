package br.com.labuonapasta.banco;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Classe genérica para acesso ao banco de dados.
 * @param <T>
 */
public class GenericDao<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 6215525951089290928L;

    @Inject
    private EntityManager manager;

    private final Class<T> persistenceClass;

    @SuppressWarnings("unchecked")
    public GenericDao() {
        persistenceClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public T getById(int codigo) {
        return manager.find(persistenceClass, codigo);
    }

    public T getById(long codigo) {
        return manager.find(persistenceClass, codigo);
    }

    public T create(T objeto) {
        manager.persist(objeto);
        return objeto;
    }

    public T update(T objeto) {
        return manager.merge(objeto);
    }

    public void delete(T objeto) {
        manager.remove(objeto);
    }

    public List<T> selectAll() {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(persistenceClass);
        criteriaQuery.from(persistenceClass);
        return manager.createQuery(criteriaQuery).getResultList();
    }

    public EntityManager getEntityManager() {
        return this.manager;
    }

    public void setEntityManager(EntityManager manager) {
        this.manager = manager;
    }

}
