package br.com.labuonapasta.util;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class JSFExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory exceptionHandlerFactory;

    public JSFExceptionHandler jsfExceptionHandler;

    public JSFExceptionHandlerFactory(ExceptionHandlerFactory exceptionHandlerFactory) {
        this.exceptionHandlerFactory = exceptionHandlerFactory;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new JSFExceptionHandler(exceptionHandlerFactory.getExceptionHandler());
    }

}
