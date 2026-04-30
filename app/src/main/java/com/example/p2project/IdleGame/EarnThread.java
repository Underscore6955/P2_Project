package com.example.p2project.IdleGame;

public class EarnThread {
    Boolean earning = true;
    public void run()
    {
        while (earning)
        {
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            CurrencyTracker.treats += CurrencyTracker.curEarn();
        }
    }
    public void stopEarn()
    {
        earning = false;
    }
}

