package com.charlotte.lab.leetcode;

/**
 * Created by Charlotte on2020/3/25
 * 在 N * N 的网格上，我们放置一些 1 * 1 * 1  的立方体。
 * <p>
 * 每个值 v = grid[i][j] 表示 v 个正方体叠放在对应单元格 (i, j) 上。
 * <p>
 * 请你返回最终形体的表面积。
 */
public class No892_SurfaceArea {


    public static int surfaceArea(int[][] grid) {
        return surfaceArea(grid, new boolean[grid.length][grid.length], 0, 0);
    }

    public static int surfaceArea(int[][] grid, boolean[][] e, int x, int y) {
        if (x == grid.length || y == grid[x].length) {
            return 0;
        }
        if (e[x][y]) {
            return 0;
        }

        int face = 0;
        int n = grid[x][y];
        if (x == 0) {
//            左边界
            face += n;
        }
        if (y == 0) {
//            下边界
            face += n;
        }
        if (grid[x][y] > 0) {
            face += 2;
        }
        if (x < grid.length - 1) {
            face += Math.abs(grid[x + 1][y] - grid[x][y]);
        } else {
//            右边界
            face += n;
        }
        if (y < grid[x].length - 1) {
            face += Math.abs(grid[x][y + 1] - grid[x][y]);
        } else {
//            上边界
            face += n;
        }
        e[x][y] = true;
        face += surfaceArea(grid, e, x + 1, y);
        face += surfaceArea(grid, e, x, y + 1);
        return face;
    }

    public static void main(String[] args) {
        System.out.println(surfaceArea(new int[][]{{1, 2}, {3, 4}}));
    }
}
