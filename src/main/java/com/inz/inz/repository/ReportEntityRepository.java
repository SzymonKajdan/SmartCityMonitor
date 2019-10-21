package com.inz.inz.repository;

import com.inz.inz.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportEntityRepository extends JpaRepository<ReportEntity,Long> {
    List<ReportEntity> findAllByUserId(Long id);
}
