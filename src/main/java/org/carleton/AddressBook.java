package org.carleton;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AddressBook {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BuddyInfo> buddyList;

    public AddressBook() {
        buddyList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BuddyInfo> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(List<BuddyInfo> buddyList) {
        this.buddyList = buddyList;
    }

    public void addBuddy(BuddyInfo buddy) {
        buddyList.add(buddy);
    }

    public void removeBuddy(BuddyInfo buddy) {
        buddyList.remove(buddy);
    }

    public String printBuddyList() {
        StringBuilder buddyListString = new StringBuilder();
        for (BuddyInfo b : buddyList) {
            buddyListString.append(b.getName()).append(": ").append(b.getEmail()).append(", ").append(b.getPhone()).append(", ").append(b.getAddress()).append("\n");
        }
        return buddyListString.toString();
    }

    public boolean containsBuddy(BuddyInfo buddy) {
        return buddyList.contains(buddy);
    }
}
