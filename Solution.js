
/**
 * @param {string[]} words
 * @return {string}
 */
var alienOrder = function (words) {

    this.adjacencyList = new Map();
    this.nodeToNumberOfIncomingEdges = new Map();
    this.THERE_IS_NO_SOLUTION = "";
    this.numberOfWords = words.length;

    initializeAdjacencyList(words);
    initializeNodeToNumberOfIncomingEdges(words);
    if (!buildGraph(words)) {
        return THERE_IS_NO_SOLUTION;
    }
    return breadthFirstSearch();
};


/**
 * @return {string}
 */
function breadthFirstSearch() {
    const queue = new Queue();
    initializeQueueWithNodesThatHaveZeroIncomingEdges(queue);
    let alienDictionary = [];

    while (!queue.isEmpty()) {
        let current = queue.dequeue();
        alienDictionary.push(current);
        if (!this.adjacencyList.has(current)) {
            continue;
        }

        const neigbours = this.adjacencyList.get(current);
        for (let n of neigbours) {
            this.nodeToNumberOfIncomingEdges.set(n, this.nodeToNumberOfIncomingEdges.get(n) - 1);
            if (this.nodeToNumberOfIncomingEdges.get(n) === 0) {
                queue.enqueue(n);
            }
        }
    }
    return alienDictionary.length < this.adjacencyList.size ? this.THERE_IS_NO_SOLUTION : alienDictionary.join('');
}


/**
 * @param {string[]} words
 */
function initializeNodeToNumberOfIncomingEdges(words) {
    for (let word of words) {
        for (let character of word) {
            if (!this.nodeToNumberOfIncomingEdges.has(character)) {
                this.nodeToNumberOfIncomingEdges.set(character, 0);
            }
        }
    }
}


/**
 * @param {string[]} words
 */
function initializeAdjacencyList(words) {
    for (let word of words) {
        for (let character of word) {
            if (!this.adjacencyList.has(character)) {
                this.adjacencyList.set(character, new Set());
            }
        }
    }
}


/**
 * @param {Queue of strings} queue
 */
function initializeQueueWithNodesThatHaveZeroIncomingEdges(queue) {
    for (let node of this.nodeToNumberOfIncomingEdges.keys()) {
        if (this.nodeToNumberOfIncomingEdges.get(node) === 0) {
            queue.enqueue(node);
        }
    }
}

/**
 * @param {string[]} words
 * @return {boolean}
 */
function buildGraph(words) {
    for (let i = 1; i < this.numberOfWords; i++) {
        if (words[i - 1].length > words[i].length && words[i - 1].startsWith(words[i])) {
            return false;
        }

        let sizeShorterWord = Math.min(words[i - 1].length, words[i].length);
        for (let j = 0; j < sizeShorterWord; j++) {

            if (words[i - 1].charAt(j) !== words[i].charAt(j)) {

                if (this.adjacencyList.get(words[i - 1].charAt(j)).has(words[i].charAt(j)) === false) {
                    this.adjacencyList.get(words[i - 1].charAt(j)).add(words[i].charAt(j));
                    let newIndegreeValue = this.nodeToNumberOfIncomingEdges.get(words[i].charAt(j)) + 1;
                    this.nodeToNumberOfIncomingEdges.set(words[i].charAt(j), newIndegreeValue);
                }
                break;
            }
        }
    }
    return true;
}
