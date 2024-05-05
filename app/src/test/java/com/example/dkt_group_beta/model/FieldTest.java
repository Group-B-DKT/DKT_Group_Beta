package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import android.content.Context;


import com.example.dkt_group_beta.model.enums.FieldType;

public class FieldTest {

    private Field field;
    private Context context;

    @BeforeEach
    public void setUp() {
        field = new Field(1, "Test Field", 100, true, FieldType.NORMAL);
    }

    @Test
    public void testGetName() {
        assertEquals("Test Field", field.getName());
    }

    @Test
    public void testEnterField() {
        Player player = mock(Player.class);
        assertEquals(player.getId(), field.enterField(player));
    }

    @Test
    public void testGetId() {
        assertEquals(1, field.getId());
    }

    @Test
    public void testGetPrice() {
        assertEquals(100, field.getPrice());
    }

    @Test
    public void testSetOwner() {
        Player owner = mock(Player.class);
        field.setOwner(owner);
        assertEquals(owner, field.getOwner());
    }

    @Test
    public void testGetOwnable() {
        assertTrue(field.getOwnable());
    }

    @Test
    public void testIsOwnable() {
        assertTrue(field.getOwnable());
    }

    @Test
    public void testGetFieldType() {
        assertEquals(FieldType.NORMAL, field.getFieldType());
    }

    @Test
    public void testLoadFields() {
        assertThrows(AssertionError.class, () -> Field.loadFields(null));
    }

    @Test
    public void testEquals() {
        Field sameField = new Field(1, "Test Field", 100, true, FieldType.NORMAL);
        assertEquals(field, sameField);
    }

    @Test
    public void testNotEquals() {
        Field differentField = new Field(2, "Different Field", 200, true, FieldType.SPECIAL);
        assertNotEquals(field, differentField);
    }

    @Test
    public void testHashCode() {
        Field sameField = new Field(1, "Test Field", 100, true, FieldType.NORMAL);
        assertEquals(field.hashCode(), sameField.hashCode());
    }
}
