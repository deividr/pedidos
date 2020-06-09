package br.com.labuonapasta.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import br.com.labuonapasta.modelo.Endereco;

public class EnderecoService implements Serializable {

	private static final long serialVersionUID = 2537331674969774513L;
	
	final BigDecimal FRETE_DEFAULT = new BigDecimal("6.0");
	final BigDecimal DISTANCE_DEFAULT = new BigDecimal("4.0");
	final BigDecimal EXCESS_VALUE = new BigDecimal("2.0");

	public BigDecimal calcularFrete(Endereco endereco) {
		
		if (Objects.isNull(endereco.getDistancia())) {
			return BigDecimal.ZERO;			
		}
		
		String justNumber = endereco.getDistancia().replaceAll("[a-zA-Z\\s]+", "");

		BigDecimal distance = new BigDecimal(justNumber).add(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_EVEN);

		if (distance.compareTo(DISTANCE_DEFAULT) == 1) {
			return distance.subtract(DISTANCE_DEFAULT).multiply(EXCESS_VALUE).add(FRETE_DEFAULT);
		} else {
			return FRETE_DEFAULT;
		}
	}
}
