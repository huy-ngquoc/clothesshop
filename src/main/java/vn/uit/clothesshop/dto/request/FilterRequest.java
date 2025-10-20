package vn.uit.clothesshop.dto.request;
import java.util.List;
public class FilterRequest {
    private int pageNumber;
    private String name;
    private Integer fromPrice;
    private Integer toPrice;
    private List<String> listColors;
    private List<String> listSize;
    public int getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getFromPrice() {
        return fromPrice;
    }
    public void setFromPrice(Integer fromPrice) {
        this.fromPrice = fromPrice;
    }
    public Integer getToPrice() {
        return toPrice;
    }
    public void setToPrice(Integer toPrice) {
        this.toPrice = toPrice;
    }
    public List<String> getListColors() {
        return listColors;
    }
    public void setListColors(List<String> listColors) {
        this.listColors = listColors;
    }
    public List<String> getListSize() {
        return listSize;
    }
    public void setListSize(List<String> listSize) {
        this.listSize = listSize;
    }
    public FilterRequest(int pageNumber, String name, Integer fromPrice, Integer toPrice, List<String> listColors,
            List<String> listSize) {
        this.pageNumber = pageNumber;
        this.name = name;
        this.fromPrice = fromPrice;
        this.toPrice = toPrice;
        this.listColors = listColors;
        this.listSize = listSize;
    }
    public FilterRequest() {
    }
    
}
