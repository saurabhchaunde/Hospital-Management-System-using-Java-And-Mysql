package HospitalManagmentSystem;

import java.sql.*;
import java.util.Scanner;

public class hospitalmanagementsystem {
    private static String url="jdbc:mysql://localhost:3306/hospital";
    private static String username="root";
    private static String password="12345678";
    public static void main(String[] args) {
        Scanner Scanner = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            patient patient = new patient(connection, Scanner);
            doctor doctor = new doctor(connection);
            while (true) {
                System.out.println("Hospital Management System");
                System.out.println("1: Add patients");
                System.out.println("2: View patients ");
                System.out.println("3: view doctors");
                System.out.println("4: Boo Appoinments");
                System.out.println("5: Exit");
                System.out.println("Enter Your choice:");
                int choice = Scanner.nextInt();
                switch (choice) {
                    case 1:
                        patient.addpatient();
                        break;

                    case 2:
                        patient.viewpatient();
                        break;

                    case 3:
                        doctor.viewdoctor();
                        break;

                    case 4:
                        Appointmentbook(connection,Scanner,patient,doctor);
                        break;


                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid Choice:");
                        break;


                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public static void Appointmentbook(Connection connection, Scanner Scanner,patient patient,doctor doctor){
            System.out.println("Enter the patient id:");
            int patientid=Scanner.nextInt();
            System.out.println("Enter the doctor id");
            int  doctorid=Scanner.nextInt();
            System.out.println("Enter the date YYYY-MM-DD");
            String date=Scanner.nextLine();
            if(patient.getpatientbyid(patientid) && doctor.getdoctorbyid(doctorid)){
                if(checkdoctoravailability(doctorid,date,connection)){
                    String aq="insert into appoinment (p_id,D_id,dates)values(?,?,?)";
                    try{
                        PreparedStatement preparedStatement=connection.prepareStatement(aq);
                        preparedStatement.setInt(1,patientid);
                        preparedStatement.setInt(2,doctorid);
                        preparedStatement.setString(3,date);
                        int affecrro=preparedStatement.executeUpdate(aq);
                        if(affecrro>0){
                            System.out.println("Appointment Booked!");
                        }else{
                            System.out.println("failed to Appointment Book");
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }else{
                    System.out.println("Doctor  not available on this date");
                }
            }else {
                System.out.println("Either the doctor or patients does't Exist:");
            }
        }
        public static boolean checkdoctoravailability(int doctorid,String date,Connection connection ){
        String q="Select count(*) from appoinment where D_id=? AND date=? ";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(q);
            preparedStatement.setInt(1,doctorid);
            preparedStatement.setString(2,date);
            ResultSet set=preparedStatement.executeQuery();
            while(set.next()){
                int count=set.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }




        }catch(Exception e){
            e.printStackTrace();
        }



        return false;
        }


}
