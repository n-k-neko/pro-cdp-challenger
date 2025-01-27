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

    // ISSのAPIのURL
    public static final String ISS_API_URL = "http://api.open-notify.org/iss-now.json";

    @Override
    public String getType() {
        return "iss";
    }

    @Override
    public List<?> execute() {
        final Iss iss = apiClientPort.fetchDataSync(ISS_API_URL, Iss.class);
        return List.of(iss);
    }
}
