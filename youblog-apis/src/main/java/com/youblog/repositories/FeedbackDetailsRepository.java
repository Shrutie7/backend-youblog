package com.youblog.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.youblog.entities.FeedbackDetailsEntity;

public interface FeedbackDetailsRepository extends JpaRepository<FeedbackDetailsEntity,Long>{


	@Query(value="SELECT FD.RATING,\r\n"
			+ "	FD.MESSAGE,\r\n"
			+ "	concat (UD.FIRST_NAME,' ',\r\n"
			+ "	UD.LAST_NAME) AS userName\r\n"
			+ "FROM FEEDBACK_DETAILS AS FD\r\n"
			+ "LEFT JOIN USER_DETAILS AS UD ON UD.USER_ID = FD.USER_ID\r\n"
			+ "WHERE FD.feedback_type_id = 2 and gym_id=:gymId\r\n"
			+ "",nativeQuery = true)
	public ArrayList<Object[]> getfeedbacklist(Long gymId);
}
