package com.mycompany.progetto_finale;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/categories")
public class CatalogoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Gson gson;

    @Override
    public void init(ServletConfig config)
            throws ServletException {

        super.init(config);

        gson = new Gson();

        ServletContext context =
                config.getServletContext();

        File f = new File(
                context.getRealPath("northwind.db")
        );

        HibernateUtil.SetFilePath(
                f.getPath()
        );
    }



    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        CatalogoDao dao = new CatalogoDao();
        // prende il parametro id dall'database
        String idParam =
                request.getParameter("id");

        

        if (idParam == null ||
            idParam.isEmpty()) {

            List<Catalogo> lista =
                    dao.getAllCatalogo();

            response.getWriter().write(
                    gson.toJson(lista)
            );

            return;
        }


        try {

            int id =
                    Integer.parseInt(idParam);

            Catalogo catalogo =
                    dao.getCatalogo(id);

            if (catalogo == null) {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                response.getWriter().write("""
                    {
                        "error":"Categoria non trovata"
                    }
                """);

                return;
            }

            response.getWriter().write(
                    gson.toJson(catalogo)
            );

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write("""
                {
                    "error":"ID non valido"
                }
            """);
        }
    }

    

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        CatalogoDao dao = new CatalogoDao();

        try {

            BufferedReader reader =
                    request.getReader();

            JsonObject json =
                    gson.fromJson(
                            reader,
                            JsonObject.class
                    );

            String nome =
                    json.get("categoryName")
                        .getAsString();

            String descrizione =
                    json.get("description")
                        .getAsString();

            Catalogo nuovo =
                    new Catalogo();

            nuovo.setCategoryName(nome);

            nuovo.setDescription(
                    descrizione
            );

            dao.saveCatalogo(nuovo);

            response.setStatus(
                    HttpServletResponse.SC_CREATED
            );

            response.getWriter().write(
                    gson.toJson(nuovo)
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write("""
                {
                    "error":"Errore creazione categoria"
                }
            """);
        }
    }

   
    @Override
    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        CatalogoDao dao = new CatalogoDao();

        String idParam =
                request.getParameter("id");

        if (idParam == null) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write("""
                {
                    "error":"ID mancante"
                }
            """);

            return;
        }

        try {

            int id =
                    Integer.parseInt(idParam);

            Catalogo catalogo =
                    dao.getCatalogo(id);

            if (catalogo == null) {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                response.getWriter().write("""
                    {
                        "error":"Categoria non trovata"
                    }
                """);

                return;
            }

            BufferedReader reader =
                    request.getReader();

            JsonObject json =
                    gson.fromJson(
                            reader,
                            JsonObject.class
                    );

            

            if (json.has("categoryName")) {

                catalogo.setCategoryName(
                        json.get("categoryName")
                            .getAsString()
                );
            }

           

            if (json.has("description")) {

                catalogo.setDescription(
                        json.get("description")
                            .getAsString()
                );
            }

            dao.updateCatalogo(catalogo);

            response.getWriter().write(
                    gson.toJson(catalogo)
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write("""
                {
                    "error":"Errore aggiornamento categoria"
                }
            """);
        }
    }

    
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );

        CatalogoDao dao =
                new CatalogoDao();

        String idParam =
                request.getParameter("id");

        if (idParam == null) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write("""
                {
                    "error":"ID mancante"
                }
            """);

            return;
        }

        try {

            int id =
                    Integer.parseInt(idParam);

            boolean eliminato =
                    dao.deleteCatalogo(id);

            if (eliminato) {

                response.getWriter().write("""
                    {
                        "message":"Categoria eliminata"
                    }
                """);

            } else {

                response.setStatus(
                        HttpServletResponse.SC_NOT_FOUND
                );

                response.getWriter().write("""
                    {
                        "error":"Categoria non trovata"
                    }
                """);
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().write("""
                {
                    "error":"Errore eliminazione categoria"
                }
            """);
        }
    }

    @Override
    public String getServletInfo() {

        return "Northwind Categories REST API";
    }
}