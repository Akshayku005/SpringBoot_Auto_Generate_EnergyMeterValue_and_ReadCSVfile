package com.ecoaxis.SpringBoot.Assign.service;

import com.ecoaxis.SpringBoot.Assign.bean.EnergyMeter;
import com.ecoaxis.SpringBoot.Assign.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ReadCSVFileService implements IReadFileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    static final Logger log = LoggerFactory.getLogger(DB.class);
    static final List<EnergyMeter> readfilelist = new ArrayList<>();

    @Override
    public void saveInDB(Statement s, String ts, int min, int hour) {
        try {
            if (hour == 1) {
                jdbcTemplate.batchUpdate("update energy.energytable set hr = " + null + " where time_stamp = '" + ts + "'");
            } else {
                jdbcTemplate.batchUpdate("update energy.energytable set hr = " + hour + " where time_stamp = '" + ts + "'");
            }
            s.executeBatch();
        } catch (SQLException e) {
            log.warn("Exception On SQL" + e.getMessage());
        }
    }


    @Override
    public void insertEnergyMeterValues(List<EnergyMeter> hs) {
        Connection con = null;
        PreparedStatement s = null;
        try {
            con = DB.createC();
            s = con.prepareStatement("insert into energy.energytable (time_stamp, meter) values (?,?)");
            for (EnergyMeter data : hs) {
                EnergyMeter energyMeter = new EnergyMeter(data.getDate(), data.getMeter());
                s.setTimestamp(1, java.sql.Timestamp.valueOf(data.getDate()));
                s.setInt(2, data.getMeter());
                System.out.println(("insert into energy.energytable (time_stamp, meter) values (" + java.sql.Timestamp.valueOf(data.getDate()) + ", " + data.getMeter() + ")"));
                s.addBatch();
            }
            s.executeBatch();
            System.out.println(("batch executed"));
        } catch (SQLException e) {
            log.warn("Exception On SQL" + e.getMessage());
        } finally {
            DB.closePreparedStatement(s);
            DB.closeConnection(con);
        }
    }

    @Override
    public void calculateEnergyConsumption(String date) {
        Connection con = null;
        PreparedStatement s = null;
        try {
            con = DB.createC();
            List<EnergyMeter> list = new ArrayList<>();
            s = con.prepareStatement("SELECT time_stamp, meter FROM energytable where time_stamp like '" + date + "%'");
            ResultSet r = s.executeQuery();

            while (r.next()) {
                if (r.getInt(2) >= 0) {
                    list.add(new EnergyMeter(r.getTimestamp("time_stamp").toLocalDateTime(), r.getInt("meter")));
                }
            }
            //f= to get initial, z= to get hourly value, im= to set initial value
            int f = 0, z = 0, im = 0, hour = 0;
            for (EnergyMeter data : list) {
                if (f == 0 || z == 60) {
                    if (f == 0) {
                        int min = data.getMeter();
                        String ts = String.valueOf(data.getDate());
                        im = min;
                        f++;
                        z++;
                        saveInDB(s, ts, min, hour);
                    } else {
                        int min = data.getMeter();
                        String ts = String.valueOf(data.getDate());
                        hour = min - im;
                        saveInDB(s, ts, min, hour);
                        z = 1;
                        im = min;
                    }
                } else {
                    int min = data.getMeter();
                    String ts = String.valueOf(data.getDate());
                    saveInDB(s, ts, min, 1);
                    z++;
                }
            }
        } catch (SQLException e) {
            log.warn("Exception On SQL" + e.getMessage());
        } finally {
            DB.closeConnection(con);
            DB.closePreparedStatement(s);
            System.out.println("connection Closed!");
        }
    }

    @Override
    public List<EnergyMeter> getEnergyConsumption(String date) {
        List<EnergyMeter> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement s = null;
        try {
            con = DB.createC();
            System.out.println("Connection open");
            System.out.println(" ");
            s = con.prepareStatement("SELECT * FROM energy.energytable where time_stamp like '" + date + "%'");
            ResultSet r = s.executeQuery();
            Boolean empty = true;
            if (r.next()) {
                empty = false;
                calculateEnergyConsumption(date);
            }

            while (r.next()) {
                if (r.getInt(2) >= 0) {
                    if (r.getInt(3) > 1) {
                        String Time = r.getString(1);
                        int meter = r.getInt(2);
                        int hour = r.getInt(3);
                        EnergyMeter energyMeter = new EnergyMeter();
                        energyMeter.setDate(Timestamp.valueOf(Time).toLocalDateTime());
                        energyMeter.setMeter(meter);
                        energyMeter.setHour(hour);
                        list.add(energyMeter);
                        System.out.println("Time: " + r.getString(1));
                        System.out.println("Hourly energy consumed:" + r.getInt(3));
                        System.out.println("");
                    }
                } else {
                    calculateEnergyConsumption(date);
                    getEnergyConsumption(date);
                }
            }
        } catch (SQLException e) {
            log.warn("Exception On SQL" + e.getMessage());
        } finally {
            DB.closeConnection(con);
            DB.closePreparedStatement(s);
            System.out.println("connection Closed!");
        }
        return list;
    }

    @Override
    public void readCsv(MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        String st = new String();
        try {
            byte[] bytes = file.getBytes();
            ByteArrayInputStream inputFilestream = new ByteArrayInputStream(bytes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputFilestream));
            String line = "";

//            BufferedReader reader = new BufferedReader(new FileReader(excelFilePath));
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String dateTime = fields[0];
                ZonedDateTime parsed = ZonedDateTime.parse(dateTime, formatter.withZone(ZoneId.of("UTC")));

                if (fields.length > 0) {
                    EnergyMeter energyMeter = new EnergyMeter();
                    energyMeter.setDate(parsed.toLocalDateTime());
                    energyMeter.setMeter(Integer.parseInt(fields[1]));
                    readfilelist.add(energyMeter);
                }
            }
            for (EnergyMeter energyMeter : readfilelist) {
                System.out.printf("[timeStamp=%s, meter=%d]\n", energyMeter.getDate(), energyMeter.getMeter());


                st = String.valueOf(energyMeter.getDate());      //converted localdate to string
                LocalDate date = LocalDate.parse(st, formatter);
            }
            insertEnergyMeterValues(readfilelist);


            calculateEnergyConsumption(st);
            getEnergyConsumption(st);




        } catch (IOException ex) {
            log.warn("IO exception occured" + ex.getMessage());
        }
    }
}


