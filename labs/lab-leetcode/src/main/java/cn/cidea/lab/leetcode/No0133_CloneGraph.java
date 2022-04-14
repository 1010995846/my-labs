package cn.cidea.lab.leetcode;

import java.util.*;

/**
 * Clone an undirected graph. Each node in the graph contains a label and a list of its neighbors.
 * <p>
 * <p>
 * OJ's undirected graph serialization:
 * Nodes are labeled uniquely.
 * <p>
 * We use # as a separator for each node, and , as a separator for node label and each neighbor of the node.
 * As an example, consider the serialized graph {0,1,2#1,2#2,2}.
 * <p>
 * The graph has a total of three nodes, and therefore contains three parts as separated by #.
 * <p>
 * First node is labeled as 0. Connect node 0 to both nodes 1 and 2.
 * Second node is labeled as 1. Connect node 1 to node 2.
 * Third node is labeled as 2. Connect node 2 to node 2 (itself), thus forming a self-cycle.
 * Visually, the graph looks like the following:
 * <p>
 * 1
 * /  \
 * /     \
 * 0 - - -  2
 * / \
 * \_/
 */
public class No0133_CloneGraph {

    public static void main(String[] args) {

        No0133_CloneGraph cloneGraph = new No0133_CloneGraph();

        UndirectedGraphNode node;
//        node = new UndirectedGraphNode(0);
//        node.neighbors.add(node);
//        node.neighbors.add(node);

        node = new UndirectedGraphNode(-1);
        UndirectedGraphNode node1 = new UndirectedGraphNode(1);
        node.neighbors.add(node1);
        node1.neighbors.add(node);

        node = cloneGraph.cloneGraph(node);
        return;
    }

    /**
     * {0,1,5#1,2,5#2,3#3,4,4#4,5,5#5}
     * 首节点入栈
     * top出栈，取邻居neighbor，是否已访问，若未访问，克隆，添加访问，入栈
     * 添加关系线
     *
     * @param node
     * @return
     */
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        if (node == null) {
            return node;
        }
        UndirectedGraphNode result = new UndirectedGraphNode(node.label);
        LinkedList<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
        queue.add(node);
        Map<UndirectedGraphNode, UndirectedGraphNode> map = new HashMap<UndirectedGraphNode, UndirectedGraphNode>();
        map.put(node, result);

        while (!queue.isEmpty()) {
            node = queue.poll();
            for (UndirectedGraphNode neighbor : node.neighbors) {
                UndirectedGraphNode cloneNeighbor = map.get(neighbor);
                if (cloneNeighbor == null) {
//                    首次访问节点
//                    克隆邻居
                    cloneNeighbor = new UndirectedGraphNode(neighbor.label);
//                    克隆节点添加克隆邻居
//                    添加访问记录和克隆索引
                    map.put(neighbor, cloneNeighbor);
//                    邻居入栈
                    queue.add(neighbor);
                }
                map.get(node).neighbors.add(cloneNeighbor);
            }

        }
        return result;
    }

    private void cloneGraphNeighbor(Queue<UndirectedGraphNode> queue, Queue<UndirectedGraphNode> cloneQueue) {

    }

    static class UndirectedGraphNode {
        int label;
        List<UndirectedGraphNode> neighbors;

        UndirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<>();
        }
    }
}
