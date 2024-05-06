package e_commerce.service;

import static com.e_commerce.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commerce.dto.CardInfo;
import com.e_commerce.entity.CardInformation;
import com.e_commerce.entity.Payment;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.CardInformationRepository;
import com.e_commerce.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CardInformationService {

	private final PaymentRepository paymentRepository;

	private final CardInformationRepository cardInformationRepository;

	@Transactional
	public void addCardInfo(Long paymentId, CardInformation cardInformation) {
		setPayment(paymentId, cardInformation);
		cardInformationRepository.save(cardInformation);
	}

	public CardInfo findByCardInfo(Long paymentId) {
		Payment payment = getPayment(paymentId);
		return getCardInfo(payment);
	}

	private static CardInfo getCardInfo(Payment payment) {
		return CardInfo.fromPayment(payment);
	}

	private void setPayment(Long paymentId, CardInformation cardInformation) {
		Payment payment = getPayment(paymentId);

		payment.addCardInformation(cardInformation);
	}

	private Payment getPayment(Long paymentId) {
		return paymentRepository.findById(paymentId)
			.orElseThrow(() -> new CustomException(DUPLICATE_PAYMENT));
	}
}
