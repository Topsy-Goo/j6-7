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

@RestController //< все методы возвращают JSON'ы, плюс позволяет не указывать над методами @ResponseBody.
@RequiredArgsConstructor    //< создаёт конструктор с параметрами для инициализации всех final-полей.
public class ProductController
{
    private final ProductService productService;
    private int pageSize = 8;


    //@Autowired < эта аннотация для конструктора необязательна
    //public ProductController (ProductService ps)  < ломбок создаст этот конструктор
    //{
    //    productService = ps;
    //}
//--------------------------------------------------------------------

   //http://localhost:8189/market/products  POST
    @PostMapping ("/products")
    public Optional<ProductDto> createProduct (@RequestBody ProductDto pdto)
    {
        Product p = productService.createProduct (pdto.getProductTitle(), pdto.getProductCost());
        return toOptionalProductDto (p);
    }

    private static Optional<ProductDto> toOptionalProductDto (Product p)
    {
        return p != null ? Optional.of (ProductService.dtoFromProduct(p))
                         : Optional.empty();
    }

    //http://localhost:8189/market/products/11
     @GetMapping ("/products/{id}")
    public Optional<ProductDto> findById (@PathVariable Long id)
    {
        return toOptionalProductDto (productService.findById(id));
    }


    //http://localhost:8189/market/products/delete/11
    @GetMapping ("/products/delete/{id}")
    public void deleteById (@PathVariable Long id)
    {
        productService.deleteById (id);
    }


    //http://localhost:8189/market/products/page?p=0
    @GetMapping ("/products/page")
    public Page<ProductDto> getProductsPage (
                    @RequestParam (defaultValue="0", name="p", required = false) Integer pageIndex)
    {
        if (pageIndex == null)
            pageIndex = 0;

        return productService.findAll (pageIndex, pageSize).map(ProductService::dtoFromProduct);
    }


    //http://localhost:8189/market/products?min=50&max=90
    //http://localhost:8189/market/products?min=50
    //http://localhost:8189/market/products?max=90
    @GetMapping ("/products")
    public List<ProductDto> getProductsByPriceRange (
                                    @RequestParam(name="min", required = false) Integer min,
                                    @RequestParam(name="max", required = false) Integer max)
    {
        return productListToDtoList (productService.getProductsByPriceRange(min, max));
    }

}
