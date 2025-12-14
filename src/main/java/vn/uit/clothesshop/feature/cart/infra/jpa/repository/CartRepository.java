package vn.uit.clothesshop.feature.cart.infra.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.cart.domain.id.CartId;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    List<Cart> findByUser_Id(long userId);
    @Query("SELECT COALESCE(SUM(c.amount),0) FROM Cart c WHERE c.user.id = :userId")
    Long sumQuantityByUserId(@Param("userId") long userId);
}
