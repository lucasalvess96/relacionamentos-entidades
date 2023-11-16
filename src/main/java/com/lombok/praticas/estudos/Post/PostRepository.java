package com.lombok.praticas.estudos.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<PostEntity> findByTitleContainingIgnoreCase(String title);
}
