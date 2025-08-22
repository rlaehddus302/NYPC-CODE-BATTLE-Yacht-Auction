package real;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;



public class YachtAuctionGraph 
{
	public static int[][][][][][][][] memoScore; 
	public static float probabilityDistribution[][][][] = new float[220][252][64][146];
	public static float preProbability[][][][] = new float[66][252][64][116];
	public static int tempStore[][][][][];
	public static int levelStartLocation[] = {0, 1, 13, 79, 299, 794, 1586, 2510, 3302, 3797, 4017, 4083, 4095};
	private static final int[] selectDiceNumber = new int[5];
	private static final int[] leftNumber = new int[5];
	
	static int probToU16(float p) 
	{
	    if (p <= 0f) return 0;
	    if (p >= 1f) return 65535;
	    return Math.round(p * 65535f);
	}

	static float u16ToProb(int u) 
	{
	    return (u & 0xFFFF) / 65535.0f;
	}
	
    public static List<List<Integer>> combinations(int[] arr, int r) 
    {
        List<List<Integer>> path = new ArrayList<List<Integer>>();
        List<Integer> pathElement = new ArrayList<Integer>();
        dfs(arr, r, 0, pathElement, path);
        return path;
    }

    private static void dfs(int[] arr, int r, int start, List<Integer> pathElement ,List<List<Integer>> path) 
    {
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
    
    public static int findMax(int firstLocation, int secondLocation, boolean[] category, int level, int number, int sectionSum, int categoryLocation)
    {
    	int possSum = 0;
		int temp;
		int[] answer;
		int value;
		int afterSectionSum;
		int firstArray[] = PossibleDiceState.possibleDiceState[firstLocation];
		int secondArray[] = PossibleDiceState.possibleDiceState[secondLocation];
		int total[] = new int [10];
		System.arraycopy(firstArray, 0, total, 0, firstArray.length);
		System.arraycopy(secondArray, 0, total, firstArray.length, secondArray.length);
		List<List<Integer>> result = UniqueCombination.getUniqueCombinations(total, 5);
		possSum = result.size()*3;
		boolean judge = true;
		if(firstLocation == secondLocation)
		{
			judge = false;
		}
        for(List<Integer> select : result)
        {
        	List<Integer> remaining = UniqueCombination.getRemaining(total, select);
        	int start = 0;
            for(Integer element : select)
            {
            	selectDiceNumber[start] = element;
            	++start;
            }
            start = 0;
            for(Integer element : remaining)
            {
            	leftNumber[start] = element;
            	++start;
            }
    		int location = PossibleDiceState.possibleDiceStateLocation[leftNumber[0]][leftNumber[1]][leftNumber[2]][leftNumber[3]][leftNumber[4]];
    		for(int j=1; j<category.length; j++) // 남아있는 규칙이 뭔지 파악
    		{
    			if(!category[j])// 아직 선택 안한 규칙이라면 선택해서 계산.
    			{
            		answer = memoScore[selectDiceNumber[0]][selectDiceNumber[1]][selectDiceNumber[2]][selectDiceNumber[3]][selectDiceNumber[4]][sectionSum][j];
            		value = answer[0] / 1000;
            		afterSectionSum = answer[1];
            		temp = number | (1 << j);
            		int index = Combination.location[temp];
            		for(int s=0;s<116;s++)
            		{
            			float prob = preProbability[index][location][afterSectionSum][s];
            			if(prob != 0)
            			{
                			probabilityDistribution[categoryLocation][firstLocation][sectionSum][s+value] = probabilityDistribution[categoryLocation][firstLocation][sectionSum][s+value] + (float)(Widget.realPossiblity[secondLocation] * prob / possSum);
                			if(judge)
                			{
                				probabilityDistribution[categoryLocation][secondLocation][sectionSum][s+value] = probabilityDistribution[categoryLocation][secondLocation][sectionSum][s+value] + (float)(Widget.realPossiblity[firstLocation] * prob / possSum);
                			}
            			}
            		}
    			}
    		}
        }
    	return possSum;
    }
    
    
	public static void makeGraph(int level)
	{
		int arr1[] = {1,2,3,4,5,6};
		PossibleBasicSum.findReachableStates(arr1, 6);
		int arr[]= {1,2,3,4,5,6,7,8,9,10,11,12};
		List<List<Integer>> selectedCategories = combinations(arr,level); //규칙 카테고리중에 이미 선택된 경우의 수를 구함
		int start = 0;
		Combination.setCombinationLocation(12, level+1);
		for(List<Integer> selectedCategory : selectedCategories) //구한 각각의 규칙 카테고리를 차례대로 시작
		{
	        long timestart = System.currentTimeMillis();
			boolean[] category = new boolean[13];
			category[0] = true;
			int mask = 1;
			for(Integer element : selectedCategory)
			{
				category[element] = true;
				mask |= (1 << element);
			}
			int defaultSelectedBasicRule = 0; //기본 규칙에서 무엇이 선택되었는지를 구함
			String selectedBasicRule = "0";
			for(int i=1; i<=6; i++)
			{
				if(category[i])
				{
					selectedBasicRule = selectedBasicRule + String.valueOf(i);
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
			for(int i=0;i<possibleSum.score.length;i++) // 가능한 합들을 순서대로 실행
			{
				if(possibleSum.score[i])
				{
		    		for(int j=0;j<252;j++)
		    		{
		    			for(int k=j;k<252;k++)
		    			{
			    			findMax(j, k, category, level, mask, i, start);
		    			}
		    		}
				}
				//System.out.println(i);
			}
	        long timeend = System.currentTimeMillis();
	        System.out.println("걸린 시간: " + (timeend - timestart) + " ms");
			++start;
		}
	}
    
    /*
     
    public static double findMax(int dice[],boolean[] category , int level, int number, int sectionSum)
    {
    	double max = 0;
    	int start = 0;
		int temp;
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
    	    
    		int location = PossibleDiceState.possibleDiceStateLocation[leftNumber[0]][leftNumber[1]][leftNumber[2]][leftNumber[3]][leftNumber[4]];
    		for(int j=1; j<category.length; j++) // 남아있는 규칙이 뭔지 파악
    		{
    			if(!category[j])// 아직 선택 안한 규칙이라면 선택해서 계산.
    			{
            		answer = memoScore[selectDiceNumber[0]][selectDiceNumber[1]][selectDiceNumber[2]][selectDiceNumber[3]][selectDiceNumber[4]][sectionSum][j];
            		value = answer[0];
            		afterSectionSum = answer[1];
            		temp = number | (1 << j);
            		int index = Combination.location[temp];
            		index = index + levelStartLocation[level+1];
            		double nextStateMaxExpectedValue = expectedValue[index][location][afterSectionSum];
            		max = Math.max(max, value + nextStateMaxExpectedValue);
    			}
    		}
    	}
    	return max;
    }
     
	public static void makeGraph(int level)
	{
		int arr1[] = {1,2,3,4,5,6};
		PossibleBasicSum.findReachableStates(arr1, 6);
		int arr[]= {1,2,3,4,5,6,7,8,9,10,11,12};
		List<List<Integer>> selectedCategories = combinations(arr,level); //규칙 카테고리중에 이미 선택된 경우의 수를 구함
		int start = levelStartLocation[level];
		boolean eleven = level == 11; 
		Combination.setCombinationLocation(12, level+1);
		double storeExpectedValue[][] = new double[252][252];
		for(List<Integer> selectedCategory : selectedCategories) //구한 각각의 규칙 카테고리를 차례대로 시작
		{
			boolean[] category = new boolean[13];
			category[0] = true;
			int mask = 1;
			for(Integer element : selectedCategory)
			{
				category[element] = true;
				mask |= (1 << element);
			}
			int defaultSelectedBasicRule = 0; //기본 규칙에서 무엇이 선택되었는지를 구함
			String selectedBasicRule = "0";
			for(int i=1; i<=6; i++)
			{
				if(category[i])
				{
					selectedBasicRule = selectedBasicRule + String.valueOf(i);
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
			for(int i=0;i<possibleSum.score.length;i++) // 가능한 합들을 순서대로 실행
			{
				if(possibleSum.score[i])
				{
					if(eleven)
					{
						for(int j=0; j<PossibleDiceState.possibleDiceState.length; j++) // 나올 수 있는 주사위 수를 순서대로 실행
						{
				    		for(int k=1;k<category.length;k++)
				    		{
				    			if(!category[k])
				    			{
				    				expectedValue[start][j][i] = memoScore[PossibleDiceState.possibleDiceState[j][0]][PossibleDiceState.possibleDiceState[j][1]][PossibleDiceState.possibleDiceState[j][2]][PossibleDiceState.possibleDiceState[j][3]][PossibleDiceState.possibleDiceState[j][4]][i][k][0];
				    			}
				    		}
						}
					}
			    	else
			    	{
				    	double value = 0;
				    	double sum = 0;
				    	int firstindex;
				    	int secondindex; 
		    			storeExpectedValue = new double[252][252];
			    		for(int j=0;j<PossibleDiceState.possibleTwoDiceState.length;j++)
			    		{
			    			int total[] = PossibleDiceState.possibleTwoDiceState[j];
			    			value = findMax(total, category, level, mask, i);
			    	    	for(int k=0;k<252;k++) // 10C5의 경우의 수를 나타낸 배열
			    	    	{
			    	    	    selectDiceNumber[0] = total[TenCFive.PICK_IDX[k][0]];
			    	    	    selectDiceNumber[1] = total[TenCFive.PICK_IDX[k][1]];
			    	    	    selectDiceNumber[2] = total[TenCFive.PICK_IDX[k][2]];
			    	    	    selectDiceNumber[3] = total[TenCFive.PICK_IDX[k][3]];
			    	    	    selectDiceNumber[4] = total[TenCFive.PICK_IDX[k][4]];
			    	    	    leftNumber[0] = total[TenCFive.UNPICK_IDX[k][0]];
			    	    	    leftNumber[1] = total[TenCFive.UNPICK_IDX[k][1]];
			    	    	    leftNumber[2] = total[TenCFive.UNPICK_IDX[k][2]];
			    	    	    leftNumber[3] = total[TenCFive.UNPICK_IDX[k][3]];
			    	    	    leftNumber[4] = total[TenCFive.UNPICK_IDX[k][4]];
			    	    	    firstindex = PossibleDiceState.possibleDiceStateLocation[selectDiceNumber[0]][selectDiceNumber[1]][selectDiceNumber[2]][selectDiceNumber[3]][selectDiceNumber[4]];
			    	    	    secondindex = PossibleDiceState.possibleDiceStateLocation[leftNumber[0]][leftNumber[1]][leftNumber[2]][leftNumber[3]][leftNumber[4]];
			    	    	    storeExpectedValue[firstindex][secondindex] = value;
			    	    	}
			    		}
			    		for(int j=0;j<252;j++)
			    		{
			    			sum = 0;
				    		for(int k=0;k<252;k++)
				    		{
				    			sum = sum + storeExpectedValue[j][k] * Widget.realPossiblity[k];
				    		}
					    	expectedValue[start][j][i] = sum;
			    		}
			    	}
				}
			}
			++start;
		}
	}
	*/
	
	public static void main(String[] args) 
	{
        long timestart = System.currentTimeMillis();
		ScoreMemozation.memo();
		Widget.setRealPossiblity();
		UniqueCombination.setInitial();
		memoScore = ScoreMemozation.memozation;
		PossibleDiceState.setPossibleDiceStateLocation();
		int arr1[] = {1,2,3,4,5,6};
		PossibleBasicSum.findReachableStates(arr1, 6);
		int arr[]= {1,2,3,4,5,6,7,8,9,10,11,12};
		List<List<Integer>> selectedCategories = combinations(arr,10);
		int start = 0;
		String zipPath = "probability10.zip";   // ZIP 파일 경로
        try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
        {
			for(int i=levelStartLocation[10];i<levelStartLocation[11];i++)
			{
		        String targetFile = "probability"+i+".bin";
	            ZipEntry entry = zipFile.getEntry(targetFile); // 내부 파일 찾기
	            if (entry == null) {
	                System.out.println("ZIP 안에 " + targetFile + " 파일이 없습니다.");
	                return;
	            }
	            boolean[] category = new boolean[13];
				category[0] = true;
				List<Integer> selectedCategory = selectedCategories.get(start);
				for(Integer element : selectedCategory)
				{
					category[element] = true;
				}
				int defaultSelectedBasicRule = 0; //기본 규칙에서 무엇이 선택되었는지를 구함
				String selectedBasicRule = "0";
				for(int j=1; j<=6; j++)
				{
					if(category[j])
					{
						selectedBasicRule = selectedBasicRule + String.valueOf(j);
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
	            try (InputStream in = zipFile.getInputStream(entry);
	            		DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
	            {
					for(int j=0;j<252;j++)
					{
						for(int k=0;k<possibleSum.score.length;k++) // 가능한 합들을 순서대로 실행
						{
							if(possibleSum.score[k])
							{
								int startIndex = din.readChar();
								int endIndex = din.readChar();
								for(int s=startIndex; s<=endIndex; s++)
								{
									int value = din.readChar();
									preProbability[start][j][k][s] = u16ToProb(value);
								}
							}
						}
					}
					if (din.read() != -1)
						throw new IOException("파일 길이가 예상보다 깁니다: " + targetFile);
	            }
	            ++start;
			}          
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        long timeend = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (timeend - timestart) + " ms");
		makeGraph(9);
		int arr2[]= {1,2,3,4,5,6,7,8,9,10,11,12};
        selectedCategories = combinations(arr2,9); //규칙 카테고리중에 이미 선택된 경우의 수를 구함
		start = 0;
		for(int i=levelStartLocation[9];i<levelStartLocation[10];i++)
		{
	        String filePath = "probability"+i+".bin";
	        List<Integer> selectedCategory = selectedCategories.get(start);
			boolean[] category = new boolean[13];
			category[0] = true;
			for(Integer element : selectedCategory)
			{
				category[element] = true;
			}
			int defaultSelectedBasicRule = 0; //기본 규칙에서 무엇이 선택되었는지를 구함
			String selectedBasicRule = "0";
			for(int j=1; j<=6; j++)
			{
				if(category[j])
				{
					selectedBasicRule = selectedBasicRule + String.valueOf(j);
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
	        try (DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePath), 1 << 20))) 
	        {
				for(int j=0;j<252;j++)
				{
					for(int k=0;k<possibleSum.score.length;k++) // 가능한 합들을 순서대로 실행
					{
						if(possibleSum.score[k])
						{
							int startIndex = 0;
							int endIndex = 0;
							int s;
							for(s=0;s<146;s++)
							{
								float a = probabilityDistribution[start][j][k][s];
								if(a != 0)
								{
									startIndex = s;
									break;
								}
							}
							for(s=startIndex + 1; s<146;s++)
							{
								float a = probabilityDistribution[start][j][k][s];
								if(a != 0)
								{
									endIndex = s;
								}
							}
							if(endIndex == 0)
							{
								endIndex = startIndex;
							}
							dout.writeChar(startIndex);
							dout.writeChar(endIndex);
							for(s=startIndex;s<=endIndex;s++)
							{
								float a = probabilityDistribution[start][j][k][s];
								int value = probToU16(a);
								dout.writeChar(value);
							}
						}
					}
				}
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	        ++start;
		}
	}
}
