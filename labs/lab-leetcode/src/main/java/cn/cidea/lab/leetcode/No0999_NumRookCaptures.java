package cn.cidea.lab.leetcode;

/**
 * Created by Charlotte on2020/3/26
 * 在一个 8 x 8 的棋盘上，有一个白色车（rook）。也可能有空方块，白色的象（bishop）和黑色的卒（pawn）。它们分别以字符 “R”，“.”，“B” 和 “p” 给出。大写字符表示白棋，小写字符表示黑棋。
 * <p>
 * 车按国际象棋中的规则移动：它选择四个基本方向中的一个（北，东，西和南），然后朝那个方向移动，直到它选择停止、到达棋盘的边缘或移动到同一方格来捕获该方格上颜色相反的卒。另外，车不能与其他友方（白色）象进入同一个方格。
 * <p>
 * 返回车能够在一次移动中捕获到的卒的数量。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/available-captures-for-rook
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class No0999_NumRookCaptures {


    public static int numRookCaptures(char[][] board) {
        Integer x = null;

        int count = 0;
        for (int i = 0; i < board.length; i++) {
            int xCount = 0;
            for (int j = 0; j < board[i].length; j++) {
                char c = board[i][j];
                if ('R' == c) {
                    x = j;
                } else if ('B' == c) {
                    if (x != null) {
                        break;
                    } else {
                        xCount = 0;
                    }
                } else if ('p' == c) {
                    if (x == null) {
                        xCount = 1;
                    } else {
                        xCount += 1;
                        break;
                    }
                }
            }
            if (x != null) {
                count += xCount;
                break;
            }
        }
        Integer y = null;
        int yCount = 0;
        for (int i = 0; i < board.length; i++) {
            char c = board[i][x];
            if ('R' == c) {
                y = i;
            } else if ('B' == c) {
                if (y != null) {
                    break;
                } else {
                    yCount = 0;
                }
            } else if ('p' == c) {
                if (y == null) {
                    yCount = 1;
                } else {
                    yCount += 1;
                    break;
                }
            }
        }
        return count + yCount;
    }

    public static void main(String[] args) {
        numRookCaptures(new char[][]{
                {'.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', 'p', '.', '.', '.', '.'},
                {'.', '.', '.', 'p', '.', '.', '.', '.'},
                {'p', 'p', '.', 'R', '.', 'p', 'B', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.'},
                {'.', '.', '.', 'B', '.', '.', '.', '.'},
                {'.', '.', '.', 'p', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.', '.', '.', '.'}});
    }

}
