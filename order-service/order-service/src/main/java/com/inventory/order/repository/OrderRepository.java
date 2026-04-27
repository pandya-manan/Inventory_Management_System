package com.inventory.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventory.order.entity.*;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{

}
