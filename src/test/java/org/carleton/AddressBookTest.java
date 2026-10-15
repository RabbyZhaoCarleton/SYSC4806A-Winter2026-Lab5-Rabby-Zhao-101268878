package org.carleton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressBookTest {
    private AddressBook addressBook;

    @BeforeEach
    void setUp() {
        addressBook = new AddressBook();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addBuddy() {
        BuddyInfo buddy = new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON");
        addressBook.addBuddy(buddy);
        assertTrue(addressBook.containsBuddy(buddy));
    }

    @Test
    void removeBuddy() {
        BuddyInfo buddy = new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON");
        addressBook.addBuddy(buddy);
        addressBook.removeBuddy(buddy);
        assertFalse(addressBook.containsBuddy(buddy));
    }

    @Test
    void printBuddyList() {
        BuddyInfo buddy = new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON");
        addressBook.addBuddy(buddy);
        assertEquals("Rabby: Rabby@gmail.com, 123-456-7890, 123 Main St, Carleton, ON\n", addressBook.printBuddyList());
    }

    @Test
    void containsBuddy() {
    }
}