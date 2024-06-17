package com.example.dkt_group_beta.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Context;

import com.example.dkt_group_beta.model.enums.FieldType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertThrows(NullPointerException.class, () -> Field.loadFields(null));
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
    @Test
    void testSetId() {
        field.setId(2);
        assertEquals(2, field.getId());
    }
    @Test
    void testSetPrice() {
        field.setPrice(200);
        assertEquals(200, field.getPrice());
    }

    @Test
    void testCopyFrom() {
        Field copiedField = new Field(3, "Copied Field", 300, true, FieldType.SPECIAL);
        field.copyFrom(copiedField);
        assertEquals(3, field.getId());
        assertEquals("Copied Field", field.getName());
    }
    @Test
    void testAddHouse(){
        House house = mock(House.class);
        field.addHouse(house);
        assertTrue(field.getHouses().contains(house));
    }
    @Test
    void testRemoveHouse(){
        House house = mock(House.class);
        field.addHouse(house);
        field.getHouses().remove(house);
        assertTrue(field.getHouses().isEmpty());
    }
}
