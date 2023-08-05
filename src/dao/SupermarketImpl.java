package dao;

import model.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SupermarketImpl implements Supermarket {
    private Map<Long, Product> products;

    public SupermarketImpl() {
        products = new HashMap<>();
    }

    @Override
    public boolean addProduct(Product product) {
        if (product == null) {
            return false;
        } else if (!products.containsKey(product.getBarCode())) {
            products.put(product.getBarCode(), product);
            return true;
        } else if (products.containsKey(product.getBarCode())
                && products.get(product.getBarCode()).getBrand().equals(product.getBrand())
                && products.get(product.getBarCode()).getCategory().equals(product.getCategory())
                && products.get(product.getBarCode()).getName().equals(product.getName())
                && products.get(product.getBarCode()).getPrice() == product.getPrice()) {
            products.get(product.getBarCode()).add();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Product removeProduct(long barCode) {
        return products.remove(barCode);
    }

    public Product sellOne(long barCode) {
        products.get(barCode).setCount(products.get(barCode).getCount() - 1);
        return products.get(barCode);
    }

    @Override
    public Product findByBarCode(long barCode) {
        return products.get(barCode);
    }

    @Override
    public Iterable<Product> findByCategory(String category) {
        return findByPredicate(product -> product.getCategory().equals(category));
    }

    @Override
    public Iterable<Product> findByBrand(String brand) {
        return findByPredicate(product -> product.getBrand().equals(brand));
    }

    @Override
    public Iterable<Product> findProductByExpiredDate() {
        return findByPredicate(product -> product.getExpDate().isBefore(LocalDate.now()));
    }

    private Iterable<Product> findByPredicate(Predicate<Product> predicate) {
        List<Product> res = new ArrayList<>();
        for (Map.Entry<Long, Product> entry : products.entrySet()) {
            if (predicate.test(entry.getValue())) {
                res.add(entry.getValue());
            }
        }
        return res;
    }

    @Override
    public int skuQuantity() {
        return products.size();
    }
}