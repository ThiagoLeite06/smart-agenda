package com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.resolver;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Patient;
import com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.dto.input.PatientInput;
import com.thiagoleite06.smartagenda.schedule.usecase.patient.CreatePatientUseCase;
import com.thiagoleite06.smartagenda.schedule.usecase.patient.GetAllPatientsUseCase;
import com.thiagoleite06.smartagenda.schedule.usecase.patient.GetPatientByEmailUseCase;
import com.thiagoleite06.smartagenda.schedule.usecase.patient.GetPatientByIdUseCase;
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
public class PatientResolver {

    private final GetPatientByIdUseCase getPatientByIdUseCase;
    private final GetAllPatientsUseCase getAllPatientsUseCase;
    private final GetPatientByEmailUseCase getPatientByEmailUseCase;
    private final CreatePatientUseCase createPatientUseCase;

    @QueryMapping
    public Patient getPatientById(@Argument Long id) {
        log.info("GraphQL Query: getPatientById({})", id);
        return getPatientByIdUseCase.execute(id);
    }

    @QueryMapping
    public List<Patient> getAllPatients() {
        log.info("GraphQL Query: getAllPatients()");
        return getAllPatientsUseCase.execute();
    }

    @QueryMapping
    public Patient getPatientByEmail(@Argument String email) {
        log.info("GraphQL Query: getPatientByEmail({})", email);
        return getPatientByEmailUseCase.execute(email);
    }

    @MutationMapping
    public Patient createPatient(@Argument @Valid PatientInput input) {
        log.info("GraphQL Mutation: createPatient({})", input.getEmail());

        Patient patient = Patient.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .phone(input.getPhone())
                .dateOfBirth(input.getDateOfBirth())
                .build();

        return createPatientUseCase.execute(patient);
    }
}
