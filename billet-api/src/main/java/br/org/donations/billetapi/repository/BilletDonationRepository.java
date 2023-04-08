package br.org.donations.billetapi.repository;

import br.org.donations.billetapi.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BilletDonationRepository extends JpaRepository<Donation, Long> {

}
