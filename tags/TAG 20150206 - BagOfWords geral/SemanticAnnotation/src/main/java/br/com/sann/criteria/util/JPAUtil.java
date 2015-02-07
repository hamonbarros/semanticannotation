package br.com.sann.criteria.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Classe utilitária para instanciar o EntityManager.
 * 
 * @author Hamon
 * 
 */
public class JPAUtil {

	private static final String PERSISTENCE_UNIT = "sann";

	private static ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
	private static EntityManagerFactory entityManagerFactory;

	/**
	 * Construtor privado.
	 */
	private JPAUtil() {
		
	}

	/**
	 * Método singleton para recupera uma única instância do EntityManager.
	 * 
	 * @return A instância única  do EntityManager.
	 */
	public static EntityManager getEntityManager() {

		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence
					.createEntityManagerFactory(PERSISTENCE_UNIT);
		}

		EntityManager entityManager = threadEntityManager.get();

		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = entityManagerFactory.createEntityManager();
		}

		return entityManager;
	}

	/**
	 * Método para finalizar a instância do EntityManager.
	 */
	public static void closeEntityManager() {

		EntityManager em = threadEntityManager.get();

		if (em != null) {

			EntityTransaction transaction = em.getTransaction();

			if (transaction.isActive()) {
				transaction.commit();
			}

			em.close();

			threadEntityManager.set(null);
		}
	}

	/**
	 * Método para finalizar a fabrica de EntityManager;
	 */
	public static void closeEntityManagerFactory() {
		closeEntityManager();
		entityManagerFactory.close();
	}

}
