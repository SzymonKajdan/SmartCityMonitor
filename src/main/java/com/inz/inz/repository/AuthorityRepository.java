package com.inz.inz.repository;

import com.inz.inz.security.model.Authority;
import com.inz.inz.security.model.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(AuthorityName authorityName);

}
