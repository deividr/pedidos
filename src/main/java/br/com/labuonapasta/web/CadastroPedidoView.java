package br.com.labuonapasta.web;

import br.com.labuonapasta.banco.ClienteDao;
import br.com.labuonapasta.banco.PedidoDao;
import br.com.labuonapasta.banco.ProdutoDao;
import br.com.labuonapasta.excessao.CepInvalidoException;
import br.com.labuonapasta.excessao.EnderecoInvalidoException;
import br.com.labuonapasta.excessao.ErroImpressaoException;
import br.com.labuonapasta.excessao.NegocioException;
import br.com.labuonapasta.modelo.*;
import br.com.labuonapasta.service.ClienteService;
import br.com.labuonapasta.service.EnderecoService;
import br.com.labuonapasta.service.PedidoService;
import br.com.labuonapasta.util.ConsultarCep;
import br.com.labuonapasta.util.ConsultarDistancia;
import br.com.labuonapasta.util.EfetuarImpressaoDaruma;
import br.com.labuonapasta.util.SpringUtil;
import org.omnifaces.util.Messages;
import org.primefaces.mobile.event.SwipeEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Named
@ViewScoped
public class CadastroPedidoView implements Serializable {

	private static final long serialVersionUID = 5713810841734533660L;

	private Pedido pedido;
	private ItemPedido novoItemPedido;
	private boolean massa;
	private int numeroPedido;
	private PedidoDao pedidoDao;
	private List<Produto> molhos;
	private List<Produto> produtos;
	private Map<String, List<Produto>> letrasProdutos;
	private List<String> letras;
	private List<ProdutoEnum> tiposProdutos;
	private PedidoService pedidoService;
	private ClienteDao clienteDao;
	private ClienteService clienteService;
	private EnderecoService enderecoService;
	private ProdutoDao produtoDao;
	private FacesContext facesContext;
	private ConsultarDistancia consultarDistancia;
	private String distancia;
	private boolean imprimir;

	private final ExternalContext externalContext;

	@Inject
	public CadastroPedidoView(ExternalContext externalContext, PedidoService pedidoService, PedidoDao pedidoDao,
			ClienteDao clienteDao, ProdutoDao produtoDao, ClienteService clienteService, EnderecoService enderecoService, FacesContext facesContext, ConsultarDistancia consultarDistancia) {
		this.externalContext = externalContext;
		this.pedidoService = pedidoService;
		this.pedidoDao = pedidoDao;
		this.clienteDao = clienteDao;
		this.clienteService = clienteService;
		this.enderecoService = enderecoService;
		this.produtoDao = produtoDao;
		this.facesContext = facesContext;
		this.consultarDistancia = consultarDistancia;
		this.distancia = "";
	}

	@PostConstruct
	public void init() {
		tiposProdutos = Arrays.asList(ProdutoEnum.values());

		String pedId = externalContext.getRequestParameterMap().get("pedido");

		if (Objects.nonNull(pedId)) {
			pedido = pedidoDao.getById(Integer.parseInt(pedId));
		} else {
			pedido = new Pedido();
		}

		novoItemPedido = new ItemPedido();
		molhos = produtoDao.selecionarPorTipo(ProdutoEnum.MOLHO);
		imprimir = true;
	}

	public void incluir() {
		incluirCliente();

		try {
			pedido.setUsuario(SpringUtil.getUsuarioLogado());
			pedidoService.inserir(pedido, isImprimir());
			setNumeroPedido(pedido.getNumeroPedido());

			imprimirCupom();

			Messages.addGlobalInfo("Pedido " + getNumeroPedido() + " incluído com sucesso");
			pedido = new Pedido();
		} catch (NegocioException ex) {
			Messages.addGlobalError(ex.getMessage());
			facesContext.validationFailed();
		}
	}

	private void incluirCliente() {
		if (pedido.getCliente().getClieId() == 0) {
			clienteService.inserir(pedido.getCliente());
		} else {
			clienteService.alterar(pedido.getCliente());
		}
	}

	public String alterar() {
		String redirect = "";

		incluirCliente();

		try {
			pedido.setUsuario(SpringUtil.getUsuarioLogado());
			pedidoService.alterar(pedido, isImprimir());
			setNumeroPedido(pedido.getNumeroPedido());
			externalContext.getFlash().setKeepMessages(true);

			imprimirCupom();

			Messages.addGlobalInfo("Pedido alterado com sucesso");
			redirect = "/pedidos/DetalharPedido?faces-redirect=true&pedido=" + this.pedido.getPedId();
		} catch (NegocioException ex) {
			Messages.addGlobalError(ex.getMessage());
			facesContext.validationFailed();
		}

		return redirect;
	}

	public String alterarMobile() {
		alterar();
		return "m_detalharpedido?faces-redirect=true&pedido=" + this.pedido.getPedId();
	}

	public void incluirItem() {
		pedido.addItem(novoItemPedido);
		novoItemPedido = new ItemPedido();
		massa = false;
	}

