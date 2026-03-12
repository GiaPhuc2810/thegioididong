package com.hutech.tan.repository;

import com.hutech.tan.model.Order; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository; 
@Repository 
public interface OrderRepository extends JpaRepository<Order, Long> { 
} 