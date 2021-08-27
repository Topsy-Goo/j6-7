package ru.gb.antonov.j67.beans.services;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j67.ShoppingCart;
import ru.gb.antonov.j67.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j67.beans.repos.ProductRepo;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor    //< создаёт конструктор с параметрами для инициализации всех final-полей.
public class ProductService
{
    private final ProductRepo productRepo;
    private static int pageIndexLast = 0;
    private final ShoppingCart cart;


    @Autowired //< эта аннотация для конструктора необязательна
    public ProductService (ProductRepo pr)
    {
        productRepo = pr;
        cart = new ShoppingCart();
    }
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
        pageIndex = validatePageIndex (pageIndex, pageSize, productRepo.count());
        return productRepo.findAll (PageRequest.of (pageIndex, pageSize));
    }

    public Page<Product> getCartPage (int pageIndex, int pageSize)
    {
        List<Product> list = cart.getProducts();
        int length = list.size();

        pageIndex = validatePageIndex (pageIndex, pageSize, length);

        int fromIndex = pageSize * pageIndex;
        int toIndex = fromIndex + pageSize;
        if (toIndex > length)
            toIndex = length;

        Page<Product> page = new PageImpl<>(
                                    list.subList(fromIndex, toIndex),
                                    PageRequest.of (pageIndex, pageSize),
                                    length);
        Pageable pageable = page.getPageable();
        return page;
    }

    private int validatePageIndex (int pageIndex, int pageSize, long productsCount)
    {
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

    public static List<Product> getEmptyProductList()
    {
        return new LinkedList<>();
    }

    public Integer addToCart (Long pid)
    {
        cart.addToCart (findById (pid));
        return cart.getItemsCount();
    }

    public Integer removeFromCart (Long pid)
    {
        cart.removeFromCart (findById(pid));
        return cart.getItemsCount();
    }

    public Integer getCartItemsCount()
    {
        return cart.getItemsCount();
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
