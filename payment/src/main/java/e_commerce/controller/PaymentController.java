package e_commerce.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.PaymentInfo;
import com.e_commerce.entity.Payment;

import e_commerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "경제수단 등록.", description = "경제수단을 등록합니다")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/register/{userId}/payment")
	@Operation(summary = "add payment", description = "결제수단을 등록합니다..")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Please enter your information."),
	})
	public String addPayment(@PathVariable
	@Positive(message = "user id는 필수입니다.")
	@Schema(description = "userId", example = "2")
	Long userId,
		@Positive(message = "유저정보를 입력해주세요.")
		@RequestBody PaymentInfo paymentInfo) {
		return paymentService.addPayment(userId, Payment.toEntity(paymentInfo));
	}

	@GetMapping("/{userId}/payment")
	@Operation(summary = "find payment", description = "결제수단을 조회합니다..")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Please enter your information."),
	})
	public Payment findByPayment(@PathVariable
	@Positive(message = "user id는 필수입니다.")
	@Schema(description = "userId", example = "2")
	Long userId) {
		return paymentService.findByPayment(userId);
	}

	@DeleteMapping("{userId}/payment")
	@Operation(summary = "delete payment", description = "결제수단을 삭제합니다..")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Please enter your information."),
	})
	public void deletePayment(@PathVariable
	@Positive(message = "user id는 필수입니다.")
	@Schema(description = "userId", example = "2")
	Long userId) {
		paymentService.deletePayment(userId);
	}
}
