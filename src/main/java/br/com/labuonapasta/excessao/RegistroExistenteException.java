package br.com.labuonapasta.excessao;

public class RegistroExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistroExistenteException(String mensagem) {
        super(mensagem);
    }
}
