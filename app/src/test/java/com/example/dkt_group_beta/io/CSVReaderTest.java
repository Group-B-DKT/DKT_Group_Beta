package com.example.dkt_group_beta.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.core.app.ApplicationProvider;

import com.example.dkt_group_beta.model.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;


@RunWith(RobolectricTestRunner.class)
public class CSVReaderTest {
    Context context;
    @Mock
    Context contextMock;
    @Mock
    AssetManager assetManagerMock;
    String path = "fields.csv";

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testReadFieldsIOException() throws IOException {
        contextMock = Mockito.mock(Context.class);
        assetManagerMock = Mockito.mock(AssetManager.class);
        Mockito.when(contextMock.getAssets()).thenReturn(assetManagerMock);
        Mockito.when(assetManagerMock.open(path)).thenThrow(IOException.class);
        List<Field> fields = CSVReader.readFields(contextMock);
        assertEquals(0, fields.size());
    }
    @Test
    public void testReadFieldsSuccess() {
        List<Field> fields = CSVReader.readFields(context);
        assertEquals(30, fields.size());
    }
}