package vn.uit.clothesshop.dbsetup;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.enumerator.ETarget;
import vn.uit.clothesshop.product.repository.ProductRepository;

//@Component
public class DBSetUp {
    private final ProductRepository productRepo;
    private final ETarget[] genders={ETarget.MALE, ETarget.FEMALE};
    private final ETarget[] ages= {ETarget.YOUNG, ETarget.MIDDLE_AGE, ETarget.OLD};
    private final ETarget[] shapes = {ETarget.THIN, ETarget.NORMAL, ETarget.FAT};
    private final ETarget[] bodyShapes  = {ETarget.TRIANGLE, ETarget.RECTANGLE, ETarget.APPLE};
    public DBSetUp(ProductRepository productRepo) {
        this.productRepo = productRepo;
        Random random = new Random();
        List<Product> listProducts= productRepo.findAll();
        for(Product p: listProducts) {
            List<ETarget> listTargets = new ArrayList<>();
            listTargets.add(genders[random.nextInt(2)]);
            listTargets.add(ages[random.nextInt(3)]);
            listTargets.add(shapes[random.nextInt(3)]);
            listTargets.add(bodyShapes[random.nextInt(3)]);
            p.setTargets(new HashSet<>(listTargets));
            p.setUpdatedAt(Instant.now());
            p.setCreatedAt(Instant.now());
        }
        productRepo.saveAll(listProducts);
        
    }
}
