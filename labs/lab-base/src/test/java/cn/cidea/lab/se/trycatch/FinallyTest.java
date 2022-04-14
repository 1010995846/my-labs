package cn.cidea.lab.se.trycatch;

import org.junit.Test;

public class FinallyTest {

    @Test
    public void returnTest(){
        System.out.println("3、" + ret());
    }

    public int ret() {
        int i = 1;
        try {
            System.out.println("1、");
            return ++i;
        } finally {
            System.out.println("2、" + ++i);
        }
    }

    @Test
    public void throwTest(){
        int i = 1;
        try {
            System.out.println("1、");
            throw new RuntimeException("" + ++i);
        } catch (Throwable t){
            System.out.println("2、" + ++i);
        } finally {
            System.out.println("3、" + ++i);
        }
    }
}