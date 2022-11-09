package com.example.bsp_104_schuelerliste;

import com.example.bsp_104_schuelerliste.data.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "ControllerServlet", value = "/ControllerServlet")
public class ControllerServlet extends HttpServlet {

    private List<Student> students;

    @Override
    public void init() throws ServletException {
        students = new ArrayList<>();

        InputStream is = getClass().getClassLoader().getResourceAsStream("students_2020.csv");
        Stream<String> resources = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines();
        students =
                resources
                        .skip(1)
                        .map(Student::fromString)
                        .collect(Collectors.toList());

        students.sort(Comparator.comparing(Student::getClassName).thenComparing(Student::getLastName).thenComparing(Student::getFirstName));
        setCatNr();

        getServletContext().setAttribute("schueler", students);
        getServletContext().setAttribute("filterschueler", students);
    }


    private void setCatNr() {
        Map<String, Integer> catNr = students.stream().map(Student::getClassName).distinct().collect(Collectors.toMap(s -> s, s -> 0));

        students.forEach(student -> {
            int catNumber = catNr.get(student.getClassName()) + 1;
            student.setCatNr(catNumber);
            catNr.put(student.getClassName(), catNumber);
        });

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        filterStudent("");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><header><meta charset=\"UTF-8\"></header><body onload='loadStudents();loadStudent()'>");
            printPage(out, req, resp);

            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body onload='loadStudents();filter();loadStudent()';>");
            if (req.getParameter("setFilter") == null) {
                out.println("<script>function filter() {document.getElementById(\'filter\').value = \'\';}</script>");
                filterStudent("");
            } else {
                out.println("<script>function filter() {document.getElementById(\'filter\').value = \'" + req.getParameter("filter") + "\';}</script>");
                filterStudent(req.getParameter("filter"));
            }

            printPage(out, req, resp);
            out.println("</body></html>");
        }
    }

    private void printPage(PrintWriter out, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        out.print("<script>function loadStudents(){document.getElementById('students').innerHTML = \"");
        for (Student student : ((List<Student>) getServletContext().getAttribute("filterschueler"))) {
            out.print("<option value='" + student.getClassName() + student.getCatNr() + "'>" + student.getLastName() + " " + student.getFirstName() + "</option>");
        }
        out.print("\";}</script>");
        req.getRequestDispatcher("searchbar.html").include(req, resp);
        req.getRequestDispatcher("schueler.html").include(req, resp);
        out.println("<!-- Latest compiled and minified CSS -->\n" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">\n" +
                "\n" +
                "<!-- jQuery library -->\n" +
                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>\n" +
                "\n" +
                "<!-- Popper JS -->\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js\"></script>\n" +
                "\n" +
                "<!-- Latest compiled JavaScript -->\n" +
                "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js\"></script>");
    }

    private void filterStudent(String filter) {
        getServletContext().setAttribute("filterschueler", ((List<Student>) getServletContext().getAttribute("schueler"))
                .stream()
                .filter(s -> (s.getFirstName() + " " + s.getLastName()).toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList()));
    }

}
