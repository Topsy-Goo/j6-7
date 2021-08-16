package ru.gb.antonov.j67.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j67.beans.services.ProductService;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.List;

@RestController //< все методы возвращают JSON'ы, плюс позволяет не указывать над методами @ResponseBody.
@RequiredArgsConstructor    //< создаёт конструктор с параметрами для инициализации всех final-полей.
public class ProductController
{
    private final ProductService productService;


    //http://localhost:8189/market/products
    @GetMapping ("/products")
    public List<Product> findAll()
    {
        return productService.findAll();
    }

   //http://localhost:8189/market/products
    @PostMapping ("/products")
    public ProductDto createProduct (@RequestBody ProductDto pdto)
    {
        return productService.createProduct (pdto.getProductTitle(), pdto.getProductCost())
                             .orElse(null); //< заглушка (ошибки обрабатывать мы пока не умеем), которую postman вполне корректно обработал со статусом 200
    }

    //http://localhost:8189/market/products/11
     @GetMapping ("/products/{id}")
    public ProductDto findById (@PathVariable Long id)
    {
        return productService.findById (id);
    }

    //http://localhost:8189/market/products/delete/11
    @GetMapping ("/products/delete/{id}")
    public void deleteById (@PathVariable Long id)
    {
        productService.deleteById (id);
    }

    //http://localhost:8189/market/products/price_filter?min=50&max=90
    //http://localhost:8189/market/products/price_filter?min=50
    //http://localhost:8189/market/products/price_filter?max=90
    @GetMapping ("/products/price_filter")
    public List<Product> getProductsByPriceRange (@RequestParam(name="min", required = false) Integer min,
                                                  @RequestParam(name="max", required = false) Integer max)
    {
        return productService.getProductsByPriceRange (min, max);
    }

}