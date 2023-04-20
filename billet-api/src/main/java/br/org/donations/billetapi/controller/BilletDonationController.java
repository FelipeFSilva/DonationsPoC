package br.org.donations.billetapi.controller;

import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.service.BilletDonationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "BilletDonationService")
@RestController
@RequestMapping("billet")
public class BilletDonationController {

    @Autowired
    private BilletDonationService billetDonationService;

    @PostMapping("send-donation")
    public ResponseEntity<String> createBilletDonation(@RequestBody @Valid BilletDonationDTO billetDonationDTO){
        try {
            log.info("Envio de doação por boleto iniciada");
            billetDonationService.processBilletDonation(billetDonationDTO);
            log.info("Envio de doação finalizado.");
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body("Ocorreu um erro ao processar a doação!");
        }
        return ResponseEntity.ok().body("Doação enviada com sucesso!");
    }

    @GetMapping("get-donations")
    public ResponseEntity<String> getAllBilletDonations(){
        log.info("Envio de doação por boleto iniciada");
        billetDonationService.getAllDonations();
        log.info("Envio de doação finalizado.");
        return ResponseEntity.ok().body("Dados serão postados no webhook assim que possível");
    }
}
