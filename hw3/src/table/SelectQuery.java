package table;

import java.util.Iterator;
import java.util.List;

/**
 * A simple select query of the form:
 *    select attrs from table on condition
 * 
 * @author Glenn
 *
 */

public class SelectQuery extends Query {
	
	private Condition cond;
	private List<String> attrs;	   // a value of null means all attributes of the table
	
	// select attrs where cond
	public SelectQuery(List<String> attrs, Condition cond) {
		this.attrs = attrs;
		this.cond = cond;
	}
	
	// select * where cond
	public SelectQuery(Condition cond) {
		this(null, cond);
	}
	
	/**
	 * Return the schema of the result of this query, given the
	 * schema of the table to which the query is applied
	 * @return
	 */
	public Schema resultSchema(Schema inSchema) {
		return inSchema;
	}
	
	@Override
	public Table eval(Table table) {
		// YOUR CODE HERE
		// replace the following line with your code
		// hint: after selecting the tuples you need, use the
		// table project operation
		
		Table out = new Table(table.getSchema());
		
		for (Tuple t : table) {
			Boolean value = cond.eval(t);
			
			if (value == null) {
				return null;
			}
			
			boolean isMatch = value.booleanValue();
			
			if (isMatch) {
				out.insert(t);
			}
		}
		
		if (attrs == null)
			return out;
			
		return out.project(attrs);
	}

	@Override
	public String toString() {
	    String proj_attrs = "*";
	    if (attrs != null) {
	    	proj_attrs = String.join(",", attrs);
	    }
	    return "select "+proj_attrs+" where "+cond;
	}

}
