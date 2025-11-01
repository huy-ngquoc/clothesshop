package vn.uit.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.domain.embeddedkey.CartDetailKey;
import vn.uit.clothesshop.domain.entity.CartDetail;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, CartDetailKey> {
}
