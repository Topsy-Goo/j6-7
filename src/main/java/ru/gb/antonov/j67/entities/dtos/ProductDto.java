package ru.gb.antonov.j67.entities.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.gb.antonov.j67.entities.Product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data   //< здесь ломбок пришёлся очень кстати: как раз для «умолчальных» геттеров и сеттеров.
public class ProductDto
{
    private Long   productId;

    @NotNull (message="задайте название для продукта")
    @Length (min=3, max=255, message="длина названия [3…255] символов")
    private String productTitle;

    @Min(value=0, message="неотрицательная цена продукта")
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
