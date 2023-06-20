package datastorage;

import model.Pfleger;
import utils.DateConverter;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import static utils.PasswordEncryption.encryptPassword;

public class PflegerDAO extends DAOimp<Pfleger> {

    public PflegerDAO(Connection conn){
        super(conn);
    }

    /**
     *      * generates a <code>select</code>-Statement for a given key
     * @param key for which a specific SELECTis to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM pfleger WHERE pfid = %d", key);
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient</code>
     * @param result ResultSet with a single row. Columns will be mapped to a pfleger-object.
     * @return pfleger with the data from the resultSet.
     */
    @Override
    protected Pfleger getInstanceFromResultSet(ResultSet result) throws SQLException {
        Pfleger p = null;
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
        LocalDate dateOfLocking=null;
        if(result.getString(8)!=null) {
            dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(8));
        }
        p = new Pfleger(result.getInt(1), result.getString(2),
                result.getString(3), date, result.getString(5),
                result.getString(6), result.getString(7),dateOfLocking);
        return p;
    }

    /**
     * generates a <code>SELECT</code>-Statement for all pflegers.
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM pfleger";
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Pfleger-List</code>
     * @param result ResultSet with a multiple rows. Data will be mapped to pfleger-object.
     * @return ArrayList with patients from the resultSet.
     */
    @Override
    protected ArrayList<Pfleger> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Pfleger> list = new ArrayList<Pfleger>();
        Pfleger p = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
            LocalDate dateOfLocking=null;
            if(result.getString(8)!=null) {
                 dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(8));
            }
            p = new Pfleger(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6)
                    , result.getString(7),dateOfLocking);
            list.add(p);
        }
        return list;
    }


    /**
     * generates a <code>UPDATE</code>-Statement for a given pfleger
     * @param pfleger for which a specific update is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getUpdateStatementString(Pfleger pfleger) {
        return String.format("UPDATE pfleger SET firstname = '%s', surname = '%s', dateOfBirth = '%s', login = '%s', " +
                        "password = '%s' WHERE pfid = %d", pfleger.getFirstName(), pfleger.getSurname(), pfleger.getDateOfBirth(),
                pfleger.getLogin(), encryptPassword(pfleger.getPassword()), pfleger.getPfid());
    }

    @Override
    protected String getCreateStatementString(Pfleger pfleger) {
        return String.format("INSERT INTO pfleger (pfid, firstname, surname, dateOfBirth, login, password) VALUES " +
                        "(%d, '%s', '%s', '%s', '%s', '%s')", pfleger.getPfid(), pfleger.getFirstName(),
                pfleger.getSurname(), pfleger.getDateOfBirth(), pfleger.getLogin(),
                encryptPassword(pfleger.getPassword()));
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM pfleger WHERE pfid=%d", key);
    }


    private String getLoginFromDatabase(String loginValue){
        return String.format("SELECT login from PFLEGER where Login='"+loginValue+"'");
    }
    private String getPasswordFromDatabase(String passwordValue){
        return String.format("SELECT password from PFLEGER where password='"+passwordValue+"'");
    }

    public boolean checkPflegerLogin(String login) throws SQLException {

        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery(getLoginFromDatabase(login));
        resultSet.next();
        try {
            return !resultSet.getString(1).isEmpty();
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean checkPflegerPassword(String password) throws SQLException {

        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery(getPasswordFromDatabase(password));
        resultSet.next();
        try {
            return !resultSet.getString(1).isEmpty();
        } catch (SQLException e) {
            return false;
        }
    }
}
