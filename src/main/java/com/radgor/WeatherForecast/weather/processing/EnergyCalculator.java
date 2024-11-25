package com.radgor.WeatherForecast.weather.processing;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnergyCalculator {

    private static final BigDecimal INSTALLATION_POWER_KW = BigDecimal.valueOf(2.5);
    private static final BigDecimal PANEL_EFFICIENCY = BigDecimal.valueOf(0.2);
    private static final BigDecimal SECONDS_IN_AN_HOUR = BigDecimal.valueOf(3600);

    public List<BigDecimal> calculateEnergy(List<BigDecimal> sunshineDurations) {
        List<BigDecimal> generatedEnergy_kWh = new ArrayList<>();
        for (BigDecimal sunshineDuration : sunshineDurations) {
            BigDecimal sunshineHours = sunshineDuration.divide(SECONDS_IN_AN_HOUR, 4, RoundingMode.HALF_UP);
            BigDecimal energy = INSTALLATION_POWER_KW.multiply(sunshineHours).multiply(PANEL_EFFICIENCY);
            generatedEnergy_kWh.add(energy);
        }
        return generatedEnergy_kWh;
    }
}

