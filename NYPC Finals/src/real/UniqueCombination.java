package real;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueCombination 
{
	public static List<List<Integer>> select[][] = new List[252][252];
	public static List<List<Integer>> remaining[][] = new List[252][252];
	
    public static void main(String[] args) 
    {

    }

    public static void setInitial()
    {
        for(int i=0;i<252;i++)
        {
        	for(int j=0;j<252;j++)
        	{
        		int firstArray[] = PossibleDiceState.possibleDiceState[i];
        		int secondArray[] = PossibleDiceState.possibleDiceState[j];
        		int total[] = new int [10];
        		System.arraycopy(firstArray, 0, total, 0, firstArray.length);
        		System.arraycopy(secondArray, 0, total, firstArray.length, secondArray.length);
        		select[i][j] = getUniqueCombinations(total,5);
        	}
        }
    }
    
    public static List<List<Integer>> getUniqueCombinations(int[] nums, int k) 
    {
        Arrays.sort(nums);

        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack(int[] nums, int k, int start, List<Integer> current, List<List<Integer>> result) 
    {
        if (current.size() == k) 
        {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < nums.length; i++) 
        {
            if (i > start && nums[i] == nums[i - 1]) 
            	continue;

            current.add(nums[i]);
            backtrack(nums, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
	
    public static List<Integer> getRemaining(int[] nums, List<Integer> chosen) 
    {
        // 원래 배열 카운트
        Map<Integer, Integer> count = new HashMap<>();
        for (int n : nums) count.put(n, count.getOrDefault(n, 0) + 1);

        // 선택된 원소 차감
        for (int c : chosen) {
            count.put(c, count.get(c) - 1);
        }

        // 남은 원소 리스트로 변환
        List<Integer> remaining = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int val = entry.getKey();
            int freq = entry.getValue();
            for (int i = 0; i < freq; i++) {
                remaining.add(val);
            }
        }
        Collections.sort(remaining);
        return remaining;
    }
}
