package com.ecoaxis.SpringBoot.Assign.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnergyMeter {
    private LocalDateTime date;
    private int meter;
    private int hour;

    public EnergyMeter(LocalDateTime date, int meter) {
        this.date = date;
        this.meter = meter;
    }


}
