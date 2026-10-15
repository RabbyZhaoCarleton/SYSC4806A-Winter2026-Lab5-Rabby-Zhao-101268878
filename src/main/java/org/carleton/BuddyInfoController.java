package org.carleton;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for buddies. @RestController = @Controller + @ResponseBody, so every
 * return value is serialized to JSON by Jackson instead of being treated as a view name.
 */
@RestController
@RequestMapping("/buddies")
public class BuddyInfoController {

    private final BuddyInfoRepository buddyRepository;

    // Constructor injection: Spring passes in the repository bean it created for us.
    public BuddyInfoController(BuddyInfoRepository buddyRepository) {
        this.buddyRepository = buddyRepository;
    }

    /** POST /buddies  with a JSON body -> creates a stand-alone buddy (not yet in any book). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BuddyInfo createBuddy(@RequestBody BuddyInfo buddy) {
        buddy.setId(null); // let the database generate the id
        return buddyRepository.save(buddy);
    }

    /** GET /buddies -> every buddy in the database. */
    @GetMapping
    public Iterable<BuddyInfo> getAllBuddies() {
        return buddyRepository.findAll();
    }

    /** GET /buddies/{id} -> one buddy, or 404. */
    @GetMapping("/{id}")
    public BuddyInfo getBuddy(@PathVariable long id) {
        BuddyInfo buddy = buddyRepository.findById(id);
        if (buddy == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Buddy " + id + " not found");
        }
        return buddy;
    }
}
