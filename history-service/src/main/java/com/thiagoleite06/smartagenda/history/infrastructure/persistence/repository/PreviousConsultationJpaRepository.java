package com.thiagoleite06.smartagenda.history.infrastructure.persistence.repository;

import com.thiagoleite06.smartagenda.history.infrastructure.persistence.entity.PreviousConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreviousConsultationJpaRepository extends JpaRepository<PreviousConsultationEntity, Long> {

    Optional<PreviousConsultationEntity> findByConsultationId(Long consultationId);

    List<PreviousConsultationEntity> findByPatientIdOrderByScheduledAtDesc(Long patientId);

    List<PreviousConsultationEntity> findByDoctorIdOrderByScheduledAtDesc(Long doctorId);

    List<PreviousConsultationEntity> findByStatus(String status);
}
