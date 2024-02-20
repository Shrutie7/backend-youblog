package com.youblog.utils;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class QueryExec {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	public int updateQuery(String sqlQuery) throws SqlCustomException {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNativeQuery(sqlQuery);
		log.info("Query : "+sqlQuery);
		entityManager.getTransaction().begin();
		int result = 0;
		try {
			result = query.executeUpdate();
		} catch (PersistenceException e) {
			entityManager.close();
			if (e.getCause().getCause() instanceof PSQLException) {
				final PSQLException exception = (PSQLException) e.getCause().getCause();
				switch (exception.getServerErrorMessage().getRoutine()) {
				case "_bt_check_unique":
					log.error("Data already Exist. Cannot be duplicated");
					throw new SqlCustomException("Data Cannot be duplicated");
				case "ExecConstraints":
					log.error("Mandatory field are not provided.");
					throw new SqlCustomException("Mandatory field are not provided.");
				default:
					log.error(exception.getServerErrorMessage().toString());
					throw new SqlCustomException(exception.getServerErrorMessage().toString());
				}
			}
		}
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
}
