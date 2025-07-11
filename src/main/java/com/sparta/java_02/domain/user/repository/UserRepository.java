package com.sparta.java_02.domain.user.repository;

import com.sparta.java_02.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findFirstByNameAndEmail(String name, String email);

  @Query("SELECT u FROM User u JOIN FETCH u.purchases")
  List<User> findAllByWithPurchases();
}
