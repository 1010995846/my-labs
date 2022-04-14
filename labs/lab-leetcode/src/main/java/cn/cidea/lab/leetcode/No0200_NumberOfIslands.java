package cn.cidea.lab.leetcode;

import java.util.*;

/**
 * PASS
 * <p>
 * Given a 2d grid map of '1's (land) and '0's (water), count the number of islands.
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
 * You may assume all four edges of the grid are all surrounded by water.
 */
public class No0200_NumberOfIslands {

    public static void main(String[] args) {

        No0200_NumberOfIslands numberOfIslands = new No0200_NumberOfIslands();

        char[][] chars;
        chars = new char[4][];
        chars[0] = new char[]{'1', '1', '1', '1', '0'};
        chars[1] = new char[]{'1', '1', '0', '1', '0'};
        chars[2] = new char[]{'1', '1', '0', '0', '0'};
        chars[3] = new char[]{'0', '0', '0', '0', '0'};
        System.out.println(numberOfIslands.numIslands(chars));//except 1

        chars = new char[4][];
        chars[0] = new char[]{'1', '1', '0', '0', '0'};
        chars[1] = new char[]{'1', '1', '0', '0', '0'};
        chars[2] = new char[]{'0', '0', '1', '0', '0'};
        chars[3] = new char[]{'0', '0', '0', '1', '1'};
        System.out.println(numberOfIslands.numIslands(chars));//except 3

        chars = new char[3][];
        chars[0] = new char[]{'1', '1', '1'};
        chars[1] = new char[]{'0', '1', '0'};
        chars[2] = new char[]{'1', '1', '1'};
        System.out.println(numberOfIslands.numIslands(chars));//except 1
    }

    public int numIslands(char[][] grid) {
        int count = 0;
        boolean[][] visited = new boolean[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            visited[i] = new boolean[grid[i].length];
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '0') {
                    continue;
                }
                if (true == visited[i][j]) {
                    continue;
                }
                count++;
//                numIslandsDFS(grid, visited, i, j);
                numIslandsBFS(grid, visited, i, j);
            }
        }
        return count;
    }

    /**
     * 深度优先算法
     *
     * @param grid
     * @param visited
     * @param i
     * @param j
     */
    public void numIslandsDFS(char[][] grid, boolean[][] visited, int i, int j) {
//        超出边界
        if (i < 0 || i >= grid.length) {
            return;
        }
        if (j < 0 || j >= grid[i].length) {
            return;
        }
//        不为目标或已访问
        if (grid[i][j] != '1' || visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        numIslandsDFS(grid, visited, i + 1, j);
        numIslandsDFS(grid, visited, i, j + 1);
        numIslandsDFS(grid, visited, i - 1, j);
        numIslandsDFS(grid, visited, i, j - 1);
    }


    /**
     * BFS
     *
     * @param grid
     * @param visited
     * @param i
     * @param j
     */
    public void numIslandsBFS(char[][] grid, boolean[][] visited, int i, int j) {
//        超出边界
        Queue<Integer> iQueue = new LinkedList<>();
        Queue<Integer> jQueue = new LinkedList<>();
        iQueue.add(i);
        jQueue.add(j);

        while (!iQueue.isEmpty()) {
            i = iQueue.poll();
            j = jQueue.poll();
            if (i < 0 || i >= grid.length) {
                continue;
            }
            if (j < 0 || j >= grid[i].length) {
                continue;
            }
            if (grid[i][j] != '1' || visited[i][j]) {
                continue;
            }
            visited[i][j] = true;
            iQueue.add(i + 1);
            jQueue.add(j);
            iQueue.add(i - 1);
            jQueue.add(j);

            iQueue.add(i);
            jQueue.add(j + 1);
            iQueue.add(i);
            jQueue.add(j - 1);
        }

    }

}
