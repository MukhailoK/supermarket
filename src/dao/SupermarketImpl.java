package dao;

import model.Product;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        } else if (products.containsKey(product.getBarCode())) {
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
        if (products.get(barCode).getCount() == 0) {
            throw new NoSuchElementException("No product in supermarket");
        } else
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
        return products.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public int skuQuantity() {
        return products.size();
    }

//    @Override
//    public Iterator<Product> iterator() {
//        return new Iterator<>() {
//            private final List<Product> productsValues = new ArrayList<>(products.values());
//            private int count;
//
//            @Override
//            public boolean hasNext() {
//                return count < productsValues.size();
//            }
//
//            @Override
//            public Product next() {
//                if (!hasNext()) {
//                    throw new NoSuchElementException("No more products in the supermarket.");
//                }
//                return productsValues.get(count++);
//            }
//        };
//    }

    @Override
    public Iterator<Product> iterator() {
        return products.values().iterator();
    }
}