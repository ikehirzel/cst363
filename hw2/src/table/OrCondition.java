package table;

/**
 * A condition of the form cond1 or cond2, where cond1 and cond2 are conditions.
 * 
 * @author Glenn
 *
 */

public class OrCondition extends Condition {

	private Condition cond1, cond2;
	
	public OrCondition(Condition cond1, Condition cond2) {
		this.cond1 = cond1;
		this.cond2 = cond2;
	}
	
	@Override
	public Boolean eval(InstTuple tuple) {
		Boolean val1 = cond1.eval(tuple);
		Boolean val2 = cond2.eval(tuple);
		
		if (val1 == null || val2 == null)
			return null;
		
		return val1 || val2;
	}
	
	@Override
	public String toString() {
		return cond1+" and "+cond2;
	}
}
