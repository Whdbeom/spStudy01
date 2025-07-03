package com.sparta.java_02.domain.user.mapper;

import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "userEmail", source = "email")
  UserSearchResponse toResponse(User user);

  UserSearchResponse toSearch(User user);

  @Mapping(target = "passwordHash", source = "password")
  User toEntity(UserCreateRequest request);


}
