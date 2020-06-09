package br.com.labuonapasta;

import java.io.IOException;
import javax.xml.bind.JAXBException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import org.junit.Test;

public class TesteGoogleApi {

	@Test
	public void main() throws IOException, JAXBException {
		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyDIa-0lzOBxDH0Qg_bkNn5llVSg4LdgJ4E").build();

		String[] origins = new String[1];
		origins[0] = "Avenida Santo Antônio, 2504 - Vila Osasco, Osasco - SP";

		String[] destinations = new String[1];
		destinations[0] = "asdfasdfa asdf asdf asdf asdfasdf, 100 - asdfasdf asdf , asdf asdf  - asdfasdf";

		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context);

		request = request.origins(origins).destinations(destinations);

		try {
			DistanceMatrix result = request.await();			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(gson.toJson(result));
			System.out.println(result.rows[0].elements[0]);
			context.shutdown();			
		} catch (InterruptedException | ApiException e) {
			e.printStackTrace();
		}

	}
}
