package com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.resolver;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Consultation;
import com.thiagoleite06.smartagenda.schedule.domain.enums.ConsultationStatus;
import com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.dto.input.ConsultationInput;
import com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.dto.input.UpdateConsultationInput;
import com.thiagoleite06.smartagenda.schedule.usecase.consultation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ConsultationResolver {

    private final GetConsultationByIdUseCase getConsultationByIdUseCase;
    private final GetAllConsultationsUseCase getAllConsultationsUseCase;
    private final GetConsultationsByPatientIdUseCase getConsultationsByPatientIdUseCase;
    private final GetConsultationsByDoctorIdUseCase getConsultationsByDoctorIdUseCase;
    private final GetFutureConsultationsByPatientIdUseCase getFutureConsultationsByPatientIdUseCase;
    private final GetFutureConsultationsByDoctorIdUseCase getFutureConsultationsByDoctorIdUseCase;
    private final GetConsultationsByStatusUseCase getConsultationsByStatusUseCase;
    private final CreateConsultationUseCase createConsultationUseCase;
    private final UpdateConsultationUseCase updateConsultationUseCase;
    private final CancelConsultationUseCase cancelConsultationUseCase;
    private final CompleteConsultationUseCase completeConsultationUseCase;

    @QueryMapping
    public Consultation getConsultationById(@Argument Long id) {
        log.info("GraphQL Query: getConsultationById({})", id);
        return getConsultationByIdUseCase.execute(id);
    }

    @QueryMapping
    public List<Consultation> getAllConsultations() {
        log.info("GraphQL Query: getAllConsultations()");
        return getAllConsultationsUseCase.execute();
    }

    @QueryMapping
    public List<Consultation> getConsultationsByPatientId(@Argument Long patientId) {
        log.info("GraphQL Query: getConsultationsByPatientId({})", patientId);
        return getConsultationsByPatientIdUseCase.execute(patientId);
    }

    @QueryMapping
    public List<Consultation> getConsultationsByDoctorId(@Argument Long doctorId) {
        log.info("GraphQL Query: getConsultationsByDoctorId({})", doctorId);
        return getConsultationsByDoctorIdUseCase.execute(doctorId);
    }

    @QueryMapping
    public List<Consultation> getFutureConsultationsByPatientId(@Argument Long patientId) {
        log.info("GraphQL Query: getFutureConsultationsByPatientId({})", patientId);
        return getFutureConsultationsByPatientIdUseCase.execute(patientId);
    }

    @QueryMapping
    public List<Consultation> getFutureConsultationsByDoctorId(@Argument Long doctorId) {
        log.info("GraphQL Query: getFutureConsultationsByDoctorId({})", doctorId);
        return getFutureConsultationsByDoctorIdUseCase.execute(doctorId);
    }

    @QueryMapping
    public List<Consultation> getConsultationsByStatus(@Argument ConsultationStatus status) {
        log.info("GraphQL Query: getConsultationsByStatus({})", status);
        return getConsultationsByStatusUseCase.execute(status);
    }

    @MutationMapping
    public Consultation createConsultation(@Argument @Valid ConsultationInput input) {
        log.info("GraphQL Mutation: createConsultation(patientId={}, doctorId={})",
                input.getPatientId(), input.getDoctorId());

        Consultation consultation = Consultation.builder()
                .patientId(input.getPatientId())
                .doctorId(input.getDoctorId())
                .scheduledAt(input.getScheduledAt())
                .notes(input.getNotes())
                .build();

        return createConsultationUseCase.execute(consultation);
    }

    @MutationMapping
    public Consultation updateConsultation(@Argument Long id, @Argument @Valid UpdateConsultationInput input) {
        log.info("GraphQL Mutation: updateConsultation({})", id);
        return updateConsultationUseCase.execute(id, input);
    }

    @MutationMapping
    public Consultation cancelConsultation(@Argument Long id) {
        log.info("GraphQL Mutation: cancelConsultation({})", id);
        return cancelConsultationUseCase.execute(id);
    }

    @MutationMapping
    public Consultation completeConsultation(@Argument Long id) {
        log.info("GraphQL Mutation: completeConsultation({})", id);
        return completeConsultationUseCase.execute(id);
    }
}
