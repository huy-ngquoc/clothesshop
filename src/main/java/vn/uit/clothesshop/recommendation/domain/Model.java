package vn.uit.clothesshop.recommendation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Model {
    private int age;
    private boolean gender;
    private String shape;
    @JsonProperty("body_shape")
    private String bodyShape;
    public Model() {

    } 
    public Model(int age, boolean gender, String shape, String bodyShape) {
        this.age= age;
        this.gender = gender;
        this.shape = shape;
        this.bodyShape = bodyShape;
    } 
    public int getAge() {
        return this.age;
    } 
    public boolean getGender() {
        return this.gender;
    } 
    public String getShape() {
        return this.shape;
    } 
    public String getBodyShape() {
        return this.bodyShape;
    }
    public void setAge(int age) {
        this.age = age;
    } 
    public void setGender(boolean gender) {
        this.gender = gender;
    } 
    public void setShape(String shape) {
        this.shape=shape;    
    }
    public void setBodyShape(String bodyShape) {
        this.bodyShape = bodyShape;
    }
}
