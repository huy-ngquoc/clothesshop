package vn.uit.clothesshop.utils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ParamValidator {
    public static String getStringFromOptional(Optional<String> optionalString) {
        return optionalString.orElse(null);
    }
    public static Integer getIntFromOptional(Optional<String> optionalInt) {
        String optionalString = optionalInt.orElse(null);
        if(optionalString==null) {
            return null;
        } 
        try {
            return Integer.valueOf(optionalString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static boolean validateFromPriceAndToPrice(Integer from, Integer to) {
        if(from==null||to==null) {
            return false;
        }
        if(from<0||to<=0) {
            return false;
        } 
        return from < to;
    }
    public static boolean  validateList(List listItem) {
        return listItem!=null&&!listItem.isEmpty();
    }
    public static boolean validateSet(Set set) {
        return set!=null&&!set.isEmpty();
    }
}
