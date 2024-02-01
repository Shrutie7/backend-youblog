package com.youblog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.PostSaveDetailsEntity;

@Repository
public interface PostSaveDetailsRepository extends JpaRepository<PostSaveDetailsEntity, Long>{

	@Query(value = "select * from post_save_details where post_id = :postId and user_id = :userId",nativeQuery = true)
	public PostSaveDetailsEntity getPostBookmarkUserId(Long postId, Long userId);

	@Query(value = "select * from post_save_details where user_id = :userId and active_flag = true order by post_saved_date desc",nativeQuery = true)
	public List<PostSaveDetailsEntity> getBookmarksList(Long userId);

}
