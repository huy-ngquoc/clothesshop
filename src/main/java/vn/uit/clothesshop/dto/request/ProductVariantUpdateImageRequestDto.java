package vn.uit.clothesshop.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public final class ProductVariantUpdateImageRequestDto {
    @NotNull
    private MultipartFile imageFile = null;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}
