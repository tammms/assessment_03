package vttp2023.batch4.paf.assessment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp2023.batch4.paf.assessment.models.AccommodationSummary;
import vttp2023.batch4.paf.assessment.repositories.ListingsRepository;

@SpringBootApplication
public class AssessmentApplication implements CommandLineRunner {

	@Autowired
	ListingsRepository listRepo;
	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// List<String> d = listRepo.getSuburbs("ss");

		// System.out.println("\nResult\n" + d);

		List<AccommodationSummary> d = listRepo.findListings("bronte", 1, 100, 900);

		System.out.println("\nResult\n" + d);
	}
}
