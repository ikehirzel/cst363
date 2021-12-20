package table;

import java.util.Iterator;
import java.util.List;

/**
 * A simple select query of the form:
 *    select * from table on condition
 * 
 * @author Glenn
 *
 */

public class SelectQuery extends Query {
	
	private Condition cond;
	
	public SelectQuery(Condition cond) {
		this.cond = cond;
	}
	
	@Override
	public InstTable eval(InstTable table) {
		InstTable out = new InstTable();
		
		for (InstTuple t : table) {
			Boolean result = cond.eval(t);
			if (result == null)
				return null;
			
			if (result.equals(true)) {
				out.insert(t);
			}
		}
		
		return out;
	}

	@Override
	public String toString() {
		return "select * where "+cond;
	}

}
