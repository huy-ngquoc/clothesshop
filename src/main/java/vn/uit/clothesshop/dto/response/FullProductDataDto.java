package vn.uit.clothesshop.dto.response;

import java.util.ArrayList;
import java.util.List;

import vn.uit.clothesshop.dto.selectcolumninteface.ProductVariantInfo;

public class FullProductDataDto {
    private long id;
    private String name;
    private String shortDesc;
    private String detailDesc;
    private List<ProductVariantInfo> listVariants;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getShortDesc() {
        return shortDesc;
    }
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
    public String getDetailDesc() {
        return detailDesc;
    }
    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }
    public List<ProductVariantInfo> getListVariants() {
        if(listVariants==null) return null;
        return new ArrayList<>(listVariants);
    }
    public void setListVariants(List<ProductVariantInfo> listVariants) {
        this.listVariants = listVariants;
    }
    public FullProductDataDto() {
    }
    public FullProductDataDto(long id, String name, String shortDesc, String detailDesc,
            List<ProductVariantInfo> listVariants) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.detailDesc = detailDesc;
        this.listVariants = listVariants;
    }
    
}
