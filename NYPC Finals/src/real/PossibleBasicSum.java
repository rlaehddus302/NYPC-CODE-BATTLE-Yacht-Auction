package real;

import java.util.ArrayList;
import java.util.List;

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

public class PossibleBasicSum 
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
	
	public static void main(String[] args) 
	{
		int arr[] = {1,2,3,4,5,6};
		findReachableStates(arr, 6);
		int count = 0;
		for(int i=1; i<7; i++)
		{
			for(PossibleSum element : reachableStates[i])
			{
				int total = 0;
				System.out.println(element.number);
				boolean[] score = element.score;
				for(int j=0;j<64;j++)
				{
					if(score[j])
					{
						System.out.print(j + " ");
						++total;
						++count;
					}
				}
				System.out.println();
			}
		}
		System.out.println(count);
	}
	
}
