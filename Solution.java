
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Solution {

    Map<Character, Set<Character>> adjacencyList;
    Map<Character, Integer> nodeToNumberOfIncomingEdges;
    static final String THERE_IS_NO_SOLUTION = "";
    int numberOfWords;

    public String alienOrder(String[] words) {
        numberOfWords = words.length;
        initializeAdjacencyList(words);
        initializeNodeToNumberOfIncomingEdges(words);
        if (!buildGraph(words)) {
            return THERE_IS_NO_SOLUTION;
        }
        return breadthFirstSearch();
    }

    public String breadthFirstSearch() {
        Queue<Character> queue = new LinkedList<>();
        initializeQueueWithNodesThatHaveZeroIncomingEdges(queue);
        StringBuilder alienDictionary = new StringBuilder();

        while (!queue.isEmpty()) {
            char current = queue.poll();
            alienDictionary.append(Character.toString(current));
            if (!adjacencyList.containsKey(current)) {
                continue;
            }

            Set<Character> neigbours = adjacencyList.get(current);
            for (char n : neigbours) {
                nodeToNumberOfIncomingEdges.put(n, nodeToNumberOfIncomingEdges.get(n) - 1);
                if (nodeToNumberOfIncomingEdges.get(n) == 0) {
                    queue.add(n);
                }
            }
        }
        return alienDictionary.length() < adjacencyList.size() ? THERE_IS_NO_SOLUTION : alienDictionary.toString();
    }

    public void initializeNodeToNumberOfIncomingEdges(String[] words) {
        nodeToNumberOfIncomingEdges = new HashMap<>();
        for (String word : words) {
            int size = word.length();
            for (int i = 0; i < size; i++) {
                nodeToNumberOfIncomingEdges.putIfAbsent(word.charAt(i), 0);
            }
        }
    }

    public void initializeAdjacencyList(String[] words) {
        adjacencyList = new HashMap<>();
        for (String word : words) {
            int size = word.length();
            for (int i = 0; i < size; i++) {
                adjacencyList.putIfAbsent(word.charAt(i), new HashSet<>());
            }
        }
    }

    public void initializeQueueWithNodesThatHaveZeroIncomingEdges(Queue<Character> queue) {
        for (char node : nodeToNumberOfIncomingEdges.keySet()) {
            if (nodeToNumberOfIncomingEdges.get(node) == 0) {
                queue.add(node);
            }
        }
    }

    public boolean buildGraph(String[] words) {
        for (int i = 1; i < numberOfWords; i++) {
            if (words[i - 1].length() > words[i].length() && words[i - 1].startsWith(words[i])) {
                return false;
            }

            int sizeShorterWord = Math.min(words[i - 1].length(), words[i].length());
            for (int j = 0; j < sizeShorterWord; j++) {

                if (words[i - 1].charAt(j) != words[i].charAt(j)) {

                    if (adjacencyList.get(words[i - 1].charAt(j)).add(words[i].charAt(j)) == true) {
                        int newIndegreeValue = nodeToNumberOfIncomingEdges.get(words[i].charAt(j)) + 1;
                        nodeToNumberOfIncomingEdges.put(words[i].charAt(j), newIndegreeValue);
                    }
                    break;
                }
            }
        }
        return true;
    }
}
