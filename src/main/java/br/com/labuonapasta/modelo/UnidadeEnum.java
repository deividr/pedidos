package br.com.labuonapasta.modelo;

public enum UnidadeEnum {

    UNIDADE("Unidade", "UN"), KILOGRAMA("Kilograma", "KG"), LITROS("Litros", "LT");

    private final String descricao;
    private final String codigo;

    UnidadeEnum(String descricao, String codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public static UnidadeEnum parse(String codigo) {
        for (UnidadeEnum prodEnum : UnidadeEnum.values()) {
            if (prodEnum.getCodigo().equals(codigo)) {
                return prodEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.descricao;
    }

}
