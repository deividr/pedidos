package br.com.labuonapasta.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Classe utilizada para filtros de pesquisa de pedidos.
 */
public class PedidoFilter implements Serializable {

	private static final long serialVersionUID = -8130616305944958649L;

	private Date dataInicial;
	private Date dataFinal;
	private String geladeira;
	private Produto produto;

	public PedidoFilter() {
		this.dataInicial = new Date();

		LocalDate dtHoje = dataInicial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		LocalDate dtSomada = dtHoje.plusDays(7 - dtHoje.getDayOfWeek().getValue());

		this.dataFinal = Date.from(dtSomada.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getGeladeira() {
		return geladeira;
	}

	public void setGeladeira(String geladeira) {
		this.geladeira = geladeira;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
