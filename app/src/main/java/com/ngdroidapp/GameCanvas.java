package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;
import java.util.Vector;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.core.AppManager;
import istanbul.gamelab.ngdroid.core.NgMediaPlayer;
import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    private Bitmap tileset, spritesheet,bullet, enemy,explode,laser;
    private Bitmap buttons;
    private Rect restartsrc,playsrc,exitsrc,restartdst,playdst,exitdst;
    private int kareno, animasyonno, animasyonyonu;
    private Rect lasersrc,laserdst,laserdst2;
    private int laserspeed, lasery,laserx1,laserx2;
    private int spritex, spritey, hiz, hizx, hizy, bulletoffsetx_temp, bulletoffsety_temp,/*bulletspeedx,bulletspeedy,*/bulletspeed, bulletx_temp, bullety_temp,explodeframeno;
    private Rect tilesrc, tiledst, spritesrc,spritedst,bulletsrc,enemydst,enemysrc,explodesrc,explodedst;
    public Vector<Rect>bulletdst2;
    public Vector<Integer> bulletx2,bullety2,bulletoffsetx2,bulletoffsety2,bulletspeedx2,bulletspeedy2;
    private boolean enemyexits,exploded,spriteexist;
    private boolean donmeboolean,guishow,playshow;
    private Random enemyrnd;
    private int sesefekti_patlama;
    private long prevtime,time;
    private int enemyspeedx,enemyspeedy,enemyx,enemyy,donmenoktasi;
    int touchx, touchy;

    private NgMediaPlayer arkaplan_muzik;
    private Paint textcolor;
    private int textsize;
    private String text;


    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {

        try {
            sesefekti_patlama=root.soundManager.load("sounds/se2.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }

        prevtime=System.currentTimeMillis();


        arkaplan_muzik=new NgMediaPlayer(root);
        arkaplan_muzik.load("sounds/m2.mp3");
        arkaplan_muzik.setVolume(0.5f);
        arkaplan_muzik.prepare();
        arkaplan_muzik.start();

        laser=Utils.loadImage(root,"images/beams1.png");
        lasersrc=new Rect();
        laserdst=new Rect();
        laserdst2=new Rect();

        laserspeed=32;
        lasery =-400;

        tileset = Utils.loadImage(root, "images/tilea2.png");
        tilesrc = new Rect();
        tiledst = new Rect();

        spritesheet= Utils.loadImage(root, "images/cowboy.png");
        spritesrc = new Rect();
        spritedst = new Rect();
        spriteexist=true;

        explode = Utils.loadImage(root, "images/exp2_0.png");
        explodesrc = new Rect();
        explodedst = new Rect();

        explodeframeno=0;
        exploded=false;
        bullet=Utils.loadImage(root,"images/bullet.png");
        bulletsrc=new Rect();

        enemy=Utils.loadImage(root,"images/mainship03.png");
        enemysrc=new Rect();
        enemydst=new Rect();

        buttons=Utils.loadImage(root,"images/buttons.png");
        restartsrc=new Rect();
        restartdst=new Rect();
        playsrc=new Rect();
        playdst=new Rect();
        exitsrc=new Rect();
        exitdst=new Rect();

        guishow=false;
        playshow=true;

        enemyspeedx=10;
        enemyspeedy=0;
        enemyx=getWidthHalf()-128;
        enemyy=getHeight()-256;

        enemyexits=true;
        donmenoktasi=getWidth();
        donmeboolean=true;
        enemyrnd=new Random();

        bulletdst2=new Vector<>();
        bulletx2 =new Vector<>();
        bullety2=new Vector<>();
        bulletoffsetx2=new Vector<>();
        bulletoffsety2=new Vector<>();
        bulletspeedx2=new Vector<>();
        bulletspeedy2=new Vector<>();

        kareno = 0;
        spritex=0;
        spritey=0;
        hizx = 0;
        animasyonno=0;
        hizy=0;
        animasyonyonu =0;
        hiz=16;

        bulletspeed=0;
        bulletoffsetx_temp =256;
        bulletoffsety_temp =128;
        bulletx_temp =0;
        bullety_temp =0;

        textcolor=new Paint();
        textcolor.setARGB(255,255,0,0);
        textsize=64;
        textcolor.setTextSize(textsize);
        textcolor.setTextAlign(Paint.Align.CENTER);
        text="GAME OVER";
    }

    public void draw(Canvas canvas) {

        for (int i=0; i<getWidth();i+=128){
            for (int j=0; j<getHeight();j+=128){
                tiledst.set(i,j,i+128,j+128);
                canvas.drawBitmap(tileset, tilesrc,tiledst,null);
            }
        }
        canvas.drawBitmap(spritesheet, spritesrc, spritedst, null);

        for (int i=0;i<bulletx2.size();i++)
        {
            canvas.drawBitmap(bullet,bulletsrc,bulletdst2.elementAt(i),null);
        }
        // canvas.drawBitmap(bullet,bulletsrc,bulletdst,null);
        if(enemyexits) {
            canvas.drawBitmap(enemy, enemysrc, enemydst, null);
        }
        if(exploded)
        {
            canvas.drawBitmap(explode,explodesrc,explodedst,null);
        }
        canvas.drawBitmap(laser,lasersrc,laserdst,null);
        canvas.drawBitmap(laser,lasersrc,laserdst2,null);

        if(playshow)
        {
            canvas.drawBitmap(buttons, playsrc, playdst, null);
        }

        if(guishow) {

            canvas.drawBitmap(buttons, restartsrc, restartdst, null);
            canvas.drawBitmap(buttons, exitsrc, exitdst, null);
            canvas.drawText(text,getWidthHalf(),getHeightHalf()-300,textcolor);
        }
    }

    public void update()
    {


        tilesrc.set(0,0,64,64);
        playsrc.set(0,0,256,256);
        playdst.set(getWidthHalf()-64,getHeightHalf()-64,getWidthHalf()+64,getHeightHalf()+64);
        if(playshow) {
            return;
        }
        restartsrc.set(256,0,512,256);
        exitsrc.set(512,0,768,256);


        restartdst.set(getWidthHalf()-192,getHeightHalf()-64,getWidthHalf()-64,getHeightHalf()+64);
        exitdst.set(getWidthHalf()+64,getHeightHalf()-64,getWidthHalf()+192,getHeightHalf()+64);


        lasersrc.set(0,0,64,128);

        time=System.currentTimeMillis();
        if(time>prevtime+6000&&enemyexits){
            prevtime=time;
            laserx1=enemyx;
            laserx2=enemyx+192;
            laserdst.set(laserx1,enemyy-128,enemyx+64,enemyy);
            laserdst2.set(laserx2,enemyy-128,enemyx+256,enemyy);
            lasery=enemyy-128;
        }
        lasery-=laserspeed;
        laserdst.set(laserx1,lasery,laserx1+64,lasery+128);
        laserdst2.set(laserx2,lasery,laserx2+64,lasery+128);
        if(spritedst.intersect(laserdst)|| spritedst.intersect(laserdst2))
        {
            spritedst.set(0,0,0,0);
            spriteexist=false;
            guishow=true;
        }
        if(donmeboolean)
        {
            if(enemyspeedx>0)
            {
                donmenoktasi=enemyrnd.nextInt(getWidth()-256-(enemyx+50))+enemyx;
            }
            else if(enemyspeedx<0)
            {
                donmenoktasi=enemyrnd.nextInt(enemyx);
            }
            donmeboolean=false;
        }
        if(enemyspeedx>0&&enemyx>donmenoktasi)
        {
            donmeboolean=true;
            enemyspeedx=-enemyspeedx;
        }
        else if(enemyspeedx<0&&enemyx<donmenoktasi)
        {
            donmeboolean=false;
            enemyspeedx=-enemyspeedx;
        }
        if(enemyexits) {
            enemysrc.set(0, 0, 64, 64);
            //  enemydst.set(getWidthHalf() - 128, getHeight() - 256, getWidthHalf() + 128, getHeight());
            enemydst.set(enemyx, enemyy, enemyx+256, enemyy+256);
        }
        for (int i=0;i< bulletdst2.size();i++)
        {
            if(enemydst.contains(bulletdst2.elementAt(i)))
            {
                //explodedst.set(bulletx2.elementAt(i)-64,bullety2.elementAt(i)-64,bulletx2.elementAt(i)+64,bullety2.elementAt(i)+64);
                explodedst.set(enemyx,enemyy,enemyx+256,enemyy+256);
                bulletx2.removeElementAt(i);
                bullety2.removeElementAt(i);
               /* bulletoffsetx2.removeElementAt(i);
                bulletoffsety2.removeElementAt(i);*/
                bulletspeedx2.removeElementAt(i);
                bulletspeedy2.removeElementAt(i);
                bulletdst2.removeElementAt(i);
                enemyexits=false;
                enemydst.set(0,0,0,0);
                exploded=true;
                guishow=true;
                root.soundManager.play(sesefekti_patlama);
            }
        }
        if(exploded){
            explodesrc=getexplodeframe(explodeframeno);
            explodeframeno+=2;
        }
        if(explodeframeno>15) {
            explodeframeno = 0;
            exploded=false;
        }
        spritex += hizx;
        spritey += hizy;

        enemyx+=enemyspeedx;
        enemyy+=enemyspeedy;

        if(enemyx+256>getWidth()||enemyx<0)
        {
            enemyspeedx=-enemyspeedx;
        }

        for (int  i=0;i<bulletx2.size();i++)
        {
            bulletx2.set(i,bulletx2.elementAt(i)+bulletspeedx2.elementAt(i));
            bullety2.set(i,bullety2.elementAt(i)+bulletspeedy2.elementAt(i));
            if (bulletx2.elementAt(i)>getWidth() || bulletx2.elementAt(i)<0 || bullety2.elementAt(i)>getHeight() || bullety2.elementAt(i)<0 )
            {
                bulletx2.removeElementAt(i);
                bullety2.removeElementAt(i);
               /* bulletoffsetx2.removeElementAt(i);
                bulletoffsety2.removeElementAt(i);*/
                bulletspeedx2.removeElementAt(i);
                bulletspeedy2.removeElementAt(i);
                bulletdst2.removeElementAt(i);
            }
            Log.i("Control",String.valueOf(bulletx2.size()));
        }

        /*bulletx_temp+=bulletspeedx;
        bullety_temp+=bulletspeedy;*/

        if(spritex+256>getWidth()){
            spritex= getWidth()-256;
            animasyonno=0;
        }
        if(spritex<0){
            spritex= 0 ;
            animasyonno=0;
        }
        if(spritey+256> getHeight()){
            spritey=getHeight()-256;
            animasyonno=0;
        }
        if(spritey<0){
            spritey= 0 ;
            animasyonno=0;
        }
        if(animasyonno==1) {
            kareno++;
        }
        else if(animasyonno==0){
            kareno=0;
        }
        if(kareno>8){
            kareno=1;
        }

        if(hizx>0){
            animasyonyonu=0;
        }
        else if (hizy>0){
            animasyonyonu=9;
        }
        if(Math.abs(hizx)>0||Math.abs(hizy)>0)
        {
            animasyonno=1;
        }
        else {
            animasyonno=0;
        }
        spritesrc.set(kareno*128,animasyonyonu*128,(kareno+1)*128,(animasyonyonu+1)*128);
        if(spriteexist) {
            spritedst.set(spritex, spritey, spritex + 256, spritey + 256);
        }
        bulletsrc.set(0,0,70,70);
        for (int i=0;i<bulletdst2.size();i++)
        {
            bulletdst2.elementAt(i).set(bulletx2.elementAt(i),bullety2.elementAt(i),bulletx2.elementAt(i)+32,bullety2.elementAt(i)+32);
        }
        //  bulletdst.set(bulletx_temp,bullety_temp,bulletx_temp+32,bullety_temp+32);
    }

    public Rect getexplodeframe(int frameno)
    {
        frameno=15-frameno;
        Rect temp=new Rect();
        temp.set((frameno%4)*64,(frameno/4)*64,((frameno%4)+1)*64,((frameno/4)+1)*64);
        return temp;
    }
    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y) {
        touchx = x;
        touchy = y;
    }

    public void touchMove(int x, int y) {
    }

    public void touchUp(int x, int y) {
        if (x-touchx>100){
            animasyonno=1;
            animasyonyonu=0;
            hizx=hiz;
            hizy=0;
        }
        else if (touchx-x > 100){
            animasyonno=1;
            animasyonyonu=1;
            hizx=-hiz;
            hizy=0;
        }
        else if (y-touchy>100){
            animasyonno=1;
            animasyonyonu=9;
            hizy=hiz;
            hizx=0;
        }
        else if (touchy-y > 100){
            animasyonno=1;
            animasyonyonu=5;
            hizy=-hiz;
            hizx=0;
        }
        else{
            animasyonno=0;
            hizx=0;
            hizy=0;
            bulletspeed=32;

            if(animasyonyonu==0)
            {
                /*bulletspeedx=bulletspeed;
                bulletspeedy=0;
                bulletoffsetx_temp=256;
                bulletoffsety_temp=128;*/
                bulletspeedx2.add(bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp =256;
                bulletoffsety_temp =128;
            }
            else if(animasyonyonu==1)
            {
                bulletspeedx2.add(-bulletspeed);
                bulletspeedy2.add(0);
                bulletoffsetx_temp =0;
                bulletoffsety_temp =128;
            }
            else if(animasyonyonu==9)
            {
                bulletspeedy2.add(bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp =128;
                bulletoffsety_temp =256;
            }
            else if(animasyonyonu==5)
            {
                bulletspeedy2.add(-bulletspeed);
                bulletspeedx2.add(0);
                bulletoffsetx_temp =128;
                bulletoffsety_temp =0;
            }
            bulletx2.add(spritex+ bulletoffsetx_temp);
            bullety2.add(spritey+ bulletoffsety_temp);
            bulletx_temp =spritex+ bulletoffsetx_temp;
            bullety_temp =spritey+ bulletoffsety_temp;
            bulletdst2.add(new Rect(bulletx_temp, bullety_temp, bulletx_temp +32, bullety_temp +32));
        }
        if(guishow)
        {
            if(restartdst.contains(x,y)){
                Log.i(TAG,"RESTART TIKLANDI");
                setup();
            }
            if(exitdst.contains(x,y)){
                Log.i(TAG,"EXİT TIKLANDI");
                System.exit(0);
            }
        }
        if(playdst.contains(x,y)){
            Log.i(TAG,"PLAY TIKLANDI");
            playshow=false;
        }

    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
