import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
//customClass

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class ScoreRule 
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
}

class ScoreMemozation 
{
	public static int memozation [][][][] = new int[252][64][13][2];
	
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
		for(int i=0;i<252;i++)
		{
			for(int sum=0;sum<64;sum++)
			{
				for(int category=1;category<=12;category++)
				{
					memozation[i][sum][category] = calculateValue(category, Game.possibleDiceState[i], sum);
				}
			}
		}
	}
}

class Combination 
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
}

class PossibleSum
{
	int number;
	boolean score[] = new boolean[64];
	
	public PossibleSum(int number, boolean[] score) {
		super();
		this.number = number;
		this.score = score;
	}
}

class PossibleBasicSum 
{
	public static List<PossibleSum> reachableStates [] = new ArrayList[7];
	
	public static void findReachableStates(int arr[],int n)
	{
		List<PossibleSum> temp = new ArrayList<PossibleSum>();
		boolean initialScore[] = new boolean[64];
		initialScore[0] = true;
		PossibleSum initialState = new PossibleSum(0,initialScore);
		temp.add(initialState);
		for(int i=1;i<=6;i++)
		{
			boolean score[] = new boolean[64];
			for(int j=0;j<=5;j++)
			{
				int location = i*j;
				score[location] = true;
			}
			PossibleSum state = new PossibleSum(i,score);
			temp.add(state);
		}
		reachableStates[1] = temp;
		for(int i=2;i<=n;i++)
		{
			temp = new ArrayList<PossibleSum>();
			List<List<Integer>> combination = combinations(arr, i);
			for(List<Integer> element : combination)
			{
				String number = "";
				for(int j=0;j<element.size()-1;j++)
				{
					int value = element.get(j);
					number = number + String.valueOf(value);
				}
				int first = Integer.valueOf(number);
				int last = element.get(element.size()-1);
				PossibleSum prevState = null;
				for(PossibleSum stateElement : reachableStates[i-1])
				{
					if(first == stateElement.number)
					{
						prevState = stateElement;
						break;
					}
				}
				PossibleSum addState = null;
				for(PossibleSum stateElement : reachableStates[1])
				{
					if(last == stateElement.number)
					{
						addState = stateElement;
						break;
					}
				}
				boolean[] prevScore = prevState.score;
				boolean[] addScore = addState.score;
				boolean newScore[] = new boolean[64];
				for(int prevScoreElement=0;prevScoreElement<64;++prevScoreElement)
				{
					if(prevScore[prevScoreElement])
					{
						for(int addScoreElement=0;addScoreElement<64;++addScoreElement)
						{
							if(addScore[addScoreElement])
							{
								int location = prevScoreElement + addScoreElement;
								if(location > 63)
								{
									location = 63;
								}
								newScore[location] = true;
							}
						}
					}
				}
				PossibleSum state = new PossibleSum(first*10+last, newScore);
				temp.add(state);
			}
			reachableStates[i] = temp;
		}
	}
	
    public static List<List<Integer>> combinations(int[] arr, int r) {
        List<List<Integer>> path = new ArrayList<List<Integer>>();
        List<Integer> pathElement = new ArrayList<Integer>();
        dfs(arr, r, 0, pathElement, path);
        return path;
    }

    private static void dfs(int[] arr, int r, int start, List<Integer> pathElement ,List<List<Integer>> path) {
        if (pathElement.size() == r) 
        {
        	List<Integer> temp = new ArrayList<Integer>();
        	for(Integer element : pathElement)
        	{
        		temp.add(element);
        	}
        	path.add(temp);
            return;
        }

        for (int i = start; i < arr.length; i++) 
        {
        	pathElement.add(arr[i]);
        	dfs(arr, r, i + 1, pathElement, path);  
        	pathElement.remove(pathElement.size() - 1); 
        }
    }
}

class TenCFive 
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
}


// 가능한 주사위 규칙들을 나타내는 enum
enum DiceRule {
    ONE, TWO, THREE, FOUR, FIVE, SIX,
    CHOICE, FOUR_OF_A_KIND, FULL_HOUSE, SMALL_STRAIGHT, LARGE_STRAIGHT, YACHT
}

// 입찰 방법을 나타내는 구조체
class Bid {
    public char group; // 입찰 그룹 ('A' 또는 'B')
    public int amount; // 입찰 금액

    public Bid() {
    }

    public Bid(char group, int amount) {
        this.group = group;
        this.amount = amount;
    }
}

// 주사위 배치 방법을 나타내는 구조체
class DicePut {
    public DiceRule rule; // 배치 규칙
    public int[] dice; // 배치할 주사위 목록

    public DicePut() {
    }

    public DicePut(DiceRule rule, int[] dice) {
        this.rule = rule;
        this.dice = dice;
    }
}

