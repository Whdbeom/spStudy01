package com.sparta.java_02.domain.category.repository;

import com.sparta.java_02.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
