package ru.gb.antonov.j67.entities;

import ru.gb.antonov.j67.beans.errorhandlers.ProductUpdatingException;

import javax.persistence.*;

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

    @Column(name="title")
    private String title;

    @Column(name="price")
    private double cost;


    public Product(){}     //< эта строчка вдвое короче аннотации ломбока


    public void update (String t, double c)
    {
        if (!setTitle (t) || !setCost (c))
        {
            throw new ProductUpdatingException (String.format(
                "Недопустимый набор значений:\r    название продукта = %s,\r    цена = %.2f.", t, c));
                /*   Код для окон сообщений в Google Chrome писали идиоты. В результате, в них символ \t заменяется символом т, а символ \n — двойной кавычкой.   */
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

}
