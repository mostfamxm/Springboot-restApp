import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
	        private OrderRepo orderDatabase;
	          private OrderEntityFactory orderFactory;
	          public OrderController(OrderRepo aDatabase,OrderEntityFactory aOrderFactory){
	              this.orderDatabase = aDatabase;
	              this.orderFactory = aOrderFactory;
	          }
	          
	         // 2.1 show all the orders 
	        @GetMapping("/Orders")
	        ResponseEntity<CollectionModel<EntityModel<Order>>> allOrders(){
	        	// we get all the orders
	        	List<EntityModel<Order>> orders = orderDatabase.findAll()
	                     .stream().map(orderFactory::toModel).collect(Collectors.toList());
	           //show all the orders we have
	        	return ResponseEntity
	                     .ok(orders.getRequiredLink(IanaLinkRelations.SELF).toUri())
	                     .body(CollectionModel.of(orders,
	                      linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()));
	
	        }
	         
	        @GetMapping("/products")
	        public Product singleProduct(@RequestParam Double price) {
	         return productRepo.findByPrice(name);
	        }
	        
	        
	        
	        
	        //1.3. this method returns all the products in the order if matches the id  	
	        @GetMapping("/orders/{id}/products")
	        public ResponseEntity<CollectionModel<EntityModel<products>>> productsByOrder(@PathVariable long id){
	        	// get the order by id
	        	EntityModel <Order> order = orderDatabase.findId(id);
	        	// check if the order is empty
	        	if(order==null) {
	        		return ResponseEntity 
	        				.notFound()
	        				.body(new OrderNotFound(id));
	        	}
	        	// return all the products
	        	return ResponseEntity
	        			.ok(order.getRequiredLink(IanaLinkRelations.SELF).toUri())
	        			.body(order.getOrderProducts());    	
	        }
	        
	        
	        
	        
	        //2.2 show the order with the matching id if it does not exist we return an exeption
	        @GetMapping("/Order/{id}")
	        ResponseEntity<EntityModel<Order>> singleOrder(@PathVariable long id){
	        	// get the order by id
	        	 EntityModel<Order> order=orderDatabase.findById(id);
	            //check if empty
	        	 if(order==null) {
	                 return ResponseEntity
	                             .notFound()
	                             .body(new OrderNotFoundException(id));
	             }
	        	 //return the order
	             return ResponseEntity
	                      .ok(order.getRequiredLink(IanaLinkRelations.SELF).toUri())
	                     .body(order);
	        	
	        }
	        
	        
	        
	       	        
	        
	        
	        
	        // 2.6 return all the order after making a discount of 25% on all the products
	        @GetMapping("/Order/{id}/sale")
	        ResponseEntity<EntityModel<Order>> singleOrder(@PathVariable long id){
	        	// get the order by the id 
	        	 EntityModel<Order> order=productsByOrder(id);
	            //check if the order is empty
	        	 if(order==null) {
	                 return ResponseEntity
	                             .notFound()
	                             .body(new OrderNotFoundException(id));
	             }
	            //we put the orginal order in a temp so we dont lose it
	             EntituModel <Order> temp=order;
	             //we put the discount on the temp modle
	             for (int i=0; i<temp.size();i++) {
	            	 temp[i]+=0.25;
	             }
	             // and the last thing we return  the copy of the order after the discount(temp)
	             return ResponseEntity
                 .ok(temp.getRequiredLink(IanaLinkRelations.SELF).toUri())
                 .body(CollectionModel.of(temp,
                  linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()));
	        	
	        }
	        
	        
	        
	        //2.3 add another order 
	        @PostMapping("/orders")
	        ResponseEntity<?> createOrder(@RequestBody Order newOrder){
	            // add the new order to the database
	        	EntityModel<Order> orderRepresentation
	                     = orderFactory.toModel(orderDatabase.save(newOrder));
	             return ResponseEntity
	                             .created(orderRepresentation.getRequiredLink(IanaLinkRelations.SELF).toUri())
	                             .body(orderRepresentation);
	        }
	        
	        
	        //2.4 get the order with the matching id and update
	        @PutMapping("/orders/{id}")
	      Product addToOrder(@PathVariable Long id,@RequestBody Product aProduct){       	
	        	//get the order
	        	EntityModel<Order> order= orderDatabase.findById(id).
	        //create a copy of the order
	        	List<Products> temp = new ArrayList<>();
	        	
	        	temp= order.getOrderProducts();
	        	//add to the list  the product we got
	        	temp.add(aProduct);
	        	//we save the database with the product we added
	        	return orderDatabase.save(temp);	                  
	                
	      }
	        
	        @DeleteMapping("/order/{id}")
	        ResponseEntity<?> deleteProduct( @PathVariable(value = "id") Long orderID){
	            // מוחק את המוצר לפי ה id הייחודי שלו , אשר נמצא בתוך orders מתוך -> orderDatabase
	            orderDatabase.deleteById(orderID);
	            // נחזיר באמצעות ResponseEntity משפט וגם מוסיף סטאטוס 200
	            return ResponseEntity.ok(orderEntityFactory.toModel());
	        }
	       
	          
	          
	          

}
