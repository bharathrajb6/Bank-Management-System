package org.example.dao.employee;import org.example.model.Account;import static org.example.utils.CommonUtils.*;import org.hibernate.Session;import org.hibernate.Transaction;import org.hibernate.query.Query;import java.util.List;import static org.example.dao.employee.EmployeeDAO.validateAccountDetails;public abstract class EmployeeOPS {    protected boolean createAccount(Session session, Account account) {        account.setAccountNumber(String.valueOf(generateRandomNumber(10)));        boolean isValidData = validateAccountDetails(session, account);        if (isValidData) {            Transaction transaction = null;            try {                transaction = session.beginTransaction();                session.save(account);                transaction.commit();            } catch (Exception e) {                if (transaction != null) {                    transaction.rollback();                }                return false;            }            return true;        }        return false;    }    public boolean updateAccount(Session session, Account upAccount) {        Account account = session.get(Account.class, upAccount.getAccountNumber());        account.setAadharNo(upAccount.getAadharNo());        account.setPanNo(upAccount.getPanNo());        account.setMobileNo(upAccount.getMobileNo());        boolean isValidData = validateAccountDetails(session, account);        if (isValidData) {            Transaction transaction = null;            try {                transaction = session.beginTransaction();                session.save(account);                transaction.commit();            } catch (Exception e) {                if (transaction != null) {                    transaction.rollback();                }                return false;            }            return true;        }        return false;    }    public boolean deleteAccount(Session session, String accountNumber) {        if (accountNumber != null && !accountNumber.isBlank()) {            Transaction transaction = null;            try {                Account account = session.get(Account.class, accountNumber);                if (account.getAccountNumber() == accountNumber) {                    transaction = session.beginTransaction();                    session.delete(account);                    transaction.commit();                    return true;                }            } catch (Exception e) {                if (transaction != null) {                    transaction.rollback();                }                return false;            }        }        return false;    }    protected static List<Account> getAllAccountData(Session session) {        return session.createQuery("from account", Account.class).list();    }    List getAllAccountDataInBranch(Session session, String ifscCode) {        String hql = "from account where branchCode=:filterCondition";        Query query = session.createQuery(hql);        query.setParameter("filterCondition", ifscCode);        return query.list();    }}