// 게임 상태를 관리하는 클래스
class Game {
	static public final int[][][][][] possibleDiceStateLocation = new int[7][7][7][7][7];
	public static int levelStartLocation[] = {0, 1, 13, 79, 299, 794, 1586, 2510, 3302, 3797, 4017, 4083, 4095};
	private static final int[] selectDiceNumber = new int[5];
	private static final int[] leftNumber = new int[5];
	private int expectedValue[][] = new int[252][64];
	private int selectCategory[][] = new int[2][2];
	private int selectDiceLocation[][] = new int[2][2];
	public static final int possibleDiceState[][] = {{ 1, 1, 1, 1, 1, },{ 1, 1, 1, 1, 2, },{ 1, 1, 1, 1, 3, },{ 1, 1, 1, 1, 4, },{ 1, 1, 1, 1, 5, },{ 1, 1, 1, 1, 6, },{ 1, 1, 1, 2, 2, },{ 1, 1, 1, 2, 3, },{ 1, 1, 1, 2, 4, },{ 1, 1, 1, 2, 5, },{ 1, 1, 1, 2, 6, },
													{ 1, 1, 1, 3, 3, },{ 1, 1, 1, 3, 4, },{ 1, 1, 1, 3, 5, },{ 1, 1, 1, 3, 6, },{ 1, 1, 1, 4, 4, },{ 1, 1, 1, 4, 5, },{ 1, 1, 1, 4, 6, },{ 1, 1, 1, 5, 5, },{ 1, 1, 1, 5, 6, },{ 1, 1, 1, 6, 6, },{ 1, 1, 2, 2, 2, },{ 1, 1, 2, 2, 3, },
													{ 1, 1, 2, 2, 4, },{ 1, 1, 2, 2, 5, },{ 1, 1, 2, 2, 6, },{ 1, 1, 2, 3, 3, },{ 1, 1, 2, 3, 4, },{ 1, 1, 2, 3, 5, },{ 1, 1, 2, 3, 6, },{ 1, 1, 2, 4, 4, },{ 1, 1, 2, 4, 5, },{ 1, 1, 2, 4, 6, },{ 1, 1, 2, 5, 5, },
													{ 1, 1, 2, 5, 6, },{ 1, 1, 2, 6, 6, },{ 1, 1, 3, 3, 3, },{ 1, 1, 3, 3, 4, },{ 1, 1, 3, 3, 5, },{ 1, 1, 3, 3, 6, },{ 1, 1, 3, 4, 4, },{ 1, 1, 3, 4, 5, },{ 1, 1, 3, 4, 6, },{ 1, 1, 3, 5, 5, },{ 1, 1, 3, 5, 6, },{ 1, 1, 3, 6, 6, },
													{ 1, 1, 4, 4, 4, },{ 1, 1, 4, 4, 5, },{ 1, 1, 4, 4, 6, },{ 1, 1, 4, 5, 5, },{ 1, 1, 4, 5, 6, },{ 1, 1, 4, 6, 6, },{ 1, 1, 5, 5, 5, },{ 1, 1, 5, 5, 6, },{ 1, 1, 5, 6, 6, },{ 1, 1, 6, 6, 6, },{ 1, 2, 2, 2, 2, },{ 1, 2, 2, 2, 3, },{ 1, 2, 2, 2, 4, },{ 1, 2, 2, 2, 5, },
													{ 1, 2, 2, 2, 6, },{ 1, 2, 2, 3, 3, },{ 1, 2, 2, 3, 4, },{ 1, 2, 2, 3, 5, },{ 1, 2, 2, 3, 6, },{ 1, 2, 2, 4, 4, },{ 1, 2, 2, 4, 5, },{ 1, 2, 2, 4, 6, },
													{ 1, 2, 2, 5, 5, },{ 1, 2, 2, 5, 6, },{ 1, 2, 2, 6, 6, },{ 1, 2, 3, 3, 3, },{ 1, 2, 3, 3, 4, },{ 1, 2, 3, 3, 5, },{ 1, 2, 3, 3, 6, },{ 1, 2, 3, 4, 4, },{ 1, 2, 3, 4, 5, },{ 1, 2, 3, 4, 6, },{ 1, 2, 3, 5, 5, },{ 1, 2, 3, 5, 6, },{ 1, 2, 3, 6, 6, },
													{ 1, 2, 4, 4, 4, },{ 1, 2, 4, 4, 5, },{ 1, 2, 4, 4, 6, },{ 1, 2, 4, 5, 5, },{ 1, 2, 4, 5, 6, },{ 1, 2, 4, 6, 6, },{ 1, 2, 5, 5, 5, },{ 1, 2, 5, 5, 6, },
													{ 1, 2, 5, 6, 6, },{ 1, 2, 6, 6, 6, },{ 1, 3, 3, 3, 3, },{ 1, 3, 3, 3, 4, },{ 1, 3, 3, 3, 5, },{ 1, 3, 3, 3, 6, },{ 1, 3, 3, 4, 4, },{ 1, 3, 3, 4, 5, },{ 1, 3, 3, 4, 6, },{ 1, 3, 3, 5, 5, },{ 1, 3, 3, 5, 6, },{ 1, 3, 3, 6, 6, },
													{ 1, 3, 4, 4, 4, },{ 1, 3, 4, 4, 5, },{ 1, 3, 4, 4, 6, },{ 1, 3, 4, 5, 5, },{ 1, 3, 4, 5, 6, },{ 1, 3, 4, 6, 6, },{ 1, 3, 5, 5, 5, },
													{ 1, 3, 5, 5, 6, },{ 1, 3, 5, 6, 6, },{ 1, 3, 6, 6, 6, },{ 1, 4, 4, 4, 4, },{ 1, 4, 4, 4, 5, },{ 1, 4, 4, 4, 6, },{ 1, 4, 4, 5, 5, },{ 1, 4, 4, 5, 6, },
													{ 1, 4, 4, 6, 6, },{ 1, 4, 5, 5, 5, },{ 1, 4, 5, 5, 6, },{ 1, 4, 5, 6, 6, },{ 1, 4, 6, 6, 6, },{ 1, 5, 5, 5, 5, },{ 1, 5, 5, 5, 6, },{ 1, 5, 5, 6, 6, },{ 1, 5, 6, 6, 6, },{ 1, 6, 6, 6, 6, },{ 2, 2, 2, 2, 2, },{ 2, 2, 2, 2, 3, },{ 2, 2, 2, 2, 4, },{ 2, 2, 2, 2, 5, },
													{ 2, 2, 2, 2, 6, },{ 2, 2, 2, 3, 3, },{ 2, 2, 2, 3, 4, },{ 2, 2, 2, 3, 5, },{ 2, 2, 2, 3, 6, },{ 2, 2, 2, 4, 4, },{ 2, 2, 2, 4, 5, },{ 2, 2, 2, 4, 6, },{ 2, 2, 2, 5, 5, },{ 2, 2, 2, 5, 6, },{ 2, 2, 2, 6, 6, },{ 2, 2, 3, 3, 3, },{ 2, 2, 3, 3, 4, },{ 2, 2, 3, 3, 5, },
													{ 2, 2, 3, 3, 6, },{ 2, 2, 3, 4, 4, },{ 2, 2, 3, 4, 5, },{ 2, 2, 3, 4, 6, },{ 2, 2, 3, 5, 5, },{ 2, 2, 3, 5, 6, },{ 2, 2, 3, 6, 6, },{ 2, 2, 4, 4, 4, },{ 2, 2, 4, 4, 5, },{ 2, 2, 4, 4, 6, },{ 2, 2, 4, 5, 5, },{ 2, 2, 4, 5, 6, },{ 2, 2, 4, 6, 6, },{ 2, 2, 5, 5, 5, },
													{ 2, 2, 5, 5, 6, },{ 2, 2, 5, 6, 6, },{ 2, 2, 6, 6, 6, },{ 2, 3, 3, 3, 3, },{ 2, 3, 3, 3, 4, },{ 2, 3, 3, 3, 5, },{ 2, 3, 3, 3, 6, },{ 2, 3, 3, 4, 4, },{ 2, 3, 3, 4, 5, },{ 2, 3, 3, 4, 6, },{ 2, 3, 3, 5, 5, },{ 2, 3, 3, 5, 6, },{ 2, 3, 3, 6, 6, },{ 2, 3, 4, 4, 4, },
													{ 2, 3, 4, 4, 5, },{ 2, 3, 4, 4, 6, },{ 2, 3, 4, 5, 5, },{ 2, 3, 4, 5, 6, },{ 2, 3, 4, 6, 6, },{ 2, 3, 5, 5, 5, },{ 2, 3, 5, 5, 6, },{ 2, 3, 5, 6, 6, },
													{ 2, 3, 6, 6, 6, },{ 2, 4, 4, 4, 4, },{ 2, 4, 4, 4, 5, },{ 2, 4, 4, 4, 6, },{ 2, 4, 4, 5, 5, },{ 2, 4, 4, 5, 6, },{ 2, 4, 4, 6, 6, },{ 2, 4, 5, 5, 5, },{ 2, 4, 5, 5, 6, },{ 2, 4, 5, 6, 6, },{ 2, 4, 6, 6, 6, },{ 2, 5, 5, 5, 5, },{ 2, 5, 5, 5, 6, },{ 2, 5, 5, 6, 6, },
													{ 2, 5, 6, 6, 6, },{ 2, 6, 6, 6, 6, },{ 3, 3, 3, 3, 3, },{ 3, 3, 3, 3, 4, },{ 3, 3, 3, 3, 5, },{ 3, 3, 3, 3, 6, },{ 3, 3, 3, 4, 4, },{ 3, 3, 3, 4, 5, },
													{ 3, 3, 3, 4, 6, },{ 3, 3, 3, 5, 5, },{ 3, 3, 3, 5, 6, },{ 3, 3, 3, 6, 6, },{ 3, 3, 4, 4, 4, },{ 3, 3, 4, 4, 5, },{ 3, 3, 4, 4, 6, },{ 3, 3, 4, 5, 5, },{ 3, 3, 4, 5, 6, },
													{ 3, 3, 4, 6, 6, },{ 3, 3, 5, 5, 5, },{ 3, 3, 5, 5, 6, },{ 3, 3, 5, 6, 6, },{ 3, 3, 6, 6, 6, },{ 3, 4, 4, 4, 4, },{ 3, 4, 4, 4, 5, },{ 3, 4, 4, 4, 6, },{ 3, 4, 4, 5, 5, },
													{ 3, 4, 4, 5, 6, },{ 3, 4, 4, 6, 6, },{ 3, 4, 5, 5, 5, },{ 3, 4, 5, 5, 6, },{ 3, 4, 5, 6, 6, },{ 3, 4, 6, 6, 6, },{ 3, 5, 5, 5, 5, },{ 3, 5, 5, 5, 6, },{ 3, 5, 5, 6, 6, },{ 3, 5, 6, 6, 6, },{ 3, 6, 6, 6, 6, },
													{ 4, 4, 4, 4, 4, },{ 4, 4, 4, 4, 5, },{ 4, 4, 4, 4, 6, },{ 4, 4, 4, 5, 5, },{ 4, 4, 4, 5, 6, },{ 4, 4, 4, 6, 6, },{ 4, 4, 5, 5, 5, },{ 4, 4, 5, 5, 6, },{ 4, 4, 5, 6, 6, },{ 4, 4, 6, 6, 6, },
													{ 4, 5, 5, 5, 5, },{ 4, 5, 5, 5, 6, },{ 4, 5, 5, 6, 6, },{ 4, 5, 6, 6, 6, },{ 4, 6, 6, 6, 6, },{ 5, 5, 5, 5, 5, },{ 5, 5, 5, 5, 6, },{ 5, 5, 5, 6, 6, },{ 5, 5, 6, 6, 6, },{ 5, 6, 6, 6, 6, },{ 6, 6, 6, 6, 6, }}; 
    public int round;
	public GameState myState; // 내 팀의 현재 상태
    public GameState oppState; // 상대 팀의 현재 상태
    private int selectRule;
    private int selectDice; 
    private int AB;
    public int scoreCount;
    public Game() {
        myState = new GameState();
        oppState = new GameState();
        round = 1;
        scoreCount = 2;
    }

