package com.inz.inz.repository;

import com.inz.inz.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityEntityRepository extends JpaRepository<CityEntity,Long> {
    Optional<CityEntity> findByName(String name);

}
