package vn.uit.clothesshop.area.admin.statistic.model;

public class StatisticModel {
    private int productAmount;
    private int totalPrice;
    public StatisticModel() {

    } 
    public StatisticModel(int productAmount, int totalPrice) {
        this.productAmount = productAmount;
        this.totalPrice = totalPrice;
    } 
    public int getProductAmount() {
        return this.productAmount;
    } 
    public int getTotalPrice() {
        return this.totalPrice;
    } 
    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    } 
    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
