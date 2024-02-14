package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.PostDetailsEntity;

@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetailsEntity, Long> {

	@Query(value = "select pd.post_id,pd.title,pd.content,pd.user_id,pd.category_id,to_char(pd.created_date,'dd Mon yy HH24:MI') as createdDate,pd.decription,\r\n"
			+ "pd.active_flag,pd.archive_flag,pd.remarks,to_char(pd.updated_date,'dd Mon yy HH24:MI') as updatedDate,pd.updated_user_id,ud.email_id as cemail,concat(ud.first_name,' ',ud.last_name) as cuser,\r\n"
			+ "ud.role_id as crole,ud.location_id as cloc,uud.email_id as uemail,concat(uud.first_name,' ',uud.last_name) as uuser,\r\n"
			+ "uud.role_id as urole,uud.location_id as uloc,likeCount.likes,case when likeStatus.post_id is null then false else true end as like_status\r\n"
			+ ",case when bookmarkStatus.post_id is null then false else true end as bookmark_status \r\n"
			+ ",case when commentCount.commCount is null then 0 else commentCount.commCount end as comment_count from post_details pd \r\n"
			+ "inner join user_details ud on ud.user_id = pd.user_id and ud.active_flag = true\r\n"
			+ "left join user_details uud on uud.user_id = pd.updated_user_id and ud.active_flag = true\r\n"
			+ "left join (select post_id,count(post_likes_id)as likes from \r\n"
			+ "post_likes pl where pl.active_flag=true group by post_id)likeCount on likeCount.post_id = pd.post_id\r\n"
			+ "left join (select post_id from post_likes\r\n"
			+ "where liked_user_id = :userId and active_flag = true group by post_id)likeStatus on likeStatus.post_id = pd.post_id\r\n"
			+ "left join (select post_id,user_id from post_save_details where active_flag = true group by post_id,user_id)bookmarkStatus\r\n"
			+ "on bookmarkStatus.post_id = pd.post_id and bookmarkStatus.user_id = :userId\r\n"
			+ "left join (select cd.post_id,count(*) as commCount from comment_details cd \r\n"
			+ "inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "where cd.active_flag = true group by cd.post_id) commentCount on commentCount.post_id = pd.post_id\r\n"
			+ "where pd.active_flag = true and pd.archive_flag= false and :categoryId = any(pd.category_id) and pd.user_id in \r\n"
			+ "(with recursive userids as (select user_id,parent_user_id,gym_id,role_id from user_details \r\n"
			+ " where gym_id = (select ud.gym_id from user_details ud where ud.user_id = :userId) and active_flag=true  union all \r\n"
			+ " select ud.user_id,ud.parent_user_id,ud.gym_id,ud.role_id from user_details as ud inner join\r\n"
			+ "userids as ui on ud.user_id = ui.parent_user_id)\r\n"
			+ "select distinct(user_id) from userids) order by pd.created_date desc", nativeQuery = true)
	public List<Object[]> postListWithCategory(Long userId, Long categoryId);

	@Query(value = "select pd.post_id,pd.title,pd.content,pd.user_id,pd.category_id,to_char(pd.created_date,'dd Mon yy HH24:MI') as createdDate,pd.decription,\r\n"
			+ "			pd.active_flag,pd.archive_flag,pd.remarks,to_char(pd.updated_date,'dd Mon yy HH24:MI') as updatedDate,pd.updated_user_id,ud.email_id as cemail,concat(ud.first_name,' ',ud.last_name) as cuser,\r\n"
			+ "			ud.role_id as crole,ud.location_id as cloc,uud.email_id as uemail,concat(uud.first_name,' ',uud.last_name) as uuser,\r\n"
			+ "			uud.role_id as urole,uud.location_id as uloc,likeCount.likes,case when likeStatus.post_id is null then false else true end as like_status\r\n"
			+ "			,case when bookmarkStatus.post_id is null then false else true end as bookmark_status\r\n"
			+ " 		 ,case when commentCount.commCount is null then 0 else commentCount.commCount end as comment_count from post_details pd \r\n"
			+ "			inner join user_details ud on ud.user_id = pd.user_id and ud.active_flag = true\r\n"
			+ "			left join user_details uud on uud.user_id = pd.updated_user_id and ud.active_flag = true\r\n"
			+ "			left join (select post_id,count(post_likes_id)as likes from \r\n"
			+ "			post_likes pl where pl.active_flag=true group by post_id)likeCount on likeCount.post_id = pd.post_id\r\n"
			+ "			left join (select post_id from post_likes\r\n"
			+ "			where liked_user_id = :userId and active_flag = true group by post_id)likeStatus on likeStatus.post_id = pd.post_id\r\n"
			+ "			left join (select post_id,user_id from post_save_details where active_flag = true group by post_id,user_id)bookmarkStatus\r\n"
			+ "			on bookmarkStatus.post_id = pd.post_id and bookmarkStatus.user_id = :userId\r\n"
			+ "			left join (select cd.post_id,count(*) as commCount from comment_details cd \r\n"
			+ "			inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "			where cd.active_flag = true group by cd.post_id) commentCount on commentCount.post_id = pd.post_id\r\n"
			+ "			where pd.active_flag = true and pd.archive_flag= false and pd.user_id in \r\n"
			+ "			(with recursive userids as (select user_id,parent_user_id,gym_id,role_id from user_details \r\n"
			+ "			 where gym_id = (select ud.gym_id from user_details ud where ud.user_id = :userId) and active_flag=true  union all \r\n"
			+ "			 select ud.user_id,ud.parent_user_id,ud.gym_id,ud.role_id from user_details as ud inner join\r\n"
			+ "			userids as ui on ud.user_id = ui.parent_user_id)\r\n"
			+ "			select distinct(user_id) from userids) order by pd.created_date desc", nativeQuery = true)
	public List<Object[]> postListWithUserId(Long userId);

	@Query(value = "select pd.post_id,pd.title,pd.content,pd.user_id,pd.category_id,to_char(pd.created_date,'dd Mon yy HH24:MI') as createdDate,pd.decription,\r\n"
			+ "			pd.active_flag,pd.archive_flag,pd.remarks,to_char(pd.updated_date,'dd Mon yy HH24:MI') as updatedDate,pd.updated_user_id,ud.email_id as cemail,concat(ud.first_name,' ',ud.last_name) as cuser,\r\n"
			+ "			ud.role_id as crole,ud.location_id as cloc,uud.email_id as uemail,concat(uud.first_name,' ',uud.last_name) as uuser,\r\n"
			+ "			uud.role_id as urole,uud.location_id as uloc,likeCount.likes,case when likeStatus.post_id is null then false else true end as like_status\r\n"
			+ "			,case when bookmarkStatus.post_id is null then false else true end as bookmark_status\r\n"
			+ "			,case when commentCount.commCount is null then 0 else commentCount.commCount end as comment_count from post_details pd \r\n"
			+ "			inner join user_details ud on ud.user_id = pd.user_id and ud.active_flag = true\r\n"
			+ "			left join user_details uud on uud.user_id = pd.updated_user_id and ud.active_flag = true\r\n"
			+ "			left join (select post_id,count(post_likes_id)as likes from\r\n"
			+ "			post_likes pl where pl.active_flag=true group by post_id)likeCount on likeCount.post_id = pd.post_id\r\n"
			+ "			left join (select post_id from post_likes\r\n"
			+ "			where liked_user_id = :userId and active_flag = true group by post_id)likeStatus on likeStatus.post_id = pd.post_id\r\n"
			+ "			left join (select post_id,user_id from post_save_details where active_flag = true group by post_id,user_id)bookmarkStatus\r\n"
			+ "			on bookmarkStatus.post_id = pd.post_id and bookmarkStatus.user_id = :userId\r\n"
			+ "			left join (select cd.post_id,count(*) as commCount from comment_details cd \r\n"
			+ "			inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "			where cd.active_flag = true group by cd.post_id) commentCount on commentCount.post_id = pd.post_id\r\n"
			+ "			where pd.active_flag = true and pd.archive_flag= false and :categoryId = any(pd.category_id) and (pd.user_id in \r\n"
			+ "(select user_id from user_details ud where ud.gym_id = \r\n"
			+ "(select gym_id from gym_details gd where owner_id = :userId limit 1)\r\n"
			+ "and ud.active_flag=true and role_id = 3) or pd.user_id in (1,:userId)) order by pd.created_date desc", nativeQuery = true)
	public List<Object[]> postListWithCategoryForOwner(Long userId, Long categoryId);

	@Query(value = "select pd.post_id,pd.title,pd.content,pd.user_id,pd.category_id,to_char(pd.created_date,'dd Mon yy HH24:MI') as createdDate,pd.decription,\r\n"
			+ "			pd.active_flag,pd.archive_flag,pd.remarks,to_char(pd.updated_date,'dd Mon yy HH24:MI') as updatedDate,pd.updated_user_id,ud.email_id as cemail,concat(ud.first_name,' ',ud.last_name) as cuser,\r\n"
			+ "			ud.role_id as crole,ud.location_id as cloc,uud.email_id as uemail,concat(uud.first_name,' ',uud.last_name) as uuser,\r\n"
			+ "			uud.role_id as urole,uud.location_id as uloc,likeCount.likes,case when likeStatus.post_id is null then false else true end as like_status\r\n"
			+ "			,case when bookmarkStatus.post_id is null then false else true end as bookmark_status\r\n"
			+ "			,case when commentCount.commCount is null then 0 else commentCount.commCount end as comment_count from post_details pd \r\n"
			+ "			inner join user_details ud on ud.user_id = pd.user_id and ud.active_flag = true\r\n"
			+ "			left join user_details uud on uud.user_id = pd.updated_user_id and ud.active_flag = true\r\n"
			+ "			left join (select post_id,count(post_likes_id)as likes from\r\n"
			+ " 		post_likes pl where pl.active_flag=true group by post_id)likeCount on likeCount.post_id = pd.post_id\r\n"
			+ "			left join (select post_id from post_likes\r\n"
			+ "			where liked_user_id = :userId and active_flag = true group by post_id)likeStatus on likeStatus.post_id = pd.post_id\r\n"
			+ "			left join (select post_id,user_id from post_save_details where active_flag = true group by post_id,user_id)bookmarkStatus\r\n"
			+ "			on bookmarkStatus.post_id = pd.post_id and bookmarkStatus.user_id = :userId\r\n"
			+ "left join (select cd.post_id,count(*) as commCount from comment_details cd \r\n"
			+ "			inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "			where cd.active_flag = true group by cd.post_id) commentCount on commentCount.post_id = pd.post_id\r\n"
			+ "			where pd.active_flag = true and pd.archive_flag= false and (pd.user_id in \r\n"
			+ "(select user_id from user_details ud where ud.gym_id = \r\n"
			+ "(select gym_id from gym_details gd where owner_id = :userId limit 1)\r\n"
			+ "and ud.active_flag=true and role_id = 3) or pd.user_id in (1,:userId)) order by pd.created_date desc", nativeQuery = true)
	public List<Object[]> postListWithUserIdForOwner(Long userId);

	@Query(value = "select pd.post_id,pd.title,pd.content,pd.user_id,pd.category_id,pd.created_date,pd.decription,\r\n"
			+ "			pd.active_flag,pd.archive_flag,pd.remarks,pd.updated_date,pd.updated_user_id,ud.email_id as cemail,concat(ud.first_name,' ',ud.last_name) as cuser,\r\n"
			+ "			ud.role_id as crole,ud.location_id as cloc,uud.email_id as uemail,concat(uud.first_name,' ',uud.last_name) as uuser,\r\n"
			+ "			uud.role_id as urole,uud.location_id as uloc,likeCount.likes,case when likeStatus.post_id is null then false else true end as like_status\r\n"
			+ " 		,case when bookmarkStatus.post_id is null then false else true end as bookmark_status\r\n"
			+ "			,case when commentCount.commCount is null then 0 else commentCount.commCount end as comment_count from post_details pd \r\n"
			+ "			inner join user_details ud on ud.user_id = pd.user_id and ud.active_flag = true\r\n"
			+ "			left join user_details uud on uud.user_id = pd.updated_user_id and ud.active_flag = true\r\n"
			+ "			left join (select post_id,count(post_likes_id)as likes from\r\n"
			+ "			post_likes pl where pl.active_flag=true group by post_id)likeCount on likeCount.post_id = pd.post_id\r\n"
			+ "			left join (select post_id from post_likes\r\n"
			+ "			where liked_user_id = :userId and active_flag = true group by post_id)likeStatus on likeStatus.post_id = pd.post_id \r\n"
			+ "			left join (select post_id,user_id from post_save_details where active_flag = true group by post_id,user_id)bookmarkStatus\r\n"
			+ "			on bookmarkStatus.post_id = pd.post_id and bookmarkStatus.user_id = :userId\r\n"
			+ "			left join (select cd.post_id,count(*) as commCount from comment_details cd \r\n"
			+ "			inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "			where cd.active_flag = true group by cd.post_id) commentCount on commentCount.post_id = pd.post_id\r\n"
			+ "			where pd.post_id = :postId", nativeQuery = true)
	public List<Object[]> getPostDetails(Long postId, Long userId);

	@Query(value = "select pd.post_id,pd.title,pd.content,pd.user_id,pd.category_id,pd.created_date,pd.decription,\r\n"
			+ "			pd.active_flag,pd.archive_flag,pd.remarks,pd.updated_date,pd.updated_user_id,ud.email_id as cemail,concat(ud.first_name,' ',ud.last_name) as cuser,\r\n"
			+ "			ud.role_id as crole,ud.location_id as cloc,uud.email_id as uemail,concat(uud.first_name,' ',uud.last_name) as uuser,\r\n"
			+ "			uud.role_id as urole,uud.location_id as uloc,likeCount.likes,case when likeStatus.post_id is null then false else true end as like_status\r\n"
			+ " 		,case when bookmarkStatus.post_id is null then false else true end as bookmark_status\r\n"
			+ "			,case when commentCount.commCount is null then 0 else commentCount.commCount end as comment_count from post_details pd \r\n"
			+ "			inner join user_details ud on ud.user_id = pd.user_id and ud.active_flag = true\r\n"
			+ "			left join user_details uud on uud.user_id = pd.updated_user_id and ud.active_flag = true\r\n"
			+ "			left join (select post_id,count(post_likes_id)as likes from\r\n"
			+ "			post_likes pl where pl.active_flag=true group by post_id)likeCount on likeCount.post_id = pd.post_id\r\n"
			+ "			left join (select post_id from post_likes\r\n"
			+ "			where liked_user_id = :userId and active_flag = true group by post_id)likeStatus on likeStatus.post_id = pd.post_id \r\n"
			+ "			left join (select post_id,user_id from post_save_details where active_flag = true group by post_id,user_id)bookmarkStatus\r\n"
			+ "			on bookmarkStatus.post_id = pd.post_id and bookmarkStatus.user_id = :userId\r\n"
			+ "			left join (select cd.post_id,count(*) as commCount from comment_details cd \r\n"
			+ "			inner join user_details ud on ud.user_id = cd.user_id and ud.active_flag = true\r\n"
			+ "			where cd.active_flag = true group by cd.post_id) commentCount on commentCount.post_id = pd.post_id\r\n"
			+ "			where pd.user_id = :userId and pd.active_flag = true and pd.archive_flag = :archiveFlag", nativeQuery = true)
	public List<Object[]> postListBasedOnUser(Long userId, Boolean archiveFlag);

}
