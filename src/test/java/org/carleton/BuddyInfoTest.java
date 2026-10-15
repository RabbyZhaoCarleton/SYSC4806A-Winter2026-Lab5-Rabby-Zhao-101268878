package org.carleton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuddyInfoTest {
    private BuddyInfo buddy;

    @BeforeEach
    void setUp() {
        buddy = new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        assertEquals("Rabby", buddy.getName());
    }

    @Test
    void getEmail() {
        assertEquals("Rabby@gmail.com", buddy.getEmail());
    }

    @Test
    void getPhone() {
        assertEquals("123-456-7890", buddy.getPhone());
    }

    @Test
    void getAddress() {
        assertEquals("123 Main St, Carleton, ON", buddy.getAddress());
    }

    @Test
    void nicknameIsNullWhenNotGiven() {
        assertNull(buddy.getNickname());
    }

    @Test
    void getNickname() {
        BuddyInfo withNickname = new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON", "Rab");
        assertEquals("Rab", withNickname.getNickname());
    }

    @Test
    void setNickname() {
        buddy.setNickname("Rab");
        assertEquals("Rab", buddy.getNickname());
    }
}
