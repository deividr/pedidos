package br.com.labuonapasta.util;

import br.com.labuonapasta.excessao.NegocioException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.io.IOException;
import java.util.Iterator;

public class JSFExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    private static Log log = LogFactory.getLog(JSFExceptionHandler.class);

    public JSFExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> eventos = getUnhandledExceptionQueuedEvents().iterator();

        while (eventos.hasNext()) {
            ExceptionQueuedEvent event = eventos.next();
            ExceptionQueuedEventContext eventContext = event.getContext();
            Throwable exception = eventContext.getException();

            try {
                if (exception instanceof ViewExpiredException) {
                    eventos.remove();
                    redirecionar("/Home.xhtml");
                } else if (getNegocioException(exception) != null) {
                    Messages.addGlobalInfo(exception.getMessage());
                } else {
                    log.error("Erro no sistema: " + exception.getMessage(), exception);
                    redirecionar("/Error.xhtml");
                }
            } finally {
            }
        }

        getWrapped().handle();
    }

    private NegocioException getNegocioException(Throwable exception) {
        if (exception instanceof NegocioException) {
            return (NegocioException) exception;
        } else {
            if (exception.getCause() != null) {
                return getNegocioException(exception.getCause());
            }
        }
        return null;
    }

    private void redirecionar(String page) {
        try {
            Faces.getExternalContext().redirect(Faces.getRequestContextPath() + page);
            Faces.responseComplete();
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }
}
