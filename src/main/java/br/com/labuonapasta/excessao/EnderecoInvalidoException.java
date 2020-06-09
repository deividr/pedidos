package br.com.labuonapasta.excessao;

public class EnderecoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EnderecoInvalidoException(String mensagem) {
        super(mensagem);
    }

}
