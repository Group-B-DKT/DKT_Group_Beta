package com.example.dkt_group_beta.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

public class CSVReaderTest {
    @Mock
    Context contextMock;
    @Mock
    AssetManager assetManagerMock;
    String path = "fields.csv";

    @SuppressLint("CheckResult")
    @Test
    public void testReadFieldsIOException() throws IOException {
        contextMock = Mockito.mock(Context.class);
        assetManagerMock = Mockito.mock(AssetManager.class);
        Mockito.when(contextMock.getAssets()).thenReturn(assetManagerMock);
        Mockito.when(assetManagerMock.open(path)).thenThrow(IOException.class);
        assertThrows(AssertionError.class,()->CSVReader.readFields(contextMock));
    }


}