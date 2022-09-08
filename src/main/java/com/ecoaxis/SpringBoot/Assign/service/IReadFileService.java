package com.ecoaxis.SpringBoot.Assign.service;

import com.ecoaxis.SpringBoot.Assign.bean.EnergyMeter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Statement;
import java.util.List;

public interface IReadFileService {

    public void saveInDB(Statement s, String ts, int min, int hour);

    public void insertEnergyMeterValues(List<EnergyMeter> hs);

    public void calculateEnergyConsumption(String date);

    public List<EnergyMeter> getEnergyConsumption(String date);

    public void readCsv(MultipartFile file);

//    public List<EnergyMeter> readCsvAndGetHourlyEnergy(MultipartFile file);

//    public List<EnergyMeter> readCsv2(MultipartFile file);
}
