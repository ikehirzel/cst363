package hw7;

import org.h2.api.Trigger;
import java.sql.*;

/* 
 * oldRow and newRow refer to row in takes table
 * update student table tot_cred column when takes grade changes.
 * 
 * define the trigger using sql statements:
 * create trigger credit_u after update on takes for each row call "hw7.UpdateCreditsTrigger";
 * create trigger credit_d after delete on takes for each row call "hw7.UpdateCreditsTrigger";
 * create trigger credit_i after insert on takes for each row call "hw7.UpdateCreditsTrigger";
 */
public class UpdateCreditsTrigger implements Trigger {
	
	// column index of takes table for oldRow, newRow arrays
	private static final int ID = 0;
	private static final int COURSE_ID = 1;
	private static final int SEC_ID = 2;
	private static final int SEMESTER = 3;
	private static final int YEAR = 4;
	private static final int GRADE = 5;

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
		System.out.println("UpdateCreditsTrigger fire called.");
		
		// determine is there a change in grade value
		
		if (oldRow==null) {
			// must be insert of takes rows.  Is there a grade?
			if (newRow[GRADE]==null || ((String)newRow[GRADE]).trim().equals("")) return; // insert row without a grade value, nothing to do
		} else if (newRow==null) {
			// must be delete.  was there a grade? 
			if (oldRow[GRADE]==null || ((String)oldRow[GRADE]).trim().equals("")) return; // delete a row that did not have grade, nothing to do
		} else {
			// must be update.  Did the grade change?
			if (oldRow[GRADE]==null && newRow[GRADE]==null) return; 										// nothing to do. old and new grade values are null. 
			if (oldRow[GRADE]!=null && newRow[GRADE]!=null && oldRow[GRADE].equals(newRow[GRADE])) return;  // nothing to do. old and new grades are same.
		}
		
		// if we get here, there is a grade change, so now recalculate the student's total credits
		
		String student_id = (oldRow!=null ? (String)oldRow[ID] : (String)newRow[ID]); 
		
		/*
		 * for an H2 trigger, the connection object is passed as argument to the "fire" method
		 * do not commit or rollback or close the connection.  That is handled by the h2 server.
		 */

		try {
			ResultSet rs1 = null;
			
			// TODO prepare and execute a query to calculate the total credits for a student where id = student_id
			//      Use parameter markers (e.g. ?) to pass the value of student_id to the sql statement.
			
			 rs1.next();
			 int credits = rs1.getInt(1);
			 
			
			 int row_update_count = 0; 
			 
			 // TODO update the tot_cred in student table for the student with id = student_id
			 
			 
			 if (row_update_count == 0) {
				 System.out.println("UpdateCreditsTrigger update failed id= "+student_id+" tot_cred="+credits );
			 }
			 
		} catch (SQLException ex) {
			System.out.println("UpdateCreditsTrigger exception. "+ex.getMessage());
		}
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3, boolean arg4, int arg5)
			throws SQLException {
	}

	@Override
	public void remove() throws SQLException { 
	}
}
