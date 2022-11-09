package com.example.bsp_104_schuelerliste;

import com.example.bsp_104_schuelerliste.data.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "StudentServlet", value = "/StudentServlet")
public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper om = new ObjectMapper();
        String data = om.writeValueAsString(getStudent(req.getParameter("student")));

        try (PrintWriter out = resp.getWriter()) {
            out.println(data);
        }
    }

    private Student getStudent (String classCatNr){
        List<Student> students = (List<Student>) getServletContext().getAttribute("schueler");
        final Student[] studentValue = {null};

        students.forEach(student -> {
            if (classCatNr.equals(student.getClassName() + student.getCatNr())){
                studentValue[0] = student;
            }
        });
        return studentValue[0];
    }
}
