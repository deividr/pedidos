package br.com.labuonapasta.excessao;

public class SenhaInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }

}
