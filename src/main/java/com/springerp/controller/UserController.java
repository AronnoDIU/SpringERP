package com.springerp.controller;

import com.springerp.entity.User;
import com.springerp.service.UserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/user/profile")
@Validated
public class UserController {

    private final UserService userService;
    private final Bucket bucket;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping
    @Cacheable(value = "users", key = "#root.method.name + '_' + authentication?.name", condition = "#result != null")
    public ResponseEntity<User> getUser() {
        if (bucket.tryConsume(1)) {
            User user = userService.getUser();
            return ResponseEntity.ok()
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .body(user);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PostMapping
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (bucket.tryConsume(1)) {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .body(createdUser);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @PutMapping
    @CacheEvict(value = "users", key = "#root.method.name + '_' + authentication?.name")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (bucket.tryConsume(1)) {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok()
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .body(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @DeleteMapping("/delete")
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<HttpStatus> deleteUser() {
        if (bucket.tryConsume(1)) {
            userService.deleteUser();
            return ResponseEntity.noContent()
                    .header("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens()))
                    .build();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
