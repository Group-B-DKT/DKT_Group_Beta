package com.example.dkt_group_beta.io;

import android.content.Context;
import android.content.res.AssetManager;


import com.example.dkt_group_beta.model.Card;
import com.example.dkt_group_beta.model.JokerCard;
import com.example.dkt_group_beta.model.MoveCard;
import com.example.dkt_group_beta.model.PayCard;
import com.example.dkt_group_beta.model.enums.CardType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CardCSVReader {
    private CardCSVReader() {}
    public static List<Card> readCards(Context context, String path) {
        ArrayList<Card> list = new ArrayList<>();

        AssetManager am = context.getAssets();
        try (InputStream is = am.open(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            //br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                // Skip processing the first line (assuming it's the header)
                if (line.startsWith("ID;VALUE;TYPE;PATHNAME")) {
                    continue;
                }
                String[] values = line.split(";");
                CardType cardType = CardType.valueOf(values[2]);
                if(cardType == CardType.MOVE){
                    list.add(new MoveCard(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]),
                            CardType.valueOf(values[2]),
                            Integer.parseInt(values[3]),
                            values[4]));
                }else if(cardType == CardType.PAY){
                    list.add(new PayCard(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]),
                            CardType.valueOf(values[2]),
                            values[3]));
                }else{
                    list.add(new JokerCard(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]),
                            CardType.valueOf(values[2]),
                            values[3]));
                }
            }
        } catch (IOException e) {
            return list;
        }
        return list;
    }
}
