package com.example.restapp;

import org.springframework.data.jpa.repository.JpaRepository;

// ProductDAL
public interface ProductRepo extends JpaRepository<Product,Long> {
	public static Product findByName(String st);
	public Product [] findByPrice(Double price);

}
