package com.charlotte.lab.base.extend;

public class Call {
    public static void main(String[] args) {
        new Parent().call();
        new Child().call();
    }

}

class Parent{

    public void call(){
        System.out.println(res());
    }

    protected String res(){
        return "parent";
    }

}

class Child extends Parent{

    @Override
    protected String res() {
        return "child";
    }
}