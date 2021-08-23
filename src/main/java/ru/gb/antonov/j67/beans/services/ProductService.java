package ru.gb.antonov.j67.beans.services;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j67.beans.errorhandlers.ProductUpdatingException;
import ru.gb.antonov.j67.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j67.beans.repos.ProductRepo;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor    //< создаёт конструктор с параметрами для инициализации всех final-полей.
public class ProductService
{
    private final ProductRepo productRepo;
    private static int pageIndexLast = 0;


    //@Autowired < эта аннотация для конструктора необязательна
    //public ProductService (ProductRepo pr)    < ломбок создаст этот конструктор
    //{
    //    productRepo = pr;
    //}
//-----------------------------------------------------------------------

    @NotNull
    public Product findById (Long id)
    {
        String errMessage = "Не найден продукт с id = "+ id;
        return productRepo.findById(id)
                          .orElseThrow(()->new ResourceNotFoundException (errMessage));
    }

    public Page<Product> findAll (int pageIndex, int pageSize)
    {
        pageIndex = validatePageIndex (pageIndex, pageSize);
        return productRepo.findAll (PageRequest.of (pageIndex, pageSize));
    }

    private int validatePageIndex (int pageIndex, int pageSize)
    {
        long productsCount = productRepo.count();
        int pagesCount    = (int)(productsCount / pageSize);

        if (productsCount % pageSize > 0)
            pagesCount ++;

        if (pageIndex >= pagesCount)
            pageIndex = pagesCount -1;

        return Math.max(pageIndex, 0);
    }

    public void deleteById (Long id)
    {
        Product p = findById (id);  //< бросает ResourceNotFoundException
        productRepo.delete(p);
    }

    public Product createProduct (String title, double cost)
    {
        Product p = new Product();
        p.update (title, cost);     //< бросает ProductUpdatingException
        return productRepo.save (p);
    }

    public Product updateProduct (long id, String title, double cost)
    {
        Product p = findById (id);  //< бросает ResourceNotFoundException
        p.update (title, cost);     //< бросает ProductUpdatingException
        return productRepo.save (p);
    }

    public List<Product> getProductsByPriceRange (Integer min, Integer max)
    {
        double minPrice = min != null ? min.doubleValue() : Product.MIN_PRICE;
        double maxPrice = max != null ? max.doubleValue() : Product.MAX_PRICE;

        return productRepo.findAllByCostBetween (minPrice, maxPrice);
    }

//--------- Методы для преобразований Product в ProductDto --------------

    public static ProductDto dtoFromProduct (Product product)
    {
        return new ProductDto (product);
    }

    public static List<ProductDto> productListToDtoList (List<Product> pp)
    {
        if (pp != null)
        {
            return pp.stream()
                     .map(ProductService::dtoFromProduct)
                     .collect (Collectors.toList());
        }
        return Collections.emptyList();
    }
}
