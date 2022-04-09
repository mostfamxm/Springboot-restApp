package com.example.restapp;

import jdk.jfr.DataAmount;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sun.jmx.remote.util.OrderClassLoaders;

import java.util.Comparator;
import java.util.Objects;

/**
 * A Domain Object for our project.
 * Lombok library - generates: getters,setters,equals,HashCode,toString
 */
@Data
@Entity
public class Product implements Comparable<Product> {
    @Id @GeneratedValue private Long id;
    private String productName;
    private String category;
    private Double price;
    private String description;
    private Order order;

    public Product(){

    }

    public Product(String name, String category,Double price,des){
        this.productName = name;
        this.category = category;
        this.price = price;
        this.description=des;
    }
    

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.getPrice(),other.getPrice());
    }
    
   
    
    }
