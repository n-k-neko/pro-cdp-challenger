package com.example.procdpchallenger.adapter.outbound.webclient.mapper;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.example.procdpchallenger.adapter.outbound.webclient.dto.IssResponse;
import com.example.procdpchallenger.domain.information.entity.Iss;
import com.example.procdpchallenger.domain.information.valueobject.Latitude;
import com.example.procdpchallenger.domain.information.valueobject.Longitude;
import com.example.procdpchallenger.domain.information.valueobject.Timestamp;

@Component
public class IssResponseMapper implements ResponseMapper<IssResponse, Iss> {
    @Override
    public Iss mapResponse(IssResponse response) {
        if (response == null || response.issPosition() == null) {
            throw new IllegalArgumentException("Invalid ISS response: response or position is null");
        }
        
        return new Iss(
            new Longitude(new BigDecimal(response.issPosition().longitude())),
            new Latitude(new BigDecimal(response.issPosition().latitude())),
            new Timestamp(Instant.ofEpochSecond(response.timestamp()))
        );
    }

    @Override
    public Class<IssResponse> getResponseType() {
        return IssResponse.class;
    }

    @Override
    public Class<Iss> getDomainType() {
        return Iss.class; 
    }
}
