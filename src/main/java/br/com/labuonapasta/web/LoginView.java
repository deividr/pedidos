package br.com.labuonapasta.web;

import org.omnifaces.util.Messages;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Named
@RequestScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 3361291778226971916L;

    private String login;
    private String senha;

    private FacesContext facesContext;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Inject
    public LoginView(HttpServletRequest request, HttpServletResponse response) {
        this.facesContext = FacesContext.getCurrentInstance();
        this.request = request;
        this.response = response;
    }

    public void preRender() {
        if (request.getParameter("error") != null) {
            Messages.addGlobalError("Login Inválido");
        }
    }

    public String efetuarLogin() throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login");

        dispatcher.forward(request, response);

        facesContext.responseComplete();
        
        return "";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
