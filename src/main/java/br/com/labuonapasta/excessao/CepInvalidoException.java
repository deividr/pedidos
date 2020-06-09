package br.com.labuonapasta.excessao;

public class CepInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CepInvalidoException(String mensagem) {
        super(mensagem);
    }

}
