package io.openems.edge.weatherbit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.LinkedHashMap;

public class WeatherBitAPI {

	// API key from Jan
	private static final String API_KEY = "";
	private static final int HOURS = 48;

	// Location: Regenstauf (Regensburg)
	// FEMS 450
	private static final double LAT_REGENSTAUF = 49.123020;
	private static final double LON_REGENSTAUF = 12.127700;

	public static void callWeatherBit() throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("https://api.weatherbit.io/v2.0/forecast/hourly?");
		builder.append("lat=").append(LAT_REGENSTAUF);
		builder.append("&lon=").append(LON_REGENSTAUF);
		builder.append("&key=").append(API_KEY);
		builder.append("&hours=").append(HOURS);

		JsonObject jsonObject = getJson(builder.toString());

		System.out.println(parseGhi(jsonObject).entrySet().toString());
		System.out.println(parseTemperature(jsonObject).entrySet().toString());
		System.out.println(parseClouds(jsonObject).entrySet().toString());
	}
//    public static void main(String[] args) throws IOException {
//        StringBuilder builder = new StringBuilder();
//        builder.append("https://api.weatherbit.io/v2.0/forecast/hourly?");
//        builder.append("lat=").append(LAT_REGENSTAUF);
//        builder.append("&lon=").append(LON_REGENSTAUF);
//        builder.append("&key=").append(API_KEY);
//        builder.append("&hours=").append(HOURS);
//
//        JsonObject jsonObject = getJson(builder.toString());
//
//        System.out.println(parseGhi(jsonObject).entrySet().toString());
//        System.out.println(parseTemperature(jsonObject).entrySet().toString());
//        System.out.println(parseClouds(jsonObject).entrySet().toString());
//    }

	/*
	 * Parses the given measurement
	 */
	private static LinkedHashMap<String, Double> parseMeasurements(JsonObject jsonObject, String measurement) {
		LinkedHashMap<String, Double> map = new LinkedHashMap<String, Double>();

		// adapting to gauva json library
		JsonArray jsonArray = jsonObject.getAsJsonArray("data");// .getJSONArray("data");
		for (JsonElement element : jsonArray) {
			JsonObject jsonElement = (JsonObject) element;
			String time = jsonElement.get("timestamp_local").getAsString();
			double temp = jsonElement.get(measurement).getAsDouble();
			map.put(time, temp);
		}

//        for (int i = 0; i < jsonArray.size()    /*length()*/; ++i) {
//            String time = jsonArray.getAsJsonObject(i) //jsonArray.getJSONObject(i).getString("timestamp_local");
//            double temp = jsonArray.getJSONObject(i).getDouble(measurement);
//
//            map.put(time, temp);
//        }

		return map;
	}

	/*
	 * Parsing the temperatures -> Map<Time, Temperature) Temperature: °C
	 */
	private static LinkedHashMap<String, Double> parseTemperature(JsonObject jsonObject) {
		return parseMeasurements(jsonObject, "temp");
	}

	/*
	 * Parsing the GHI -> Map<Time, GHI> GHI: W / m²
	 */
	private static LinkedHashMap<String, Double> parseGhi(JsonObject jsonObject) {
		return parseMeasurements(jsonObject, "ghi");
	}

	/*
	 * Parsing clouds -> Map<Time, Cloud> Clouds: 0 - 100 (%)
	 */
	private static LinkedHashMap<String, Double> parseClouds(JsonObject jsonObject) {
		return parseMeasurements(jsonObject, "clouds");
	}

	private static JsonObject getJson(String resource) {

		try {

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(resource).build();
			Response response = null;

			response = client.newCall(request).execute();
			{
				if (!response.isSuccessful()) {
					throw new IOException("Unexpected code " + response);
				}
			}

//        HttpClient client = HttpClients.createDefault();
//        HttpGet request = new HttpGet(resource);
//        HttpResponse response = client.execute(request);

			String jsonData = response.body().string();
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(jsonData);

			return jsonObject;
		} catch (IOException e) {
			e.printStackTrace();
			JsonObject jsonObject = null;
			return jsonObject;
		}
	}
}