	public void onRowSwipeRight(SwipeEvent event) {
		ItemPedido item = ((ItemPedido) event.getData());
		pedido.removeItem(pedido.getItens().indexOf(item));
	}

	public void obterCliente() {
		Cliente cliente;

		try {
			cliente = clienteDao.procurarPorTelefone(pedido.getCliente().getTelefone1());
		} catch (NoResultException ex) {
			cliente = new Cliente();
		}

		Endereco endereco = cliente.getEnderecos().get(0);

		pedido.setCliente(cliente);

		if (Objects.nonNull(endereco.getCep()) && Objects.isNull(endereco.getDistancia())) {
			obterDistancia();
		}
	}

	public void obterCep() {
		Endereco endereco = pedido.getCliente().getEnderecos().get(0);
		try {
			Endereco enderecoConsultado = ConsultarCep.consultarCep(endereco.getCep());

			endereco.setCep(enderecoConsultado.getCep());
			endereco.setLogradouro(converterUTF8(enderecoConsultado.getLogradouro()));
			endereco.setNumero(0);
			endereco.setBairro(converterUTF8(enderecoConsultado.getBairro()));
			endereco.setLocalidade(converterUTF8(enderecoConsultado.getLocalidade()));
			endereco.setUf(enderecoConsultado.getUf().toUpperCase());
			endereco.setComplemento(converterUTF8(enderecoConsultado.getComplemento()));
			endereco.setDistancia(null);
		} catch (CepInvalidoException e) {
			limparEndereco(endereco);
			Messages.addGlobalError(e.getMessage());
		} catch (Exception e) {
			limparEndereco(endereco);
			Messages.addGlobalError("Erro ao obter o CEP, inserir as informações manualmente.");
		}
	}
	
	public void limparEndereco(Endereco endereco) {
		endereco.setLogradouro(null);
		endereco.setNumero(0);
		endereco.setBairro(null);
		endereco.setLocalidade(null);
		endereco.setUf(null);
		endereco.setComplemento(null);
		endereco.setDistancia(null);		
	}

	public void obterDistancia() {
		Endereco endereco = pedido.getCliente().getEnderecos().get(0);

		StringBuilder enderecoCompleto = new StringBuilder(endereco.getLogradouro());

		enderecoCompleto.append(", ").append(endereco.getNumero());
		enderecoCompleto.append(" - ").append(endereco.getBairro());
		enderecoCompleto.append(", ").append(endereco.getLocalidade());
		enderecoCompleto.append(" - ").append(endereco.getUf());

		try {
			endereco.setDistancia(consultarDistancia.consultarDistanciaGoogle(enderecoCompleto.toString()));
		} catch (EnderecoInvalidoException e) {			
			Messages.addGlobalError(e.getMessage());
		}	catch (Exception e) {
			Messages.addGlobalError("Erro ao obter a distância do endereço.");
		}
	}

	private String converterUTF8(String text) throws UnsupportedEncodingException {
		byte[] ptext = text.getBytes("ISO-8859-1");
		return new String(ptext, "UTF-8").toUpperCase();
	}

