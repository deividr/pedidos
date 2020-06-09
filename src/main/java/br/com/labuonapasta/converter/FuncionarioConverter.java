package br.com.labuonapasta.converter;

import br.com.labuonapasta.banco.FuncionarioDao;
import br.com.labuonapasta.modelo.Funcionario;
import br.com.labuonapasta.service.CDIServiceLocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Objects;

@FacesConverter(value = "funcionarioConverter", forClass = Funcionario.class)
public class FuncionarioConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        FuncionarioDao funcionarioDao = CDIServiceLocator.getBean(FuncionarioDao.class);

        Funcionario funcionario;
        try {
            funcionario = funcionarioDao.getById(Long.parseLong(s));
        } catch (NumberFormatException ex) {
            funcionario = null;
        }

        return funcionario;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        Funcionario funcionario = (Funcionario) o;
        if (Objects.nonNull(funcionario) && funcionario.getCpf() != 0) {
            return funcionario.getCpf().toString();
        } else{
            return "";
        }
    }
}
