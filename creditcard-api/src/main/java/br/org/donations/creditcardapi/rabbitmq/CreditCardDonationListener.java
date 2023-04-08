package br.org.donations.creditcardapi.rabbitmq;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.service.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CreditCardDonationListener")
@Service
public class CreditCardDonationListener {

    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private RetryTemplate retryTemplate;

    @RabbitListener(queues = "${app-config.rabbit.queue.credit-card}")
    public void receiveCreditCardDonationMessage(DonationDTO donation){
        log.info("Recebendo mensagem com doação de cartão de crédito para salvar.");

        Long savedDonationId = creditCardService.saveDonation(donation);
        log.info("Mensagem processada com sucesso. Id da doação salva: {}", savedDonationId);
    }

    @RabbitListener(queues = "${app-config.rabbit.queue.credit-card-retry}")
    public void retryReceiveCreditCardDonationMessage(DonationDTO donation) {
        log.info("Reprocessando mensagem com doação de cartão de crédito para salvar.");

        Long savedDonationId = retryTemplate.execute(retryContext -> (creditCardService.saveDonation(donation)));
        log.info("Mensagem reprocessada com sucesso. Id da doação salva: {}", savedDonationId);
    }
}
