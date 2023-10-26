package com.lombok.praticas.estudos.person;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PersonEntityTest {

    @Test
    @DisplayName("Should return true for objects being equal")
    void testEqualsForSameObjects() {
        PersonEntity personEntity = new PersonEntity(1L, "John Doe", "30", "123456789");
        assertEquals(personEntity, personEntity);
    }

    @Test
    @DisplayName("Should return true for objects with the same fields")
    void testEqualsForSameFields() {
        PersonEntity personEntity1 = new PersonEntity(1L, "John Doe", "30", "123456789");
        PersonEntity personEntity2 = new PersonEntity(1L, "John Doe", "30", "123456789");
        assertEquals(personEntity1, personEntity2);
    }

    @Test
    @DisplayName("Should return false for objects with different fields")
    void testEqualsForDifferentFields() {
        PersonEntity personEntity1 = new PersonEntity(1L, "John Doe", "30", "123456789");
        PersonEntity personEntity2 = new PersonEntity(2L, "Jane Smith", "25", "987654321");
        assertNotEquals(personEntity1, personEntity2);
    }

    @Test
    @DisplayName("Should return same hash code for objects with same fields")
    void testHashCodeForSameFields() {
        PersonEntity personEntity1 = new PersonEntity(1L, "John Doe", "30", "123456789");
        PersonEntity personEntity2 = new PersonEntity(1L, "John Doe", "30", "123456789");
        assertEquals(personEntity1.hashCode(), personEntity2.hashCode());
    }
}
