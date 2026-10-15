package org.carleton;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTest {

    @Autowired
    private AddressBookController addressBookController;

    @Autowired
    private BuddyInfoController buddyInfoController;

    @Autowired
    private AddressBookViewController addressBookViewController;

    @Test
    void contextLoads() {
        assertThat(addressBookController).isNotNull();
        assertThat(buddyInfoController).isNotNull();
        assertThat(addressBookViewController).isNotNull();
    }
}
