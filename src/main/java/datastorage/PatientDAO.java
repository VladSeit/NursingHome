package datastorage;

import model.Patient;
import model.Treatment;
import utils.DateConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Interface <code>DAOImp</code>. Overrides methods to generate specific patient-SQL-queries.
 */
public class  PatientDAO extends DAOimp<Patient> {

    /**
     * constructs Onbject. Calls the Constructor from <code>DAOImp</code> to store the connection.
     * @param conn
     */
    public PatientDAO(Connection conn) {
        super(conn);
        is10YearPassed();
    }

    /**
     * Check all archived patients, if 10 years after archive passed
     * */
    private void is10YearPassed(){
        PatientDAO dao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            List<Patient> checkList = dao.readAll();
            for(int i=0;i<checkList.toArray().length;i++){
                if(checkList.get(i).isLocked().equals("true")){
                    if(checkList.get(i).getDateOfLocking().getYear()+10<LocalDate.now().getYear()){
                        dao.deleteById(checkList.get(i).getPid());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * generates a <code>INSERT INTO</code>-Statement for a given patient
     * @param patient for which a specific INSERT INTO is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getCreateStatementString(Patient patient) {
        return String.format("INSERT INTO patient (pid,firstname, surname, dateOfBirth, carelevel, roomnumber,islocked,dateoflocking) VALUES ('%d','%s', '%s', '%s', '%s', '%s','%s','%s')",
               patient.getPid(), patient.getFirstName(), patient.getSurname(), patient.getDateOfBirth(), patient.getCareLevel(), patient.getRoomnumber(),patient.isLocked(),patient.getDateOfLocking());
    }
    /**
     *      * generates a <code>select</code>-Statement for a given key
     * @param key for which a specific SELECT is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM patient WHERE pid = %d", key);
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient</code>
     * @param result ResultSet with a single row. Columns will be mapped to a patient-object.
     * @return patient with the data from the resultSet.
     */
    @Override
    protected Patient getInstanceFromResultSet(ResultSet result) throws SQLException {
        Patient p = null;
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
        LocalDate dateOfLocking = null;
        if (result.getString(8) != null) {
            dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(8));
        }
        p = new Patient(result.getInt(1), result.getString(2),
                result.getString(3), date, result.getString(5),
                result.getString(6), result.getString(7),
                        dateOfLocking);
        return p;
    }

    /**
     * generates a <code>SELECT</code>-Statement for all patients.
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM patient";
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient-List</code>
     * @param result ResultSet with a multiple rows. Data will be mapped to patient-object.
     * @return ArrayList with patients from the resultSet.
     */
    @Override
    protected ArrayList<Patient> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        Patient p = null;
        while (result.next()) {
         if(result.getString(7).equals("false")) {
             LocalDate date = DateConverter.convertStringToLocalDate(result.getString(4));
             LocalDate dateOfLocking = null;
             if (result.getString(8) != null) {
                 dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(8));
             }
             p = new Patient(result.getInt(1), result.getString(2),
                     result.getString(3), date,
                     result.getString(5), result.getString(6), result.getString(7),
                     dateOfLocking);
             list.add(p);
         }
        }
        return list;
    }

    /**
     * generates a <code>UPDATE</code>-Statement for a given patient
     * @param patient for which a specific update is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getUpdateStatementString(Patient patient) {
        return String.format("UPDATE patient SET firstname = '%s', surname = '%s', dateOfBirth = '%s', carelevel = '%s', " +
                "roomnumber = '%s', islocked='%s',dateoflocking='%s' WHERE pid = %d", patient.getFirstName(), patient.getSurname(), patient.getDateOfBirth(),
                patient.getCareLevel(), patient.getRoomnumber(),patient.isLocked(),patient.getDateOfLocking(), patient.getPid());
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM patient WHERE pid=%d", key);
    }

}
