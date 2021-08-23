package ru.gb.antonov.j67.beans.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j67.entities.Product;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>
{
    List<Product> findAllByCostBetween (double minPrice, double maxPrice);

}
