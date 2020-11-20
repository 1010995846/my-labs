package com.charlotte.lab.proxy;

public class AniWrapper implements Ani{

    private Ani ani;

    public AniWrapper(Ani ani) {
        this.ani = ani;
    }

    public void invoke(){
        System.out.println("before invoke.");
        return;
    }

    @Override
    public void run(){
        System.out.println("brfore run.");
        ani.run();
    }

}
