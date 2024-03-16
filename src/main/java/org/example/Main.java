package org.example;

import org.example.dao.admin.AdminDAO;
import org.example.dao.employee.EmployeeDAO;
import org.example.dao.user.UserDAO;
import org.example.hibernate.HibernateUtils;
import org.hibernate.Session;

import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to HDFC Bank");
        Session session = null;
        try {
            session = HibernateUtils.getSessionFactory().openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (session != null) {
            System.out.println("Press\n1.User\n2.Employee\n3.Admin\n4.Exit");
            System.out.print("Enter the option - ");
            int opt = scanner.nextInt();
            switch (opt) {
                case 1:
                    UserDAO userDAO = new UserDAO();
                    userDAO.entry(session);
                    break;
                case 2:
                    EmployeeDAO employeeDAO = new EmployeeDAO();
                    employeeDAO.entry(session);
                    break;
                case 3:
                    AdminDAO adminDAO = new AdminDAO();
                    adminDAO.entry(session);
                    break;
                case 4:
                    exit(1);
                    break;
            }
        }
    }
}