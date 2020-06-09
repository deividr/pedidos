package br.com.labuonapasta.util;

import br.com.daruma.jna.DUAL;
import br.com.labuonapasta.excessao.ErroImpressaoException;

public class EfetuarImpressaoDaruma {

	static {
		try {
			System.loadLibrary("DarumaFramework");
			System.out.println("Biblioteca carregada!");
		} catch (UnsatisfiedLinkError err) {
			System.out.println("Biblioteca já carregada!");
		}
	}

	public void imprimir(String buffer) {

		int iRetorno = DUAL.iImprimirTexto(buffer.toString(), buffer.length());

		if (iRetorno != 1) {
			throw new ErroImpressaoException(
					"Erro na impressão - Cod. " + iRetorno + ": verifique a impressora e tente novamente.");
		}
	}
	
	public void finalize() {
		System.out.println("Coletor de lixo limpou EfetuarImpressaoDaruma!!!");
	}
}
