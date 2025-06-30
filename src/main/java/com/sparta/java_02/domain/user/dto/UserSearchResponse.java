package com.sparta.java_02.domain.user.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSearchResponse {

  Long id;

  String name;

  String userEmail;

  LocalDateTime createdAt;

}
