package real;

public class ScoreMemozation 
{
	public static int memozation [][][][][][][][] = new int[7][7][7][7][7][64][13][2];
	
    public static int[] calculateValue(int category, int selectedNumber[], int sectionSum)
    {
    	int returnValue[] = null;
    	if(category >=1 && category <= 6)
    	{
    		returnValue = ScoreRule.basicRule(selectedNumber, sectionSum, category);
    	}
    	else if(category == 7)
    	{
    		returnValue = ScoreRule.rule7(selectedNumber, sectionSum);
    	}
    	else if(category == 8)
    	{
    		returnValue = ScoreRule.rule8(selectedNumber, sectionSum);
    	}
    	else if(category == 9)
    	{
    		returnValue = ScoreRule.rule9(selectedNumber, sectionSum);
    	}
    	else if(category == 10)
    	{
    		returnValue = ScoreRule.rule10(selectedNumber, sectionSum);
    	}
    	else if(category == 11)
    	{
    		returnValue = ScoreRule.rule11(selectedNumber, sectionSum);
    	}
    	else if(category == 12)
    	{
    		returnValue = ScoreRule.rule12(selectedNumber, sectionSum);
    	}
    	return returnValue;
    }
	
	public static void memo()
	{
		int selectedNumber[] = new int [5]; 
		for(int i1=1;i1<=6;i1++)
		{
			selectedNumber[0] = i1;
			for(int i2=1;i2<=6;i2++)
			{
				selectedNumber[1] = i2;
				for(int i3=1;i3<=6;i3++)
				{
					selectedNumber[2] = i3;
					for(int i4=1;i4<=6;i4++)
					{
						selectedNumber[3] = i4;

						for(int i5=1;i5<=6;i5++)
						{
							selectedNumber[4] = i5;
							for(int sum=0;sum<64;sum++)
							{
								for(int category=1;category<=12;category++)
								{
									memozation[i1][i2][i3][i4][i5][sum][category] = calculateValue(category, selectedNumber, sum);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) 
	{
		memo();
	}

}
