package lab.andersen.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class FrontController {
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;


    public void init(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
        this.context = servletContext;
        this.request = request;
        this.response = response;
    }

    public abstract void process() throws ServletException, IOException;

    protected void forward(String target) throws ServletException, IOException {
        target = String.format("/%s", target);
        RequestDispatcher requestDispatcher = context.getRequestDispatcher(target);
        requestDispatcher.forward(request, response);
    }
}
