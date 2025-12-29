package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.Post;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<@NonNull Post, @NonNull UUID> {
}
