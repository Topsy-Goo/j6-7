package ru.gb.antonov.j67.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j67.beans.errorhandlers.OurValidationException;
import ru.gb.antonov.j67.beans.services.ProductService;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.gb.antonov.j67.beans.services.ProductService.productListToDtoList;

@RequestMapping ("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;
    private final int pageSize = 6;


    public int getPageSize()    {   return pageSize;   }

    //http://localhost:8189/market/api/v1/products/11
     @GetMapping ("/{id}")
    public ProductDto findById (@PathVariable Long id)
    {
        return ProductService.dtoFromProduct (productService.findById (id));
    }

    //http://localhost:8189/market/api/v1/products/page?p=0
    @GetMapping ("/page")
    public Page<ProductDto> getProductsPage (
                   @RequestParam (defaultValue="0", name="p", required = false) Integer pageIndex)
    {
        return productService.findAll (pageIndex, pageSize).map(ProductService::dtoFromProduct);
    }

   //http://localhost:8189/market/api/v1/products   POST
    @PostMapping
    public Optional<ProductDto> createProduct (@RequestBody @Validated ProductDto pdto, BindingResult br)
    {
        if (br.hasErrors())
        {
            throw new OurValidationException (br.getAllErrors()
                                                .stream()
                                                .map(ObjectError::getDefaultMessage)
                                                .collect (Collectors.toList()));
        }
        Product p = productService.createProduct (pdto.getProductTitle(),
                                                  pdto.getProductCost());
        return toOptionalProductDto (p);
    }

   //http://localhost:8189/market/api/v1/products   PUT
    @PutMapping
    public Optional<ProductDto> updateProduct (@RequestBody ProductDto pdto)
    {
        Product p = productService.updateProduct (pdto.getProductId(),
                                                  pdto.getProductTitle(),
                                                  pdto.getProductCost());
        return toOptionalProductDto (p);
    }

    //http://localhost:8189/market/api/v1/products/delete/11
    @GetMapping ("/delete/{id}")
    public void deleteProductById (@PathVariable Long id)
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
