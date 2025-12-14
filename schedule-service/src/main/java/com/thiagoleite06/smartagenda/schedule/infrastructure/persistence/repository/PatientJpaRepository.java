package com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository;

import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {

    Optional<PatientEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
