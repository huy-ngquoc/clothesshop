package vn.uit.clothesshop.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user = new User();

    public Cart(
            final User user) {
        this.user = user;
    }

    Cart() {
    }

    public long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    void setId(final long id) {
        this.id = id;
    }
}
