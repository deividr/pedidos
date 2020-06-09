package br.com.labuonapasta.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.labuonapasta.banco.ItemPedidoDao;
import br.com.labuonapasta.modelo.ProdutoQuantidade;

@Named
@ViewScoped
public class HomeView implements Serializable {

	private static final long serialVersionUID = -8971193519735531075L;

	private List<ProdutoQuantidade> produtosQuantidades;
	private List<ProdutoQuantidade> filteredProdutos;
	private Date dataInicio;
	private Date dataFim;

	private ItemPedidoDao itemPedidoDao;

	@Inject
	public HomeView(ItemPedidoDao itemPedidoDao) {
		this.dataInicio = new Date();

		LocalDate dtHoje = dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		LocalDate dtSomada = dtHoje.plusDays(7 - dtHoje.getDayOfWeek().getValue());

		this.dataFim = Date.from(dtSomada.atStartOfDay(ZoneId.systemDefault()).toInstant());

		this.itemPedidoDao = itemPedidoDao;
	}

	@PostConstruct
	public void init() {
		produtosQuantidades = itemPedidoDao.listarProdutoQuantidade(dataInicio, dataFim);
	}

	public List<ProdutoQuantidade> getProdutosQuantidades() {
		return produtosQuantidades;
	}

	public void setProdutosQuantidades(List<ProdutoQuantidade> produtosQuantidades) {
		this.produtosQuantidades = produtosQuantidades;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<ProdutoQuantidade> getFilteredProdutos() {
		return filteredProdutos;
	}

	public void setFilteredProdutos(List<ProdutoQuantidade> filteredProdutos) {
		this.filteredProdutos = filteredProdutos;
	}
}
