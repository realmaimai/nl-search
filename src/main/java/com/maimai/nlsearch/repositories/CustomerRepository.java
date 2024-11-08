package com.maimai.nlsearch.repositories;

import com.maimai.nlsearch.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query(value = """
            SELECT * FROM customers 
            WHERE (:city IS NULL OR city = :city)
            AND (:firstName IS NULL OR first_name LIKE CONCAT('%', :firstName, '%'))
            AND (:lastName IS NULL OR last_name LIKE CONCAT('%', :lastName, '%'))
            AND (:note IS NULL OR note LIKE CONCAT('%', :note, '%'))
            """, nativeQuery = true)
    List<Customer> searchCustomers(
            @Param("city") String city,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("note") String note
    );
}
