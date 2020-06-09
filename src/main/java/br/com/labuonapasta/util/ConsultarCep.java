package br.com.labuonapasta.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.labuonapasta.excessao.CepInvalidoException;
import br.com.labuonapasta.modelo.Endereco;

public class ConsultarCep {

	public static Endereco consultarCep(String cep) throws MalformedURLException, IOException, JAXBException {
		String uri = "https://viacep.com.br/ws/" + cep + "/xml/";

		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		if (con.getResponseCode() != 200) {
			throw new CepInvalidoException("CEP Inválido.");
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

		JAXBContext jaxbContext = JAXBContext.newInstance(Endereco.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

		Endereco enderecoConsultado = (Endereco) jaxbUnmarshaller.unmarshal(br);

		if (Objects.isNull(enderecoConsultado.getCep())) {
			throw new CepInvalidoException("CEP Inválido.");
		}

		return enderecoConsultado;
	}

}
