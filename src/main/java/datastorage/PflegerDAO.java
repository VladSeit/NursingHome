package datastorage;

import model.Pfleger;
import model.Treatment;
import utils.DateConverter;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static utils.PasswordEncryption.encryptPassword;

public class PflegerDAO extends DAOimp<Pfleger> {

    public PflegerDAO(Connection conn){
        super(conn);
        is10YearPassed();
    }
    /**
     * Check all archived pfleger, if 10 years after archive passed
     * */
    private void is10YearPassed(){
        PflegerDAO dao = DAOFactory.getDAOFactory().createPflegerDAO();
        try {
            List<Pfleger> checkList = dao.readAll();
            for(int i=0;i<checkList.toArray().length;i++){
                if(checkList.get(i).getIsLocked().equals("true")){
                    if(checkList.get(i).getDateOfLocking().getYear()+10<=LocalDate.now().getYear()){
                        dao.deleteById(checkList.get(i).getPfid());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * generates a <code>select</code>-Statement for a given key
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
            if(result.getString(7).equals("false")){
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
                        "password = '%s', islocked='%s',dateoflocking='%s' WHERE pfid = %d", pfleger.getFirstName(), pfleger.getSurname(), pfleger.getDateOfBirth(),
                pfleger.getLogin(), encryptPassword(pfleger.getPassword()),pfleger.getIsLocked(),pfleger.getDateOfLocking(),pfleger.getPfid());
    }

    @Override
    protected String getCreateStatementString(Pfleger pfleger) {
        return String.format("INSERT INTO pfleger (pfid, firstname, surname, dateOfBirth, login, password,islocked,dateoflocking) VALUES " +
                        "(%d, '%s', '%s', '%s', '%s', '%s','%s','%s')", pfleger.getPfid(), pfleger.getFirstName(),
                pfleger.getSurname(), pfleger.getDateOfBirth(), pfleger.getLogin(),
                encryptPassword(pfleger.getPassword()),pfleger.getIsLocked(),pfleger.getDateOfLocking());
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


    /**
     * Check, if the @param loginValue in database is. Statement.
     *
     * @return
     */
    private String getLoginFromDatabase(String loginValue){
        return String.format("SELECT login from PFLEGER where Login='"+loginValue+"'");
    }
    /**
     * Check, if the @param passwordValue in database is. Statement.
     *
     * @return
     */
    private String getPasswordFromDatabase(String passwordValue){
        return String.format("SELECT password from PFLEGER where password='"+passwordValue+"'");
    }

    /**
     * Check, if the @param login in database is
     *
     * @return true of false
     */
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
    /**
     * Check, if the @param password in database is
     *
     * @return true of false
     */
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

    /**
     * Get pfleger by login. Statement
     *
     * @return
     */
    private String getPflegerByLogintatementString(String login){
        return String.format("SELECT * from PFLEGER where login='"+login+"'");
    }
    /**
     * Get pfleger by login
     *
     * @return
     */
    public Pfleger getPflegerByLogin(String login) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getPflegerByLogintatementString(login));
        result.next();
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
        LocalDate dateOfLocking=null;
        if(result.getString(8)!=null) {
            dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(8));
        }
            return  new Pfleger(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6)
                    , result.getString(7),dateOfLocking);
    }
}
