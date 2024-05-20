package com.example.dkt_group_beta.io;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.dkt_group_beta.model.Card;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.model.enums.FieldType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardCSVReader {
    private CardCSVReader() {}
    public static List<Card> readCards(Context context, String path) {
        ArrayList<Card> list = new ArrayList<>();

        AssetManager am = context.getAssets();
        try (InputStream is = am.open(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                list.add(new Card(Integer.parseInt(values[0]),
                                   Integer.parseInt(values[1]),
                        CardType.valueOf(values[2]),
                                    values[3]));
            }
        } catch (IOException e) {
            return list;
        }
        return list;
    }
}
