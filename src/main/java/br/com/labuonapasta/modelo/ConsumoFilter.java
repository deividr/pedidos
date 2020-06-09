package br.com.labuonapasta.modelo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilizada para filtros de pesquisa de pedidos.
 */
public class ConsumoFilter implements Serializable {

    private static final long serialVersionUID = -8416218150717019515L;

    private Date dataInicial;
    private Date dataFinal;
    private Funcionario funcionario;

    public ConsumoFilter() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        this.dataInicial = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        this.dataFinal = calendar.getTime();
        this.funcionario = new Funcionario();
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
