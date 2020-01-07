package com.clearavenue.fdadi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.clearavenue.fdadi.model.LabelResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {

	private final RestTemplate restTemplate;

	public Optional<LabelResult> getLabel(final String medicationName) {
		final String fixedMedicationName = fixString(medicationName);
		final String url = String.format("https://api.fda.gov/drug/label.json?search=(openfda.generic_name:\"%s\")+(openfda.brand_name:\"%s\")", fixedMedicationName,
				fixedMedicationName);
		final LabelResult result = restTemplate.getForObject(url, LabelResult.class);
		return Optional.ofNullable(result);
	}

	private String fixString(final String s) {
		return s.replaceAll(" ", "+").replaceAll(",", "");
	}

	public Optional<LabelResult> getLabelsByPharmClass(final String pharmClassName) {
		final String fixedPharmClassName = fixString(pharmClassName);
		final String url = String.format("https://api.fda.gov/drug/label.json?search=(openfda.pharm_class_epc:\"%s\")&limit=100", fixedPharmClassName);
		log.info(url);

		try {
			final LabelResult result = restTemplate.getForObject(url, LabelResult.class);
			return Optional.ofNullable(result);
		} catch (final RestClientException e) {
			// Not found so do nothing
		}

		return Optional.empty();
	}

}