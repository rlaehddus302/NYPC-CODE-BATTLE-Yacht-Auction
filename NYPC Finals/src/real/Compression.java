package real;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Compression 
{
	public static boolean[][] result;
	public static int[][][][][][][][] memoScore; 
	public static double expectedValue[][][] = new double[4095][252][64];
	public static double expectedValue1[][][] = new double[4095][252][64];
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
	
	public static void main(String[] args) throws IOException 
	{
		ScoreMemozation.memo();
		TenCFive.initializePickAndUnpickIndices();
		memoScore = ScoreMemozation.memozation;
		PossibleDiceState.setPossibleDiceStateLocation();
	
		String zipPath = "output8.zip";   // ZIP 파일 경로
        try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
        {
			for(int i=levelStartLocation[8];i<levelStartLocation[9];i++)
			{
		        String targetFile = "output"+i+".bin"; 
	            ZipEntry entry = zipFile.getEntry(targetFile); // 내부 파일 찾기
	            if (entry == null) {
	                System.out.println("ZIP 안에 " + targetFile + " 파일이 없습니다.");
	                return;
	            }
	            try (InputStream in = zipFile.getInputStream(entry);
	            		DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
	            {
					for(int j=0;j<252;j++)
					{
						for(int k=0;k<64;k++)
						{
							expectedValue[i][j][k] = din.readFloat();
						}
					}
					if (din.read() != -1)
						throw new IOException("파일 길이가 예상보다 깁니다: " + targetFile);
	            }
			}          
        }
		
		zipPath = "NewPos8.zip";   // ZIP 파일 경로
        try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
        {
			for(int i=levelStartLocation[8];i<levelStartLocation[9];i++)
			{
		        String targetFile = "output"+i+".bin"; 
	            ZipEntry entry = zipFile.getEntry(targetFile); // 내부 파일 찾기
	            if (entry == null) {
	                System.out.println("ZIP 안에 " + targetFile + " 파일이 없습니다.");
	                return;
	            }
	            try (InputStream in = zipFile.getInputStream(entry);
	            		DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
	            {
					for(int j=0;j<252;j++)
					{
						for(int k=0;k<64;k++)
						{
							expectedValue1[i][j][k] = din.readDouble();
						}
					}
					if (din.read() != -1)
						throw new IOException("파일 길이가 예상보다 깁니다: " + targetFile);
	            }
			}          
        }
        int max = 0;
		int arr1[] = {1,2,3,4,5,6};
		int start = 0;
		PossibleBasicSum.findReachableStates(arr1, 6);
		int arr[]= {1,2,3,4,5,6,7,8,9,10,11,12};
		List<List<Integer>> selectedCategories = combinations(arr,8);   
		int location1 = -1;
		int location2 = -1;
		int location3 = -1;
		int value = -1;
		for(int i=levelStartLocation[8];i<levelStartLocation[9];i++)
		{
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
			
			for(int j=0;j<252;j++)
			{
				for(int k=0;k<64;k++) // 가능한 합들을 순서대로 실행
				{
					if(possibleSum.score[k])
					{
						int diff = (int)expectedValue[i][j][k] - (int)expectedValue1[i][j][k];
						if(max< Math.abs(diff))
						{
							max = Math.abs(diff);
							location1=i;
							location2 = j;
							location3 = k;
						}
					}
				}
			}
	        ++start;
		}
		System.out.println(max);
		System.out.println(location1 + " " + location2 + " " + location3);
		System.out.println(value);
		/*
        zipPath = "test3.zip";   // ZIP 파일 경로
        try (ZipFile zipFile = new ZipFile(zipPath, StandardCharsets.UTF_8)) 
        {
			int arr1[] = {1,2,3,4,5,6};
			int start = 0;
			PossibleBasicSum.findReachableStates(arr1, 6);
			int arr[]= {1,2,3,4,5,6,7,8,9,10,11,12};
			List<List<Integer>> selectedCategories = combinations(arr,3);      
			for(int i=levelStartLocation[3];i<levelStartLocation[4];i++)
			{
		        //String filePath = "output"+i+".bin";
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
		        String filePath = "output.txt";
		        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)))) 
		        {
					try (InputStream in = zipFile.getInputStream(entry);DataInputStream din = new DataInputStream(new BufferedInputStream(in, 1 << 20)))
					{
						for(int j=0;j<252;j++)
						{
							for(int k=0;k<64;k++) // 가능한 합들을 순서대로 실행
							{
								if(possibleSum.score[k])
								{
									 int value =  (int) expectedValue[i][j][k];
									 int test = din.read();
									 value = value / 1000;
									 if(value != test)
									 {
										 System.out.println("오류 발생" + test + " " + value);
									 }
									 //value = value / 1000;
									 //dos.write(value & 0xFF);
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
	        System.out.println("바이너리 파일 저장 완료");
        }
        */
	}
}
