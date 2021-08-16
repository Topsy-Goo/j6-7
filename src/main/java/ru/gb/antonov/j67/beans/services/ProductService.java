package ru.gb.antonov.j67.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j67.beans.repos.ProductRepo;
import ru.gb.antonov.j67.entities.Product;
import ru.gb.antonov.j67.entities.dtos.ProductDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor    //< создаёт конструктор с параметрами для инициализации всех final-полей.
public class ProductService
{
    private final ProductRepo productRepo;

//-----------------------------------------------------------------------

    public ProductDto findById (Long id)
    {
        return new ProductDto (productRepo.findById(id).orElse(null));
    }

    public List<Product> findAll ()  {   return productRepo.findAll ();   }

    public void deleteById (Long id)  {   productRepo.deleteById(id);   }

    public List<Product> getProductsByPriceRange (Integer min, Integer max)
    {
        double minPrice = min != null ? min.doubleValue() : Product.MIN_PRICE;
        double maxPrice = max != null ? max.doubleValue() : Product.MAX_PRICE;

        return productRepo.findAllByCostBetween (minPrice, maxPrice);
    }

    public Optional<ProductDto> createProduct (String title, double cost)
    {
        Product p = Product.newProduct(title, cost).orElse(null);
        if (p != null)
        {
            Product saved = productRepo.save(p);
            return Optional.of(new ProductDto (saved));
        }
        return Optional.empty();
    }
}
