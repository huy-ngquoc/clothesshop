package vn.uit.clothesshop.area.site.homepage.presentation;

public class ProductClientBasicInfoViewModel {
    private final long id;
    private final String name;
    private final String shortDesc;
    private final String imageFilePath;
    private final int minPrice;
    private final int maxPrice;
    private final int sold;
    public ProductClientBasicInfoViewModel() {
        this.id=0;
        this.name="";
        this.shortDesc="";
        this.imageFilePath="";
        this.minPrice=0;
        this.maxPrice= 0;
        this.sold=0;
    } 
    public ProductClientBasicInfoViewModel(long id, String name, String shortDesc, String imageFilePath, int minPrice, int maxPrice, int sold) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.imageFilePath = imageFilePath;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sold= 0;
    } 

    public long getId() {
        return this.id;
    } 
    public String getName() {
        return this.name;
    } 
    public int getSold() {
        return this.sold;
    }
    public String getShortDesc() {
        return this.shortDesc;
    } 
    public String getImageFilePath() {
        return this.imageFilePath;
    } 
    public int getMinPrice() {
        return this.minPrice;
    }
    public int getMaxPrice() {
        return this.maxPrice;
    }
    
    
    
}
