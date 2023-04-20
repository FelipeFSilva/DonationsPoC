package br.org.donations.billetapi.mappers;

import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.BilletDonationResponseDTO;
import br.org.donations.billetapi.model.Donation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class DTOMapper {

    @Autowired
    private ModelMapper mapper;

    public Donation convertBilletDonationResponseDTOToDonationEntity(BilletDonationResponseDTO billetDonationResponseDTO) {
        return mapper.map(billetDonationResponseDTO, Donation.class);
    }

    public List<BilletDonationResponseDTO> convertBilletDonationResponseDTOToDonationEntity(List<Donation> donations) {
        List<BilletDonationResponseDTO> billetDonationDTOS = new ArrayList<>();
        donations.forEach(donation -> billetDonationDTOS.add(mapper.map(donation, BilletDonationResponseDTO.class)));
        return billetDonationDTOS;
    }
}
