package table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * An instructor table, implemented in-memory as a simple list of rows.
 * 
 * @author Glenn
 *
 */

public class Table implements Iterable<Tuple> {
	
	private List<Tuple> tuples;
	private Schema schema;
	
	public Table(Schema schema) {
		this.schema = schema;
		tuples = new ArrayList<>();
	}
	
	public int size() {
		return tuples.size();
	}
	
	public Schema getSchema() {
		return schema;
	}
	
	public int numCols() {
		return schema.size();
	}
	
	/**
	 * Insert the given tuple into the table.  Return false if inserting
	 * the tuple would result in an integrity constraint violation.
	 * @param tuple
	 */
	public boolean insert(Tuple tuple) {
		// get the column name(s) that make up the primary key
		List<String> primaryKey = schema.getPrimaryKey();
		if (primaryKey !=null ) {
			// YOUR CODE HERE
			// for each tuple t in the table can use the for loop  for (Tuple t: this) 
			// check if the primary key values for all columns of tuple equal the 
			// primary key columns of t,  if so, this is duplicate key. return false
		    // otherwise, continue
						
			for (Tuple t : tuples) {
				boolean isDuplicate = true;
				
				for (String columnName : primaryKey) {
					Field key = t.get(columnName);					
					if (!key.equals(tuple.get(columnName))) {
						isDuplicate = false;
						break;
					}
				}
				
				if (isDuplicate) {
					return false;
				}
			}			
		}
		// if you get here, we have checked all rows in the table for a duplicate key
		// Did not find any.  So add tuple.
		tuples.add(tuple);
		return true;

	}
	
    /**
     * Return the result of a project of this table on the given attributes
     * @param attrs
     * @return
     */
	public Table project(List<String> attrs) {
		Schema proj_schema = schema.project(attrs);
		Table result = new Table(proj_schema);
		for (Tuple tup: tuples) {
			result.insert(new Tuple(tup.project(attrs), proj_schema));
		}
		return result;
	}
	
	/**
	 * Return the result of the natural join of table1 and table2
	 * @param table1
	 * @param table2
	 * @return
	 */
	public static Table naturalJoin(Table table1, Table table2) {
		throw new UnsupportedOperationException("Natural join not yet implemented.");
	}

	/**
	 * Iterate over all the tuples in this table.
	 */
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Tuple tuple: tuples) {
			sb.append(tuple+"\n");
		}
		return sb.toString();
	}

}
