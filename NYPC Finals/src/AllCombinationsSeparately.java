import java.util.*;
import java.util.stream.Collectors;

public class AllCombinationsSeparately {

    private static final int MAX_SCORE = 63;
    // 규칙 인덱스를 이름으로 변환하기 위한 배열
    private static final String[] RULE_NAMES = {"ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX"};

    public static void main(String[] args) {
        // 1. 각 규칙별로 가능한 점수 목록을 생성합니다.
        List<Set<Integer>> ruleScoreSets = new ArrayList<>();
        for (int ruleNum = 1; ruleNum <= 6; ruleNum++) {
            Set<Integer> scores = new HashSet<>();
            for (int count = 0; count <= 5; count++) {
                scores.add(count * ruleNum);
            }
            ruleScoreSets.add(scores);
        }

        // 선택한 규칙 개수별로, [조합 이름]과 [점수 목록]을 매핑하여 저장할 최종 결과 자료구조
        // 예: results.get(2)는 "ONE & TWO" -> {점수들}, "ONE & THREE" -> {점수들} 등을 담고 있음
        List<Map<String, List<Integer>>> results = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            // 결과를 정렬해서 보여주기 위해 TreeMap 사용
            results.add(new TreeMap<>());
        }

        // --- 0개 선택 ---
        results.get(0).put("규칙 없음", Collections.singletonList(0));

        // --- 1개 ~ 6개 선택 ---
        for (int k = 1; k <= 6; k++) {
            // k개의 규칙을 선택하는 모든 인덱스 조합을 구합니다.
            List<List<Integer>> ruleCombinations = new ArrayList<>();
            findCombinations(0, k, new ArrayList<>(), ruleCombinations);

            // 각각의 조합에 대해 점수 계산
            for (List<Integer> combination : ruleCombinations) {
                // 현재 조합에 해당하는 규칙들의 점수 목록만 따로 모읍니다.
                List<Set<Integer>> setsForThisCombination = new ArrayList<>();
                for (int ruleIndex : combination) {
                    setsForThisCombination.add(ruleScoreSets.get(ruleIndex));
                }

                // 이 조합으로 만들 수 있는 모든 점수 합을 계산합니다.
                Set<Integer> sumsForThisCombination = new HashSet<>();
                calculateSums(0, 0, setsForThisCombination, sumsForThisCombination);

                // 점수가 63을 초과하면 63으로 처리하고, 정렬된 리스트로 변환합니다.
                List<Integer> finalScores = sumsForThisCombination.stream()
                        .map(score -> Math.min(score, MAX_SCORE))
                        .collect(Collectors.toSet()) // 중복 제거
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
                
                // "ONE & THREE" 와 같은 조합 이름을 만듭니다.
                String combinationKey = combination.stream()
                        .map(index -> RULE_NAMES[index])
                        .collect(Collectors.joining(" & "));
                
                // 최종 결과에 저장합니다.
                results.get(k).put(combinationKey, finalScores);
            }
        }
        
        // 4. 최종 결과를 모두 출력합니다.
        printAllResults(results);
    }

    /**
     * 최종 결과를 형식에 맞게 모두 출력하는 함수
     */
    private static void printAllResults(List<Map<String, List<Integer>>> results) {
    	int total = 0;
        for (int i = 0; i <= 6; i++) {
            System.out.printf("=========================================%n");
            System.out.printf("### %d개 규칙 선택 시 가능한 모든 경우 ###%n", i);
            System.out.printf("=========================================%n");
            
            Map<String, List<Integer>> combinationsMap = results.get(i);
            
            for (Map.Entry<String, List<Integer>> entry : combinationsMap.entrySet()) {
                System.out.printf("▶ 조합: [ %s ]%n", entry.getKey());
                System.out.println("   " + entry.getValue());
                System.out.printf("   (총 %d가지)%n%n", entry.getValue().size());
                total = total+entry.getValue().size();
            }
        }
        System.out.println(total);
    }
    
    // (이하 findCombinations, calculateSums 함수는 이전 답변과 동일합니다)

    private static void findCombinations(int start, int k, List<Integer> current, List<List<Integer>> all) {
        if (k == 0) {
            all.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i <= 6 - k; i++) {
            current.add(i);
            findCombinations(i + 1, k - 1, current, all);
            current.remove(current.size() - 1);
        }
    }
    
    private static void calculateSums(int setIndex, int sum, List<Set<Integer>> sets, Set<Integer> result) {
        if (setIndex == sets.size()) {
            result.add(sum);
            return;
        }
        for (int score : sets.get(setIndex)) {
            calculateSums(setIndex + 1, sum + score, sets, result);
        }
    }
}
