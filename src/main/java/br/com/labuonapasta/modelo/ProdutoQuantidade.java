package br.com.labuonapasta.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProdutoQuantidade implements Serializable {

	private static final long serialVersionUID = -8952022399199542395L;

	private Produto produto;
	private BigDecimal quantidade;
	
	public ProdutoQuantidade(Produto produto, BigDecimal quantidade) {
		this.produto = produto;
		this.quantidade = quantidade;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public BigDecimal getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProdutoQuantidade that = (ProdutoQuantidade) o;

		return produto != null ? produto.equals(that.produto) : that.produto == null;
	}

	@Override
	public int hashCode() {
		return produto != null ? produto.hashCode() : 0;
	}

}
