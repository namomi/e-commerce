package e_commerce.controller;

import com.e_commerce.dto.CardInfo;
import com.e_commerce.entity.CardInformation;
import e_commerce.service.CardInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CardInformationController {

    private final CardInformationService cardInformationService;

    @PostMapping("payment/{paymentId}/card")
    public void addCardInfo(@PathVariable Long paymentId, @RequestBody CardInfo cardInfo) {
        cardInformationService.addCardInfo(paymentId, CardInformation.toEntity(cardInfo));
    }

    @GetMapping("payment/{paymentId}/card")
    public CardInfo findByCardInfo(@PathVariable Long paymentId) {
        return cardInformationService.findByCardInfo(paymentId);
    }
}
