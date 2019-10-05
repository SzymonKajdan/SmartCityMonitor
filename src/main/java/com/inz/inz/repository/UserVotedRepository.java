package com.inz.inz.repository;

import com.inz.inz.entity.UserVoted;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVotedRepository extends JpaRepository<UserVoted,Long> {
    Optional<UserVoted> findByUserId(Long userId);
}
