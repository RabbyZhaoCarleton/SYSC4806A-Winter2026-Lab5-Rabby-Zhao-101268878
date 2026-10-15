package org.carleton;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for address books, consuming and producing JSON.
 *
 *   POST   /addressbooks                              create an empty address book
 *   GET    /addressbooks                              list all address books
 *   GET    /addressbooks/{bookId}                     get one address book (with its buddies)
 *   POST   /addressbooks/{bookId}/buddies             create a NEW buddy (JSON body) inside the book
 *   PUT    /addressbooks/{bookId}/buddies/{buddyId}   add an EXISTING buddy to the book
 *   DELETE /addressbooks/{bookId}/buddies/{buddyId}   remove a buddy from the book
 */
@RestController
@RequestMapping("/addressbooks")
public class AddressBookController {

    private final AddressBookRepository addressBookRepository;
    private final BuddyInfoRepository buddyRepository;

    public AddressBookController(AddressBookRepository addressBookRepository,
                                 BuddyInfoRepository buddyRepository) {
        this.addressBookRepository = addressBookRepository;
        this.buddyRepository = buddyRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressBook createAddressBook() {
        return addressBookRepository.save(new AddressBook());
    }

    @GetMapping
    public Iterable<AddressBook> getAllAddressBooks() {
        return addressBookRepository.findAll();
    }

    @GetMapping("/{bookId}")
    public AddressBook getAddressBook(@PathVariable long bookId) {
        return findBookOr404(bookId);
    }

    /** Creates a brand-new buddy from the JSON body and puts it straight into the book. */
    @PostMapping("/{bookId}/buddies")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressBook createBuddyInBook(@PathVariable long bookId, @RequestBody BuddyInfo buddy) {
        AddressBook book = findBookOr404(bookId);
        buddy.setId(null);
        book.addBuddy(buddy);
        return addressBookRepository.save(book); // cascade = ALL saves the new buddy too
    }

    /** Adds a buddy that already exists (e.g. created with POST /buddies) to the book. */
    @PutMapping("/{bookId}/buddies/{buddyId}")
    public AddressBook addExistingBuddy(@PathVariable long bookId, @PathVariable long buddyId) {
        AddressBook book = findBookOr404(bookId);
        BuddyInfo buddy = findBuddyOr404(buddyId);
        if (!book.containsBuddy(buddy)) {
            book.addBuddy(buddy);
        }
        return addressBookRepository.save(book);
    }

    @DeleteMapping("/{bookId}/buddies/{buddyId}")
    public AddressBook removeBuddy(@PathVariable long bookId, @PathVariable long buddyId) {
        AddressBook book = findBookOr404(bookId);
        BuddyInfo buddy = findBuddyOr404(buddyId);
        book.removeBuddy(buddy);
        return addressBookRepository.save(book);
    }

    private AddressBook findBookOr404(long id) {
        AddressBook book = addressBookRepository.findById(id);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AddressBook " + id + " not found");
        }
        return book;
    }

    private BuddyInfo findBuddyOr404(long id) {
        BuddyInfo buddy = buddyRepository.findById(id);
        if (buddy == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Buddy " + id + " not found");
        }
        return buddy;
    }
}
