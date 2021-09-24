package ru.gb.antonov.j67.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j67.beans.cart.ShoppingCart;
import ru.gb.antonov.j67.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j67.beans.repos.ProductRepo;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor    //< создаёт конструктор с параметрами для инициализации всех final-полей.
public class ProductService
{
    private final ProductRepo productRepo;
    private final ShoppingCart cart;
    private static int pageIndexLast = 0;
//-----------------------------------------------------------------------

    @Transactional                  //ProductController,
    public Product findById (Long id)
    {
        String errMessage = "Не найден продукт с id = "+ id;
        return productRepo.findById(id)
                          .orElseThrow(()->new ResourceNotFoundException (errMessage));
    }

    @Transactional                  //ProductController,
    public Page<Product> findAll (int pageIndex, int pageSize)
    {
        pageIndex = validatePageIndex (pageIndex, pageSize, productRepo.count());
        return productRepo.findAll (PageRequest.of (pageIndex, pageSize));
    }

    @Transactional              //CartController,
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

    @Transactional              //ProductController,
    public void deleteById (Long id)
    {
        Product p = findById (id);  //< бросает ResourceNotFoundException
        cart.removeFromCart (p);    //< сперва удаляем из корзины
        productRepo.delete(p);
    }

    @Transactional                  //ProductController,
    public Product createProduct (String title, double cost)
    {
        Product p = new Product();
        p.update (title, cost);     //< бросает ProductUpdatingException
        return productRepo.save (p);
    }

    @Transactional                  //ProductController,
    public Product updateProduct (long id, String title, double cost)
    {
        Product p = findById (id);  //< бросает ResourceNotFoundException
        p.update (title, cost);     //< бросает ProductUpdatingException
        cart.updateProduct (p);
        return productRepo.save (p);
    }

    @Transactional                  //ProductController,
    public List<Product> getProductsByPriceRange (Integer min, Integer max)
    {
        double minPrice = min != null ? min.doubleValue() : Product.MIN_PRICE;
        double maxPrice = max != null ? max.doubleValue() : Product.MAX_PRICE;

        return productRepo.findAllByCostBetween (minPrice, maxPrice);
    }
//-------------------- Корзина ------------------------------------------

    @Transactional          //CartController,
    public Integer addToCart (Long pid)
    {
        cart.addToCart (findById (pid));
        return cart.getItemsCount();
    }

    @Transactional          //CartController,
    public Integer removeFromCart (Long pid)
    {
        cart.removeFromCart (findById(pid));
        return cart.getItemsCount();
    }

    @Transactional          //CartController,
    public Integer getCartItemsCount ()
    {
        return cart.getItemsCount();
    }
//--------- Методы для преобразований Product в ProductDto --------------

    public static ProductDto dtoFromProduct (Product product)           //CartController, ProductController
    {
        return new ProductDto (product);
    }

    public static List<ProductDto> productListToDtoList (List<Product> pp)      //ProductController,
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
