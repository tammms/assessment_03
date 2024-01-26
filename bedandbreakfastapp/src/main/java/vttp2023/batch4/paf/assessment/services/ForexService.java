package vttp2023.batch4.paf.assessment.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ForexService {

	RestTemplate template = new RestTemplate();
	// TODO: Task 5 
	public float convert(String from, String to, float amount) {
		 
		String url = UriComponentsBuilder
					.fromUriString("https://www.api.frankfurt.app/latest")
					.queryParam("amount", amount)
					.queryParam("from", from)
					.queryParam("to", to)
					.toUriString();

		
		try {
			Float result = template.getForObject(url, Float.class);
			return result;
		} catch (Exception e) {
			return -1000f;
		}
		
	}
}
