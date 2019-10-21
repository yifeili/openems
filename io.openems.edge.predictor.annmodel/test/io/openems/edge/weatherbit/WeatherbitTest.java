package io.openems.edge.weatherbit;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import org.junit.Test;

import com.google.gson.JsonObject;

import io.openems.edge.locations.Locations;
import okhttp3.OkHttpClient;

/**
 * 
 * @author Jan Seidemann
 *
 */
public class WeatherbitTest {

	@Test
	public void testHttpRequest() {

		/* TODO Throws exception */
		OkHttpClient client = new OkHttpClient();

	}

	@Test
	public void testGetHourlyForecastData() throws IOException {
		Weatherbit api = Weatherbit.getInstance();

		JsonObject jsonObject = api.getHourlyForecastData(Locations.FEMS450, 48);

		System.out.println(jsonObject.toString());
	}

	@Test
	public void testGetParsedHourlyForecastData() throws IOException, ParseException {
		Weatherbit api = Weatherbit.getInstance();

		LinkedHashMap<Date, Double> result = api.getParsedHourlyForecastData(Locations.FEMS450, 48, WeatherParams.ghi);

		System.out.println(result.entrySet().toString());
	}

	@Test
	public void testGetHourlyHistoricalData() throws IOException {
		Weatherbit api = Weatherbit.getInstance();

		JsonObject jsonObject = api.getHourlyHistoricalData(Locations.FEMS450,
				new GregorianCalendar(2019, Calendar.JULY, 31, 12, 0).getTime(),
				new GregorianCalendar(2019, Calendar.AUGUST, 2).getTime(), true);

		System.out.println(jsonObject.toString());
	}

	@Test
	public void testGetParsedHourlyHistoricalData() throws IOException, ParseException {
		Weatherbit api = Weatherbit.getInstance();

		LinkedHashMap<Date, Double> result = api.getParsedHourlyHistoricalData(Locations.FEMS450,
				new GregorianCalendar(2019, Calendar.AUGUST, 1, 12, 0).getTime(),
				new GregorianCalendar(2019, Calendar.AUGUST, 2).getTime(), true, WeatherParams.ghi);

		System.out.println(result.entrySet().toString());
	}

}
