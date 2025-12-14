package com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.repository;

import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
import com.thiagoleite06.smartagenda.schedule.infrastructure.persistence.entity.ConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationJpaRepository extends JpaRepository<ConsultationEntity, Long> {

    List<ConsultationEntity> findByPatientIdOrderByScheduledAtDesc(Long patientId);

    List<ConsultationEntity> findByDoctorIdOrderByScheduledAtDesc(Long doctorId);

    List<ConsultationEntity> findByStatus(ConsultationStatus status);

    @Query("SELECT c FROM ConsultationEntity c WHERE c.patientId = :patientId AND c.scheduledAt > :now AND c.status = 'SCHEDULED' ORDER BY c.scheduledAt")
    List<ConsultationEntity> findFutureConsultationsByPatientId(@Param("patientId") Long patientId, @Param("now") LocalDateTime now);

    @Query("SELECT c FROM ConsultationEntity c WHERE c.doctorId = :doctorId AND c.scheduledAt > :now AND c.status = 'SCHEDULED' ORDER BY c.scheduledAt")
    List<ConsultationEntity> findFutureConsultationsByDoctorId(@Param("doctorId") Long doctorId, @Param("now") LocalDateTime now);
}
