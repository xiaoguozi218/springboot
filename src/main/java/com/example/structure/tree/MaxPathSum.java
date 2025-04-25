package com.example.structure.tree;

/**
 * #### 标题: 二叉树中最大路径和问题
 * #### 题目描述: 给定一棵二叉树，每个节点上都有一个整数值。要求找到树中和最大的路径。路径可以从任意节点开始和结束，但路径必须连续。
 * #### 示例: 假设输入的二叉树如下所示：
 *
 *     1
 *    / \
 *   2   3
 *  / \  / \
 * 4  5  6
 *
 */

public class MaxPathSum {


    private int maxSum = Integer.MIN_VALUE;

    public int maxGain(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 计算左子树的最大增益
        int leftGain = Math.max(maxGain(root.left), 0);
        // 计算右子树的最大增益
        int rightGain = Math.max(maxGain(root.right), 0);
        // 计算经过当前节点的路径和
        int priceNewPath = root.val + leftGain + rightGain;
        // 更新全局最大路径和
        maxSum = Math.max(maxSum, priceNewPath);
        // 返回当前节点值加上左右子树中较大的增益
        return root.val + Math.max(leftGain, rightGain);
    }

    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        MaxPathSum solution = new MaxPathSum();
        System.out.println(solution.maxPathSum(root));
    }

}
