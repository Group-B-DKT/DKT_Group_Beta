package com.example.dkt_group_beta.io;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.dkt_group_beta.model.Field;

import junit.framework.TestCase;

import java.util.ArrayList;

public class CSVReaderTest extends TestCase {
    Context context;
    ArrayList<Field> fields = new ArrayList<>();
    public void setUp() {
        fields = new ArrayList<>();
        context = ApplicationProvider.getApplicationContext();
    }

    public void testReadFields() {
        fields = CSVReader.readFields(context);
        assertEquals(30, fields.size());
    }
}