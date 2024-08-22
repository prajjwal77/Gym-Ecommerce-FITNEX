package com.Gym.Application.Project.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Gym.Application.Project.Model.Product;
import com.Gym.Application.Project.Repository.ProductRepository;
import com.Gym.Application.Project.Sevice.ProductService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	 private ProductService productService;
	

	
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("image") MultipartFile imageFile,
                             @RequestParam("image2") MultipartFile imageFile2,
                             @RequestParam("image3") MultipartFile imageFile3) throws IOException {
        saveImage(imageFile, product::setImg);
        saveImage(imageFile2, product::setImg2);
        saveImage(imageFile3, product::setImg3);

        // Save the product to the database
        productRepository.save(product);

        return "redirect:/shop";
    }
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/shop"; // Redirect to shop page or handle not found case
        }
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,
                              @ModelAttribute Product updatedProduct,
                              @RequestParam(value = "image", required = false) MultipartFile imageFile,
                              @RequestParam(value = "image2", required = false) MultipartFile imageFile2,
                              @RequestParam(value = "image3", required = false) MultipartFile imageFile3) throws IOException {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/shop"; // Redirect to shop page or handle not found case
        }

        // Set existing image paths if no new image is uploaded
        if (imageFile == null || imageFile.isEmpty()) {
            updatedProduct.setImg(product.getImg());
        } else {
            saveImage(imageFile, updatedProduct::setImg);
        }

        if (imageFile2 == null || imageFile2.isEmpty()) {
            updatedProduct.setImg2(product.getImg2());
        } else {
            saveImage(imageFile2, updatedProduct::setImg2);
        }

        if (imageFile3 == null || imageFile3.isEmpty()) {
            updatedProduct.setImg3(product.getImg3());
        } else {
            saveImage(imageFile3, updatedProduct::setImg3);
        }

        // Update other fields
        product.setCategory(updatedProduct.getCategory());
        product.setTitle(updatedProduct.getTitle());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());
        product.setSize(updatedProduct.getSize());
        product.setDiscountPrice(updatedProduct.getDiscountPrice());
        product.setImg(updatedProduct.getImg());
        product.setImg2(updatedProduct.getImg2());
        product.setImg3(updatedProduct.getImg3());

        // Save the updated product to the database
        productRepository.save(product);

        return "redirect:/shop"; // Redirect to the product detail page
    }

    private void saveImage(MultipartFile imageFile, ImageSetter imageSetter) throws IOException {
        if (!imageFile.isEmpty()) {
            // Get the path to the 'uploads' folder in the static directory
            Path staticPath = Paths.get("src/main/resources/static/uploads");
            Path imagePath = staticPath.resolve(imageFile.getOriginalFilename());

            // Ensure the uploads directory exists
            if (!Files.exists(staticPath)) {
                Files.createDirectories(staticPath);
            }

            // Save the file
            Files.write(imagePath, imageFile.getBytes());

            // Set the image path for the product
            imageSetter.setImage(imageFile.getOriginalFilename());
        }
    }

    @FunctionalInterface
    private interface ImageSetter {
        void setImage(String imagePath);
    }
    
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/shop";
    }
    
    @GetMapping("/shop")
    public String showShop(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "shop";
    }
    
//    @ModelAttribute("cart")
//    public List<Product> cart() {
//        return new ArrayList<>();
//    }
//
//    @PostMapping("/cart/add")
//    public String addToCart(@RequestParam("id") Long productId, @ModelAttribute("cart") List<Product> cart) {
//        Product product = productRepository.findById(productId).orElse(null);
//        if (product != null) {
//            cart.add(product);
//        }
//        return "redirect:/shop";
//    }
//
//    @GetMapping("/cart")
//    public String viewCart(Model model, @ModelAttribute("cart") List<Product> cart) {
//        model.addAttribute("cartItems", cart);
//        return "cart";
//    }
    
    @GetMapping("/uploads/{filename:.+}")
    public void getImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        Path imagePath = Paths.get("src/main/resources/static/uploads/", filename);
        if (Files.exists(imagePath)) {
            response.setContentType("image/jpeg"); // Adjust content type as per your image type
            Files.copy(imagePath, response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @GetMapping("/shop/{category}")
    public String shopByCategory(@PathVariable("category") String category, Model model) {
        model.addAttribute("products", productService.findByCategory(category));
        model.addAttribute("category", category);
        return "shop";
    }
    
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/error"; // Redirect to an error page or display a meaningful message
        }
        model.addAttribute("product", product);
        return "product-detail";
    }

}
