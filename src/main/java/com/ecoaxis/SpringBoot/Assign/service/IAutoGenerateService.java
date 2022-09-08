package com.ecoaxis.SpringBoot.Assign.service;

import com.ecoaxis.SpringBoot.Assign.bean.EnergyMeter;

import java.sql.Statement;
import java.util.List;

public interface IAutoGenerateService {

    public void saveInDB(Statement s, String ts, int min, int hour);

    public void getEnergyMeterData(String date);

    public void insertEnergyMeterValues(List<EnergyMeter> hs);

    public void calculateEnergyConsumption(String date);

    public List<EnergyMeter> getEnergyConsumption(String date);
}
