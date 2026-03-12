package com.hutech.tan.repository; 
import com.hutech.tan.model.OrderDetail; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository; 
@Repository 
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> { 
} 