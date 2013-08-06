import java.sql.*;


public class X {


X() {


try {


Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");


Connection c = DriverManager.getConnection("jdbc:odbc:MedSol","","");


Statement  s = c.createStatement();


ResultSet  r = s.executeQuery("SELECT * from Employee");


c.close();


while(r.next()) {


System.out.println(r.getString(1));


}


}


catch(Exception e)


{


e.printStackTrace();


}


}


public static void main(String str[])


{


new X();


}


}