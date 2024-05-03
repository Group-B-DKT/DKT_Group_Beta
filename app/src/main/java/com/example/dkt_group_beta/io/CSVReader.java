package com.example.dkt_group_beta.io;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.enums.FieldType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

public class CSVReader {
    public static ArrayList<Field> readFields(Context context) throws IOException {
        ArrayList<Field> list = new ArrayList<>();

        String path = "fields.csv";
        BufferedReader br = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(path);
            br = new BufferedReader(new InputStreamReader(is));

            Log.d("LOAD FIELDS", br.readLine());
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                Log.d("DEBUG", Arrays.toString(values));
                list.add(new Field(Integer.parseInt(values[0]),
                                   values[1],
                                   Integer.parseInt(values[2]),
                                   Boolean.parseBoolean(values[3]),
                                   FieldType.valueOf(values[4])));
            }
            br.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        finally {
            assert br != null;
            br.close();
        }
        return list;
    }
}
