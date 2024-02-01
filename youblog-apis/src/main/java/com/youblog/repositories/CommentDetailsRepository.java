package com.youblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.CommentDetailsEntity;

@Repository
public interface CommentDetailsRepository extends JpaRepository<CommentDetailsEntity, Long>{

	@Query(value = "select * from comment_details where comment_id = :commentId and active_flag = true",nativeQuery = true)
	public CommentDetailsEntity getCommentDetails(Long commentId);
	
}
