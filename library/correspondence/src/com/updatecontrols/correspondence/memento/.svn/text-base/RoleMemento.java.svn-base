package com.updatecontrols.correspondence.memento;


public class RoleMemento {

    private CorrespondenceFactType declaringType;
    private String roleName;
    private CorrespondenceFactType targetType;
    private boolean isPivot;
    
	public RoleMemento(CorrespondenceFactType declaringType, String roleName, CorrespondenceFactType targetType, boolean isPivot) {
		super();
		this.declaringType = declaringType;
		this.roleName = roleName;
		this.targetType = targetType;
		this.isPivot = isPivot;
	}

	public CorrespondenceFactType getDeclaringType() {
		return declaringType;
	}

	public String getRoleName() {
		return roleName;
	}

	public CorrespondenceFactType getTargetType() {
		return targetType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((declaringType == null) ? 0 : declaringType.hashCode());
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
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
		RoleMemento other = (RoleMemento) obj;
		if (declaringType == null) {
			if (other.declaringType != null)
				return false;
		} else if (!declaringType.equals(other.declaringType))
			return false;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}

	public boolean isPivot() {
		return isPivot;
	}
	
	public String toString() {
		return String.format("%1s.%2s", declaringType, roleName);
	}
}
