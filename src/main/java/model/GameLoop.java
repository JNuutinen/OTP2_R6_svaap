package model;

import view.GameMain;

import java.util.ArrayList;

public class GameLoop extends Thread {

    public static ArrayList<Updateable> updateables = new ArrayList<Updateable>();
    private GameMain gameMain;
    private boolean isLooping = false;


    public GameLoop(GameMain gameMain){
        this.gameMain = gameMain;
    }


    @Override
    public void run() {
        isLooping = true;

        //Juhan looppi
        /*
        new AnimationTimer() {
            long lastNanoTime = startNanoTime;
            public void handle(long currentNanoTime) {
                // Lasketaan aika viime updatesta
                double elapsedTime = (currentNanoTime - lastNanoTime);// / 1000000000.0;
                System.out.println(elapsedTime);
                lastNanoTime = currentNanoTime;

                //ajaa update metodin jokaisessa looppiluokassa
                //Updateables pitää käydä läpi väärässä järjestyksessä koska sieltä poistetaan olioita välissä
                for (int i = updateables.size(); i > 0; i-- ) {
                    updateables.get(i-1).update();
                }
            }
        }.start();*/

        //Hegen looppi
        final double targetDelta = 0.0083; // 16.6ms ~ 60fps | 8.3 ~ 129fps
        long previousTime = System.nanoTime();

        while (isLooping) {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;

            //------------------paivita kaikki updateable-rajapintaluokat------------------
            for(Updateable updateable : updateables){
                updateable.update(deltaTime);
            }

            previousTime = currentTime;
            double frameTime = (System.nanoTime() - currentTime) / 1_000_000_000.0;
            if (frameTime < targetDelta) {
                try {
                    //System.out.println((long)((targetDelta - frameTime) * 1000)); //talla voi katsoa ajan (ms) framejen valissa
                    Thread.sleep( (long) ((targetDelta - frameTime) * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeThread(){
        isLooping = false;
    }
}
