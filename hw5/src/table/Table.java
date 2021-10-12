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
		List<String> key = schema.getPrimaryKey();
		if (key != null) {
			for (Tuple tup: tuples) {
				boolean someFieldDiffers = false;
				for (String keyField : key) {
					if (!tuple.get(keyField).equals(tup.get(keyField))) {
						someFieldDiffers = true;
						break;
					}
				}
				if (!someFieldDiffers) {
					return false;
				}
			}
		}
			
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
				
		// Here's a sketch of one possible simple implemtation.  Please ignore it
		// if you don't want any hints!
	 
		// YOUR CODE HERE 
		// - use the schemas of table1, table2 to compute a schema of the result table
		
		//   ?????  =  Schema.naturalJoin(table1.getSchema(), table2.getSchema());
		
		// - create the result table
		//   Table result = new Table(??????);
		
		// - find the fields that are shared by the schemas of the tables
		
		//   ????? = table1.getSchema().sharedFields(table2.getSchema());
		
		// - for all tuples tup1 of table1 and tup2 of table2:
		
		// for (Tuple tup1 : table1) {
		//   	for (Tuple tup2 : table2 ) {
		//     - use the compareTuples method below to see if tuples match on join columns
        //       - create a new tuple that contains all attributes of first tuple,
		//         plus non-shared attributes of second tuple
		//       - add the new tuple to the result table
		
		//  (replace the throw statement below with return of result table.
		
		Schema schema = Schema.naturalJoin(table1.getSchema(), table2.getSchema());
		
		Table result = new Table(schema);
		
		Set<String> sharedFields = table1.getSchema().sharedFields(table2.getSchema());
		List<String> schemaFieldNames = schema.getFieldNames();
		
		for (Tuple tup1 : table1) {
			for (Tuple tup2 : table2) {
				if (compareTuples(tup1, tup2, sharedFields)) {
					List<Field> joinedFields = new ArrayList<Field>(schema.size());
					
					joinedFields.addAll(tup1.fields);
					
					List<String> tup2FieldNames = tup2.getSchema().getFieldNames();
					
					for (int i = 0; i < tup2.fields.size(); ++i) {
						if (!sharedFields.contains(tup2FieldNames.get(i))) {
							joinedFields.add(tup2.fields.get(i));
						}
					}
										
					result.insert(new Tuple(joinedFields, schema));
				}
			}
		}
		
		return result;		
	}
	
	/**
	 * 
	 * @param tuple t1
	 * @param tuple t2
	 * @param the name of the join column
	 * @return true  both tuples have equal values for join columns
	 *         false otherwise 
	 */
	private static boolean compareTuples( Tuple t1, Tuple t2, Set<String> fields) {
		for (String s: fields) {
			if (t1.get(s).equals(t2.get(s))) continue;
			else return false;
		}
		return true;
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
