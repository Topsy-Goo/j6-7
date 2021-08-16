package ru.gb.antonov.j67.beans.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j67.entities.Product;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>
{
    List<Product> findAllByCostBetween (double minPrice, double maxPrice);

/*  Чтобы создать другую сущность, нужно лишь создать такой же интерфейс для неё.

    Репозитории бывают разные:

    • Repository<T, ID> — базовый;

    • CrudRepository<T, ID> — предоставляет базовые операции;

    • PagingAndSortingRepository<T, ID> — добавляет метод для сортировки и метод для разбивки всей массы объектов на группы (страницы) (допускается одновременное применение сортировки) (?требуется уточнение?);

    • JpaRepository<T, ID> — добавляет пакетные операции, возможность работы с объектными запросами.
*/
}
