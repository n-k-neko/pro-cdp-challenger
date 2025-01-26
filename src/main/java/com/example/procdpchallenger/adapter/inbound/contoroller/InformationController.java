package com.example.procdpchallenger.adapter.inbound.contoroller;

import org.springframework.web.bind.annotation.RestController;

import com.example.procdpchallenger.application.port.inbound.InformationUseCase;


import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(ApiEndpoints.API_INFORMATION)
public class InformationController {
    private final Map<String, InformationUseCase> informationUseCaseMap;

    public InformationController(List<InformationUseCase> informationUseCases) {
        this.informationUseCaseMap = informationUseCases.stream()
            /*
            Function.identity() は、引数をそのまま返す関数.
            つまり、SpringのDIコンテナによってインスタンス化されたInformationUseCaseの実装クラスのリストを、そのクラスのgetTypeメソッドの戻り値をキーとして、
            そのクラスのインスタンスを値とするMapに変換する.
            */ 
            .collect(Collectors.toMap(InformationUseCase::getType, Function.identity()));
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getInformation(@PathVariable String type) {
        InformationUseCase useCase = informationUseCaseMap.get(type.toLowerCase());
        if (useCase == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(useCase.execute());
    }
}