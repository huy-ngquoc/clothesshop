package vn.uit.clothesshop.recommendation.util;

import java.util.ArrayList;
import java.util.List;

import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.enumerator.ETarget;
import vn.uit.clothesshop.recommendation.domain.Model;



public class RecommendationUtil {
    public static List<ETarget> listTargetFromRecommendationModel(Model model) {
        List<ETarget> result = new ArrayList<>();
        if(model.getGender()==false) { //Nam
            result.add(ETarget.MALE);
        }
        else {
            result.add(ETarget.FEMALE);
        } 
        if(model.getAge()<=40) {
            result.add(ETarget.YOUNG);
        } 
        if(model.getAge()>40&&model.getAge()<=70) {
            result.add(ETarget.MIDDLE_AGE);
        }
        if(model.getAge()>70) {
            result.add(ETarget.OLD);
        }
        switch(model.getShape()) {
            case "thin": 
            {
                result.add(ETarget.THIN);
                break;
            } 
            case "normal":
            {
                result.add(ETarget.NORMAL);
                break;
            }  
            case "fat": 
            {
                result.add(ETarget.FAT);
            } 
            default:{
                
            }

        }
        switch(model.getBodyShape()) {
            case "Apple": {
                result.add(ETarget.APPLE);
                break;
            } 
            case "Triangle": {
                result.add(ETarget.TRIANGLE);
                break;
            } 
            case "Rectangle": {
                result.add(ETarget.RECTANGLE);
                break;
            }
            default: {

            } 
        }
        return result;
    }
    public static boolean isProductFit(List<ETarget> listTargets, Product product) {
        List<ETarget> productTargets= new ArrayList<>(product.getTargets());
        List<ETarget> copyList= new ArrayList<>(listTargets);
        copyList.retainAll(productTargets);
        return !copyList.isEmpty();

    }
}