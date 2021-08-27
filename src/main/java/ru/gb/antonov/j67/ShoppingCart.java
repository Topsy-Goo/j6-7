package ru.gb.antonov.j67;

import ru.gb.antonov.j67.beans.services.ProductService;
import ru.gb.antonov.j67.entities.Product;

import java.util.Collections;
import java.util.List;

public class ShoppingCart
{
    private final List<Product> products;


    public ShoppingCart()
    {
        products = ProductService.getEmptyProductList();
    }

    public List<Product> getProducts()
    {
        return Collections.unmodifiableList (products);
    }

    public Integer getItemsCount()    {   return products.size();   }

    public void addToCart (Product product)
    {
        if (product != null)
        {
            int index = products.indexOf (product);

            if (index < 0)
                index = products.size();

            products.add (index, product);
        }
    }

    public boolean removeFromCart (Product product)
    {
        if (product != null)
        for (Product p : products)
        {
            if (p.getId().equals(product.getId()))
                return products.remove (p);
        }
        return false;
    }

}
