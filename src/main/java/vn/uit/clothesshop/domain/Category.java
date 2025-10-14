package vn.uit.clothesshop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="CATEGORY")
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;
    private String image;
    private int amountOfProduct;
    public int getId() {
        return id;
    }
    public int getAmountOfProduct() {
        return this.amountOfProduct;
    } 
    public void setAmountOfProduct(int amountOfProduct) {
        this.amountOfProduct= amountOfProduct;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public Category() {
    }
    public Category(int id, String name, String image, int amountOfProduct) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.amountOfProduct=amountOfProduct;
    }

    
}
