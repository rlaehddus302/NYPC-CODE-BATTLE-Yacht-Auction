package real;

import java.util.ArrayList;
import java.util.List;

public class TenCFive 
{
	private static int count;
	static public final byte[][] PICK_IDX = new byte[252][5];
	static public final byte[][] UNPICK_IDX = new byte[252][5];
	
    public static boolean[][] combinations() 
    {
    	count = 0;
    	boolean result[][] = new boolean[252][10];
        List<Integer> pathElement = new ArrayList<Integer>();
        dfs(5, 0, pathElement, result);
        return result;
    }

    private static void dfs(int r, int start, List<Integer> pathElement, boolean result[][]) 
    {
        if (pathElement.size() == r) 
        {
        	for(Integer element : pathElement)
        	{
        		result[count][element] = true;
        	}
    		++count;
            return;
        }
        for (int i = start; i < 10; i++) 
        {
        	pathElement.add(i);
        	dfs(r, i + 1, pathElement, result);  
        	pathElement.remove(pathElement.size() - 1); 
        }
    }
    
    public static void initializePickAndUnpickIndices()
    {
		boolean[][] result = combinations();
		for(int i=0;i<result.length;i++)
		{
			int pick = 0;
			int unpick = 0;
			for(int j=0;j<result[0].length;j++)
			{
				if(result[i][j])
				{
					PICK_IDX[i][pick] = (byte) j;
					++pick;
				}
				else
				{
					UNPICK_IDX[i][unpick] = (byte) j;
					++unpick;
				}
			}
		}
    }
    
	public static void main(String[] args) 
	{
		boolean[][] result = combinations();
		for(int i=0;i<result.length;i++)
		{
			int pick = 0;
			int unpick = 0;
			for(int j=0;j<result[0].length;j++)
			{
				if(result[i][j])
				{
					PICK_IDX[i][pick] = (byte) j;
					++pick;
				}
				else
				{
					UNPICK_IDX[i][unpick] = (byte) j;
					++unpick;
				}
			}
		}
	}


}
