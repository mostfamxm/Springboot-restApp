import java.time.LocalDate;
package com.example.restapp;

import jdk.jfr.DataAmount;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.sun.jmx.remote.util.OrderClassLoaders;
import com.sun.tools.javac.util.List;

import java.util.Comparator;
import java.util.Objects;

@Data
@Entity
public class Order {
	
 @Id @GeneratedValue private long id;
	private LocalDate purchaseDate;
	private String title;
	private Double price;
	
	private List<Product> orderProducts = new ArrayList<>();

	
	public Order(LocalDate date,String title, Double Price,List<Product> pr) {
		this.price=price;
		this.purchaseDate=purchaseDate;
		this.title=title;
		this.orderProducts=pr;
		
	}
	 @Transient
  public Double getTotalOrderPrice() {
	        double sum = 0D;
	        List<OrderProduct> orderProducts = getOrderProducts();
	        for (OrderProduct op : orderProducts) {
	            sum += op.getTotalPrice();
	        }
	        return sum;
	    }

	    @Transient
	    public int getNumberOfProducts() {
	        return this.orderProducts.size();
	    }
	    
	 
	    
	
	

}
