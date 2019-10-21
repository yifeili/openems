package io.openems.edge.weatherbit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.openems.edge.locations.Locations;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @author Jan Seidemann
 */
public class Weatherbit {
	private static final String CONFIG_FILENAME = "config.properties";
	private static final String API_URL = "https://api.weatherbit.io/v2.0/";

	private String api_key;

	/**
	 * Initialize Weatherbit with a valid api key.
	 */
	private Weatherbit() {
		// get API key from configuration file
		Properties properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_FILENAME));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		this.api_key = properties.getProperty("weather_api_key");
	}

	/**
	 * Class for holding an instance of Weatherbit (Singleton, thread-safe)
	 */
	private static class WeatherbitHolder {
		private static final Weatherbit INSTANCE = new Weatherbit();
	}

	/**
	 * Return the singleton instance of Weatherbit
	 *
	 * @return Weatherbit instance
	 */
	public static Weatherbit getInstance() {
		return WeatherbitHolder.INSTANCE;
	}

	/**
	 * Get hourly forecast data from weatherbit.io
	 *
	 * @param location for which data should be collected
	 * @param hours    to forecast
	 * @return json object containing data from api
	 * @throws IOException in case of problems when reading data from resource
	 */
	public JsonObject getHourlyForecastData(Locations location, int hours) throws IOException {
		return getHourlyForecastData(location.getLatitude(), location.getLongitude(), hours);
	}

	/**
	 * Get hourly forecast data from weatherbit.io
	 *
	 * @param latitude  of location
	 * @param longitude of location
	 * @param hours     to forecast
	 * @return json object containing data from api
	 * @throws IOException in case of problems when reading data from resource
	 */
	private JsonObject getHourlyForecastData(double latitude, double longitude, int hours) throws IOException {
		String url = API_URL + "forecast/hourly?" + "lat=" + latitude + "&lon=" + longitude + "&key=" + this.api_key
				+ "&hours=" + hours;

		return getJson(url);
	}

	/**
	 * Get hourly historical data from weatherbit.io
	 *
	 * @param location      for which data should be collected
	 * @param start         date to read data
	 * @param end           date to read data
	 * @param considerHours of start and end date
	 * @return json object containing data from api
	 * @throws IOException in case of problems when reading data from resource
	 */
	public JsonObject getHourlyHistoricalData(Locations location, Date start, Date end, boolean considerHours)
			throws IOException {

		return getHourlyHistoricalData(location.getLatitude(), location.getLongitude(), start, end, considerHours);
	}

	/**
	 * Get hourly historical data from weatherbit.io
	 *
	 * @param latitude      of location
	 * @param longitude     of location
	 * @param start         date to read data
	 * @param end           date to read data
	 * @param considerHours of start and end date
	 * @return json object containing data from api
	 * @throws IOException in case of problems when reading data from resource
	 */
	public JsonObject getHourlyHistoricalData(double latitude, double longitude, Date start, Date end,
			boolean considerHours) throws IOException {

		boolean firstRun = true;
		JsonObject dataObject = null;

		// get data for all requested days
		Calendar calendar = Calendar.getInstance();
		Date current = start;
		while (current.before(end)) {
			calendar.setTime(current);
			calendar.add(Calendar.DATE, 1);
			Date tempEnd = calendar.getTime();

			// get data for a single day from weatherbit.io
			StringBuilder url = new StringBuilder();
			url.append(API_URL).append("history/hourly?").append("lat=").append(latitude).append("&lon=")
					.append(longitude).append("&key=").append(this.api_key);

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = dateFormat.format(current);
			String endDate = dateFormat.format(tempEnd);

			if (considerHours) {
				dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH");
				startDate = dateFormat.format(current);
				endDate = dateFormat.format(tempEnd);
			}

			url.append("&start_date=").append(startDate);
			url.append("&end_date=").append(endDate);

			JsonObject dayObject = getJson(url.toString());

			// merge all days into a single JSONArray
			if (firstRun) {
				dataObject = dayObject;
				firstRun = false;
			} else {
				JsonArray dayArray = dayObject.getAsJsonArray("data");
				JsonArray dataArray = dataObject.getAsJsonArray("data");

				dataArray.addAll(dayArray);
			}

			current = tempEnd;
		}

		return dataObject;
	}

	/**
	 * Get hourly forecast data from weatherbit.io
	 *
	 * @param location    for which data should be collected
	 * @param hours       to forecast
	 * @param measurement to parse
	 * @return LinkedHashMap with parsed measurement
	 * @throws IOException in case of problems when reading data from resource
	 */
	public LinkedHashMap<Date, Double> getParsedHourlyForecastData(Locations location, int hours,
			WeatherParams measurement) throws IOException, ParseException {

		return getParsedHourlyForecastData(location.getLatitude(), location.getLongitude(), hours, measurement);
	}

	/**
	 * Get hourly forecast data from weatherbit.io
	 *
	 * @param latitude    of location
	 * @param longitude   of location
	 * @param hours       to forecast
	 * @param measurement to parse
	 * @return LinkedHashMap with parsed measurement
	 * @throws IOException in case of problems when reading data from resource
	 */
	private LinkedHashMap<Date, Double> getParsedHourlyForecastData(double latitude, double longitude, int hours,
			WeatherParams measurement) throws IOException, ParseException {

		return parseMeasurements(getHourlyForecastData(latitude, longitude, hours), measurement.toString());
	}

	/**
	 * Get hourly historical data from weatherbit.io
	 *
	 * @param location      for which data should be collected
	 * @param start         date to read data
	 * @param end           date to read data
	 * @param considerHours of start and end date
	 * @param measurement   to parse
	 * @return LinkedHashMap with parsed measurement
	 * @throws IOException in case of problems when reading data from resource
	 */
	public LinkedHashMap<Date, Double> getParsedHourlyHistoricalData(Locations location, Date start, Date end,
			boolean considerHours, WeatherParams measurement) throws IOException, ParseException {

		return getParsedHourlyHistoricalData(location.getLatitude(), location.getLongitude(), start, end, considerHours,
				measurement);
	}

	/**
	 * Get hourly historical data from weatherbit.io
	 *
	 * @param latitude      of location
	 * @param longitude     of location
	 * @param start         date to read data
	 * @param end           date to read data
	 * @param considerHours of start and end date
	 * @param measurement   to parse
	 * @return LinkedHashMap with parsed measurement
	 * @throws IOException in case of problems when reading data from resource
	 */
	private LinkedHashMap<Date, Double> getParsedHourlyHistoricalData(double latitude, double longitude, Date start,
			Date end, boolean considerHours, WeatherParams measurement) throws IOException, ParseException {

		return parseMeasurements(getHourlyHistoricalData(latitude, longitude, start, end, considerHours),
				measurement.toString());
	}

	/**
	 * Parse the given measurement.
	 *
	 * @param jsonObject  containing data from api
	 * @param measurement to parse
	 * @return LinkedHashMap with parsed measurement
	 */
	private LinkedHashMap<Date, Double> parseMeasurements(JsonObject jsonObject, String measurement)
			throws ParseException {
		LinkedHashMap<Date, Double> map = new LinkedHashMap<>();

		if (jsonObject == null) {
			return map;
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		JsonArray jsonArray = jsonObject.getAsJsonArray("data");
		for (JsonElement element : jsonArray) {
			JsonObject jsonElement = (JsonObject) element;
			String time = jsonElement.get("timestamp_local").getAsString();
			double value = jsonElement.get(measurement).getAsDouble();
			map.put(dateFormat.parse(time), value);
		}

		return map;
	}

	/**
	 * Retrieve a JSONObject from the given url.
	 *
	 * @param resource to load data from
	 * @return data from given url
	 * @throws IOException              in case of problems when reading data from
	 *                                  resource
	 * @throws IllegalArgumentException if invalid arguments are passed to method
	 */
	private JsonObject getJson(String resource) throws IllegalArgumentException, IOException {

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(resource).build();
		Response response = client.newCall(request).execute();

		if (!response.isSuccessful()) {
			String errorMessage = response.message();
			throw new IllegalArgumentException(errorMessage);
		}

		String jsonData = response.body().string();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = (JsonObject) parser.parse(jsonData);

		return jsonObject;
	}

}
