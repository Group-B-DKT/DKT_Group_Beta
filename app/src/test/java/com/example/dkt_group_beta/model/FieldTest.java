package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import android.content.Context;


import com.example.dkt_group_beta.model.enums.FieldType;

class FieldTest {

    private Field field;
    private Context context;

    @BeforeEach
    void setUp() {
        field = new Field(1, "Test Field", 100, true, FieldType.NORMAL);
    }

    @Test
    void testGetName() {
        assertEquals("Test Field", field.getName());
    }

    @Test
    void testEnterField() {
        Player player = mock(Player.class);
        assertEquals(player.getId(), field.enterField(player));
    }

    @Test
    void testGetId() {
        assertEquals(1, field.getId());
    }

    @Test
    void testGetPrice() {
        assertEquals(100, field.getPrice());
    }

    @Test
    void testSetOwner() {
        Player owner = mock(Player.class);
        field.setOwner(owner);
        assertEquals(owner, field.getOwner());
    }

    @Test
    void testGetOwnable() {
        assertTrue(field.getOwnable());
    }

    @Test
    void testIsOwnable() {
        assertTrue(field.getOwnable());
    }

    @Test
    void testGetFieldType() {
        assertEquals(FieldType.NORMAL, field.getFieldType());
    }

    @Test
    void testLoadFields() {
        assertThrows(AssertionError.class, () -> Field.loadFields(null));
    }

    @Test
    void testEquals() {
        Field sameField = new Field(1, "Test Field", 100, true, FieldType.NORMAL);
        assertEquals(field, sameField);
    }

    @Test
    void testNotEquals() {
        Field differentField = new Field(2, "Different Field", 200, true, FieldType.SPECIAL);
        assertNotEquals(field, differentField);
    }

    @Test
    void testHashCode() {
        Field sameField = new Field(1, "Test Field", 100, true, FieldType.NORMAL);
        assertEquals(field.hashCode(), sameField.hashCode());
    }
}