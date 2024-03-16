package org.example.dao.admin;import org.example.model.Account;import org.example.model.Branch;import org.example.model.Employee;import org.example.utils.CommonUtils;import org.hibernate.Session;import org.hibernate.Transaction;import org.example.dao.employee.EmployeeOPS;import java.util.List;import java.util.Scanner;import static org.example.utils.EmployeeUtils.validateEmployeeDetails;public class AdminOPS extends EmployeeOPS implements IAdmin {    Scanner scanner = new Scanner(System.in);    @Override    public boolean createEmployee(Session session) {        Employee employee = CommonUtils.getEmployeeDataFromJSON("Employee/NewEmployee.json");        employee.setEmployeeID(String.valueOf(CommonUtils.generateRandomNumber(10)));        String valMsg = validateEmployeeDetails(session, employee);        if (valMsg == null) {            Transaction transaction = null;            try {                transaction = session.beginTransaction();                session.save(employee);                transaction.commit();            } catch (Exception e) {                if (transaction != null) {                    transaction.rollback();                }                System.out.println("Failed save to data.");                return false;            }            return true;        }        System.out.println(valMsg);        return false;    }    @Override    public boolean updateEmployee(Session session) {        System.out.print("Enter your employee ID - ");        String employeeID = scanner.next();        System.out.print("Enter the IFSC code to update - ");        String newIfscCode = scanner.next();        Employee employee = CommonUtils.getEmployeeData(employeeID);        Branch branch = CommonUtils.getBranchData(session, newIfscCode);        if (employee != null && branch != null) {            Transaction transaction = null;            try {                Employee dbEmployee = session.get(Employee.class, employee.getEmployeeID());                dbEmployee.setBranchCode(branch.getIfscCode());                session.save(employee);                transaction.commit();            } catch (Exception e) {                System.out.println("Failed to update the data");                if (transaction != null) {                    transaction.rollback();                }                return false;            }            return true;        }        System.out.println("Data not found.");        return false;    }    @Override    public boolean deleteEmployee(Session session) {        System.out.print("Enter the employeeID - ");        String employeeID = scanner.next();        Employee employee = CommonUtils.getEmployeeData(employeeID);        if (employee != null) {            Transaction transaction = null;            try {                transaction = session.beginTransaction();                session.delete(employee);                transaction.commit();                return true;            } catch (Exception e) {                if (transaction != null) {                    transaction.rollback();                }                return false;            }        }        System.out.println("Employee Data not found.");        return false;    }    @Override    public List<Employee> getEmployeeListBasedOnBranch(Session session, String branchCode) {        return null;    }    @Override    public Employee getEmployeeById(Session session, String employeeID) {        return null;    }    @Override    public List<Account> getAccountBasedOnBranch(Session session, String branchCode) {        return null;    }    @Override    public Account getAccountByUsingNumber(Session session, String accountNumber) {        return null;    }    @Override    public List<Branch> getAllBranchDetails(Session session) {        return null;    }    @Override    public Branch getBranchDetailsByIFSC(Session session, String ifscCode) {        return null;    }}