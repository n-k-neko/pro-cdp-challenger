package com.example.procdpchallenger.application.service.information;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.procdpchallenger.application.port.inbound.InformationUseCase;
import com.example.procdpchallenger.application.port.outbound.ApiClientPort;
import com.example.procdpchallenger.domain.information.entity.Iss;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IssInformationService implements InformationUseCase {
    private final ApiClientPort apiClientPort;

    @Override
    public String getType() {
        return "iss";
    }

    @Override
    public List<?> execute() {
        final Iss iss = apiClientPort.fetchDataSync(Iss.class);
        return List.of(iss);
    }
}
