package com.thiagoleite06.smartagenda.history.infrastructure.graphql.resolver;

import com.thiagoleite06.smartagenda.history.domain.entity.PreviousConsultation;
import com.thiagoleite06.smartagenda.history.usecase.GetConsultationHistoryByPatientIdUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HistoryQueryResolver {

    private final GetConsultationHistoryByPatientIdUseCase getHistoryUseCase;

    @QueryMapping
    public List<PreviousConsultation> getConsultationHistoryByPatientId(@Argument Long patientId) {
        log.info("GraphQL Query: getConsultationHistoryByPatientId({})", patientId);
        return getHistoryUseCase.execute(patientId);
    }
}
