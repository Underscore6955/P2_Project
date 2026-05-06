package com.example.p2project.IdleGame;

import com.example.p2project.MainActivity;

public class EarnThread extends Thread{
    Boolean earning = true;
    MainActivity main;
    public EarnThread(MainActivity main)
    {
        this.main = main;
    }
    public void run()
    {
        while (earning)
        {
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            CurrencyTracker.treats += CurrencyTracker.curEarn();
            main.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    main.treatView.setText(String.valueOf(CurrencyTracker.treats.intValue()));
                    main.earnView.setText(String.valueOf(CurrencyTracker.curEarn().intValue()));
                }
            });
        }
    }
    public void stopEarn()
    {
        earning = false;
    }
}

