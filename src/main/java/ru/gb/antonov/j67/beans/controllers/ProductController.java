package ru.gb.antonov.j67.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j67.beans.services.ProductService;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.List;
import java.util.Optional;

import static ru.gb.antonov.j67.beans.services.ProductService.productListToDtoList;

@RequestMapping ("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;
    private int pageSize = 6;

//--------------------------------------------------------------------

     @GetMapping ("/{id}")
    public ProductDto findById (@PathVariable Long id)
    {
        return new ProductDto (productService.findById (id));
    }

    @GetMapping ("/page")
    public Page<ProductDto> getProductsPage (
                   @RequestParam (defaultValue="0", name="p", required = false) Integer pageIndex)
    {
        return productService.findAll (pageIndex, pageSize).map(ProductService::dtoFromProduct);
    }

    @PostMapping
    public Optional<ProductDto> createProduct (@RequestBody ProductDto pdto)
    {
        Product p = productService.createProduct (pdto.getProductTitle(),
                                                  pdto.getProductCost());
        return toOptionalProductDto (p);
    }

    @PutMapping
    public Optional<ProductDto> updateProduct (@RequestBody ProductDto pdto)
    {
        Product p = productService.updateProduct (pdto.getProductId(),
                                                  pdto.getProductTitle(),
                                                  pdto.getProductCost());
        return toOptionalProductDto (p);
    }

    @GetMapping ("/delete/{id}")
    public void deleteById (@PathVariable Long id)
    {
        productService.deleteById (id);
    }

    @GetMapping
    public List<ProductDto> getProductsByPriceRange (
                                    @RequestParam(name="min", required = false) Integer min,
                                    @RequestParam(name="max", required = false) Integer max)
    {
        return productListToDtoList (productService.getProductsByPriceRange(min, max));
    }

    private static Optional<ProductDto> toOptionalProductDto (Product p)
    {
        return p != null ? Optional.of (ProductService.dtoFromProduct(p))
                         : Optional.empty();
    }
}
