package ru.gb.antonov.j67.beans.cart;

import org.springframework.stereotype.Component;
import ru.gb.antonov.j67.entities.Product;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class ShoppingCart
{
    private final List<Product> products;
    //TODO: вообще-то в корзине нужно собирать id-шники, а не копии продуктов. А можно ещё сделать
    //      map<pid, amount>. Будет быстро работать.

    public ShoppingCart()
    {
        products = new LinkedList<>();
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

//Ищем в корзине все продукты с одинаковыми id и обновляем их по образу и подобию product.
    public void updateProduct (Product product)
    {
        if (product != null)
        for (Product p : products)
        {
            if (p.getId().equals(product.getId()))
            {
                p.update (product.getTitle(), product.getCost());
            }
        }
    }
}
