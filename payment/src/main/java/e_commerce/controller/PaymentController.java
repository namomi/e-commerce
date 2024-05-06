package e_commerce.controller;

import com.e_commerce.dto.PaymentInfo;
import com.e_commerce.entity.Payment;
import e_commerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/register/{userId}/payment")
    public String addPayment(@PathVariable Long userId, @RequestBody PaymentInfo paymentInfo) {
        return paymentService.addPayment(userId, Payment.toEntity(paymentInfo));
    }

    @GetMapping("/{userId}/payment")
    public Payment findByPayment(@PathVariable Long userId) {
        return paymentService.findByPayment(userId);
    }

    @DeleteMapping("{userId}/payment")
    public void deletePayment(@PathVariable Long userId) {
        paymentService.deletePayment(userId);
    }
}
