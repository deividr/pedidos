package br.com.labuonapasta;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class TesteSemBancoDeDados {

	@Test
	public void teste() {
		BigDecimal frete = new BigDecimal("6.0");
		BigDecimal distanceDefault = new BigDecimal("4.0");
		BigDecimal excessValue = new BigDecimal("2.0");

		String justNumber = "5.1 km".replaceAll("[a-zA-Z\\s]+", "");

		BigDecimal distance = new BigDecimal(justNumber).add(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_EVEN);

		if (distance.compareTo(distanceDefault) == 1) {
			frete = distance.subtract(distanceDefault).multiply(excessValue).add(frete);
		}

		System.out.println(frete);
	}
}
