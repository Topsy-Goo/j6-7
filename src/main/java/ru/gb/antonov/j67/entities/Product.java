package ru.gb.antonov.j67.entities;

import org.hibernate.validator.constraints.Length;
import ru.gb.antonov.j67.beans.errorhandlers.ProductUpdatingException;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name="products")
//@NoArgsConstructor (access = AccessLevel.PROTECTED) < ломбок создаёт умолчальный конструктор, но непонятно, в чём выгода.
//@Data     < ломбок генерит геттеры и сеттеры, которые годятся только для учебных проектов; имеет опцию, кажется, для статического конструктора.
public class Product
{
    public static final Double MIN_PRICE = 0.0;
    public static final Double MAX_PRICE = Double.MAX_VALUE;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

/*  У hibernate.validator'а есть полезные нотации, которые перекладывают некоторые проверки на яву, разгружая от лишней работы БД и сеть.
*/
    @NotNull (message="задайте название для продукта")
    @Length (min=3, max=255, message="длина названия в интервале 3…255 символов\r")
    @Column(name="title")
    private String title;

    @Min (value=0, message="только неотрицательная цена продукта\r")
    @Column(name="price")
    private double cost;


    public Product(){}

    public void update (String t, double c)
    {
        if (!setTitle (t) || !setCost (c))
        {
            throw new ProductUpdatingException (String.format(
                "Недопустимый набор значений:\r    название продукта = %s,\r    цена = %.2f.", t, c));
        }
    }
//----------------- Геттеры и сеттеры -----------------------------------

    public Long getId ()    {   return id;   }
    private void setId (Long id)   {   this.id = id;   }


    public String getTitle ()   {   return title;   }
    public boolean setTitle (String title)
    {
        boolean ok = isTitleValid (title);
        if (ok)
            this.title = title.trim();
        return ok;
    }

    public double getCost ()    {   return cost;   }
    public boolean setCost (double cost)
    {
        boolean ok = isCostValid (cost);
        if (ok)
            this.cost = cost;
        return ok;
    }
//-----------------------------------------------------------------------

    public static boolean isTitleValid (String title)
    {
        return title != null  &&  !title.trim().isEmpty();
    }

    public static boolean isCostValid (double cost)
    {
        return cost >= MIN_PRICE  &&  cost <= MAX_PRICE;
    }

    public String toString()
    {
        return String.format("[id:%d, «%s», %.2f]", id, title, cost);
    }

    @Override public boolean equals (Object o)
    {
        Product p = (Product) o;
        return p!=null && this.id.equals(p.getId());
    }

    @Override public int hashCode()    {   return Objects.hash(id);   }

}
