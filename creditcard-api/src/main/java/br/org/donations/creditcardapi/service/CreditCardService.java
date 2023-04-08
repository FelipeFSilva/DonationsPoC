package br.org.donations.creditcardapi.service;

import br.org.donations.creditcardapi.dto.DonationDTO;
import br.org.donations.creditcardapi.dto.DonationToSaveDTO;
import br.org.donations.creditcardapi.dto.EmailDTO;
import br.org.donations.creditcardapi.feignclients.DonationsApiFeignClient;
import br.org.donations.creditcardapi.model.Donation;
import br.org.donations.creditcardapi.rabbitmq.CreditCardDonationSender;
import br.org.donations.creditcardapi.rabbitmq.EmailSender;
import br.org.donations.creditcardapi.repository.DonationRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j(topic = "CreditCardService")
@Service
public class CreditCardService {

    @Autowired
    private CreditCardDonationSender creditCardDonationSender;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private DonationRepository repository;
    @Autowired
    private DonationsApiFeignClient donationsApiFeignClient;
    @Autowired
    private ModelMapper mapper;

    public void sendDonation(DonationDTO creditCardDonation) {
        log.info("Iniciando envio de doação.");
        creditCardDonationSender.sendCreditCardDonationMessage(creditCardDonation);
        log.info("Finalizando envio de doação.");
    }

    @Transactional
    public Long saveDonation(DonationDTO donation) {
        log.info("Iniciando processo de salvar doação.");

        try{
            DonationToSaveDTO donorToSaveDTO = donationsApiFeignClient.validateCreditCardDonation(donation);
            Donation savedDonation = repository.save(convertToDonorEntity(donorToSaveDTO));
            Long savedDonationId = savedDonation.getId();
            log.info("Doação salva com sucesso! Id: " + savedDonationId);

            emailSender.sendCreditCardEmailMessage(EmailDTO.donationToEmailDTO(savedDonation));
            return savedDonationId;
        }catch (FeignException ex){
            log.error("Ocorreu um erro no processo de salvar doação: " + ex);
            throw ex;
        }catch (ConstraintViolationException ex){
            log.error("Ocorreu um erro no processo de validar doação: " + ex.getConstraintViolations());
            throw ex;
        }
    }

    private Donation convertToDonorEntity(DonationToSaveDTO donationToSaveDTO) {
        return mapper.map(donationToSaveDTO, Donation.class);
    }
}
