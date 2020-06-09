package br.com.labuonapasta.modelo;

public enum ProdutoEnum {
    
    //Não alterar a ordem dos produtos, pois senão haverá inconsistências na tabela.

    MASSA("Massa", 1), MOLHO("Molho", 2), BEBIDA("Bebida", 3), SALADA("Salada", 4),
    CARNE("Carne", 5), DIVERSOS("Diversos", 99);

    private final String descricao;
    private final int codigo;

    ProdutoEnum(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public static ProdutoEnum parse(int codigo) {
        for (ProdutoEnum prodEnum : ProdutoEnum.values()) {
            if (prodEnum.getCodigo() == codigo) {
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
