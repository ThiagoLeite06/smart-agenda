package com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository;

import com.thiagoleite06.smartagenda.schedule.domain.enums.Specialty;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Long> {

    Optional<DoctorEntity> findByEmail(String email);

    Optional<DoctorEntity> findByCrm(String crm);

    List<DoctorEntity> findBySpecialty(Specialty specialty);

    boolean existsByEmail(String email);

    boolean existsByCrm(String crm);
}
