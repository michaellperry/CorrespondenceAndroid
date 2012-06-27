package com.updatecontrols.correspondence.memento;


public class CorrespondenceFactType {

	private String name;
	private int version;

	public CorrespondenceFactType(String name, int version) {
		super();
		this.name = name;
		this.version = version;
	}
	
	public String getName() {
		return name;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + version;
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
		CorrespondenceFactType other = (CorrespondenceFactType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
	public String toString() {
		return String.format("%1s(%2s)", name, version);
	}
}
