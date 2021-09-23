package daybreaksnow.uma.search;

/**
 * 検索条件の因子
 * @author daybreaksnow
 *
 */
public class Factor {
	/** 因子名 */
	private String name;
	/** 必要な因子数 */
	private int minNum;

	public Factor(String name, int minNum) {
		this.name = name;
		this.minNum = minNum;

		if (this.minNum <= 0 || this.minNum >= 10) {
			throw new IllegalArgumentException("minNum is IllegalValue:" + this.minNum);
		}
	}

	public String getName() {
		return name;
	}

	public int getMinNum() {
		return minNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + minNum;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Factor other = (Factor) obj;
		if (minNum != other.minNum)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
