package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.Category;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<@NonNull Category, @NonNull UUID> {
}
