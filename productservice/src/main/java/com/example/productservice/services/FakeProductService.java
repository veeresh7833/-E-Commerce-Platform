package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductDto;
import com.example.productservice.dtos.UserDto;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service("fakeProductService")
@Primary
public class FakeProductService implements ProductService {

    private RestTemplate restTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    FakeProductService(RestTemplate restTemplate, RedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductById(Long id) {
        // Products - Key id - CacheKey + Key == HashKey
        // Categories - Key id
        UserDto userDto = restTemplate.getForObject("http://userservice/users/1", UserDto.class);
        if(userDto == null) {
            throw new RuntimeException("User not found");
        }

        // Check if product exists in cache
//        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_"+id);
//        if(product != null) {
//            // Cache HIT
//            return product;
//        }
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
                FakeStoreProductDto.class);

        Product product = convertDtoToProduct(fakeStoreProductDto);
        //redisTemplate.opsForHash().put("PRODUCTS", "PRODUCTS_" + product.getId(), product);
        return product;
        //throw new RuntimeException();
    }

    @Override
    public List<Product> getAllProducts() {
        // Get the products from 3rd Party
        FakeStoreProductDto[] fakeStoreProductDtoList =
        restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDto[].class);

        //convert result
        List<Product> products = new ArrayList<>();
        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtoList) {
            products.add(convertDtoToProduct(fakeStoreProductDto));
        }
        return products;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = convertProductToDto(product);
        fakeStoreProductDto = restTemplate.patchForObject("https://fakestoreapi.com/products/" + id,
                fakeStoreProductDto, FakeStoreProductDto.class);
        return convertDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public Product replaceProduct() {
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = convertProductToDto(product);
        fakeStoreProductDto =
                restTemplate
                        .postForObject("https://fakestoreapi.com/products", fakeStoreProductDto, FakeStoreProductDto.class);
        return convertDtoToProduct(fakeStoreProductDto);
    }

    @Override
    public Product deleteProduct() {
        return null;
    }

    // Converts a FakeStoreProductDto to a Product object
    private Product convertDtoToProduct(FakeStoreProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();

        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        // Assuming Category needs only id and title, and title is derived from category field of DTO
        Category category = new Category();
        // Here you would assign the id and title of the category appropriately.
        // For demonstration, let's assume the category title is what's passed in the DTO
        // and we set a dummy ID or fetch the correct ID based on the title from a database or other source.
        category.setId(0L); // Dummy ID or retrieve appropriately
        category.setTitle(dto.getCategory());

        product.setCategory(category);

        return product;
    }

    // Converts a Product to a FakeStoreProductDto object
    public static FakeStoreProductDto convertProductToDto(Product product) {
        if (product == null) {
            return null;
        }

        FakeStoreProductDto dto = new FakeStoreProductDto();

        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        // Assuming the category title is what needs to be set in the DTO's category field
        if (product.getCategory() != null) {
            dto.setCategory(product.getCategory().getTitle());
        } else {
            dto.setCategory(null); // or set to a default value as appropriate
        }

        return dto;
    }
}
