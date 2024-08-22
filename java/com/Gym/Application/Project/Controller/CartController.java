package com.Gym.Application.Project.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Gym.Application.Project.Model.Address;
import com.Gym.Application.Project.Model.CartItem;
import com.Gym.Application.Project.Model.OrderItem;
import com.Gym.Application.Project.Model.OrderStatus;
import com.Gym.Application.Project.Model.PurchaseOrder;
import com.Gym.Application.Project.Model.Product;
import com.Gym.Application.Project.Model.User;
import com.Gym.Application.Project.Repository.AddressRepository;
import com.Gym.Application.Project.Repository.CartItemRepository;
import com.Gym.Application.Project.Repository.OrderRepository;
import com.Gym.Application.Project.Repository.ProductRepository;
import com.Gym.Application.Project.Repository.UserRepository;
import com.Gym.Application.Project.Sevice.CartService;

import jakarta.transaction.Transactional;

@Controller
@SessionAttributes("cart")
public class CartController {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    AddressRepository addressRepository;
    
    @Autowired
    OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("id") Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Product product = productRepository.findById(productId).orElse(null);

        if (user != null && product != null) {
            CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
            double price = Double.parseDouble(product.getPrice());
            double discount= product.getDiscountPrice();
            
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(1);
                cartItem.setTotalPrice(price);
                cartItem.setTotalDiscount(discount);
                
             // Calculate delivery date (4 days from today)
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 4);
                cartItem.setDeliveryDate(calendar.getTime());
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setTotalPrice(cartItem.getQuantity() * price);
                cartItem.setTotalDiscount(cartItem.getQuantity() * discount);
            }

            cartItemRepository.save(cartItem);
        }

        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
    	 User user = userRepository.findByEmail(principal.getName());

         if (user != null) {
             List<CartItem> cartItems = cartItemRepository.findByUser(user);
             model.addAttribute("cartItems", cartItems);
             model.addAttribute("totalPrice", cartService.getTotalPrice(user));
             model.addAttribute("totalDiscount", cartService.getTotalDiscount(user));
         } else  {
             model.addAttribute("cartItems", new ArrayList<CartItem>());
             model.addAttribute("totalPrice", 0);
             model.addAttribute("totalDiscount", 0);
         }

         return "cart";
    }
    
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("id") Long productId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Product product = productRepository.findById(productId).orElse(null);

        if (user != null && product != null) {
            CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            }
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());

        if (user != null) {
            CartItem cartItem = cartItemRepository.findById(id).orElse(null);

            if (cartItem != null && cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItem.setTotalPrice(cartItem.getQuantity() * Double.parseDouble(cartItem.getProduct().getPrice()));
                cartItem.setTotalDiscount(cartItem.getQuantity() * cartItem.getProduct().getDiscountPrice());
                cartItemRepository.save(cartItem);
            } else if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            }
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/increase/{id}")
    public String increaseQuantity(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());

        if (user != null) {
            CartItem cartItem = cartItemRepository.findById(id).orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setTotalPrice(cartItem.getQuantity() * Double.parseDouble(cartItem.getProduct().getPrice()));
                cartItem.setTotalDiscount(cartItem.getQuantity() * cartItem.getProduct().getDiscountPrice());
                cartItemRepository.save(cartItem);
            }
        }

        return "redirect:/cart";
    }
    
   //Checkout Page controller logics....................................................................
  //Checkout Product  
    @GetMapping("/checkout")
    public String placeOrder(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());

        if (user != null) {
            List<CartItem> cartItems = cartItemRepository.findByUser(user);
            List<Address> addresses = addressRepository.findByUserId(user.getId());
            
            logger.info("User: {}", user);
            logger.info("Addresses: {}", addresses);
            
            model.addAttribute("addresses", addresses);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", cartService.getTotalPrice(user));
            model.addAttribute("totalDiscount", cartService.getTotalDiscount(user));
           
        } else {
        	 model.addAttribute("cartItems", new ArrayList<CartItem>());
             model.addAttribute("addresses", new ArrayList<Address>());
        	 model.addAttribute("totalPrice", 0);
             model.addAttribute("totalDiscount", 0);        }

        return "checkout"; // Redirect to the checkout confirmation page
    }
    
    @GetMapping("/checkout/address/change")
    public String showChangeAddressForm(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        List<Address> addresses = addressRepository.findByUserId(user.getId());
        model.addAttribute("addresses", addresses);
       
        return "change-address";
    }

 // POST method to handle address change or addition
    @PostMapping("/checkout/address/change")
    public String changeAddress(@ModelAttribute("newAddress") Address newAddress,
                                Principal principal,
                                @RequestParam(value = "selectedAddressId", required = false) Long selectedAddressId,
                                RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName());

        // Retrieve the user's existing address (assuming only one)
        List<Address> addresses = addressRepository.findByUserId(user.getId());
        Address existingAddress = addresses.isEmpty() ? null : addresses.get(0);

        if (selectedAddressId != null && selectedAddressId != 0 && existingAddress != null) {
            // Update existing address
            existingAddress.setStreet(newAddress.getStreet());
            existingAddress.setCity(newAddress.getCity());
            existingAddress.setState(newAddress.getState());
            existingAddress.setZipcode(newAddress.getZipcode());
            existingAddress.setPhone(newAddress.getPhone());
            addressRepository.save(existingAddress);
        } else {
            if (existingAddress != null) {
                // Update existing address if it exists
                existingAddress.setStreet(newAddress.getStreet());
                existingAddress.setCity(newAddress.getCity());
                existingAddress.setState(newAddress.getState());
                existingAddress.setZipcode(newAddress.getZipcode());
                existingAddress.setPhone(newAddress.getPhone());
                addressRepository.save(existingAddress);
            } else {
                // Save new address
                newAddress.setUser(user);
                addressRepository.save(newAddress);
            }
        }

        return "redirect:/checkout";
    }

    @PostMapping("/checkout/address/remove/{id}")
    public String removeAddress(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        Address address = addressRepository.findById(id).orElse(null);

        if (address != null && address.getUser().getId().equals(user.getId())) {
            addressRepository.delete(address);
        }

        return "redirect:/checkout";
    }
    //Payment controller logic................................................................................
    @GetMapping("/checkout/payment")
    public String showPaymentPage(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        if (user != null) {
            double totalPrice = cartService.getTotalPrice(user);
            model.addAttribute("totalPrice", totalPrice);
            return "payment";
        }
        return "redirect:/cart";
    }
    
    @PostMapping("/checkout/payment-success")
    @Transactional
    public String paymentSuccess(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());

        if (user != null) {
            List<CartItem> cartItems = cartItemRepository.findByUser(user);
            double totalPrice = cartService.getTotalPrice(user);

            // Create Order
            PurchaseOrder order = new PurchaseOrder();
            order.setUser(user);

            // Calculate delivery date (4 days from now)
            Date orderDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 4);
            Date deliveryDate = calendar.getTime();

            order.setTotalPrice(totalPrice);
            order.setOrderDate(orderDate);
            order.setDeliveryDate(deliveryDate);

            // Set initial order status
            order.setStatus(OrderStatus.PENDING);

            // Create OrderItems from CartItems
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
            	
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getTotalPrice());
                orderItems.add(orderItem);
            }

            // Set orderItems to order
            order.setOrderItems(orderItems);

            // Save Order with associated OrderItems
            orderRepository.save(order);

            // Clear Cart
            cartItemRepository.deleteAll(cartItems);

            // Add order details to model
            model.addAttribute("order", order);
            model.addAttribute("deliveryDate", deliveryDate);
        }

        return "payment-success";
    }
    
    @GetMapping("/order-history")
    public String viewOrderHistory(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());

        if (user != null) {
            List<PurchaseOrder> orders = orderRepository.findByUserOrderByOrderDateDesc(user);
            model.addAttribute("orders", orders);
        } else {
            model.addAttribute("orders", new ArrayList<PurchaseOrder>());
        }

        return "order-history";
    }
    
    
}
