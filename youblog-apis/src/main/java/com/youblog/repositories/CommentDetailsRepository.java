package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.CommentDetailsEntity;

@Repository
public interface CommentDetailsRepository extends JpaRepository<CommentDetailsEntity, Long> {

	@Query(value = "select * from comment_details where comment_id = :commentId and active_flag = true", nativeQuery = true)
	public CommentDetailsEntity getCommentDetails(Long commentId);

	@Query(value = "select cd.comment_id,cd.comment_desc,to_char(cd.commented_date,'dd Mon yy HH24:MI') as commentedOn, \r\n"
			+ "			concat(ud.first_name,' ',ud.last_name) as username,ud.role_id,ud.email_id,ud.location_id,ud.user_id,\r\n"
			+ "			case when reply.count is null then 0 else reply.count end from comment_details cd\r\n"
			+ "			left join (select count(*),comment_parent_id from comment_details comd \r\n"
			+ "			inner join user_details uud on uud.user_id = comd.user_id and uud.active_flag = true\r\n"
			+ "			where comd.active_flag = true group by comment_parent_id)reply on reply.comment_parent_id = cd.comment_id\r\n"
			+ "			inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "			where cd.post_id = :postId and cd.active_flag=true", nativeQuery = true)
	public List<Object[]> postCommentList(Long postId);

	@Query(value = "with recursive comment_data as(select comd.comment_id,comd.comment_desc,comd.post_id,\r\n"
			+ "	 comd.comment_parent_id,comd.user_id,to_char(comd.commented_date,'dd Mon YY HH24:MI') \r\n"
			+ "	 as commented_date,concat(ud.first_name,' ',ud.last_name) as username,ud.role_id,ud.email_id,ud.location_id,ud.user_id  from comment_details comd \r\n"
			+ "	 inner join user_details ud on ud.user_id = comd.user_id and ud.active_flag = true\r\n"
			+ "	 where comd.comment_id = :commentId and comd.active_flag = true\r\n"
			+ "	union all  select cd.comment_id,cd.comment_desc,cd.post_id,\r\n"
			+ "	 cd.comment_parent_id,cd.user_id,to_char(cd.commented_date,'dd Mon YY HH24:MI') \r\n"
			+ "	 as commented_date,concat(ud.first_name,' ',ud.last_name) as username,ud.role_id,ud.email_id,ud.location_id,ud.user_id  from comment_details cd\r\n"
			+ "	inner join comment_data cod on cod.comment_id = cd.comment_parent_id\r\n"
			+ "	 inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "	where cd.active_flag = true)\r\n"
			+ "	select * from comment_data where post_id is null order by comment_id asc", nativeQuery = true)
	public List<Object[]> getPostCommentReplyList(Long commentId);

}
