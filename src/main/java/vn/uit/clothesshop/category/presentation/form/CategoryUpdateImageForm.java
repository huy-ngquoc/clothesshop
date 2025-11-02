package vn.uit.clothesshop.category.presentation.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;

public final class CategoryUpdateImageForm {
    @NotNull
    private MultipartFile imageFile = null;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(final MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
