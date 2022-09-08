package com.ecoaxis.SpringBoot.Assign.controller;

import com.ecoaxis.SpringBoot.Assign.bean.EnergyMeter;
import com.ecoaxis.SpringBoot.Assign.bean.ResponseDto;
import com.ecoaxis.SpringBoot.Assign.service.IReadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/csvReader")
public class ReadCSVEnergyMeterController {
    @Autowired
    private IReadFileService readCSVFileService;

    @PostMapping("/readCSVfile")
    public ResponseEntity<ResponseDto> insertCSVFileToDB(@RequestParam("file") MultipartFile file) {
        readCSVFileService.readCsv(file);
        ResponseDto responseDTO = new ResponseDto("Inserted the csv data to DB succesfully", file);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.OK);
    }
    @PostMapping("/calculateEnergyConsumption/{date}")
    public ResponseEntity<ResponseDto> calculateEnergyConsumptionAndStoreInDB(@PathVariable String date) {
        readCSVFileService.calculateEnergyConsumption(date);
        ResponseDto responseDTO = new ResponseDto("calculated succesfully", date);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/getallEnergyConsumption/{Date}")
    public ResponseEntity<ResponseDto> getAllHourlyEnergyConsumptionData(@PathVariable String Date) {
        List<EnergyMeter> list = readCSVFileService.getEnergyConsumption(Date);
        ResponseDto responseDTO = new ResponseDto("get call worked succesfully", list);
        return new ResponseEntity<ResponseDto>(responseDTO, HttpStatus.OK);
    }


}

