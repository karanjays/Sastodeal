package com.sastodeal.product_service.service;

import com.sastodeal.product_service.dto.ProductRequest;
import com.sastodeal.product_service.dto.ProductResponse;
import com.sastodeal.product_service.model.Product;
import com.sastodeal.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product {} created successfully.",product.getId());
  ;  }

    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try{
            List<Product> products = productRepository.findAll();
            if(products.isEmpty()){
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
            }
            List<ProductResponse> productResponseList = products.stream().map(this::mapToProductResponse).toList();
            return new ResponseEntity<>(productResponseList,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ProductResponse> getProductById(String id) {
        try{
            Optional<Product> product = productRepository.findById(id);
            if(product.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            ProductResponse productResponse = mapToProductResponse(product.get());
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
