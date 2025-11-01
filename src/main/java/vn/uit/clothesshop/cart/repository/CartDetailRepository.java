package vn.uit.clothesshop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.cart.domain.CartDetail;
import vn.uit.clothesshop.cart.domain.id.CartDetailId;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetailId> {
}
