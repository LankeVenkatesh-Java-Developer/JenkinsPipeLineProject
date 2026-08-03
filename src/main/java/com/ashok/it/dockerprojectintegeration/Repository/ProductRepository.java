package com.ashok.it.dockerprojectintegeration.Repository;

import com.ashok.it.dockerprojectintegeration.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
