package common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class getCompanyId {
  public static int getCompanyId(int userId) {
	  int companyId=0;
      Statement st2 = null;
      try {
          st2 = Application.dbConnection.createStatement();
      } catch (SQLException e2) {
          System.out.println(e2.getMessage());
      }
      try {
			ResultSet rs2=st2.executeQuery("select * from companyMembers where userId="+userId);
			if(rs2.next()) {
				companyId=rs2.getInt("companyId");
				return companyId;
			}
		    
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
	  return -1;
  }
}
