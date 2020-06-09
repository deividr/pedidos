package br.com.labuonapasta.util;

import java.io.Serializable;

import javax.inject.Inject;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;

import br.com.labuonapasta.excessao.EnderecoInvalidoException;

public class ConsultarDistancia implements Serializable {
	
	private static final long serialVersionUID = 9030985141070717652L;
	
	@Inject
	private ConfigProperties configProperties;
	
	private String ORIGIN = "Avenida Santo Antônio, 2504 - Vila Osasco, Osasco - SP";

	public String consultarDistanciaGoogle(String enderecoDestino)
			throws Exception {
		String apiKey = configProperties.getValuePropertie("google_geoapi_apiKey");
		
		GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

		String[] origins = new String[1];
		origins[0] = ORIGIN;
 
		String[] destinations = new String[1];
		destinations[0] = enderecoDestino;

		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context);

		request = request.origins(origins).destinations(destinations);

		DistanceMatrix result = request.await();

		context.shutdown();

		if (result.rows[0].elements[0].status != DistanceMatrixElementStatus.OK) {
			throw new EnderecoInvalidoException("Endereço informado está inválido, não foi possível calcular a distância.");
		}

		return result.rows[0].elements[0].distance.toString();
	}
}
