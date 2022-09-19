package com.bansal.yash.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.bansal.yash.models.LocationStats;

/**
 * @author bansalyash
 *
 */
@Service
public class CVDataService {

	private static String CV_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<LocationStats> allStats = new ArrayList<>();

	@PostConstruct
	@Scheduled(cron = "* 15 * * * *")
	public void fetchVirusData() {
		List<LocationStats> newStats = new ArrayList<>();
		HttpResponse<String> httpResponse = null;
		Iterable<CSVRecord> records = null;

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CV_DATA_URL)).build();

		try {
			httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		StringReader csvReader = new StringReader(httpResponse.body());

		try {
			records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (CSVRecord record : records) {
			LocationStats locationStats = new LocationStats();
			locationStats.setCountry(record.get("Country/Region"));
			locationStats.setState(record.get("Province/State"));
			int latestCases = Integer.parseInt(record.get(record.size() - 1));
			int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
			locationStats.setLatestTotalCases(latestCases);
			locationStats.setDiffFromPrevDay(latestCases - prevDayCases);
			newStats.add(locationStats);
		}
		this.allStats = newStats;
	}

	public List<LocationStats> getAllStats() {
		return allStats;
	}
}
