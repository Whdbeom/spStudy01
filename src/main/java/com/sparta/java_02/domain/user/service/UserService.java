package com.sparta.java_02.domain.user.service;

import com.sparta.java_02.common.exception.ServiceException;
import com.sparta.java_02.common.exception.ServiceExceptionCode;
import com.sparta.java_02.domain.user.dto.UserCreateRequest;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.entity.User;
import com.sparta.java_02.domain.user.mapper.UserMapper;
import com.sparta.java_02.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;

  private final UserRepository userRepository;

  @Transactional
  public List<UserSearchResponse> searchUser() {
    return userRepository.findAll().stream()
        .map(userMapper::toSearch)
        .toList();
  }

  @Transactional
  public UserSearchResponse getUserById(Long userId) {
    return userMapper.toResponse(getUser(userId));
//    User user = getUser(userId);
//
//    return UserSearchResponse.builder()
//        .id(user.getId())
//        .name(user.getName())
//        .email(user.getEmail())
//        .createdAt(user.getCreatedAt())
//        .build();
  }

  @Transactional
  public void create(UserCreateRequest request) {
    userRepository.save(userMapper.toEntity(request));
  }

  @Transactional
  public void update(Long userId, UserCreateRequest request) {
    User user = getUser(userId);

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    userRepository.save(user);
  }

  @Transactional
  public void delete(Long userId) {
    userRepository.delete(getUser(userId));
  }

  public User getUser(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_DATA));
  }

}
