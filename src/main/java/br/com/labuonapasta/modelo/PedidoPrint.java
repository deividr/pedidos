package br.com.labuonapasta.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pedido_print")
public class PedidoPrint implements Serializable {

    private static final long serialVersionUID = 708148821103220625L;

    @Id
    @OneToOne
    @JoinColumn(name = "cd_pedido")
    private Pedido pedido;

    public PedidoPrint() {
        pedido = new Pedido();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.pedido.getPedId());
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

        final PedidoPrint other = (PedidoPrint) obj;

        return Objects.equals(this.pedido.getPedId(), other.pedido.getPedId());

    }

}
