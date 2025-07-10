package com.sparta.java_02;

import com.sparta.java_02.domain.user.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Java02Application {

  public static void main(String[] args) {
    SpringApplication.run(Java02Application.class, args);

    User user = User.builder()
        .name("이름")
        .email("이메일")
        .build();
  }

}
