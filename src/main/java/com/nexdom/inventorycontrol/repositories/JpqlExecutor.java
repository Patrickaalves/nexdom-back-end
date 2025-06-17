package com.nexdom.inventorycontrol.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JpqlExecutor {
    @PersistenceContext
    private EntityManager em;

    /**
     * Executa JPQL dinâmica com parâmetros nomeados e retorna uma lista de resultados.
     */
    public <T> List<T> query(String jpql,
                             Map<String, ?> params,
                             Class<T> type) {
        TypedQuery<T> q = em.createQuery(jpql, type);
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    /**
     * Executa JPQL dinâmica com parâmetros nomeados e retorna um único resultado ou null.
     */
    public <T> T querySingle(String jpql,
                             Map<String, ?> params,
                             Class<T> type) {
        TypedQuery<T> q = em.createQuery(jpql, type);
        params.forEach(q::setParameter);
        try {
            return q.getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
}

