package ru.gb.antonov.j67.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j67.beans.services.ProductService;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

@RequestMapping ("/api/v1/cart")
@RestController
@RequiredArgsConstructor    //< создаёт конструктор для инициализации всех final-полей.
public class CartController
{
    private final ProductService productService;
    private final ProductController productController;


    //http://localhost:8189/market/api/v1/cart/add/18
    @GetMapping ("/add/{id}")
    public Integer addProductToCart (@PathVariable Long id)
    {
        return productService.addToCart (id);
    }

    //http://localhost:8189/market/api/v1/cart/remove/18
    @GetMapping ("/remove/{id}")
    public Integer removeProductFromCart (@PathVariable Long id)
    {
        return productService.removeFromCart (id);
    }

    //http://localhost:8189/market/api/v1/cart/page
    //http://localhost:8189/market/api/v1/cart/page?p=0
    @GetMapping ("/page")
    public Page<ProductDto> getProductsCartPage (
                   @RequestParam (defaultValue="0", name="p", required = false) Integer pageIndex)
    {
        return productService.getCartPage (pageIndex, productController.getPageSize())
                             .map(ProductService::dtoFromProduct);
    }

    //http://localhost:8189/market/api/v1/cart/itemscount
    @GetMapping ("/itemscount")
    public Integer getCartItemsCount()
    {
        return productService.getCartItemsCount();
    }
}
