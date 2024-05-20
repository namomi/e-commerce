package e_commerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.CardInfo;
import com.e_commerce.entity.CardInformation;

import e_commerce.service.CardInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "결제수단 등록", description = "결제 수단을 등록합니다.")
public class CardInformationController {

	private final CardInformationService cardInformationService;

	@PostMapping("payment/{paymentId}/card")
	@Operation(summary = "add cardInfo", description = "카드정보를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Duplicate payment."),
	})
	public void addCardInfo(@PathVariable
	@Positive(message = "payment id는 필수입니다.")
	@Schema(description = "paymentId", example = "1")
	Long paymentId,
		@Positive(message = "카드정보를 입력합니다.")
		@RequestBody CardInfo cardInfo) {
		cardInformationService.addCardInfo(paymentId, CardInformation.toEntity(cardInfo));
	}

	@Operation(summary = "find cardInfo", description = "카드정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "403", description = "Duplicate payment."),
	})
	@GetMapping("payment/{paymentId}/card")
	public CardInfo findByCardInfo(@PathVariable
	@Positive(message = "payment id는 필수입니다.")
	@Schema(description = "paymentId", example = "1")
	Long paymentId) {
		return cardInformationService.findByCardInfo(paymentId);
	}
}
