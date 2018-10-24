package com.ayu.ml.apriori;

public class AssociationRule {
	
	private static final String PERCENT = "%";
	
	private static final String FORMAT_DOUBLE = "%.2f";
	
	private static final String CONFIDENCE = "Confidence";

	private String left;

	private String right;

	private double confidence;
	
	private String rule;

	public AssociationRule(String left, String right,String rule) {
		super();
		this.left = left;
		this.right = right;
		this.rule = rule;
	}

	public String getleft() {
		return left;
	}

	public void setleft(String left) {
		this.left = left;
	}

	public String getright() {
		return right;
	}

	public void setright(String right) {
		this.right = right;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociationRule other = (AssociationRule) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return left + " -> " + right + "\t[" + CONFIDENCE + " : " +String.format(FORMAT_DOUBLE, confidence) + PERCENT + "]";
	}

}
