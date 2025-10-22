package vn.uit.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.domain.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Category c
            set c.amountOfProduct = (c.amountOfProduct - :amount)
            where (c.id = :id)
                and (c.amountOfProduct >= :amount)
            """)
    int decreaseAmountOfProduct(
            @Param("id") final Long id,
            @Param("amount") final int amount);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Category c
            set c.amountOfProduct = (c.amountOfProduct + :amount)
            where (c.id = :id)
            """)
    int increaseAmountOfProduct(
            @Param("id") final Long id,
            @Param("amount") final int amount);
}