	private void imprimirCupom() {

		// Não imprimi o cupom se o usuário optou por não faze-lo.
		if (!isImprimir()) {
			return;
		}

		StringBuilder strTexto = new StringBuilder("<e><b><ce>La Buonapasta</ce></b></e>\n");

		strTexto.append("------------------------------------------------\n")
				.append("<b>Data:</b> <dt></dt>  <b>Hora:</b> <hr></hr>\n").append("<b>Cliente:</b> ")
				.append(pedido.getCliente().getNome()).append("\n").append("<b>Telefone:</b> ")
				.append(pedido.getCliente().getTelefone1().replaceAll("([0-9]{2})([0-9]{4,5})([0-9]{4})", "($1)$2-$3"))
				.append("\n");

		Endereco endereco = pedido.getCliente().getEnderecos().get(0);

		if (Objects.nonNull(endereco.getCep())) {
			strTexto.append("<b>Endereco:</b> ").append(endereco.getLogradouro()).append(", ").append(endereco.getNumero())
					.append(" - ").append(endereco.getBairro()).append("\n").append("<b>Cidade:</b> ")
					.append(endereco.getLocalidade()).append(" - ").append(endereco.getUf()).append("\n")
					.append("<b>Complemento:</b> ").append(endereco.getComplemento()).append("\n")
					.append("<b>Distância:</b> ").append(endereco.getDistancia())
					.append(" ").append("<b>Frete:</b> ").append(getValorFrete()).append("\n");
		}

		strTexto.append("------------------------------------------------\n").append("<da><e><ce><b>")
				.append(pedido.getNumeroPedido()).append("</b></ce></e></da>\n");

		String diaSemana = "";

		LocalDate localDate;

		try {
			localDate = pedido.getDataRetirada().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (UnsupportedOperationException ex) {
			Date date = new Date(pedido.getDataRetirada().getTime());
			localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}

		switch (localDate.getDayOfWeek()) {
		case MONDAY:
			diaSemana = "SEG";
			break;
		case TUESDAY:
			diaSemana = "TER";
			break;
		case WEDNESDAY:
			diaSemana = "QUA";
			break;
		case THURSDAY:
			diaSemana = "QUI";
			break;
		case FRIDAY:
			diaSemana = "SEX";
			break;
		case SATURDAY:
			diaSemana = "SAB";
			break;
		case SUNDAY:
			diaSemana = "DOM";
			break;
		default:
			break;
		}

		strTexto.append("<da><e><b>RETIRA:</b> ").append(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.append(String.format("(%s)", diaSemana)).append("</da></e>\n");

		if (!pedido.getGeladeira().equals("")) {
			strTexto.append("<b>GELADEIRA.:</b> ").append(pedido.getGeladeira()).append("\n");
		}

		if (!pedido.getHoraDe().equals("")) {
			strTexto.append("<b>HORARIO...:</b> ").append(pedido.getHoraDe().replaceAll("[0-9]{2}[0-9]{2}", "$1:$2"))
					.append(" as ").append(pedido.getHoraAte().replaceAll("[0-9]{2}[0-9]{2}", "$1:$2")).append("\n");
		}

		strTexto.append("------------------------------------------------\n")
				.append("<c>Item    Qtde.     Descricao                          Valor</c>\n")
				.append("<c>----------------------------------------------------------</c>\n");
		// III QQQQQQQ UU DDDDDDDDDDDDDDDDDDDDDDDDDDDDDD 9.999.99

		int nroItem = 1;

		for (ItemPedido item : pedido.getItens()) {
			String massaMaisMolho = String.format("%s %s", item.getProduto().getNome(),
					item.getMolho() != null ? item.getMolho().getNome() : "");

			String strFormat = String.format(" %3s  %7s %s  %-30s", nroItem, item.getQtde().toString(),
					item.getProduto().getUnidade().getCodigo().toLowerCase(), massaMaisMolho);

			strTexto.append("<c>" + strFormat + "</c>").append("\n");

			nroItem++;
		}

		strTexto.append("<c>                  ----------------------------------------</c>\n");

		// Se existir observações imprime:
		if (!pedido.getObservacao().equals("")) {
			strTexto.append("<l></l>").append("<b>OBSERVACOES:</b> ").append(pedido.getObservacao());
		}

		strTexto.append("<sl>2</sl>").append("<ce><ean13>").append(String.format("%09d", pedido.getNumeroPedido()))
				.append("000").append("</ean13></ce>").append("<sl>3</sl>").append("<gui></gui>");

		EfetuarImpressaoDaruma impressao = new EfetuarImpressaoDaruma();

		try {
			impressao.imprimir(strTexto.toString());
		} catch (ErroImpressaoException e) {
			Messages.addGlobalError(e.getMessage());
		} finally {
			impressao = null;
			System.gc(); 
		}

	}

	public List<Produto> obterProdutos(String queryNome) {
		return produtoDao.selecionarPorNome(queryNome.toUpperCase());
	}

	public String obterProdutos(ProdutoEnum tipoProduto) {
		this.produtos = produtoDao.selecionarPorTipo(tipoProduto);
		this.letrasProdutos = new HashMap<>();
		this.letras = new ArrayList<>();

		produtos.forEach(produto -> {
			String letra = produto.getNome().substring(0, 1);

			if (this.letrasProdutos.containsKey(letra)) {
				this.letrasProdutos.get(letra).add(produto);
			} else {
				List<Produto> prods = new ArrayList<>();
				prods.add(produto);
				this.letrasProdutos.put(letra, prods);
			}
		});

		this.letras = Arrays.asList(letrasProdutos.keySet().toArray(new String[letrasProdutos.size()]));
		Collections.sort(letras);

		return "pm:novo_item_letra?transition=flip&pedido=" + pedido.getPedId();
	}

	public String obterProdutosPorLetra(String letra) {
		this.produtos = letrasProdutos.get(letra);
		return "pm:novo_item_produto?transition=flip&pedido=" + pedido.getPedId();
	}

	public List<String> getLetras() {
		return this.letras;
	}

	public void verificarMassa() {
		massa = novoItemPedido.getProduto().getTipo().equals(ProdutoEnum.MASSA);
	}

	public Pedido getPedido() {
		return pedido;
	}

	public List<Produto> getMolhos() {
		return molhos;
	}

	public List<ProdutoEnum> getTiposProdutos() {
		return tiposProdutos;
	}

	public void setTiposProdutos(List<ProdutoEnum> tiposProdutos) {
		this.tiposProdutos = tiposProdutos;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public ItemPedido getNovoItemPedido() {
		return novoItemPedido;
	}

	public boolean isMassa() {
		return massa;
	}

	public void setMassa(boolean massa) {
		this.massa = massa;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public boolean isImprimir() {
		return imprimir;
	}

	public void setImprimir(boolean imprimir) {
		this.imprimir = imprimir;
	}

	public String getDistancia() {
		return this.distancia;
	}

	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}
	
	public BigDecimal getValorFrete() {
		return this.enderecoService.calcularFrete(pedido.getCliente().getEnderecos().get(0));
	}
}
