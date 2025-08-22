package real;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScoreRule 
{
	public static int[] basicRule(int selectedNumber[], int sectionSum, int number)
	{
		int count = 0;
		int returnValue[] = null;
		for(int i=0;i<selectedNumber.length;i++)
		{
			if(selectedNumber[i] == number)
			{
				++count;
			}
		}
		int value = count * number;
		if(sectionSum < 63)
		{
			if(value+sectionSum >= 63 )
			{
				int temp[] = {value*1000 + 35000, 63};
				returnValue = temp;
			}
			else
			{
				int temp[] = {value*1000, sectionSum+value};
				returnValue = temp;
			}
		}
		else
		{
			int temp[] = {value*1000, sectionSum};
			returnValue = temp;
		}
		return returnValue;
	}
	
	public static int[] rule7(int selectedNumber[], int sectionSum)
	{
		int value = 0;
		for(int i=0;i<selectedNumber.length;i++)
		{
			value = value + selectedNumber[i];
		}
		int returnValue[] = {value*1000,sectionSum};
		return returnValue;
	}
	
	public static int[] rule8(int selectedNumber[], int sectionSum)
	{
        Map<Integer, Integer> countMap = new HashMap<>();

        for (int num : selectedNumber) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        // 등장 횟수가 4인 숫자가 있는지 확인
        for (int count : countMap.values()) 
        {
            if (count >= 4) 
            {
                return rule7(selectedNumber, sectionSum);
            }
        }
		int returnValue[] = {0,sectionSum};
		return returnValue;
	}
	
	public static int[] rule9(int selectedNumber[], int sectionSum)
	{
        Map<Integer, Integer> countMap = new HashMap<>();

        for (int num : selectedNumber) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        if (countMap.size() == 1) 
        {
            return rule7(selectedNumber, sectionSum);
        }
        
        // 정확히 두 종류의 숫자여야 함
        if (countMap.size() != 2) 
        {
    		int returnValue[] = {0,sectionSum};
            return returnValue;
        }

        // 하나는 3개, 하나는 2개인지 확인
        boolean hasThree = false;
        boolean hasTwo = false;

        for (int count : countMap.values()) 
        {
            if (count == 3) 
            	hasThree = true;
            else if (count == 2) 
            	hasTwo = true;
        }
        if(hasThree && hasTwo)
        {
            return rule7(selectedNumber, sectionSum);
        }
		int returnValue[] = {0,sectionSum};
        return returnValue;	
    }
	
	public static int[] rule10(int selectedNumber[], int sectionSum)
	{
        Set<Integer> set = new HashSet<>();
        for (int num : selectedNumber) {
            set.add(num);
        }

        int[][] patterns = {
            {1, 2, 3, 4},
            {2, 3, 4, 5},
            {3, 4, 5, 6}
        };

        for (int[] pattern : patterns) 
        {
            boolean allPresent = true;
            for (int num : pattern) 
            {
                if (!set.contains(num)) 
                {
                    allPresent = false;
                    break;
                }
            }
            if (allPresent)
            {
        		int returnValue[] = {15000,sectionSum};
            	return returnValue;
            }
        }
		int returnValue[] = {0,sectionSum};
        return returnValue;
	}
	
	public static int[] rule11(int selectedNumber[], int sectionSum)
	{
		Set<Integer> set = new HashSet<>();
        for (int num : selectedNumber) {
            set.add(num);
        }

        int[][] patterns = {
            {1, 2, 3, 4, 5},
            {2, 3, 4, 5, 6}
        };

        for (int[] pattern : patterns) 
        {
            boolean allPresent = true;
            for (int num : pattern) 
            {
                if (!set.contains(num)) 
                {
                    allPresent = false;
                    break;
                }
            }
            if (allPresent)
            {
        		int returnValue[] = {30000,sectionSum};
            	return returnValue;
            }
        }
		int returnValue[] = {0,sectionSum};
        return returnValue;
	}

	public static int[] rule12(int selectedNumber[], int sectionSum)
	{
        int first = selectedNumber[0];
        for (int num : selectedNumber) 
        {
            if (num != first) 
            {
        		int returnValue[] = {0,sectionSum};
                return returnValue;
            }
        }
		int returnValue[] = {50000,sectionSum};
        return returnValue;
	}
	
	public static void main(String[] args) 
	{
		int selected[] = {2,5,2,1,1};
		int[] answer = basicRule(selected,0, 2);
		System.out.println(answer[0] + " " + answer[1]);
	}

}
