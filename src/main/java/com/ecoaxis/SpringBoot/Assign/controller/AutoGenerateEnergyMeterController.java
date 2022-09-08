package com.ecoaxis.SpringBoot.Assign.controller;

import com.ecoaxis.SpringBoot.Assign.bean.EnergyMeter;
import com.ecoaxis.SpringBoot.Assign.bean.ResponseDto;
import com.ecoaxis.SpringBoot.Assign.service.AutoGenerateService;
import com.ecoaxis.SpringBoot.Assign.service.IAutoGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AutoGenerateEnergyMeterController {

    @Autowired
    private IAutoGenerateService autoGenerateService;

    @RequestMapping("/hello")
    public String getHelloWorld() {
        return "hello All";
    }

    /**
     * PostMapping used to insert auto generated energy meter value to DB and need to pass date.
     * According to the given date it will generate energy values
     * need to pass date in formate 20130204
     * @param localDate
     * @return
     */
    @PostMapping("/autoinsert/{localDate}")
    public ResponseEntity<ResponseDto> insertMethod(@PathVariable String localDate) {
        autoGenerateService.getEnergyMeterData(localDate);
        ResponseDto responseDTO = new ResponseDto("Inserted succesfully", localDate);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.OK);
    }
    /**
     * PostMapping used to Calculate hourly Energy consumption and will store it in DB
     * Need to pass date in the formate 2022-08-18.
     * It will calculate according to the given date
     * @param Date
     * @return
     */
    @PostMapping("/calculate-energyconsumption/{date}")
    public ResponseEntity<ResponseDto> calculateEnergyConsumptionAndStoreInDB(@PathVariable String date) {
        autoGenerateService.calculateEnergyConsumption(date);
        ResponseDto responseDTO = new ResponseDto("calculated succesfully", date);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.OK);
    }
    /**
     * GetMapping used to fetch all the hourly energy concumption data of particular date.
     * Need to pass date in the formate 2022-08-18.
     * Also it will insert data, calculate and display the data in JSON formate
     * @param Date
     * @return
     */
    @GetMapping("/get-energy-consumption/{date}")
    public ResponseEntity<ResponseDto> getHourlyEnergyConsumptionForParticularDate(@PathVariable String date) {
        List<EnergyMeter> list = autoGenerateService.getEnergyConsumption(date);
        ResponseDto responseDTO = new ResponseDto("Get call for energy consumption worked succesfully", list);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.OK);
    }

}
