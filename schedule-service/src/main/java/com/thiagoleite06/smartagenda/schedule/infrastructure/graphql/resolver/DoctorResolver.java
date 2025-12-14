package com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.resolver;

import com.thiagoleite06.smartagenda.schedule.domain.entity.Doctor;
import com.thiagoleite06.smartagenda.schedule.domain.enums.Specialty;
import com.thiagoleite06.smartagenda.schedule.infrastructure.graphql.dto.input.DoctorInput;
import com.thiagoleite06.smartagenda.schedule.usecase.doctor.*;
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
public class DoctorResolver {

    private final GetDoctorByIdUseCase getDoctorByIdUseCase;
    private final GetAllDoctorsUseCase getAllDoctorsUseCase;
    private final GetDoctorsBySpecialtyUseCase getDoctorsBySpecialtyUseCase;
    private final GetDoctorByEmailUseCase getDoctorByEmailUseCase;
    private final GetDoctorByCrmUseCase getDoctorByCrmUseCase;
    private final CreateDoctorUseCase createDoctorUseCase;

    @QueryMapping
    public Doctor getDoctorById(@Argument Long id) {
        log.info("GraphQL Query: getDoctorById({})", id);
        return getDoctorByIdUseCase.execute(id);
    }

    @QueryMapping
    public List<Doctor> getAllDoctors() {
        log.info("GraphQL Query: getAllDoctors()");
        return getAllDoctorsUseCase.execute();
    }

    @QueryMapping
    public List<Doctor> getDoctorsBySpecialty(@Argument Specialty specialty) {
        log.info("GraphQL Query: getDoctorsBySpecialty({})", specialty);
        return getDoctorsBySpecialtyUseCase.execute(specialty);
    }

    @QueryMapping
    public Doctor getDoctorByEmail(@Argument String email) {
        log.info("GraphQL Query: getDoctorByEmail({})", email);
        return getDoctorByEmailUseCase.execute(email);
    }

    @QueryMapping
    public Doctor getDoctorByCrm(@Argument String crm) {
        log.info("GraphQL Query: getDoctorByCrm({})", crm);
        return getDoctorByCrmUseCase.execute(crm);
    }

    @MutationMapping
    public Doctor createDoctor(@Argument @Valid DoctorInput input) {
        log.info("GraphQL Mutation: createDoctor({})", input.getEmail());

        Doctor doctor = Doctor.builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .phone(input.getPhone())
                .specialty(input.getSpecialty())
                .crm(input.getCrm())
                .build();

        return createDoctorUseCase.execute(doctor);
    }
}
