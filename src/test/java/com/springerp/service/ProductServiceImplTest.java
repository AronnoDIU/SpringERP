package com.springerp.service;

import com.springerp.entity.Category;
import com.springerp.entity.Product;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop Pro");
        product.setDescription("High-performance laptop");
        product.setPrice(1299.99);
        product.setStock(50);
        product.setCategory(category);
    }

    @Test
    void createProduct_savesAndReturnsProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = service.createProduct(product);

        assertThat(result).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_updatesAllFields() {
        Product incoming = new Product();
        incoming.setName("Laptop Ultra");
        incoming.setDescription("Ultra high-performance laptop");
        incoming.setPrice(1599.99);
        incoming.setStock(30);
        incoming.setCategory(category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = service.updateProduct(1L, incoming);

        assertThat(result.getName()).isEqualTo("Laptop Ultra");
        assertThat(result.getDescription()).isEqualTo("Ultra high-performance laptop");
        assertThat(result.getPrice()).isEqualTo(1599.99);
        assertThat(result.getStock()).isEqualTo(30);
    }

    @Test
    void updateProduct_withNonExistentId_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProduct(99L, product))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_deletesExistingProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        service.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_withNonExistentId_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteProduct(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).delete(any());
    }

    @Test
    void getProductById_returnsProductForValidId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.getProductById(1L);

        assertThat(result).isEqualTo(product);
    }

    @Test
    void getProductById_withNonExistentId_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllProducts_returnsAllProducts() {
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Tablet");

        when(productRepository.findAll()).thenReturn(List.of(product, product2));

        List<Product> result = service.getAllProducts();

        assertThat(result).hasSize(2).contains(product, product2);
    }

    @Test
    void getAllProducts_returnsEmptyListWhenNoneExist() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = service.getAllProducts();

        assertThat(result).isEmpty();
    }

    @Test
    void updateProduct_preservesCategoryIfNotChanged() {
        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("Laptops");

        Product incoming = new Product();
        incoming.setName("Same Name");
        incoming.setDescription("Same description with 5+ chars");
        incoming.setPrice(999.0);
        incoming.setStock(10);
        incoming.setCategory(newCategory);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = service.updateProduct(1L, incoming);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getCategory()).isEqualTo(newCategory);
    }
}

