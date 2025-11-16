package vn.uit.clothesshop.area.site.order.presentation;

public class OrderRequestInfo {
    protected String address;
    protected String phoneNumber;
    public OrderRequestInfo() {

    } 
    public OrderRequestInfo(String address, String phoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
    } 
    public String getAddress() {
        return this.address;
    } 
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    } 
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
