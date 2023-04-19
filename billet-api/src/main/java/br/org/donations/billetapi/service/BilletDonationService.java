package br.org.donations.billetapi.service;


import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import br.org.donations.billetapi.feignclients.DonationApiFeignClient;
import br.org.donations.billetapi.mappers.DTOMapper;
import br.org.donations.billetapi.model.Donation;
import br.org.donations.billetapi.repository.BilletDonationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j(topic = "BilletDonationService")
@Service
public class BilletDonationService {
    @Autowired
    private BilletDonationRepository billetDonationRepository;
    @Autowired
    private DonationApiFeignClient donationFeignClient;
    @Autowired
    private DTOMapper DTOmapper;

    @Autowired
    private EmailSenderService emailSenderService;

    public Long processBilletDonation(BilletDonationDTO billetDonationDTO) throws InterruptedException {
        BilletDonationResponseDTO billetDonationResponseDTO = donationFeignClient.validateBilletDonation(billetDonationDTO);
        var donationToSave = DTOmapper.convertBilletDonationResponseDTOToDonationEntity(billetDonationResponseDTO);
        Donation savedDonation = billetDonationRepository.save(donationToSave);
        Long savedDonationId = savedDonation.getId();

        emailSenderService.sendEmailToDonor(savedDonation.getDonor().getEmail());

        log.info("Doação salva com sucesso! Id: " + savedDonationId);
        return savedDonationId;
    }

}
