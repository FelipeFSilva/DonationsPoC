package br.org.donations.billetapi.service;


import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import br.org.donations.billetapi.feignclients.BilletApiFeignClient;
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
    private BilletApiFeignClient billetDonatioFeignClient;
    @Autowired
    private DTOMapper DTOmapper;

    public Long processBilletDonation(BilletDonationDTO billetDonationDTO){
        BilletDonationResponseDTO billetDonationResponseDTO = billetDonatioFeignClient.validateBilletDonation(billetDonationDTO);
        Donation savedDonation = billetDonationRepository.save(DTOmapper.convertBilletDonationResponseDTOToDonationEntity(billetDonationResponseDTO));
        Long savedDonationId = savedDonation.getId();

        log.info("Doação salva com sucesso! Id: " + savedDonationId);
        return savedDonationId;
    }

}
