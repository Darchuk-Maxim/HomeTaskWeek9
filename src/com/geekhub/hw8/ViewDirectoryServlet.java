package com.geekhub.hw8;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@WebServlet(value = "/dir/view", initParams = {
        @WebInitParam(name = "root", value = "E:\\")
})
public class ViewDirectoryServlet extends HttpServlet {

    private static Path ROOT_PATH;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ROOT_PATH = Paths.get(config.getInitParameter("root"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String reqParam = req.getParameter("name")!= null ? req.getParameter("name"): ROOT_PATH.toAbsolutePath().toString();
        if (!reqParam.equals(ROOT_PATH.toAbsolutePath().toString())) {
            ROOT_PATH = Paths.get(req.getParameter("name"));
        }else
            ROOT_PATH = Paths.get("E:\\");
        if(reqParam != null){
            if(Files.exists(Paths.get(reqParam)))
            if(!Files.isDirectory(Paths.get(reqParam))){
                req.getRequestDispatcher("/file/view").forward(req,resp);
            }
        }
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\" />");
        sb.append("</head>");
        sb.append("<body>");
        getParentFolder(sb);
        for(Path path : getPaths()){
            appendLink(sb, path.getFileName().toString(), path);
        }
        //TODO: Implement directory listing here
        sb.append("</body>");
        sb.append("<h2>Create file on this directory:</h2>");
        sb.append("<form  action=\"/file/create\" method=\"get\">\n" +
                "    <label>File name</label>\n" +
                "<input type=\"hidden\" name=\"name\" value=" + reqParam + ">"+
                "    <input type=\"text\" name=\"fileName\">\n" +
                "    <button type=\"submit\">Create file</button>\n" +
                "</form>");
        sb.append("</html>");

        resp.getWriter().write(sb.toString());
    }

    private void appendLink(StringBuilder sb, String text, Path path) {
        //TODO: Implement link rendering based on path type (directory or file)
        if(Files.isDirectory(path))
            sb.append("<b><a href = "+ "?name=" +  path.toAbsolutePath().toString() + ""+">" +"--" +  text + "</a></b>" + "</br>");
        else
        sb.append("<a href = "+ "?name=" +  path.toAbsolutePath().toString() + ""+">" +"--" +  text + "</a> " +
                " <a href=" + "/file/remove?name=" + path.toAbsolutePath().toString() + "" + " >" + "Delete file" + "</a></br>");

    }

    private void getParentFolder(StringBuilder sb){
        if(ROOT_PATH.getParent() != null)
            sb.append("<b><a href = "+ "?name=" +  ROOT_PATH.getParent().toAbsolutePath().toString() + ""+">" + ROOT_PATH.getParent().toString() +  "</a></b>" + "<br>");
    }

    private List<Path> getPaths() throws IOException {
        List<Path> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ROOT_PATH)) {
            for (Path entry: stream) {
                result.add(entry);
            }
        } catch (DirectoryIteratorException ex) {
            throw ex.getCause();
        }

        return result;
    }

}
