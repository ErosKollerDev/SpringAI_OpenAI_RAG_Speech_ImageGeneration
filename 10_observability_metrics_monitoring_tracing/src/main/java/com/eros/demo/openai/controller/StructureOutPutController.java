package com.eros.demo.openai.controller;


import com.eros.demo.openai.model.CountryCities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class StructureOutPutController {


    private final ChatClient openAiClient;

    public StructureOutPutController(@Qualifier(value = "openAiClientVanilla") ChatClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @GetMapping("/structure/cities")
    public ResponseEntity<CountryCities> citiesByCountry(@RequestParam String country) {
        CountryCities entity = this.openAiClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(country)
                .call().entity(CountryCities.class);
        return ResponseEntity.ok( entity);
    }

    @GetMapping("/structure/cities-parameterized")
    public ResponseEntity<List<CountryCities>> citiesByCountryParameterized(@RequestParam String country) {
        List<CountryCities> entity = this.openAiClient
                .prompt()
                .system("Search into continent, countries and list of cities by country")
                .advisors(new SimpleLoggerAdvisor())
                .user(country)
                .call().entity(new ParameterizedTypeReference<List<CountryCities>>() {
                });
        return ResponseEntity.ok( entity);
    }


    @GetMapping("/structure/cities-bean")
    public ResponseEntity<CountryCities> citiesByCountryBean(@RequestParam String country) {
        CountryCities entity = this.openAiClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(country)
                .call().entity(new BeanOutputConverter<>( CountryCities.class));
        return ResponseEntity.ok( entity);
    }

    @GetMapping("/structure/cities-list")
    public ResponseEntity<List<String>> citiesByCountryList(@RequestParam String country) {
        List<String> entity = this.openAiClient
                .prompt()
                .system("Only a list of cities")
                .advisors(new SimpleLoggerAdvisor())
                .user(country)
                .call().entity(new ListOutputConverter());
        return ResponseEntity.ok( entity);
    }

    @GetMapping("/structure/cities-map")
    public ResponseEntity<Map<String, Object>> citiesByCountryMap(@RequestParam String country) {
        Map<String, Object> onlyAListOfCities = this.openAiClient
                .prompt()
                .system("Sigla of the state followed by a list of cities")
                .advisors(new SimpleLoggerAdvisor())
                .user(country)
                .call().entity(new MapOutputConverter());
        return ResponseEntity.ok( onlyAListOfCities);
    }


}
