package com.sparta.java_02.domain.category.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.common.utils.JedisUtil;
import com.sparta.java_02.domain.category.dto.CategoryRequest;
import com.sparta.java_02.domain.category.dto.CategoryResponse;
import com.sparta.java_02.domain.category.entity.Category;
import com.sparta.java_02.domain.category.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final Jedis jedis;
  private final JedisUtil jedisUtil;
  private final ObjectMapper objectMapper;
  private final CategoryRepository categoryRepository;
  private static final String CACHE_KEY_CATEGORY_STRUCT = "categoryStruct";

  public List<CategoryResponse> getCategoryStruct() {
    List<Category> categories = categoryRepository.findAll();

    Map<Long, CategoryResponse> categoryResponseMap = new HashMap<>();

    for (Category category : categories) {
      CategoryResponse response = CategoryResponse.builder()
          .id(category.getId())
          .name(category.getName())
          .categories(new ArrayList<>())
          .build();

      categoryResponseMap.put(category.getId(), response);
    }

    List<CategoryResponse> rootCategories = new ArrayList<>();

    for (Category category : categories) {
      CategoryResponse categoryResponse = categoryResponseMap.get(category.getId());

      if (ObjectUtils.isEmpty(category.getParent())) {
        rootCategories.add(categoryResponse);
      } else {
        CategoryResponse parentResponse = categoryResponseMap.get(category.getParent().getId());
        if (parentResponse != null) {
          parentResponse.getCategories().add(categoryResponse);
        }
      }
    }
    return rootCategories;
  }

  @Transactional(readOnly = true)
  public List<CategoryResponse> findCategoryStructCacheAside() {
    String cachedCategories = jedis.get(CACHE_KEY_CATEGORY_STRUCT);

    try {
      if (StringUtils.hasText(cachedCategories)) {
        return objectMapper.readValue(cachedCategories,
            new TypeReference<List<CategoryResponse>>() {
            });
      }
      List<CategoryResponse> categories = getCategoryStruct();
      jedisUtil.saveObject(CACHE_KEY_CATEGORY_STRUCT, categories, 3600);

//      String jsonString = objectMapper.writeValueAsString(categories);
//      jedis.setex(CACHE_KEY_CATEGORY_STRUCT, 3600, jsonString);

      return categories;
    } catch (Exception e) {
      throw new RuntimeException("JSON 파싱 에러");
    }
  }

  @Transactional
  public void saveWriteThrough(CategoryRequest request) {
    Category parentCategory = ObjectUtils.isEmpty(request)
        ? null
        : categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));

    Category category = Category.builder()
        .name(request.getName())
        .parent(parentCategory)
        .build();

    categoryRepository.save(category);

    try {
      List<CategoryResponse> rootCategories = getCategoryStruct();

      if (!ObjectUtils.isEmpty(rootCategories)) {
        String jsonString = objectMapper.writeValueAsString(rootCategories);
        jedis.set(CACHE_KEY_CATEGORY_STRUCT, jsonString);
      }
    } catch (Exception e) {
      log.error("Error updating cache key {}: {}", CACHE_KEY_CATEGORY_STRUCT, e.getMessage());
    }

  }

  @Transactional
  public void saveWriteBack(CategoryRequest request) {
    try {
      String cachedData = jedis.get(CACHE_KEY_CATEGORY_STRUCT);
      List<CategoryResponse> categories = new ArrayList<>();

      if (!StringUtils.hasText(cachedData)) {
        categories = objectMapper.readValue(cachedData,
            new TypeReference<List<CategoryResponse>>() {
            });
      }

      CategoryResponse newCategory = CategoryResponse.builder()
          .name(request.getName())
          .categories(new ArrayList<>())
          .build();

      //TODO: 신규 카테고리는 부모 클래스 하위로 들어가도록 수정되어야함
      categories.add(newCategory);

      String jsonString = objectMapper.writeValueAsString(categories);
      jedis.set(CACHE_KEY_CATEGORY_STRUCT, jsonString);

      saveToDatabaseAsync(request);
    } catch (Exception e) {
      log.info("Write-back 패턴 저장 실패: {}", e.getMessage());
    }
  }

  @Async
  public void saveToDatabaseAsync(CategoryRequest request) {

  }


}
