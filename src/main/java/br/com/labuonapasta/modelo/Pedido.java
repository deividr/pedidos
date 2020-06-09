package br.com.labuonapasta.modelo;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 708148821103220625L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_pedido")
    private int pedId;

    @Min(value = 1)
    @Column(name = "nr_pedido")
    private int numeroPedido;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "cd_cliente", foreignKey = @ForeignKey(name = "fk_pedido_cliente"))
    private Cliente cliente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_usuario", foreignKey = @ForeignKey(name = "fk_pedido_usuario1"))
    private Usuario usuario;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_pedido")
    private Date dataPedido;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_retirada")
    private Date dataRetirada;

    @Column(name = "nr_geladeira")
    private String geladeira;

    @Column(name = "hr_de")
    private String horaDe;

    @Column(name = "hr_ate")
    private String horaAte;

    @NotNull
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    @Column(name = "ds_observacao")
    private String observacao;

    @NotNull
    @Column(name = "st_retirado")
    private boolean retirado;

    public Pedido() {
        cliente = new Cliente();
        dataRetirada = new Date();
        dataPedido = new Date();
        itens = new ArrayList<ItemPedido>();
    }
    
    public BigDecimal getValorTotal() {
        BigDecimal valorTotal = new BigDecimal("0.00");
        
        for (ItemPedido item : getItens()) {
        	valorTotal = valorTotal.add(item.getValorTotal());
        }
        
    	return valorTotal; 
    }
    
    public void addItem(ItemPedido item) {
    	getItens().add(item);
    	item.setPedido(this);
    }
    
    public void removeItem(ItemPedido item) {
    	getItens().remove(item);
    	item.setPedido(null);
    }
    
    public void removeItem(int index) {
    	getItens().get(index).setPedido(null);
    	getItens().remove(index);
    }

    public Integer getPedId() {
        return pedId;
    }

    public void setPedId(int pedId) {
        this.pedId = pedId;
    }

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Integer numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public Date getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(Date dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getGeladeira() {
        return geladeira;
    }

    public void setGeladeira(String geladeira) {
        this.geladeira = geladeira;
    }

    public String getHoraDe() {
        return horaDe;
    }

    public void setHoraDe(String horaDe) {
        this.horaDe = horaDe;
    }

    public String getHoraAte() {
        return horaAte;
    }

    public void setHoraAte(String horaAte) {
        this.horaAte = horaAte;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao.toUpperCase();
    }

    public Boolean getRetirado() {
        return retirado;
    }

    public void setRetirado(Boolean retirado) {
        this.retirado = retirado;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.pedId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Pedido other = (Pedido) obj;

        return Objects.equals(this.pedId, other.pedId);

    }

}
