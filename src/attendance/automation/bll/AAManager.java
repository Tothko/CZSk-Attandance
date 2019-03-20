/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.automation.bll;

import attendance.automation.be.AttendanceUnit;
import attendance.automation.be.Student;
import attendance.automation.be.Teacher;
import attendance.automation.dal.DALException;
import attendance.automation.dal.DateDAO;
import attendance.automation.dal.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import static java.time.temporal.TemporalQueries.localDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Revy
 */
public class AAManager {
    private UserDAO ud;
    private DateDAO dd;
    private static AAManager manager;
    private Student st;
    private Teacher te;
    
    public AAManager() throws IOException{
        ud = new UserDAO();
        dd = new DateDAO();
        st = null;
        te = null;
    }
    
    public static AAManager getInstance() throws IOException{
    if(manager==null){
        manager = new AAManager();
        return manager;
    }
    else
        return manager;   
    }
    
    public boolean checkLogin(String login, String password) throws DALException, IOException{
        return ud.checkLogin(login, password);
    }
    
    public void setUser(){
        this.st=ud.getStudent();
        this.te=ud.getTeacher();
    }
    public boolean isTeacher(){
        if(te==null)
            return false;
        else
            return true;
    
    }
    public void setStudent(int studentID) throws DALException, SQLException, IOException{
        ud.setStudent(studentID);
    }
    public Student getStudent(){
        return st;
    }
    public Teacher getTeacher(){
        return te;
    }
    public boolean markAttendance(int studentID) throws DALException{
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return dd.markAttendance(studentID, date);
    }
    public double attendanceRate(Student student) throws DALException{
        double schoolDays = 0;
        for (AttendanceUnit attendanceUnit : student.getAttendance()) {
            System.out.println(attendanceUnit.getAttendanceDate().toString());
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = (Calendar) c1.clone();
        Date date = student.getAttendance().get(0).getAttendanceDate();
        c2.setTime(date);
        while(c2.before(c1)||c2.equals(c1)){
            if(c2.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c2.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                schoolDays++;
            c2.add(Calendar.DATE, 1);
        }
        System.out.println(schoolDays);
        return dd.getAttendancesForThisMonth(student.getId())/schoolDays;
    }
    
    public void changeAttendance(int studentID, Date date, String distinguisher){
    dd.changeAttendance(studentID, date, distinguisher);
    }
    
}
