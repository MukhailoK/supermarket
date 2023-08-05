package dao;

import model.Product;

import java.util.Iterator;

public interface Supermarket extends Iterable<Product> {
    boolean addProduct(Product product);

    Product removeProduct(long barCode);

    Product findByBarCode(long barCode);

    Iterable<Product> findByCategory(String category);

    Iterable<Product> findByBrand(String brand);

    Iterable<Product> findProductByExpiredDate();

    int skuQuantity();

    Product sellOne(long barCode);
}
