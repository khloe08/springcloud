package com.spring_cloud.eureka.client.auth.infrastructure;

import com.spring_cloud.eureka.client.auth.model.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  User save(User user);

  boolean existsByUsername(String username);
}
