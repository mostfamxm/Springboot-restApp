package com.example.restapp;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Indicates that the data from our methods will be injected to the response payload (body)
  @RestController
    public class OrderController { 
        private OrderRepo orderDatabase;
          private OrderEntityFactory orderFactory;

          public OrderController(OrderRepo aDatabase,OrderEntityFactory aOrderFactory){
              this.orderDatabase = aDatabase;
              this.orderFactory = aOrderFactory;
          }
  }
@RestController
public class ProductController {
    private ProductRepo productDatabase;
    private ProductEntityFactory productFactory;

    public ProductController(ProductRepo aDatabase,ProductEntityFactory aProductFactory){
        this.productDatabase = aDatabase;
        this.productFactory = aProductFactory;
    }
    
  
    

    // 1.1.first method gives all the products
    @GetMapping("/products")
    public ResponseEntity<CollectionModel<EntityModel< product>>> allProducts(){
        // get the products  
    	List<EntityModel<Product>> products = productDatabase.findAll()
                  .stream().map(productFactory::toModel).collect(Collectors.toList());
        //show all the products  
    	return ResponseEntity
                  .ok(products.getRequiredLink(IanaLinkRelations.SELF).toUri())
                  .body(CollectionModel.of(products,
                   linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel()));
    }
    
    //1.2.second method returns a single  product by id 
    @GetMapping("/products/{id}")
    public ResponseEntity<EntityModel< product>> singleProduct(@PathVariable long id){
       // get the product by id
    	EntityModel<Product> product=productDatabase.findById(id);
        //check if there is a product 
    	if(product==null) {
            return ResponseEntity
                        .notFound()
                        .body(new ProductNotFoundException(id));
        }
        //return the product we found
        return ResponseEntity
                 .ok(product.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(product);
    }
    
   
    
    @PutMapping("/products/{id}")
    ResponseEntity<?> replaceProduct(@RequestBody Product aProduct, @PathVariable Long id){
        Product updatedProduct =  productDatabase.findById(id).map(productToUpdate->{
                    productToUpdate.setProductName(aProduct.getProductName());
                    productToUpdate.setCategory((aProduct.getCategory()));
                    productToUpdate.setPrice(aProduct.getPrice());
                    return productDatabase.save(productToUpdate);

                })
                .orElseGet(()->{
                    aProduct.setId(id);
                    return productDatabase.save(aProduct);
                });
        EntityModel<Product> productRepresentation =
                productEntityFactory.toModel(productDatabase.save(updatedProduct));
        return ResponseEntity
                .created(productRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(productRepresentation);
    }


    
    @PostMapping("/products")
    ResponseEntity<?> createProduct(@RequestBody Product newProduct){
         EntityModel<Product> productRepresentation
                 = productFactory.toModel(productDatabase.save(newProduct));
         return ResponseEntity
                         .created(productRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
                         .body(productRepresentation);
    }
    
    // get a product by name 
    @GetMapping("/products")
    public Product singleProduct(@RequestParam String name) {
     return productRepo.findByName(name);
    }



    @DeleteMapping("/products/{id}")
    void deleteProduct(@PathVariable Long id){
        productDatabase.deleteById(id);
    }



}
