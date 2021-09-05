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
@RestController //< все методы возвращают JSON'ы, плюс позволяет не указывать над методами @ResponseBody.
@RequiredArgsConstructor    //< создаёт конструктор для инициализации всех final-полей.
public class ProductController
{
    private final ProductService productService;
    private final int pageSize = 6;

    /*@Autowired //< эта аннотация для конструктора необязательна
    public ProductController (ProductService ps)
    {
        productService = ps;
    }*/
    public int getPageSize()    {   return pageSize;   }

//--------------------------------------------------------------------

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
/*
Нельзя изменять последовательность следующих параметров:
    @Validated ProductDto pdto, BindingResult br
Спринг проверит параметр ProductDto pdto, и по результатам этой проверки заполнит BindingResult br.
Состояние BindingResult после проверки проверяем мы:
*/
        if (br.hasErrors())
        {
            //преобразуем набор ошибок в список сообщений, и пакуем в одно общее исключение (в наше заранее для это приготовленное исключение).
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
        //cartController.deleteById (id);
    }

    //http://localhost:8189/market/api/v1/products?min=50&max=90
    //http://localhost:8189/market/api/v1/products?min=50
    //http://localhost:8189/market/api/v1/products?max=90
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
