package com.clearavenue.fdadi.controller;

import com.clearavenue.fdadi.model.*;
import com.clearavenue.fdadi.service.ApiService;
import com.clearavenue.fdadi.service.MedicationService;
import com.clearavenue.fdadi.service.PharmClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MedicationRestController {

    private final MedicationService medService;
    private final PharmClassService pharmService;
    private final ApiService api;

    @GetMapping("/allMedications")
    public AllMedicationsResult getAllMedications() {
        return AllMedicationsResult.builder().medications(medService.findAll()).build();
    }

    @GetMapping("/medication/{medicationName}")
    public MedicationResult getMedication(@PathVariable final String medicationName) {
        return MedicationResult.builder().medication(medService.findByMedicationName(medicationName)).build();
    }

    @GetMapping("/medicationDetails/{medicationName}")
    public MedicationDetailsResult getMedicationDetails(@PathVariable final String medicationName) {
        LabelResult result = LabelResult.builder().meta(Meta.builder().disclaimer("No Details Found").build()).build();

        final Optional<LabelResult> label = api.getLabel(medicationName);
        if (label.isPresent()) {
            result = label.get();
        }

        return MedicationDetailsResult.builder().labelResult(result).build();
    }

    @GetMapping("/allPharmClasses")
    public AllPharmClassesResult getAllPharmClasses() {
        return AllPharmClassesResult.builder().pharmClasses(pharmService.findAll()).build();
    }

    @GetMapping("/medicationsByPharmClass/{pharmClassName}")
    public MedicationsResult getMedicationsByPharmClass(@PathVariable final String pharmClassName) {
        final List<Medication> medicationsList = new ArrayList<>();

        final Optional<LabelResult> label = api.getLabelsByPharmClass(pharmClassName);
        if (label.isPresent()) {
            final LabelResult result = label.get();
            result.results.forEach(l -> {
                l.getOpenfda().getBrandName().stream().map(bn -> Medication.builder().medicationName(bn.toUpperCase(Locale.getDefault())).build()).forEach(medicationsList::add);
                l.getOpenfda().getGenericName().stream().map(gn -> Medication.builder().medicationName(gn.toUpperCase(Locale.getDefault())).build()).forEach(medicationsList::add);
            });
        }

        return MedicationsResult.builder().medications(medicationsList.stream().distinct().collect(Collectors.toList())).build();
    }

}
