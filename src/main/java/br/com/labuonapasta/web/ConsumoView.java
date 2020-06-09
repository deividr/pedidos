package br.com.labuonapasta.web;

import br.com.labuonapasta.banco.ConsumoDao;
import br.com.labuonapasta.banco.FuncionarioDao;
import br.com.labuonapasta.modelo.Consumo;
import br.com.labuonapasta.modelo.ConsumoFilter;
import br.com.labuonapasta.modelo.Funcionario;
import br.com.labuonapasta.service.ConsumoService;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Named
@ViewScoped
public class ConsumoView implements Serializable {

    private static final long serialVersionUID = 1921306431203834109L;

    private List<Consumo> consumos;
    private List<Consumo> filteredConsumos;
    private Consumo novoConsumo;
    private Consumo selectedConsumo;
    private ConsumoFilter consumoFilter;
    private FuncionarioDao funcionarioDao;
    private ConsumoDao consumoDao;
    private HttpSession httpSession;

    private ConsumoService consumoService;

    @Inject
    public ConsumoView(ConsumoService consumoService, FuncionarioDao funcionarioDao, ConsumoDao consumoDao,
            HttpSession httpSession) {
        this.consumoService = consumoService;
        this.funcionarioDao = funcionarioDao;
        this.consumoDao = consumoDao;
        this.httpSession = httpSession;
    }

    @PostConstruct
    public void init() {
        this.consumoFilter = (ConsumoFilter) httpSession.getAttribute("consumoFilter");

        if (Objects.isNull(consumoFilter)) {
            novoFiltro();
        }

        this.novoConsumo = new Consumo();
        this.selectedConsumo = new Consumo();
        filtrarLista();
    }

    public void incluirConsumo() {
        consumos.add(consumoService.inserir(novoConsumo));
        Messages.addInfo("msgGeral", "Consumo incluído com sucesso.");
        novoConsumo = new Consumo();
    }

    public void excluirConsumo() {
        try {
            consumoService.excluir(selectedConsumo);
            consumos.remove(selectedConsumo);
            Messages.addInfo("msgGeral", "Consumo excluído com sucesso.");
        } catch (NoResultException ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public BigDecimal valorTotal() {
        BigDecimal valorTotal = new BigDecimal("0.00");

        for (Consumo consumo : consumos) {
            valorTotal = valorTotal.add(consumo.getValor());
        }

        return valorTotal;
    }

    public void filtrarLista() {
        consumos = consumoDao.filtrarLista(consumoFilter);
    }

    public void limparFiltros() {
        novoFiltro();
        filtrarLista();
    }

    private void novoFiltro() {
        this.consumoFilter = new ConsumoFilter();
        httpSession.setAttribute("consumoFilter", consumoFilter);
    }

    public List<Funcionario> obterFuncionarios(String queryNome) {
        return funcionarioDao.selecionarPorNome(queryNome.toUpperCase());
    }

    public List<Consumo> getConsumos() {
        return consumos;
    }

    public List<Consumo> getFilteredConsumos() {
        return filteredConsumos;
    }

    public void setFilteredConsumos(List<Consumo> filteredConsumos) {
        this.filteredConsumos = filteredConsumos;
    }

    public Consumo getNovoConsumo() {
        return novoConsumo;
    }

    public void setNovoConsumo(Consumo novoConsumo) {
        this.novoConsumo = novoConsumo;
    }

    public Consumo getSelectedConsumo() {
        return selectedConsumo;
    }

    public void setSelectedConsumo(Consumo consumo) {
        this.selectedConsumo = consumo;
    }

    public ConsumoFilter getConsumoFilter() {
        return consumoFilter;
    }

    public void setConsumoFilter(ConsumoFilter consumoFilter) {
        this.consumoFilter = consumoFilter;
    }
}
