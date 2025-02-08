package servlets;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static services.ExchangeService.*;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            String from = req.getParameter("from");
            String to = req.getParameter("to");
            Double amount = Double.valueOf(req.getParameter("amount"));

            PrintWriter out = resp.getWriter();
            out.print(countExchangeRate(from, to, amount));
            out.flush();
        } catch (Exception e) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject ex = new JsonObject();
            ex.addProperty("message", e.toString());

            PrintWriter out = resp.getWriter();
            out.print(gson.toJson(ex));
            out.flush();
            out.close();
        }
    }
}
