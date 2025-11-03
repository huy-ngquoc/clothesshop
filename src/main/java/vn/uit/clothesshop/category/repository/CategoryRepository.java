package vn.uit.clothesshop.category.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
            SELECT c
            FROM Category c
            ORDER BY RANDOM()
            """)
    List<Category> findRandom(@NonNull Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Category c
            set c.amountOfProduct = (c.amountOfProduct - :amount)
            where (c.id = :id)
                and (c.amountOfProduct >= :amount)
            """)
    int decreaseProductCount(
            @Param("id") final long id,
            @Param("amount") final int amount);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Category c
            set c.amountOfProduct = (c.amountOfProduct + :amount)
            where (c.id = :id)
            """)
    int increaseProductCount(
            @Param("id") final long id,
            @Param("amount") final int amount);
}
