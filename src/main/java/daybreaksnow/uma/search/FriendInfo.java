package daybreaksnow.uma.search;

import java.util.Collection;

/**
 * 条件に合致したフレンドの情報
 * @author daybreaksnow
 *
 */
public class FriendInfo {
	private String trainderId;
	private Collection<String> allFactors;
	private Collection<String> representFactors;

	public FriendInfo(String trainderId, Collection<String> allFactors, Collection<String> representFactors) {
		this.trainderId = trainderId;
		this.allFactors = allFactors;
		this.representFactors = representFactors;
	}

	public String getTrainderId() {
		return trainderId;
	}

	public Collection<String> getAllFactors() {
		return allFactors;
	}

	public Collection<String> getRepresentFactors() {
		return representFactors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allFactors == null) ? 0 : allFactors.hashCode());
		result = prime * result + ((representFactors == null) ? 0 : representFactors.hashCode());
		result = prime * result + ((trainderId == null) ? 0 : trainderId.hashCode());
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
		FriendInfo other = (FriendInfo) obj;
		if (allFactors == null) {
			if (other.allFactors != null)
				return false;
		} else if (!allFactors.equals(other.allFactors))
			return false;
		if (representFactors == null) {
			if (other.representFactors != null)
				return false;
		} else if (!representFactors.equals(other.representFactors))
			return false;
		if (trainderId == null) {
			if (other.trainderId != null)
				return false;
		} else if (!trainderId.equals(other.trainderId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FriendInfo [trainderId=" + trainderId + ", allFactors=" + allFactors + ", representFactors="
				+ representFactors + "]";
	}

}
