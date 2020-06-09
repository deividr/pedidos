package br.com.labuonapasta.web;

import br.com.labuonapasta.banco.PedidoDao;
import br.com.labuonapasta.modelo.Pedido;
import br.com.labuonapasta.service.PedidoService;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;

@Named
@ViewScoped
public class DetalhePedidoView implements Serializable {

    private static final long serialVersionUID = -2287171802372039094L;

    private Pedido pedido;
    private BigDecimal valorTotal;

    private final PedidoDao pedidoDao;
    private final PedidoService pedidoService;
    private final FacesContext context;

    @Inject
    public DetalhePedidoView(PedidoService pedidoService, PedidoDao pedidoDao, FacesContext context) {
        this.pedidoService = pedidoService;
        this.pedidoDao = pedidoDao;
        this.context = context;
    }

    @PostConstruct
    public void init() {
        int pedId = Integer.parseInt(context.getExternalContext().getRequestParameterMap().get("pedido"));

        pedido = pedidoDao.getById(pedId);

        valorTotal = pedido.getValorTotal();
    }

    public String excluir() {
        pedidoService.excluir(pedido);
        Messages.addFlashGlobalInfo("Pedido excluído com sucesso");
        return "/pedidos/ListarPedidos?faces-redirect=true";
    }

    public String excluirMobile() {
        pedidoService.excluir(pedido);
        Messages.addFlashGlobalInfo("Pedido excluído com sucesso");
        return "/pedidos/m_listarpedidos.xhtml?faces-redirect=true&pagina=lista";
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}
