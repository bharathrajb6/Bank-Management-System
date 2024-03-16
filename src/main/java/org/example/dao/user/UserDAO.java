package org.example.dao.user;import com.fasterxml.jackson.databind.ObjectMapper;import org.example.model.Account;import org.example.model.AccountTransaction;import org.example.model.User;import org.hibernate.Session;import org.hibernate.Transaction;import org.hibernate.query.Query;import java.io.File;import java.io.IOException;import java.util.List;import java.util.Objects;import java.util.Scanner;public class UserDAO {    Scanner scanner = new Scanner(System.in);    private boolean loggedIn;    private String username = null;    public void entry(Session session) {        System.out.print("Hello User! - ");        System.out.println("Login to the portal");        login(session);        User user = session.get(User.class, username);        while (loggedIn) {            System.out.println("Press\n1.Get Account Details\n2.Update Account Details\n3.Get Transaction Details\n4.Logout");            System.out.println("Enter your choice - ");            int ch = scanner.nextInt();            switch (ch) {                case 1:                    getAccountDetails(session, user);                    break;                case 2:                    boolean isAccountUpdated = updateAccount(session, user);                    if (isAccountUpdated) {                        System.out.println("Account info is updated successfully");                    } else {                        System.out.println("Account info is not updated. Please try again later.");                    }                    break;                case 3:                    getTransactionDetails(session, user);                    break;                case 4:                    logout();                    break;            }        }    }    private void login(Session session) {        System.out.print("If you have already registered, then enter 1 otherwise 0.");        int choice = scanner.nextInt();        if (choice == 1) {            System.out.print("Enter the username - ");            username = scanner.next();            System.out.print("Enter the password - ");            String password = scanner.next();            Transaction transaction = null;            try {                User user = session.get(User.class, username);                if (Objects.equals(user.getPassword(), password)) {                    loggedIn = true;                    username = user.getUsername();                    transaction = session.beginTransaction();                    session.save(user);                    transaction.commit();                }            } catch (Exception e) {                System.out.println("Account not found.");                if (transaction != null) {                    transaction.rollback();                }            }        } else if (choice == 0) {            System.out.print("Enter the account number  - ");            String accountNumber = scanner.next();            System.out.print("Enter the mobile number  - ");            String mobileNumber = scanner.next();            Transaction transaction = null;            try {                Account account = session.get(Account.class, accountNumber);                if (Objects.equals(account.getMobileNo(), mobileNumber)) {                    User user = new User();                    System.out.print("Enter the username - ");                    user.setUsername(scanner.next());                    System.out.print("Enter the password - ");                    user.setPassword(scanner.next());                    user.setAccountNumber(account.getAccountNumber());                    transaction = session.beginTransaction();                    session.save(user);                    transaction.commit();                    loggedIn = true;                    username = user.getUsername();                    System.out.println("Registered Successfully.Welcome!");                }            } catch (Exception e) {                if (transaction != null) {                    transaction.rollback();                }            }        }    }    private void logout() {        loggedIn = false;        username = null;    }    private void getAccountDetails(Session session, User user) {        Account account = session.get(Account.class, user.getAccountNumber());        ObjectMapper objectMapper = new ObjectMapper();        try {            objectMapper.writeValue(new File("/Users/bsringer/Documents/Projects/Java/Bank Management System/src/main/resources/UserData/MyAccountDetails.json"), account);            System.out.println("Data is saved successfully in MyAccountDetails.json file");        } catch (IOException ioException) {            System.out.println("Unable to get the account data. Please try again later.");        }    }    private boolean updateAccount(Session session, User user) {        Transaction transaction = null;        Account account = session.get(Account.class, user.getAccountNumber());        System.out.println("Enter the mobile to change - ");        String mbl = scanner.next();        try {            transaction = session.beginTransaction();            account.setMobileNo(mbl);            transaction.commit();        } catch (Exception e) {            if (transaction != null) {                transaction.rollback();            }            return false;        }        return true;    }    private void getTransactionDetails(Session session, User user) {        List<AccountTransaction> transactions;        String hql = "from accountTransaction where accountNumber=:filterCondition";        Query query = session.createQuery(hql);        query.setParameter("filterCondition", user.getAccountNumber());        transactions = query.list();        if (!transactions.isEmpty()) {            ObjectMapper objectMapper = new ObjectMapper();            try {                objectMapper.writeValue(new File("/src/main/resources/UserData/TransactionData.json"), transactions);                System.out.println("Data is saved successfully in TransactionData.json file");            } catch (IOException e) {                e.printStackTrace();            }        } else {            System.out.println("There is no data to show.");        }    }}