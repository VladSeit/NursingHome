package datastorage;

import model.Treatment;
import utils.DateConverter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO extends DAOimp<Treatment> {

    public TreatmentDAO(Connection conn) {
        super(conn);
        is10YearPassed();
    }
    /**
     * Check all archived treatments, if 10 years after archive passed
     * */
    private void is10YearPassed(){
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            List<Treatment> checkList = dao.readAll();
            for(int i=0;i<checkList.toArray().length;i++){
                if(checkList.get(i).getIsLocked().equals("true")){
                    if(checkList.get(i).getDateOfLocking().getYear()+10<LocalDate.now().getYear()){
                        dao.deleteByPid(checkList.get(i).getTid());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected String getCreateStatementString(Treatment treatment) {
        return String.format("INSERT INTO treatment (tid,pid, treatment_date, begin, end, description, remarks,pfid,pfleger_surname,islocked,dateoflocking) VALUES " +
                "(%d,%d,'%s', '%s', '%s', '%s', '%s',%d,'%s','%s',null)",treatment.getTid(), treatment.getPid(), treatment.getDate(),
                treatment.getBegin(), treatment.getEnd(), treatment.getDescription(),
                treatment.getRemarks(),treatment.getPfid(),treatment.getPflegerSurname(),treatment.getIsLocked());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM treatment WHERE tid = %d", key);
    }

    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
        LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
        LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
        LocalDate dateOfLocking=null;
        if(result.getString(8)!=null) {
            dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(8));
        }
        Treatment m = new Treatment(result.getLong(1), result.getLong(2),
                date, begin, end, result.getString(6), result.getString(7),
                result.getInt(8),result.getString(9),
                result.getString(10),dateOfLocking);
        return m;
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM treatment";
    }

    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;
        while (result.next()) {
            if(result.getString(10).equals("false")) {

                LocalDate date = DateConverter.convertStringToLocalDate(result.getString(3));
                LocalTime begin = DateConverter.convertStringToLocalTime(result.getString(4));
                LocalTime end = DateConverter.convertStringToLocalTime(result.getString(5));
                LocalDate dateOfLocking = null;
                if (result.getString(11) != null) {
                    dateOfLocking = DateConverter.convertStringToLocalDate(result.getString(11));
                }
                t = new Treatment(result.getLong(1), result.getLong(2),
                        date, begin, end, result.getString(6), result.getString(7),
                        result.getInt(8), result.getString(9),
                        result.getString(10), dateOfLocking);
                list.add(t);
            }
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Treatment treatment) {
        return String.format("UPDATE treatment SET pid = %d, treatment_date ='%s', begin = '%s', end = '%s'," +
                "description = '%s', remarks = '%s',pfid=%d,pfleger_surname='%s',islocked='%s',dateoflocking='%s' WHERE tid = %d", treatment.getPid(), treatment.getDate(),
                treatment.getBegin(), treatment.getEnd(), treatment.getDescription(), treatment.getRemarks(),treatment.getPfid(),treatment.getPflegerSurname(),treatment.getIsLocked(),treatment.getDateOfLocking(),
                treatment.getTid());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM treatment WHERE tid= %d", key);
    }

    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOnePatientByPid(pid));
        list = getListFromResultSet(result);
        return list;
    }

    private String getReadAllTreatmentsOfOnePatientByPid(long pid){
        return String.format("SELECT * FROM treatment WHERE pid = %d", pid);
    }

    public void deleteByPid(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE pid= %d", key));
    }

    // true (Behandlung ist in der letzeten 10 Jahren) -> sperren  || false (Behandlung ist mehr als 10 Jahren duchgefuehrt)-> loeschen
    public boolean checkValidityTreatment(Treatment tr) throws SQLException {
        String query = "SELECT COUNT(*) FROM treatment WHERE CAST(DATEDIFF(CURDATE(), TREATMENT.TREATMENT_DATE)/365.25 as int ) <= 10 AND tid= %d ";
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format(query, tr.getTid()));
        result.next();
        if (result.getInt(1) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void lockTreatment(Treatment tr) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("UPDATE treatment SET locked = 'y' WHERE tid = %d",
                tr.getTid()));
    }

    // Delete invalid treatments (Validity date 10 years)
    public void deleteInvalidTreatments() throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE CAST(DATEDIFF(CURDATE(), TREATMENT.TREATMENT_DATE)/365.25 as int ) > 10 AND locked = locked = 'y' "));
    }
}