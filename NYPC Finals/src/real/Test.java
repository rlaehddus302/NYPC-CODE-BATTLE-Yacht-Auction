package real;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Test {
	
	public static ArrayList<Widget> graph[][][] = new ArrayList[11][252][64];
	public static boolean[][] result;
	public static int[][][][][][][][] memoScore; 
	public static double expectedValue[][][] = new double[4095][252][64];
	public static int levelStartLocation[] = {0, 1, 13, 79, 299, 794, 1586, 2510, 3302, 3797, 4017, 4083, 4095};
	private static final int[] selectDiceNumber = new int[5];
	private static final int[] leftNumber = new int[5];

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
	
    public static double findMax(int dice[], State state, int level, int number)
    {
    	double max = 0;
    	int start = 0;
    	int selectCategoryNumber = -1;
    	int[] pool = new int[10];
    	System.arraycopy(state.leftNumber,0,pool,0,5);
    	System.arraycopy(dice,0,pool,5,5);
    	int [] selectNumber = new int[5];
		int temp;
		int[] answer;
		int value;
		int sectionSum;
    	for(int i=0;i<252;i++) // 10C5의 경우의 수를 나타낸 배열
    	{
    	    selectDiceNumber[0] = pool[TenCFive.PICK_IDX[i][0]];
    	    selectDiceNumber[1] = pool[TenCFive.PICK_IDX[i][1]];
    	    selectDiceNumber[2] = pool[TenCFive.PICK_IDX[i][2]];
    	    selectDiceNumber[3] = pool[TenCFive.PICK_IDX[i][3]];
    	    selectDiceNumber[4] = pool[TenCFive.PICK_IDX[i][4]];
    	    leftNumber[0] = pool[TenCFive.UNPICK_IDX[i][0]];
    	    leftNumber[1] = pool[TenCFive.UNPICK_IDX[i][1]];
    	    leftNumber[2] = pool[TenCFive.UNPICK_IDX[i][2]];
    	    leftNumber[3] = pool[TenCFive.UNPICK_IDX[i][3]];
    	    leftNumber[4] = pool[TenCFive.UNPICK_IDX[i][4]];
    	    
    		int location = PossibleDiceState.possibleDiceStateLocation[leftNumber[0]][leftNumber[1]][leftNumber[2]][leftNumber[3]][leftNumber[4]];
    		for(int j=1; j<state.category.length; j++) // 남아있는 규칙이 뭔지 파악
    		{
    			if(!state.category[j])// 아직 선택 안한 규칙이라면 선택해서 계산.
    			{
            		answer = memoScore[selectDiceNumber[0]][selectDiceNumber[1]][selectDiceNumber[2]][selectDiceNumber[3]][selectDiceNumber[4]][state.sectionSum][j];
            		value = answer[0];
            		sectionSum = answer[1];
            		temp = number | (1 << j);
            		int index = Combination.location[temp];
            		index = index + levelStartLocation[level+1];
            		double nextStateMaxExpectedValue = expectedValue[index][location][sectionSum] * 10;
            		if(max < value + nextStateMaxExpectedValue)
            		{
            			max = value + nextStateMaxExpectedValue;
            			selectNumber[0] = selectDiceNumber[0];
            			selectNumber[1] = selectDiceNumber[1];
            			selectNumber[2] = selectDiceNumber[2];
            			selectNumber[3] = selectDiceNumber[3];
            			selectNumber[4] = selectDiceNumber[4];
            			selectCategoryNumber = j;
            		}
    			}
    		}
    	}
    	System.out.println("선택해야 하는 숫자 : " + selectNumber[0] + " " + selectNumber[1] + " " + selectNumber[2] + " "+ selectNumber[3] + " "+ selectNumber[4]);
    	System.out.println("선택해야 하는 카터고리넘버 : " + selectCategoryNumber);
    	return max;
    }
	
	public static void main(String[] args) 
	{
		ScoreMemozation.memo();
		TenCFive.initializePickAndUnpickIndices();
		memoScore = ScoreMemozation.memozation;
		PossibleDiceState.setPossibleDiceStateLocation();
		
		String zipPath = "NewOutput1.zip";   // ZIP 파일 경로
        try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
        {
			int arr1[] = {1,2,3,4,5,6};
			int start = 0;
			PossibleBasicSum.findReachableStates(arr1, 6);
			int arr[]= {1,2,3,4,5,6,7,8,9,10,11,12};
			List<List<Integer>> selectedCategories = combinations(arr,1);  
			for(int i=levelStartLocation[1];i<levelStartLocation[2];i++)
			{
		        String targetFile = "output"+i+".bin"; 
	            ZipEntry entry = zipFile.getEntry(targetFile); // 내부 파일 찾기
	            if (entry == null) {
	                System.out.println("ZIP 안에 " + targetFile + " 파일이 없습니다.");
	                return;
	            }
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
	            try (InputStream in = zipFile.getInputStream(entry);
	            		DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
	            {
					for(int j=0;j<252;j++)
					{
						for(int k=0;k<64;k++)
						{
							if(possibleSum.score[k])
							{
								expectedValue[i][j][k] = din.readChar();
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
            System.out.println("오류발생");
        }
        /*
        Scanner sc = new Scanner(System.in);

        int[] dice1 = new int[5]; // 크기 5인 배열 선언
        int[] dice2 = new int[5]; // 크기 5인 배열 선언

        System.out.println("나온 주사위 5개를 각각 입력하세요:");
        for (int i = 0; i < dice1.length; i++) {
        	dice1[i] = sc.nextInt(); // 입력받아 배열에 저장
        }
        for (int i = 0; i < dice2.length; i++) {
        	dice2[i] = sc.nextInt(); // 입력받아 배열에 저장
        }
        int dice1Location = PossibleDiceState.possibleDiceStateLocation[dice1[0]][dice1[1]][dice1[2]][dice1[3]][dice1[4]];
        int dice2Location = PossibleDiceState.possibleDiceStateLocation[dice2[0]][dice2[1]][dice2[2]][dice2[3]][dice2[4]];
        System.out.println(expectedValue[0][dice1Location][0] * 10);
        System.out.println(expectedValue[0][dice2Location][0] * 10);
        */
        Scanner sc = new Scanner(System.in);

        int[] dice1 = new int[5]; // 크기 5인 배열 선언
        int[] dice2 = new int[5]; // 크기 5인 배열 선언

        System.out.println("나온 주사위 5개를 각각 입력하세요:");
        for (int i = 0; i < dice1.length; i++) {
        	dice1[i] = sc.nextInt(); // 입력받아 배열에 저장
        }
        for (int i = 0; i < dice2.length; i++) {
        	dice2[i] = sc.nextInt(); // 입력받아 배열에 저장
        }
        int dice1Location = PossibleDiceState.possibleDiceStateLocation[dice1[0]][dice1[1]][dice1[2]][dice1[3]][dice1[4]];
        int dice2Location = PossibleDiceState.possibleDiceStateLocation[dice2[0]][dice2[1]][dice2[2]][dice2[3]][dice2[4]];
        
        int[] leftDice1 = new int[5]; // 크기 5인 배열 선언
        int[] leftDice2 = new int[5]; // 크기 5인 배열 선언

        System.out.println("남은 주사위 5개를 각각 입력하세요:");
        System.out.println("플레이어1의 남은 주사위 입력하세요:");
        for (int i = 0; i < leftDice1.length; i++) {
        	leftDice1[i] = sc.nextInt(); // 입력받아 배열에 저장
        }
        System.out.println("플레이어2의 남은 주사위 입력하세요:");
        for (int i = 0; i < leftDice2.length; i++) {
        	leftDice2[i] = sc.nextInt(); // 입력받아 배열에 저장
        }
		Combination.setCombinationLocation(12, 1);
        int leftDice1Location = PossibleDiceState.possibleDiceStateLocation[leftDice1[0]][leftDice1[1]][leftDice1[2]][leftDice1[3]][leftDice1[4]];
        int leftDice2Location = PossibleDiceState.possibleDiceStateLocation[leftDice2[0]][leftDice2[1]][leftDice2[2]][leftDice2[3]][leftDice2[4]];
        boolean category[] = {true,false,false,false,false,false,false,false,false,false,false,false,false};
        int sectionSum = 0;
		State state = new State(category, sectionSum, PossibleDiceState.possibleDiceState[leftDice1Location]);
		int mask = 0;
		for(int i=0;i<category.length;i++)
		{
			if(category[i])
			{
				mask |= (1 << i);
			}
		}
    	double value = 0;
		System.out.println("플레이어 1이 첫 번째 주사위를 선택했을 때 나오는 기대값 및 그에 따른 선택");
		value = findMax(PossibleDiceState.possibleDiceState[dice1Location],state, 0, mask);
		System.out.println("기대값 : " + value);
		System.out.println("플레이어 1이 두 번째 주사위를 선택했을 때 나오는 기대값 및 그에 따른 선택");
		value = findMax(PossibleDiceState.possibleDiceState[dice2Location],state, 0, mask);
		System.out.println("기대값 : " + value);
		mask = 0;
    	boolean category1[] = {true,false,false,false,false,false,false,false,false,false,false,false,false};
    	sectionSum = 0;
		state = new State(category1, sectionSum, PossibleDiceState.possibleDiceState[leftDice2Location]);
		for(int i=0;i<category1.length;i++)
		{
			if(category1[i])
			{
				mask |= (1 << i);
			}
		}
    	value = 0;
		System.out.println("플레이어 2이 첫 번째 주사위를 선택했을 때 나오는 기대값 및 그에 따른 선택");
		value = findMax(PossibleDiceState.possibleDiceState[dice1Location],state, 0, mask);
		System.out.println("기대값 : " + value);
		System.out.println("플레이어 2이 두 번째 주사위를 선택했을 때 나오는 기대값 및 그에 따른 선택");
		value = findMax(PossibleDiceState.possibleDiceState[dice2Location],state, 0, mask);
		System.out.println("기대값 : " + value);
        sc.close();
        /*
        String filePath = "output.txt"; // 저장할 파일 경로
        double max = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) 
        {
			for(int i=levelStartLocation[1];i<levelStartLocation[2];i++)
			{
				for(int j=0;j<252;j++)
				{
					for(int k=0;k<64;k++)
					{
						max = Math.max(max, expectedValue[i][j][k]);
			            writer.write(expectedValue[i][j][k] + " " + i + " " + j + " " + k);
			            writer.newLine(); // 줄바꿈
					}
				}
			}
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        System.out.println(max);
        /*
        boolean category[] = {true,true,true,true,true,true,true,true,true,true,true,false,false};
		State state = new State(category, 0, PossibleDiceState.possibleDiceState[163]);
		int mask = 2047;
    	double value = 0;
    	double sum = 0;
    	for(int k=0;k<PossibleDiceState.possibleDiceState.length;k++)
    	{
    		value = findMax(PossibleDiceState.possibleDiceState[k],state, 10, mask);
    		sum = sum + value * Widget.dice[k][5];
    		System.out.println(value);
    	}
    	System.out.println(sum);
    	*/
	}

}
