import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class CombinationWithRepetitionExample 
{	
	public static int repeat = 0;
	public static double sum = 0;
	
    public static void printCombinationsWithRepetition(int[] arr, int r) {
        List<Integer> path = new ArrayList<>();
        dfs(arr, r, 0, path);
    }

    private static void dfs(int[] arr, int r, int start, List<Integer> path) {
        if (path.size() == r) {
        	int number[] = {0,0,0,0,0,0};
        	System.out.print("{ ");
        	for(Integer element : path)
        	{
            	System.out.print(element + ", ");
            	number[element-1] = number[element-1]+1;
        	}
            double[] probs = {1/6.0,1/6.0,1/6.0,1/6.0,1/6.0,1/6.0};
        	double answer = DiceCombinationProbability.multinomialProbability(number,probs);
        	System.out.println("},");
        	++repeat;
        	sum = sum + answer;
            return;
        }

        for (int i = start; i < arr.length; i++) {
            path.add(arr[i]);          // 현재 원소 선택
            dfs(arr, r, i, path);      // i 그대로 → 같은 원소 다시 선택 가능
            path.remove(path.size() - 1); // 백트래킹
        }
    }
}

class DiceCombinationProbability 
{
    // 팩토리얼 계산 (long 범위를 넘어갈 수 있으니 BigDecimal 사용)
    public static BigDecimal factorial(int n) 
    {
        BigDecimal result = BigDecimal.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigDecimal.valueOf(i));
        }
        return result;
    }

    // 다항분포 확률 계산
    public static double multinomialProbability(int[] counts, double[] probs) {
        int n = 0;
        for (int k : counts) n += k; // 총 던진 횟수

        // 계수 계산: n! / (k1! * k2! * ... * km!)
        BigDecimal coef = factorial(n);
        for (int k : counts) {
            coef = coef.divide(factorial(k), 20, RoundingMode.HALF_UP);
        }

        // 확률 곱 계산: p1^k1 * p2^k2 * ...
        BigDecimal probProduct = BigDecimal.ONE;
        for (int i = 0; i < counts.length; i++) {
            probProduct = probProduct.multiply(
                BigDecimal.valueOf(Math.pow(probs[i], counts[i]))
            );
        }

        // 최종 확률
        return coef.multiply(probProduct).doubleValue();
    }
}

public class Test 
{
    public static void main(String[] args) 
    {
    	int[] arr = {1,2,3,4,5,6};
    	CombinationWithRepetitionExample.printCombinationsWithRepetition(arr,5);
    }

}
