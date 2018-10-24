package com.ayu.ml.apriori;

public class ItemSet {
private String item ;
	
	private Integer freq;
	
	private Boolean active = true;

	public ItemSet(String item, Integer freq) {
		super();
		this.item = item;
		this.freq = freq;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getFreq() {
		return freq;
	}

	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
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
		ItemSet other = (ItemSet) obj;		
		if (active == null) {
			if (other.active != null)
				return false;
		} 
		else if (!active.equals(other.active))
			return false;		
		if (item == null) {
			if (other.item != null)
				return false;
		} 
		else if (!item.equals(other.item))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return item + "   " + freq;
	}
}
