package br.com.labuonapasta.modelo;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_produto")
    private int prodId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nm_produto")
    private String nome;

    @NotNull
    @Column(name = "st_unidade")
    private String unidade;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Min(value = 0)
    @Column(name = "vl_produto")
    private BigDecimal valor;

    @NotNull
    @Column(name = "cd_tipo_produto")
    private int tipo;

    @NotNull
    @Column(name = "cd_ativo")
    private Boolean ativo;

    public Integer getProdId() {
        return prodId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase();
    }

    public UnidadeEnum getUnidade() {
        return UnidadeEnum.parse(this.unidade);
    }

    public void setUnidade(UnidadeEnum unidade) {
        this.unidade = unidade.getCodigo();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ProdutoEnum getTipo() {
        return ProdutoEnum.parse(this.tipo);
    }

    public void setTipo(ProdutoEnum tipo) {
        this.tipo = tipo.getCodigo();
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == this.getClass()) {
            Produto prod = (Produto) o;
            if (Objects.equals(prod.getProdId(), this.getProdId())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getNome().hashCode();
    }

}
