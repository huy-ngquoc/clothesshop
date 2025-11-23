package vn.uit.clothesshop.shared.resource;

import org.springframework.lang.Nullable;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.FieldNameConstants.Include;

@FieldNameConstants
public class ResourceString {
    private static final Dotenv dotenv = Dotenv.load();

    @Include
    @Nullable
    public static final String JWT_RAW_KEY;

    static {
        JWT_RAW_KEY = dotenv.get(ResourceString.Fields.JWT_RAW_KEY);
    }

    private ResourceString() {
    }
}
