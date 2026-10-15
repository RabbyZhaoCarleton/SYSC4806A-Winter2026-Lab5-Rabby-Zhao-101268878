package org.carleton;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

/**
 * GUI controller. Unlike a @RestController, a plain @Controller method returns the
 * NAME of a view; Thymeleaf resolves "addressbook" to
 * src/main/resources/templates/addressbook.html and renders it with the Model's data.
 */
@Controller
public class AddressBookViewController {

    private final AddressBookRepository addressBookRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /** GET /view/addressbooks/{bookId} -> HTML page listing that book's buddies. */
    @GetMapping("/view/addressbooks/{bookId}")
    public String viewAddressBook(@PathVariable long bookId, Model model) {
        AddressBook book = addressBookRepository.findById(bookId);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AddressBook " + bookId + " not found");
        }
        model.addAttribute("addressBook", book);
        return "addressbook";
    }
}
