package vttp2023.batch4.paf.assessment.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {

	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred })
	 * 
	 * db.listings.aggregate([
	 * 
	 * {$group:{_id: "$address.suburb"}}
	 * 
	 * ])
	 *
	 */
	public List<String> getSuburbs(String country) {
		GroupOperation groupOperation = Aggregation.group("address.suburb");
		Aggregation pipline = Aggregation.newAggregation(groupOperation);
		AggregationResults<Document> results = template.aggregate(pipline, "listings", Document.class);
		List<Document> docList = results.getMappedResults();
		List<String> resultList = new LinkedList<>();

		for (Document doc : docList) {

			String sub = doc.getString("_id");
			if (sub != null && sub.length() > 0) {
				resultList.add(sub);
			}
		}

		return resultList;
	}

	/*
	 * Write the native MongoDB query that you will be using for this method
	 * inside this comment block
	 * eg. db.bffs.find({ name: 'fred })
	 * db.listings.aggregate([
	 * 
	 * {$match:{"address.suburb":{ $regex :"bronte",
	 * $options:"i"},
	 * "price":{$lte:900},
	 * "accommodates": {$gte:3},
	 * "min_nights":{$lte:10}}},
	 * 
	 * {$project:{name:1, accomodates:1, price:1}},
	 * {$sort:{"price":-1}}
	 * 
	 * ]);
	 *
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		MatchOperation matchFilter = Aggregation.match(
								Criteria.where("address.suburb").regex(suburb,"i")
										.and("price").lte(priceRange)
										.and("accommodates").gte(persons)
										.and("min_nights").lte(duration));
		ProjectionOperation fields = Aggregation.project("_id","name", "accommodates", "price");
		SortOperation sortPrice = Aggregation.sort(Sort.by(Direction.DESC,"price"));
		Aggregation pipeline = Aggregation.newAggregation(matchFilter, fields, sortPrice);
		List<Document> docList = template.aggregate(pipeline, "listings", Document.class)
											.getMappedResults();

		List<AccommodationSummary> summaryList = new LinkedList<>();
		System.out.println("\n\n\n");
		System.out.println("\nResult doucmuent size\n" + docList.size());
		for(Document doc: docList){

			AccommodationSummary summary = new AccommodationSummary();
			summary.setId(doc.getString("_id"));
			summary.setName(doc.getString("name"));
			summary.setAccomodates(doc.getInteger("accommodates"));
			summary.setPrice(doc.get("price", Number.class).floatValue());

			summaryList.add(summary);

		}									

		return summaryList;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