	public static void setPossibleDiceStateLocation()
	{
		for(int i1=1;i1<=6;i1++)
		{
			for(int i2=1;i2<=6;i2++)
			{
				for(int i3=1;i3<=6;i3++)
				{
					for(int i4=1;i4<=6;i4++)
					{
						for(int i5=1;i5<=6;i5++)
						{
							int[] temp = { i1, i2, i3, i4, i5 };
	                        Arrays.sort(temp);
							for(int j=0;j<possibleDiceState.length;j++)
							{
								if(Arrays.equals(temp, possibleDiceState[j]))
								{
									possibleDiceStateLocation[i1][i2][i3][i4][i5] = j;
									break;
								}
							}
						}
					}
				}
			}
		}
	}
    
    private int findMax(int dice[],boolean[] category , int level, int number, int sectionSum)
    {
    	String zipPath = "C:/Users/kimye/eclipse-workspace/NYPC/src/total.zip";   // ZIP 파일 경로
		boolean read = false;
		int max = -1;
		if(level < 2)
		{
			read = true;
		}
        try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
        {
    		for(int j=1; j<category.length; j++) // 남아있는 규칙이 뭔지 파악
    		{
    			expectedValue = new int[252][64];
    			int temp = 0;
    			if(!category[j])
    			{
                    boolean[] Tempcategory = Arrays.copyOf(category, category.length);
                    Tempcategory[j] = true;
            		temp = number | (1 << j);
            		int index = Combination.location[temp];
            		index = index + levelStartLocation[level+1];
            		String targetFile = "output"+index+".bin"; 
                    ZipEntry entry = zipFile.getEntry(targetFile); // 내부 파일 찾기
                    if (entry == null) 
                    {
                        System.out.println("ZIP 안에 " + targetFile + " 파일이 없습니다.");
                    }
                    int defaultSelectedBasicRule = 0; //기본 규칙에서 무엇이 선택되었는지를 구함
        			String selectedBasicRule = "0";
        			for(int k=1; k<=6; k++)
        			{
        				if(Tempcategory[k])
        				{
        					selectedBasicRule = selectedBasicRule + String.valueOf(k);
        				}
        			}
        			defaultSelectedBasicRule = defaultSelectedBasicRule + Integer.valueOf(selectedBasicRule);
        			int length = String.valueOf(defaultSelectedBasicRule).length();
        			PossibleSum possibleSum = null;
        			for(PossibleSum element : PossibleBasicSum.reachableStates[length]) // 선택된 기본 규칙 하에서 가능한 합을 찾음
        			{
        				if(element.number == defaultSelectedBasicRule)
        				{
        					possibleSum = element;
        					break;
        				}
        			}
                    try (InputStream in = zipFile.getInputStream(entry);DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
                    {
        				for(int k=0;k<252;k++)
        				{
        					for(int s=0;s<64;s++)
        					{
        						if(possibleSum.score[s])
        						{
        							if(read)
        							{
            							expectedValue[k][s] = din.readChar() * 10;
        							}
        							else
        							{
        								expectedValue[k][s] = din.read() * 1000;
        							}
        						}
        					}
        				}
        				if (din.read() != -1)
        					throw new IOException("파일 길이가 예상보다 깁니다: " + targetFile);
                    }
            		int[] answer;
            		int value;
            		int afterSectionSum;
                	for(int i=0;i<252;i++) // 10C5의 경우의 수를 나타낸 배열
                	{
                	    selectDiceNumber[0] = dice[TenCFive.PICK_IDX[i][0]];
                	    selectDiceNumber[1] = dice[TenCFive.PICK_IDX[i][1]];
                	    selectDiceNumber[2] = dice[TenCFive.PICK_IDX[i][2]];
                	    selectDiceNumber[3] = dice[TenCFive.PICK_IDX[i][3]];
                	    selectDiceNumber[4] = dice[TenCFive.PICK_IDX[i][4]];
                	    leftNumber[0] = dice[TenCFive.UNPICK_IDX[i][0]];
                	    leftNumber[1] = dice[TenCFive.UNPICK_IDX[i][1]];
                	    leftNumber[2] = dice[TenCFive.UNPICK_IDX[i][2]];
                	    leftNumber[3] = dice[TenCFive.UNPICK_IDX[i][3]];
                	    leftNumber[4] = dice[TenCFive.UNPICK_IDX[i][4]];
                	    
                		int location = possibleDiceStateLocation[leftNumber[0]][leftNumber[1]][leftNumber[2]][leftNumber[3]][leftNumber[4]];
                		int selectLocation = possibleDiceStateLocation[selectDiceNumber[0]][selectDiceNumber[1]][selectDiceNumber[2]][selectDiceNumber[3]][selectDiceNumber[4]];
                		answer = ScoreMemozation.memozation[selectLocation][sectionSum][j];
                		value = answer[0];
                		afterSectionSum = answer[1];
                		int nextStateMaxExpectedValue = expectedValue[location][afterSectionSum];
                		/*
                		System.out.println("계산결과 : " + value);
                		System.out.println("다음값 : " + nextStateMaxExpectedValue);
                		*/
                		if(max < value + nextStateMaxExpectedValue)
                		{
                			selectRule = j;
                			selectDice = selectLocation;
                    		max = value + nextStateMaxExpectedValue;
                		}
                	}
    			}
    		}
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.out.println("오류발생");
        }
    	return max;
    }
    
    private int[] setState(GameState state, boolean[] category,int basic,int mask)
    {
    	int answer[] = new int[2];
        for (int i = 0; i < 6; i++)
        {
            if (state.ruleScore[i].isPresent())
            {
            	category[i+1] = true;
                basic += state.ruleScore[i].get();
				mask |= (1 << (i+1));
            }
        }
        for (int i = 6; i < 12; i++)
        {
            if (state.ruleScore[i].isPresent())
            {
            	category[i+1] = true;
				mask |= (1 << (i+1));
            }
        }
        basic = basic / 1000;
        if(basic >= 63)
        {
        	basic = 63;
        }
        answer[0] = basic;
        answer[1] = mask;
        return answer;
    }
    
    // ================================ [필수 구현] ================================
    // ============================================================================
    // 주사위가 주어졌을 때, 어디에 얼마만큼 베팅할지 정하는 함수
    // 입찰할 그룹과 베팅 금액을 pair로 묶어서 반환
    // ============================================================================
    public Bid calculateBid(int[] diceA, int[] diceB) 
    {
		Combination.setCombinationLocation(12, round - 1);
    	if(round == 1)
    	{
            int diceALocation = possibleDiceStateLocation[diceA[0]][diceA[1]][diceA[2]][diceA[3]][diceA[4]];
            int diceBLocation = possibleDiceStateLocation[diceB[0]][diceB[1]][diceB[2]][diceB[3]][diceB[4]];
    		String zipPath = "C:/Users/kimye/eclipse-workspace/NYPC/src/total.zip";   // ZIP 파일 경로
            try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
            {
		        String targetFile = "output"+0+".bin"; 
	            ZipEntry entry = zipFile.getEntry(targetFile); // 내부 파일 찾기
	            if (entry == null) 
	            {
	                System.out.println("ZIP 안에 " + targetFile + " 파일이 없습니다.");
	            }
	            try (InputStream in = zipFile.getInputStream(entry);
	            		DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
	            {
					for(int j=0;j<252;j++)
					{
							expectedValue[j][0] = din.readChar() * 10;
					}
					if (din.read() != -1)
						throw new IOException("파일 길이가 예상보다 깁니다: " + targetFile);
	            }
            } 
            catch (IOException e) 
            {
				e.printStackTrace();
			}
            int diceAExpectedValue = expectedValue[diceALocation][0];
            int diceBExpectedValue = expectedValue[diceBLocation][0];
            if(diceAExpectedValue > diceBExpectedValue)
            {
            	int amount = (diceAExpectedValue - diceBExpectedValue) / 2;
                return new Bid('A', amount);
            }
            else
            {
            	int amount = (diceBExpectedValue - diceAExpectedValue) / 2;
                return new Bid('B', amount);
            }
    	}
    	else
    	{
    		int maxValue[][] = new int[2][2];
    		int myPrevDice[]= new int[5];
    		int oppPrevDice[]= new int[5];
    		int start = 0;
    		int basic = 0;
    		boolean[] category = new boolean[13];
			category[0] = true;
			int mask = 1;
    		for(Integer element : myState.dice)
    		{
    			myPrevDice[start] = element;
    			++start;
    		}
			int[] answer = setState(myState, category, basic, mask);
			basic = answer[0];
			mask = answer[1];
        	int[] total = new int[10];
        	System.arraycopy(myPrevDice,0,total,0,5);
        	System.arraycopy(diceA,0,total,5,5);
        	maxValue[0][0] = findMax(total, category, round-2, mask, basic);
        	selectCategory[0][0] = selectRule;
        	selectDiceLocation[0][0] = selectDice;
        	total = new int[10];
        	System.arraycopy(myPrevDice,0,total,0,5);
        	System.arraycopy(diceB,0,total,5,5);
        	maxValue[0][1] = findMax(total, category, round-2, mask, basic);
        	selectCategory[0][1] = selectRule;
        	selectDiceLocation[0][1] = selectDice;
    		start = 0;
    		basic = 0;
    		category = new boolean[13];
			category[0] = true;
			mask = 1;
    		for(Integer element : oppState.dice)
    		{
    			oppPrevDice[start] = element;
    			++start;
    		}
			answer = setState(oppState, category, basic, mask);
			basic = answer[0];
			mask = answer[1];
        	total = new int[10];
        	System.arraycopy(oppPrevDice,0,total,0,5);
        	System.arraycopy(diceA,0,total,5,5);
        	maxValue[1][0] = findMax(total, category, round-2, mask, basic);
        	selectCategory[1][0] = selectRule;
        	selectDiceLocation[1][0] = selectDice;
        	total = new int[10];
        	System.arraycopy(oppPrevDice,0,total,0,5);
        	System.arraycopy(diceB,0,total,5,5);
        	maxValue[1][1] = findMax(total, category, round-2, mask, basic);
        	selectCategory[1][1] = selectRule;
        	selectDiceLocation[1][1] = selectDice;
        	System.out.println(maxValue[0][0] + " " + (maxValue[0][0] + myState.getTotalScore()));
        	System.out.println(maxValue[0][1] + " " + (maxValue[0][1] + myState.getTotalScore()));
        	System.out.println(maxValue[1][0] + " " + (maxValue[1][0] + oppState.getTotalScore()));
        	System.out.println(maxValue[1][1] + " " + (maxValue[1][1] + oppState.getTotalScore()));

        	int A = maxValue[0][0] - maxValue[1][1];
        	int B = maxValue[0][1] - maxValue[1][0];
        	if(A > B)
        	{
        		int amount = (A - B)/4;
        		return new Bid('A', amount);
        	}
        	else
        	{
        		int amount = (B - A)/4;
        		return new Bid('B', amount);
        	}
    	}    	
    }

    // ============================================================================
    // 주어진 주사위에 대해 사용할 규칙과 주사위를 정하는 함수
    // 사용할 규칙과 사용할 주사위의 목록을 pair로 묶어서 반환
    // ============================================================================
    public DicePut calculatePut() 
    {
    	if(scoreCount == 13)
    	{
    		int myPrevDice[]= new int[5];
    		int start = 0;
    		for(Integer element : myState.dice)
    		{
    			myPrevDice[start] = element;
    			++start;
    		}
        	int category = -1;
            for (int i = 0; i < 12; i++)
            {
                if (!myState.ruleScore[i].isPresent())
                {
                	category = i;
                	break;
                }
            }
            return new DicePut(DiceRule.values()[category],myPrevDice);
    	}
    	else
    	{
        	if(AB == 0)
        	{
        		return new DicePut(DiceRule.values()[selectCategory[0][0] - 1], Arrays.copyOf(possibleDiceState[selectDiceLocation[0][0]], possibleDiceState[selectDiceLocation[0][0]].length));
        	}
        	else if(AB == 1)
        	{
        		return new DicePut(DiceRule.values()[selectCategory[0][1] - 1], Arrays.copyOf(possibleDiceState[selectDiceLocation[0][1]], possibleDiceState[selectDiceLocation[0][1]].length));
        	}
    	}
        return null;
    }
    // ============================== [필수 구현 끝] ==============================

    // 입찰 결과를 받아서 상태 업데이트
    public void updateGet(int[] diceA, int[] diceB, Bid myBid, Bid oppBid, char myGroup) {
        // 그룹에 따라 주사위 분배
        if (myGroup == 'A') {
        	AB = 0;
            myState.addDice(Arrays.copyOf(diceA, diceA.length));
            oppState.addDice(Arrays.copyOf(diceB, diceB.length));
        } else {
        	AB = 1;
            myState.addDice(Arrays.copyOf(diceB, diceB.length));
            oppState.addDice(Arrays.copyOf(diceA, diceA.length));
        }

        // 입찰 결과에 따른 점수 반영
        boolean myBidOk = myBid.group == myGroup;
        myState.bid(myBidOk, myBid.amount);

        char oppGroup = myGroup == 'A' ? 'B' : 'A';
        boolean oppBidOk = oppBid.group == oppGroup;
        oppState.bid(oppBidOk, oppBid.amount);
    }

    // 내가 주사위를 배치한 결과 반영
    public void updatePut(DicePut put) {
        myState.useDice(put);
    }

    // 상대가 주사위를 배치한 결과 반영
    public void updateSet(DicePut put) {
        oppState.useDice(put);
    }
}

// 팀의 현재 상태를 관리하는 구조체
class GameState {
    public List<Integer> dice; // 현재 보유한 주사위 목록
    public Optional<Integer>[] ruleScore; // 각 규칙별 획득 점수 (사용하지 않았다면 empty)
    public int bidScore; // 입찰로 얻거나 잃은 총 점수
    
    // 처음에 사용하지 않은 상태로 score를 초기화
    @SuppressWarnings("unchecked")
    public GameState() {
        dice = new ArrayList<>();
        ruleScore = (Optional<Integer>[]) new Optional<?>[12];
        Arrays.fill(ruleScore, Optional.empty());
        bidScore = 0;
    }

    // 현재까지 획득한 총 점수 계산 (상단/하단 점수 + 보너스 + 입찰 점수)
    public int getTotalScore() {
        int basic = 0, combination = 0, bonus = 0;

        // 기본 점수 규칙 계산 (ONE ~ SIX)
        for (int i = 0; i < 6; i++)
            if (ruleScore[i].isPresent())
                basic += ruleScore[i].get();
        // 보너스 점수 계산 (기본 규칙 63000점 이상시 35000점 보너스)
        if (basic >= 63000)
            bonus += 35000;
        // 조합 점수 규칙 계산 (CHOICE ~ YACHT)
        for (int i = 6; i < 12; i++)
            if (ruleScore[i].isPresent())
                combination += ruleScore[i].get();

        return basic + bonus + combination + bidScore;
    }

    // 입찰 결과에 따른 점수 반영
    public void bid(boolean isSuccessful, int amount) {
        if (isSuccessful)
            bidScore -= amount; // 성공시 베팅 금액만큼 점수 차감
        else
            bidScore += amount; // 실패시 베팅 금액만큼 점수 획득
    }

    // 주사위 획득
    public void addDice(int[] newDice) {
        for (int d : newDice)
            dice.add(d);
    }

    // 주사위 사용
    public void useDice(DicePut put) {
        // 이미 사용한 규칙인지 확인
        assert !ruleScore[put.rule.ordinal()].isPresent() : "Rule already used";

        for (int d : put.dice) {
            // 주사위 목록에 없는 주사위가 있는지 확인하고 주사위 제거
            int index = dice.indexOf(d);
            assert index != -1 : "Invalid dice";
            dice.remove(index);
        }

        // 해당 규칙의 점수 계산 및 저장
        ruleScore[put.rule.ordinal()] = Optional.of(calculateScore(put));
    }

    // 규칙에 따른 점수를 계산하는 함수
    public static int calculateScore(DicePut put) {
        DiceRule rule = put.rule;
        int[] dice = put.dice;

        switch (rule) {
            // 기본 규칙 점수 계산 (해당 숫자의 개수 × 숫자 × 1000점)
            case ONE:
                return (int) Arrays.stream(dice).filter(x -> x == 1).count() * 1 * 1000;
            case TWO:
                return (int) Arrays.stream(dice).filter(x -> x == 2).count() * 2 * 1000;
            case THREE:
                return (int) Arrays.stream(dice).filter(x -> x == 3).count() * 3 * 1000;
            case FOUR:
                return (int) Arrays.stream(dice).filter(x -> x == 4).count() * 4 * 1000;
            case FIVE:
                return (int) Arrays.stream(dice).filter(x -> x == 5).count() * 5 * 1000;
            case SIX:
                return (int) Arrays.stream(dice).filter(x -> x == 6).count() * 6 * 1000;

            case CHOICE: // 주사위에 적힌 모든 수의 합 × 1000점
                return Arrays.stream(dice).sum() * 1000;
            case FOUR_OF_A_KIND: { // 같은 수가 적힌 주사위가 4개 있다면, 주사위에 적힌 모든 수의 합 × 1000점, 아니면 0
                boolean ok = false;
                for (int i = 1; i <= 6; i++) {
                    final int num = i;
                    if (Arrays.stream(dice).filter(x -> x == num).count() >= 4)
                        ok = true;
                }
                return ok ? Arrays.stream(dice).sum() * 1000 : 0;
            }
            case FULL_HOUSE: { // 3개의 주사위에 적힌 수가 서로 같고, 다른 2개의 주사위에 적힌 수도 서로 같으면 주사위에 적힌 모든 수의 합 × 1000점, 아닐 경우
                               // 0점
                boolean pair = false, triple = false;
                for (int i = 1; i <= 6; i++) {
                    final int num = i;
                    long cnt = Arrays.stream(dice).filter(x -> x == num).count();
                    // 5개 모두 같은 숫자일 때도 인정
                    if (cnt == 2 || cnt == 5)
                        pair = true;
                    if (cnt == 3 || cnt == 5)
                        triple = true;
                }
                return (pair && triple) ? Arrays.stream(dice).sum() * 1000 : 0;
            }
            case SMALL_STRAIGHT: { // 4개의 주사위에 적힌 수가 1234, 2345, 3456중 하나로 연속되어 있을 때, 15000점, 아닐 경우 0점
                boolean e1 = Arrays.stream(dice).anyMatch(x -> x == 1);
                boolean e2 = Arrays.stream(dice).anyMatch(x -> x == 2);
                boolean e3 = Arrays.stream(dice).anyMatch(x -> x == 3);
                boolean e4 = Arrays.stream(dice).anyMatch(x -> x == 4);
                boolean e5 = Arrays.stream(dice).anyMatch(x -> x == 5);
                boolean e6 = Arrays.stream(dice).anyMatch(x -> x == 6);
                boolean ok = (e1 && e2 && e3 && e4) || (e2 && e3 && e4 && e5) ||
                        (e3 && e4 && e5 && e6);
                return ok ? 15000 : 0;
            }
            case LARGE_STRAIGHT: { // 5개의 주사위에 적힌 수가 12345,23456중 하나로 연속되어 있을 때, 30000점, 아닐 경우 0점
                boolean e1 = Arrays.stream(dice).anyMatch(x -> x == 1);
                boolean e2 = Arrays.stream(dice).anyMatch(x -> x == 2);
                boolean e3 = Arrays.stream(dice).anyMatch(x -> x == 3);
                boolean e4 = Arrays.stream(dice).anyMatch(x -> x == 4);
                boolean e5 = Arrays.stream(dice).anyMatch(x -> x == 5);
                boolean e6 = Arrays.stream(dice).anyMatch(x -> x == 6);
                boolean ok = (e1 && e2 && e3 && e4 && e5) || (e2 && e3 && e4 && e5 && e6);
                return ok ? 30000 : 0;
            }
            case YACHT: { // 5개의 주사위에 적힌 수가 모두 같을 때 50000점, 아닐 경우 0점
                boolean ok = false;
                for (int i = 1; i <= 6; i++) {
                    final int num = i;
                    if (Arrays.stream(dice).filter(x -> x == num).count() == 5)
                        ok = true;
                }
                return ok ? 50000 : 0;
            }
        }
        assert false;
        return 0;
    }
}

// 표준 입력을 통해 명령어를 처리하는 메인 클래스
public class Main {
    // 입출력을 위해 규칙 enum을 문자열로 변환
    static String toString(DiceRule rule) {
        switch (rule) {
            case ONE:
                return "ONE";
            case TWO:
                return "TWO";
            case THREE:
                return "THREE";
            case FOUR:
                return "FOUR";
            case FIVE:
                return "FIVE";
            case SIX:
                return "SIX";
            case CHOICE:
                return "CHOICE";
            case FOUR_OF_A_KIND:
                return "FOUR_OF_A_KIND";
            case FULL_HOUSE:
                return "FULL_HOUSE";
            case SMALL_STRAIGHT:
                return "SMALL_STRAIGHT";
            case LARGE_STRAIGHT:
                return "LARGE_STRAIGHT";
            case YACHT:
                return "YACHT";
        }
        assert false : "Invalid Dice Rule";
        return "";
    }

    // 문자열을 규칙 enum으로 변환
    static DiceRule fromString(String s) {
        if (s.equals("ONE"))
            return DiceRule.ONE;
        if (s.equals("TWO"))
            return DiceRule.TWO;
        if (s.equals("THREE"))
            return DiceRule.THREE;
        if (s.equals("FOUR"))
            return DiceRule.FOUR;
        if (s.equals("FIVE"))
            return DiceRule.FIVE;
        if (s.equals("SIX"))
            return DiceRule.SIX;
        if (s.equals("CHOICE"))
            return DiceRule.CHOICE;
        if (s.equals("FOUR_OF_A_KIND"))
            return DiceRule.FOUR_OF_A_KIND;
        if (s.equals("FULL_HOUSE"))
            return DiceRule.FULL_HOUSE;
        if (s.equals("SMALL_STRAIGHT"))
            return DiceRule.SMALL_STRAIGHT;
        if (s.equals("LARGE_STRAIGHT"))
            return DiceRule.LARGE_STRAIGHT;
        if (s.equals("YACHT"))
            return DiceRule.YACHT;
        assert false : "Invalid Dice Rule";
        return DiceRule.ONE;
    }

    // 표준 입력을 통해 명령어를 처리하는 메인 클래스
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Game game = new Game();
		TenCFive.initializePickAndUnpickIndices();
        Game.setPossibleDiceStateLocation();
        ScoreMemozation.memo();
		int arr1[] = {1,2,3,4,5,6};
		PossibleBasicSum.findReachableStates(arr1, 6);
        // 입찰 라운드에서 나온 주사위들
        int[] diceA = new int[5], diceB = new int[5];
        // 내가 마지막으로 한 입찰 정보
        Bid myBid = new Bid();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty())
                continue;

            String[] parts = line.trim().split("\\s+");
            String command = parts[0];

            switch (command) {
                case "READY":
                    // 게임 시작
                    System.out.println("OK");
                    System.out.flush();
                    break;
                case "ROLL":
                    // 주사위 굴리기 결과 받기
                    String strA = parts[1], strB = parts[2];
                    for (int i = 0; i < strA.length(); i++)
                        diceA[i] = strA.charAt(i) - '0'; // 문자를 숫자로 변환
                    for (int i = 0; i < strB.length(); i++)
                        diceB[i] = strB.charAt(i) - '0'; // 문자를 숫자로 변환
                    myBid = game.calculateBid(diceA, diceB);
                    System.out.println("BID " + myBid.group + " " + myBid.amount);
                    System.out.flush();
                    break;

                case "GET":
                    // 주사위 받기
                    char getGroup = parts[1].charAt(0);
                    char oppGroup = parts[2].charAt(0);
                    int oppScore = Integer.parseInt(parts[3]);
                    game.updateGet(diceA, diceB, myBid, new Bid(oppGroup, oppScore), getGroup);
                    ++game.round;
                    break;

                case "SCORE":
                    // 주사위 골라서 배치하기
                    DicePut put = game.calculatePut();
                    ++game.scoreCount;
                    game.updatePut(put);
                    System.out.print("PUT " + toString(put.rule) + " ");
                    for (int d : put.dice)
                        System.out.print(d);
                    System.out.println();
                    System.out.flush();
                    break;

                case "SET":
                    // 상대의 주사위 배치
                    String rule = parts[1], str = parts[2];
                    int[] dice = str.chars().map(c -> c - '0').toArray(); // 문자를 숫자로 변환
                    game.updateSet(new DicePut(fromString(rule), dice));
                    break;

                case "FINISH":
                    // 게임 종료
                    break;

                default:
                    // 알 수 없는 명령어 처리
                    System.err.println("Invalid command: " + command);
                    System.exit(1);

            }
        }

        sc.close();
    }
}