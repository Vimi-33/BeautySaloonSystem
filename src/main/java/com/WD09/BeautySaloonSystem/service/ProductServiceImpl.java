package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.dto.ProductForm;
import com.WD09.BeautySaloonSystem.entities.HairProduct;
import com.WD09.BeautySaloonSystem.entities.NailProduct;
import com.WD09.BeautySaloonSystem.entities.Product;
import com.WD09.BeautySaloonSystem.entities.SkinProduct;
import com.WD09.BeautySaloonSystem.repository.ProductRepository;
import com.WD09.BeautySaloonSystem.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private static final String UPLOAD_DIR = "uploads/products/";

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAvailableProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
    }

    @Override
    public Product createProduct(ProductForm form) {
        // Factory pattern: Instantiate concrete subclass for the abstract entity
        Product product = createProductInstance(form.getType());

        mapFormToEntity(form, product);

        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {
            String imagePath = saveImage(form.getImageFile());
            product.setImageUrl(imagePath);
        }

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductForm form) {
        Product product = getProductById(id);

        mapFormToEntity(form, product);

        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {
            String imagePath = saveImage(form.getImageFile());
            product.setImageUrl(imagePath);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductForm toForm(Product product) {
        if (product == null) {
            return null;
        }

        ProductForm form = new ProductForm();
        form.setName(product.getName());
        form.setBrand(product.getBrand());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setQuantity(product.getQuantity());
        form.setType(product.getTypeCode());

        return form;
    }

    /**
     * Helper factory method to instantiate concrete subclasses of abstract Product
     */
    private Product createProductInstance(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Product category type is required");
        }

        return switch (type.trim().toUpperCase()) {
            case "HAIR" -> new HairProduct();
            case "SKIN" -> new SkinProduct();
            case "NAIL" -> new NailProduct();
            default -> throw new IllegalArgumentException("Invalid product type: " + type);
        };
    }
    @Override
    public void purchaseProduct(Long id, int quantityToBuy) {
        Product product = getProductById(id);

        if (product.getQuantity() < quantityToBuy) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantityToBuy);
        productRepository.save(product);
    }

    /**
     * Saves uploaded MultipartFile to local file system and returns web path
     */
    private String saveImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/" + UPLOAD_DIR + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file: " + e.getMessage(), e);
        }
    }

    private void mapFormToEntity(ProductForm form, Product product) {
        product.setName(form.getName());
        product.setBrand(form.getBrand());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setQuantity(form.getQuantity());
    }
}