package com.geekhub.hw8;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/file/create")
public class CreateFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Path path = Paths.get(req.getParameter("name"), req.getParameter("fileName"));
        req.setAttribute("name", path.getParent().toString());
        if(creteFile(path)){
            req.getRequestDispatcher("/dir/view").forward(req, resp);
        }else{
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean creteFile(Path name){
        try {
            Files.createFile(name);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
