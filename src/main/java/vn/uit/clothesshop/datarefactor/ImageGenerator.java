package vn.uit.clothesshop.datarefactor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.repository.ProductVariantRepository;

//@Component
public class ImageGenerator {
    private final ProductVariantRepository productVariantRepo;
    public ImageGenerator(ProductVariantRepository productVariantRepo) throws IOException {
        this.productVariantRepo = productVariantRepo;
        String imageURL="https://picsum.photos/300/300.jpg";
        
        List<ProductVariant> listProductVariants = this.productVariantRepo.findAll();
        for(int i=0;i<listProductVariants.size();i++) {
            ProductVariant pv = listProductVariants.get(i);
            String destinationFileName = Long.toString(System.currentTimeMillis())+".jpg";
            String destinationFilePath = "src/main/webapp/resources/img/productvariant/"+destinationFileName;
            pv.setImage(destinationFileName);
            saveImageFromUrl(imageURL, destinationFilePath);
        }
        productVariantRepo.saveAll(listProductVariants);
    }
    public static void saveImageFromUrl(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream is = url.openStream();
             FileOutputStream fos = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}
