package br.com.labuonapasta.converter;

import br.com.labuonapasta.banco.ProdutoDao;
import br.com.labuonapasta.modelo.Produto;
import br.com.labuonapasta.service.CDIServiceLocator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Objects;

@FacesConverter(value = "produtoConverter", forClass = Produto.class)
public class ProdutoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        ProdutoDao produtoDao = CDIServiceLocator.getBean(ProdutoDao.class);

        Produto produto;

        try {
            produto = produtoDao.getById(Integer.parseInt(s));
        } catch (NumberFormatException ex) {
            produto = null;
        }

        return produto;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        Produto produto = (Produto) o;

        if (Objects.nonNull(produto) && produto.getProdId() != 0) {
            return produto.getProdId().toString();
        } else{
            return "";
        }
    }
}
