package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.PostLikesEntity;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikesEntity, Long>{

	@Query(value = "select * from post_likes where post_id = :postId and liked_user_id = :userId",nativeQuery = true)
	public PostLikesEntity getPostLikeBasedOnUserId(Long postId, Long userId);

	@Query(value = "select * from post_likes where post_id = :postId and active_flag = true",nativeQuery = true)
	public List<PostLikesEntity> getPostLikesList(Long postId);

}
