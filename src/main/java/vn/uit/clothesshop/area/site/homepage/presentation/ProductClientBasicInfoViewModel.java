package vn.uit.clothesshop.area.site.homepage.presentation;

public class ProductClientBasicInfoViewModel {
    private final long id;
    private final String name;
    private final String shortDesc;
    private final String imageFilePath;
    private final int minPriceCents;
    private final int maxPriceCents;
    private final int sold;
    public ProductClientBasicInfoViewModel() {
        this.id=0;
        this.name="";
        this.shortDesc="";
        this.imageFilePath="";
        this.minPriceCents=0;
        this.maxPriceCents= 0;
        this.sold=0;
    } 
    public ProductClientBasicInfoViewModel(long id, String name, String shortDesc, String imageFilePath, int minPriceCents, int maxPriceCents, int sold) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.imageFilePath = imageFilePath;
        this.minPriceCents = minPriceCents;
        this.maxPriceCents = maxPriceCents;
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
    public int getMinPriceCents() {
        return this.minPriceCents;
    }
    public int getMaxPriceCents() {
        return this.maxPriceCents;
    }
    
    
    
}
