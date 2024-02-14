package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.PostLikesEntity;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikesEntity, Long> {

	@Query(value = "select * from post_likes where post_id = :postId and liked_user_id = :userId", nativeQuery = true)
	public PostLikesEntity getPostLikeBasedOnUserId(Long postId, Long userId);

	@Query(value = "select pl.post_id,to_char(pl.liked_on_date,'dd Mon YY HH24:MI') as likedOnDate,\r\n"
			+ "concat(ud.first_name,' ',ud.last_name) as username,ud.role_id,ud.email_id,ud.location_id,ud.user_id from post_likes pl\r\n"
			+ "inner join user_details ud ON ud.user_id = pl.liked_user_id and ud.active_flag = true\r\n"
			+ "where pl.post_id = :postId and pl.active_flag = true order by pl.liked_on_date desc", nativeQuery = true)
	public List<Object[]> getPostLikesList(Long postId);

}
