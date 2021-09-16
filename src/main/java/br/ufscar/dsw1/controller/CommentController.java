package br.ufscar.dsw1.controller;

import br.ufscar.dsw1.dao.CommentDAO;
import br.ufscar.dsw1.domain.Comment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CommentController", value = "/comments/")
public class CommentController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            final long postId = Long.parseLong(request.getParameter("postId"));
            ArrayList<Comment> comments = CommentDAO.getAllCommentsFromPost(postId);
            request.setAttribute("comments", comments);
            request.setAttribute("errored", comments == null);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errored", true);
        }


        request.getRequestDispatcher("/components/_comment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<String> errors = new ArrayList<>();

        try {
            final long userId = Long.parseLong(request.getParameter("userId"));
            final long postId = Long.parseLong(request.getParameter("postId"));
            final String content = request.getParameter("content");

            Comment comment = CommentDAO.insert(postId, userId, content, false);

            // Inserimos em uma lista para usar o componente genérico
            ArrayList<Comment> result = new ArrayList<>();
            result.add(comment);

            request.setAttribute("comments", result);
            request.getRequestDispatcher("/components/_comment.jsp").forward(request, response);

        } catch (NumberFormatException | ServletException e) {
            response.getWriter().write("Erro ao fazer o parsing dos identificadores.");
        }


    }

}
