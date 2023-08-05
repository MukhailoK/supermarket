package test;

import dao.Supermarket;
import dao.SupermarketImpl;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class SupermarketImplTest {

    private LocalDate now = LocalDate.now();
    private Product[] products;
    private Supermarket supermarket;

    @BeforeEach
    void setUp() {
        supermarket = new SupermarketImpl();
        products = new Product[]{
                new Product(10_000_000_001L, "product1", "Category1", "Brand1", 2.20, now.minusDays(1)),
                new Product(10_000_000_002L, "product2", "Category1", "Brand1", 2.40, now.plusMonths(6)),
                new Product(10_000_000_003L, "product3", "Category2", "Brand2", 3.30, now.minusDays(2)),
                new Product(10_000_000_004L, "product4", "Category2", "Brand2", 5.20, now.minusDays(3)),
                new Product(10_000_000_005L, "product5", "Category2", "Brand3", 6.20, now.minusDays(5)),
                new Product(10_000_000_006L, "product6", "Category3", "Brand3", 1.20, now.minusDays(2)),
                new Product(10_000_000_007L, "product7", "Category4", "Brand4", 4.20, now.plusMonths(2)),
                new Product(10_000_000_007L, "product7", "Category4", "Brand4", 4.20, now.plusWeeks(3)),
                new Product(10_000_000_009L, "product9", "Category5", "Brand4", 9.20, now.plusMonths(4)),
                new Product(10_000_000_010L, "product10", "Category6", "Brand5", 12.20, now.plusMonths(21))};
        Arrays.stream(products).forEach(p -> supermarket.addProduct(p));

    }

    @Test
    void testAddNullProduct() {
        assertFalse(supermarket.addProduct(null));
    }

    @Test
    void testAddNewProduct() {
        Product expected = new Product(10_000_000_011L, "product11", "Category6", "Brand5", 13.20, now.plusMonths(21));
        assertTrue(supermarket.addProduct(expected));
        assertEquals(expected, supermarket.findByBarCode(expected.getBarCode()));

        assertEquals(10, supermarket.skuQuantity());
    }

    @Test
    void testAddSameProduct() {
        Product product = products[9];
        assertTrue(supermarket.addProduct(product));
        assertEquals(2, supermarket.findByBarCode(product.getBarCode()).getCount());
    }

    @Test
    void removeProductThatWrongBarCode() {
        assertNull(supermarket.removeProduct(-1));
    }

    @Test
    void removeProduct() {
        Product expected = products[0];
        assertEquals(expected, supermarket.removeProduct(expected.getBarCode()));
        assertEquals(8, supermarket.skuQuantity());
    }

    @Test
    void findProductThatWrongBarCode() {
        assertNull(supermarket.findByBarCode(-1));
    }

    @Test
    void findProduct() {
        Product expected = products[0];
        assertEquals(expected, supermarket.findByBarCode(expected.getBarCode()));
    }

    @Test
    void sellProduct() {
        Product expected = products[7];
        assertEquals(expected, supermarket.sellOne(expected.getBarCode()));
        assertEquals(1, supermarket.findByBarCode(expected.getBarCode()).getCount());
    }

    @Test
    void sellNoProduct() {
        supermarket.sellOne(products[0].getBarCode());
        assertThrows(NoSuchElementException.class, () -> supermarket.sellOne(products[0].getBarCode()));
    }

    @Test
    void findByCategory() {
        List<Product> expected = new ArrayList<>(List.of(products[2], products[3], products[4]));
        expected.sort(Comparator.comparingLong(Product::getBarCode));
        List<Product> sortedActual = Stream.of(supermarket.findByCategory("Category2"))
                .flatMap(products -> StreamSupport.stream(products.spliterator(), false))
                .sorted(Comparator.comparingLong(Product::getBarCode))
                .collect(Collectors.toList());
        assertIterableEquals(expected, sortedActual);
    }

    @Test
    void findByBrand() {
        List<Product> expected = new ArrayList<>(List.of(
                products[2],
                products[3]
        ));
        expected.sort(Comparator.comparingLong(Product::getBarCode));
        List<Product> sortedActual = Stream.of(supermarket.findByBrand("Brand2"))
                .flatMap(products -> StreamSupport.stream(products.spliterator(), false))
                .sorted(Comparator.comparingLong(Product::getBarCode))
                .collect(Collectors.toList());
        assertIterableEquals(expected, sortedActual);
    }

    @Test
    void findProductByExpiredDate() {
        List<Product> expected = new ArrayList<>(List.of(products[0], products[2], products[3], products[4], products[5]));
        expected.sort(Comparator.comparingLong(Product::getBarCode));
        List<Product> sortedActual = Stream.of(supermarket.findProductByExpiredDate())
                .flatMap(products -> StreamSupport.stream(products.spliterator(), false))
                .sorted(Comparator.comparingLong(Product::getBarCode))
                .collect(Collectors.toList());
        assertIterableEquals(expected, sortedActual);
    }

    @Test
    void testSkuQuantity() {
        assertEquals(products.length - 1, supermarket.skuQuantity());
    }

    @Test
    void testIterator() {
        assertTrue(supermarket.iterator().hasNext());
        for (Product product : supermarket) {
            System.out.println(product);
        }
    }

    @Test
    void testIteratorEmptySupermarket() {
        supermarket = new SupermarketImpl();
        assertFalse(supermarket.iterator().hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            Iterator<Product> iterator = supermarket.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
            }
            iterator.next();
        });
    }
}