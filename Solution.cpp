
#include <unordered_set>
#include <unordered_map>
#include <queue>
#include <vector>

using namespace std;

class Solution {
    
    unordered_map<char, unordered_set<char>> adjacencyList;
    unordered_map<char, int> nodeToNumberOfIncomingEdges;
    inline static const string THERE_IS_NO_SOLUTION{ ""};
    int numberOfWords;

public:

    string alienOrder(vector<string>& words) {
        numberOfWords = words.size();
        initializeAdjacencyList(words);
        initializeNodeToNumberOfIncomingEdges(words);
        if (!buildGraph(words)) {
            return THERE_IS_NO_SOLUTION;
        }
        return breadthFirstSearch();
    }

    string breadthFirstSearch() {
        queue<char> queue;
        initializeQueueWithNodesThatHaveZeroIncomingEdges(queue);
        string alienDictionary;

        while (!queue.empty()) {
            char current = queue.front();
            queue.pop();
            alienDictionary.append(string{current});
            if (adjacencyList.find(current) == adjacencyList.end()) {
                continue;
            }

            unordered_set<char> neigbours = adjacencyList[current];
            for (const auto& n : neigbours) {
                nodeToNumberOfIncomingEdges[n] = nodeToNumberOfIncomingEdges[n] - 1;
                if (nodeToNumberOfIncomingEdges[n] == 0) {
                    queue.push(n);
                }
            }
        }
        return alienDictionary.length() < adjacencyList.size() ? THERE_IS_NO_SOLUTION : alienDictionary;
    }

    void initializeNodeToNumberOfIncomingEdges(const vector<string>& words) {
        for (const auto& word : words) {
            for (const auto& character : word) {
                nodeToNumberOfIncomingEdges[character] = 0;
            }
        }
    }

    void initializeAdjacencyList(const vector<string>& words) {
        for (const auto& word : words) {
            for (const auto& character : word) {
                adjacencyList[character] = unordered_set<char>();
            }
        }
    }

    void initializeQueueWithNodesThatHaveZeroIncomingEdges(queue<char>& queue) {
        for (const auto& entry : nodeToNumberOfIncomingEdges) {
            if (entry.second == 0) {
                queue.push(entry.first);
            }
        }
    }

    bool buildGraph(const vector<string>& words) {
        for (int i = 1; i < numberOfWords; i++) {
            
            // since C++20: words[i - 1].starts_with(words[i]) 
            if (words[i - 1].length() > words[i].length() && words[i - 1].substr(0, words[i].length()) == words[i]) {
                return false;
            }

            int sizeShorterWord = min(words[i - 1].length(), words[i].length());
            for (int j = 0; j < sizeShorterWord; j++) {

                if (words[i - 1][j] != words[i][j]) {
                    
                    // since C++20: adjacencyList[words[i - 1][j]].contains(words[i][j]) == false)
                    if (adjacencyList[words[i - 1][j]].find(words[i][j]) == adjacencyList[words[i - 1][j]].end()) {
                        adjacencyList[words[i - 1][j]].insert(words[i][j]);
                        int newIndegreeValue = nodeToNumberOfIncomingEdges[words[i][j]] + 1;
                        nodeToNumberOfIncomingEdges[words[i][j]] = newIndegreeValue;
                    }
                    break;
                }
            }
        }
        return true;
    }
};
