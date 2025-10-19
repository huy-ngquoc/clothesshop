package vn.uit.clothesshop.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @OneToOne(cascade=CascadeType.ALL)
    private User user;
   
    

    public Cart() {

    } 
    public Cart(long id, User user) {
        this.id = id;
        this.user = user;
       
    }
    public long getId() {
        return this.id;
    } 
    public User getUser() {
        return this.user;
    } 
    public void setId(long id) {
        this.id = id;
    } 
    public void setUser(User user) {
        this.user = user;
    }
}
