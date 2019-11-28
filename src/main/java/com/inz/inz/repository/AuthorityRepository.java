package com.inz.inz.repository;

import com.inz.inz.entity.Authority;
import com.inz.inz.entity.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(AuthorityName authorityName);

}
