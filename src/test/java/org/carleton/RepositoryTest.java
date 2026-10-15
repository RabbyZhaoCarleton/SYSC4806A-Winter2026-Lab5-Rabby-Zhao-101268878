package org.carleton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryTest {

    @Autowired
    private BuddyInfoRepository buddyRepository;

    @Autowired
    private AddressBookRepository addressBookRepository;

    // Start every test from an empty database.
    @BeforeEach
    void clearDatabase() {
        addressBookRepository.deleteAll();
        buddyRepository.deleteAll();
    }

    @Test
    void saveAndFindBuddyByName() {
        buddyRepository.save(new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON"));
        List<BuddyInfo> found = buddyRepository.findByName("Rabby");
        assertEquals(1, found.size());
        assertEquals("Rabby@gmail.com", found.get(0).getEmail());
    }

    @Test
    void saveAddressBookWithBuddies() {
        AddressBook book = new AddressBook();
        book.addBuddy(new BuddyInfo("Jane", "Jane@gmail.com", "987-654-3210", "456 Elm St, Toronto, ON"));
        book.addBuddy(new BuddyInfo("Bob", "Bob@gmail.com", "555-555-5555", "789 Oak St, Ottawa, ON"));
        book = addressBookRepository.save(book); // buddies saved via cascade

        AddressBook found = addressBookRepository.findById(book.getId().longValue());
        assertNotNull(found);
        assertEquals(2, found.getBuddyList().size());
        assertEquals(2, buddyRepository.count());
    }
}
