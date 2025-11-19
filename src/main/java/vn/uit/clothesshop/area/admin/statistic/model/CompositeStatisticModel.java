package vn.uit.clothesshop.area.admin.statistic.model;

import java.util.Map;

public class CompositeStatisticModel {
    private Map<String, StatisticModel> statisticByProduct;
    private Map<String, StatisticModel> statisticByCategory;
    private int totalIncome;
    private int totalOrders;
    public CompositeStatisticModel() {

    } 
    public CompositeStatisticModel(Map<String, StatisticModel> statisticByProduct, Map<String, StatisticModel> statisticByCategory, int totalIncome, int totalOrders) {
        this.statisticByCategory = statisticByCategory;
        this.statisticByProduct = statisticByProduct;
        this.totalIncome = totalIncome;
        this.totalOrders = totalOrders;
    } 
    public Map<String,StatisticModel> getStatisticByProduct() {
        return this.statisticByProduct;
    } 
    public Map<String, StatisticModel> getStatisticByCategory() {
        return this.statisticByCategory;
    } 
    public int getTotalIncome() {
        return this.totalIncome;
    } 
    public int getTotalOrders() {
        return this.totalOrders;
    } 
    public void setStatisticByProduct(Map<String, StatisticModel> statisticByProduct) {
        this.statisticByProduct= statisticByProduct;
    }  
    public void setStatisticByCategory(Map<String, StatisticModel> statisticByCategory) {
        this.statisticByCategory = statisticByCategory;
    } 
    
}
