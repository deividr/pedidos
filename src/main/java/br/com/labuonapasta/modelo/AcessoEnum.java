package br.com.labuonapasta.modelo;

import java.util.ArrayList;
import java.util.List;

public enum AcessoEnum {

    ADMINISTRADOR("Administrador", 0), CADASTRO("Cadastro", 1), PEDIDO("Pedido", 2);

    private final String descricao;
    private final int tipoInt;

    AcessoEnum(String descricao, int tipoInt) {
        this.descricao = descricao;
        this.tipoInt = tipoInt;
    }

    public String obterDescricao() {
        return this.descricao;
    }

    public int obterTipoInt() {
        return this.tipoInt;
    }

    public static AcessoEnum valueOf(int tipoInt) {
        for (AcessoEnum acesso : AcessoEnum.values()) {
            if (acesso.obterTipoInt() == tipoInt) {
                return acesso;
            }
        }
        return null;
    }

    public static List<String> obterDescricoes() {

        List<String> acessos = new ArrayList<>();

        for (AcessoEnum acesso : AcessoEnum.values()) {
            acessos.add(acesso.obterDescricao());
        }

        return acessos;
    }

    public static List<String> valuesDescricoes() {
        List<String> acessos = new ArrayList<>();

        for (AcessoEnum acesso : AcessoEnum.values()) {
            acessos.add(acesso.obterDescricao());
        }

        return acessos;
    }

}
