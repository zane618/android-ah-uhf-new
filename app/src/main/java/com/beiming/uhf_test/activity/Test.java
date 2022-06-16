package com.beiming.uhf_test.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String r = sc.nextLine();
        for (int i = r.length()-1; i >-1; i--) {
            System.out.print(r.charAt(i));
        }
    }

    //递归执行，判断当前位置的上下左右是否可行，可行则继续执行
    private static boolean getResult(int[][] map, int i, int j, List<IndexBean> result) {
        //当索引为结尾时，结束递归
        if (i == (map.length - 1) && j == (map[0].length - 1)) {
            return true;
        }
        //1.设置当前位置为1，后续执行时就不可返回了，记录当前位置
        result.add(new IndexBean(i, j));
        map[i][j] = 1;
        //2.上方是否可同行
        if ((i - 1) >= 0 && map[i - 1][j] == 0) {
            if (getResult(map, i - 1, j, result)) {
                return true;
            }
        }
        //3.下方是否可同行
        if ((i + 1) < map.length && map[i + 1][j] == 0) {
            if (getResult(map, i + 1, j, result)) {
                return true;
            }
        }
        //4.左方是否可同行
        if ((j - 1) >= 0 && map[i][j - 1] == 0) {
            if (getResult(map, i, j - 1, result)) {
                return true;
            }
        }
        //5.右方是否可同行
        if ((j + 1) < map[0].length && map[i][j + 1] == 0) {
            if (getResult(map, i, j + 1, result)) {
                return true;
            }
        }
        //6.都不可同行则回退至上一步，删除当前位置
        result.remove(result.size() - 1);
        map[i][j] = 0;
        return false;
    }

    public static class IndexBean {
        private int r;
        private int l;

        public IndexBean(int r, int l) {
            this.r = r;
            this.l = l;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }
    }

    /*public static void main(String[] args) {
        Scanner s = new Scanner(System.in); //定义scanner，等待输入
        String line = s.nextLine(); //读取输入内容
        List<String> list = Arrays.asList(line.split(""));
        for (int i = 0; i < line.length(); i++) {

        }
        System.out.println(Integer.parseInt(line));
//        System.out.println(number);
        //数字去重，从小到大排序
        *//*Scanner s = new Scanner(System.in); //定义scanner，等待输入
        int line = s.nextInt(); //读取输入内容
        List<Integer> resultList = new ArrayList();
        for (int i = 0; i < line; i++) {
            int num = s.nextInt(); //读取输入内容
            if (!resultList.contains(num)) {
                if (resultList.get(0) > num)
                    resultList.add(0, num);
                else
                    resultList.add(num);
            }
        }
        resultList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return integer-t1;
            }
        });
        for (Integer num : resultList) {
            System.out.println(num);
        }*//*
//        Scanner s = new Scanner(System.in); //定义scanner，等待输入
//        String line = s.nextLine(); //读取输入内容
    }*/

}
