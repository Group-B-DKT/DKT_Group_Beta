package com.example.dkt_group_beta.io;

import com.example.dkt_group_beta.model.Field;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
        public static ArrayList<Field> readFields() {
            ArrayList<Field> list = new ArrayList<>();
            String path = "fields.csv";
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                System.out.println(br.readLine());
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");
                    list.add(new Field(Integer.parseInt(values[0]), values[1], Boolean.parseBoolean(values[2])));

                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
            return list;
        }

}

