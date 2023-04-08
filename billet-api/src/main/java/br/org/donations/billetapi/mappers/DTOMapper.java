package br.org.donations.billetapi.mappers;

import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import br.org.donations.billetapi.model.Donation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    @Autowired
    private ModelMapper mapper;

    public Donation convertBilletDonationResponseDTOToDonationEntity(BilletDonationResponseDTO donationToSaveDTO) {
        return mapper.map(donationToSaveDTO, Donation.class);
    }
}
