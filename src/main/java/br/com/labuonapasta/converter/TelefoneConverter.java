package br.com.labuonapasta.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "telefoneConverter")
public class TelefoneConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return s.replaceAll("[^0-9]", "");
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String telefone = (String) o;

        telefone = telefone.replaceAll("([0-9]{2})([0-9]{1,11})$", "($1) $2");
        telefone = telefone.replaceAll("([0-9]{4,5})([0-9]{4})", "$1-$2");

        return telefone;
    }
}
