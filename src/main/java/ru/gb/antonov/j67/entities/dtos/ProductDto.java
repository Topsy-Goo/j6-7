package ru.gb.antonov.j67.entities.dtos;

import lombok.Data;
import ru.gb.antonov.j67.entities.Product;

@Data   //< здесь ломбок пришёлся очень кстати: как раз для «умолчальных» геттеров и сеттеров.
public class ProductDto
{
    private Long   productId;
    private String productTitle;
    private double productCost;


    public ProductDto (){}

    public ProductDto (Product product)
    {
        if (product != null)
        {
            productId    = product.getId();
            productTitle = product.getTitle();
            productCost  = product.getCost();
        }
        else throw new IllegalArgumentException();
    }

}
