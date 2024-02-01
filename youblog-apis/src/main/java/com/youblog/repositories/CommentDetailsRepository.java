package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.CommentDetailsEntity;

@Repository
public interface CommentDetailsRepository extends JpaRepository<CommentDetailsEntity, Long>{

	@Query(value = "select * from comment_details where comment_id = :commentId and active_flag = true",nativeQuery = true)
	public CommentDetailsEntity getCommentDetails(Long commentId);

	@Query(value = "select cd.comment_id,cd.comment_desc,to_char(cd.commented_date,'dd Mon yy HH24:MI') as commentedOn,\r\n"
			+ "concat(ud.first_name,' ',ud.last_name) as username,ud.role_id,ud.email_id,ud.location_id,ud.user_id\r\n"
			+ "from comment_details cd \r\n"
			+ "inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "where cd.post_id = :postId and cd.active_flag=true",nativeQuery = true)
	public List<Object[]> postCommentList(Long postId);
	
}
