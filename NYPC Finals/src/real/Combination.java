package real;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Combination 
{
	public static final int location[] = new int[8192]; 
	
    public static List<boolean[]> combinations(int n, int r) 
    {
    	List<boolean[]> result = new ArrayList<>();
        List<Integer> pathElement = new ArrayList<Integer>();
        dfs(n, r, 1, pathElement, result);
        return result;
    }

    private static void dfs(int n, int r, int start, List<Integer> pathElement, List<boolean[]> result) 
    {
        if (pathElement.size() == r) 
        {
        	boolean[] temp = new boolean[n+1];
        	temp[0] = true;
        	for(Integer element : pathElement)
        	{
        		temp[element] = true;
        	}
        	result.add(temp);
            return;
        }
        for (int i = start; i <= n; i++) 
        {
        	pathElement.add(i);
        	dfs(n, r, i + 1, pathElement, result);  
        	pathElement.remove(pathElement.size() - 1); 
        }
    }
	
    public static void setCombinationLocation(int n, int r)
    {
		List<boolean[]> answer = combinations(n, r);
		int start = 0;
		BigInteger number;
		int intValue = -1;
		for(boolean[] element : answer)
		{
			number = BigInteger.ZERO;
			for(int i=0;i<element.length;i++)
			{
				if(element[i])
				{
					number = number.setBit(i);
				}
			}
			intValue = number.intValue();
			location[intValue] = start;
			++start;
		}
    }
    
	public static void main(String[] args) 
	{
		List<boolean[]> answer = combinations(12, 1);
		boolean test[] = {true,false,false,false,false,false,false,false,false,false,false,false,true};
		int start = 0;
		for(boolean[] element : answer)
		{
			if(Arrays.equals(element, test))
			{
				System.out.println(start);
				break;
			}
			++start;
		}
		setCombinationLocation(12, 1);
		BigInteger number = BigInteger.ZERO;
		for(int i=0;i<test.length;i++)
		{
			if(test[i])
			{
				number = number.setBit(i);
			}
		}
		int intValue = number.intValue();
		System.out.println(intValue);
		System.out.println(location[intValue]);
	}

}
