package org.carleton;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTestClient restTestClient;

    private String url(String path) {
        return "http://localhost:%d%s".formatted(port, path);
    }

    @Test
    void createAddressBookAndBuddiesOverHttp() {
        // POST /addressbooks
        AddressBook book = restTestClient.post().uri(url("/addressbooks"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddressBook.class)
                .returnResult().getResponseBody();
        assertThat(book).isNotNull();
        long bookId = book.getId();

        // POST /addressbooks/{id}/buddies with a JSON body
        restTestClient.post().uri(url("/addressbooks/" + bookId + "/buddies"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BuddyInfo("Rabby", "Rabby@gmail.com", "123-456-7890", "123 Main St, Carleton, ON"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(bookId)
                .jsonPath("$.buddyList.length()").isEqualTo(1)
                .jsonPath("$.buddyList[0].name").isEqualTo("Rabby");

        // GET /addressbooks/{id}
        restTestClient.get().uri(url("/addressbooks/" + bookId))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.buddyList[0].email").isEqualTo("Rabby@gmail.com")
                .jsonPath("$.buddyList[0].phone").isEqualTo("123-456-7890");
    }

    @Test
    void thymeleafPageIsServedOverHttp() {
        long bookId = restTestClient.post().uri(url("/addressbooks"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddressBook.class)
                .returnResult().getResponseBody().getId();

        restTestClient.post().uri(url("/addressbooks/" + bookId + "/buddies"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BuddyInfo("Jane", "Jane@gmail.com", "987-654-3210", "456 Elm St, Toronto, ON"))
                .exchange()
                .expectStatus().isCreated();

        restTestClient.get().uri(url("/view/addressbooks/" + bookId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(result -> assertThat(result.getResponseBody())
                        .contains("Address Book #" + bookId)
                        .contains("Jane@gmail.com")
                        .contains("456 Elm St, Toronto, ON"));
    }

    @Test
    void unknownBuddyReturns404OverHttp() {
        restTestClient.get().uri(url("/buddies/999999"))
                .exchange()
                .expectStatus().isNotFound();
    }
}
