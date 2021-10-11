package table;

/**
 * A condition of the form attr = value, where attr is a
 * numeric attribute, and value is a numeric constant.
 * 
 * @author Glenn
 *
 */

public class EqNumCondition extends Condition {
	
	private String attr;
	private int value;
	
	public EqNumCondition(String attr, int value) {
		this.attr = attr;
		this.value = value;
	}

	@Override
	public Boolean eval(InstTuple tuple) {
		Integer attribute = tuple.getIntAttr(attr);
		
		if (attribute == null)
			return null;
		
		return attribute.equals(value);
	}
	
	@Override
	public String toString() {
		return attr+" = "+value;
	}
}
