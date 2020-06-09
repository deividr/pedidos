package br.com.labuonapasta.web;

import br.com.labuonapasta.banco.PedidoDao;
import br.com.labuonapasta.banco.ProdutoDao;
import br.com.labuonapasta.modelo.Pedido;
import br.com.labuonapasta.modelo.PedidoFilter;
import br.com.labuonapasta.modelo.Produto;
import br.com.labuonapasta.service.PedidoService;
import org.omnifaces.util.Messages;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Named
@ViewScoped
public class PedidoView implements Serializable {

	private static final long serialVersionUID = 7009894208435986702L;

	private List<Pedido> pedidos;
	private List<Pedido> filteredPedidos;
	private Pedido selectedPedido;
	private PedidoFilter pedidoFilter;
	private final ProdutoDao produtoDao;
	private final PedidoDao pedidoDao;
	private final HttpSession httpSession;
	private final PedidoService pedidoService;
	private String imprimir;

	@Inject
	public PedidoView(PedidoService pedidoService, ProdutoDao produtoDao, PedidoDao pedidoDao,
			HttpSession httpSession) {
		this.pedidoService = pedidoService;
		this.produtoDao = produtoDao;
		this.pedidoDao = pedidoDao;
		this.httpSession = httpSession;
	}

	@PostConstruct
	public void init() {
		this.pedidoFilter = (PedidoFilter) httpSession.getAttribute("pedidoFilter");

		if (Objects.isNull(pedidoFilter)) {
			novoFiltro();
		}

		this.selectedPedido = new Pedido();
		filtrarLista();
	}

	public void excluirPedido() {
		try {
			pedidoService.excluir(selectedPedido);
			pedidos.remove(selectedPedido);
			Messages.addGlobalInfo("Pedido excluído com sucesso.");
		} catch (NoResultException ex) {
			Messages.addGlobalError(ex.getMessage());
		} catch (PersistenceException ex) {
			Messages.addGlobalError("Erro ao excluir o pedido.");
		}
	}

	public void onCellEdit(CellEditEvent event) {
		Pedido pedido = pedidos.get(event.getRowIndex());
		pedidoService.alterarSoPedido(pedido);
	}

	public void filtrarLista() {
		pedidos = pedidoDao.filtrarLista(pedidoFilter);
	}

	public String filtrarListaMobile() {
		filtrarLista();
		return "pm:lista?transition=flip";
	}

	public void limparFiltros() {
		novoFiltro();
		filtrarLista();
		setImprimir("Isso é um teste importante");
	}

	public void emitirRelatorio(Object document) {
		pedidoService.emitirRelatorio(document, pedidoDao.filtrarLista(pedidoFilter));
	}

	public void novoFiltro() {
		this.pedidoFilter = new PedidoFilter();
		httpSession.setAttribute("pedidoFilter", pedidoFilter);
	}

	public List<Produto> obterProdutos(String queryNome) {
		return produtoDao.selecionarPorNome(queryNome.toUpperCase());
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public List<Pedido> getFilteredPedidos() {
		return filteredPedidos;
	}

	public void setFilteredPedidos(List<Pedido> filteredPedidos) {
		this.filteredPedidos = filteredPedidos;
	}

	public Pedido getSelectedPedido() {
		return selectedPedido;
	}

	public void setSelectedPedido(Pedido pedido) {
		this.selectedPedido = pedido;
	}

	public PedidoFilter getPedidoFilter() {
		return pedidoFilter;
	}

	public void setPedidoFilter(PedidoFilter pedidoFilter) {
		this.pedidoFilter = pedidoFilter;
	}

	public String getImprimir() {
		return imprimir;
	}

	public void setImprimir(String imprimir) {
		this.imprimir = imprimir;
	}
}
