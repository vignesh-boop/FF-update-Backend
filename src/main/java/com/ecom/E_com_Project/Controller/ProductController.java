package com.ecom.E_com_Project.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.E_com_Project.Model.Product;
import com.ecom.E_com_Project.Service.ProductService;

@RestController
@CrossOrigin(origins = "https://flowerfluxupdate.netlify.app/")
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService service;
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts(){
		return new ResponseEntity<>(service.getAllProducts(),HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id) {
		
		Product product = service.getProductById(id);
		if(product != null) {
			return new ResponseEntity<>(product,HttpStatus.OK);
		}else {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping(value = "/product", consumes = {"multipart/form-data"})
	public ResponseEntity<?> addProduct( @RequestPart("product") Product product,
			 							@RequestPart("imageFile") MultipartFile imageFile){
		try {
			Product product1 = service.addProduct(product,imageFile);
			return new ResponseEntity<>(product1,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping("/product/{productId}/image")
	public ResponseEntity<byte[]>getImageByProductId(@PathVariable int productId){
		
		Product product = service.getProductById(productId);
		byte[] imageFile = product.getImageData();
		
		return ResponseEntity.ok()
				.contentType(MediaType.valueOf(product.getImageType()))
				.body(imageFile);
					
	}
	@PutMapping("/product/{id}")
	public ResponseEntity<String>updateProduct(@PathVariable int id,@RequestPart Product product,
												@RequestPart MultipartFile imageFile){
		Product product1=null;
		try {
			product1 = service.updateProduct(id,product,imageFile);
		} catch (IOException e) {
			
			return new ResponseEntity<>("FAILED TO UPDATE",HttpStatus.BAD_REQUEST);
		}
		if(product1 != null) {
			return new ResponseEntity<>("UPDATED",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("FAILED TO UPDATE",HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct (@PathVariable int id){
		Product product = service.getProductById(id);
		if(product != null) {
			service.deleteProduct(id);
			return new  ResponseEntity<>("DELETED",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("PRODUCT NOT FOUND",HttpStatus.NOT_FOUND);
		}	
	}
	@GetMapping("products/search")
	public ResponseEntity<List<Product>>searchProducts(@RequestParam String keyword){
		List<Product> products = service.searchProducts(keyword);
		System.out.println("searching with "+keyword);
		return new ResponseEntity<>(products,HttpStatus.OK);
		
	}

}
