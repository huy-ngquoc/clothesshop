package vn.uit.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
