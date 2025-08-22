package real;

public class State 
{
	public boolean[] category;
	public int sectionSum;
	public int leftNumber[] = new int[5];
	
	public State(boolean[] category, int sectionSum, int[] leftNumber) {
		super();
		this.category = category;
		this.sectionSum = sectionSum;
		this.leftNumber = leftNumber;
	}
}
