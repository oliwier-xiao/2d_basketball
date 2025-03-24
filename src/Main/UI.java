package Main;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UI {

    Game_Panel gp;
    Font arial_40,arial_80;
    BufferedImage keyImage;
    public boolean gameFinished;


    public UI(Game_Panel gp){
        this.gp=gp;

        arial_40=new Font("arial",Font.PLAIN,40);
        arial_80=new Font("arial",Font.BOLD,80);
    }

    public void draw(Graphics g){
       if(gameFinished == true){
            g.setColor(Color.black);
            g.fillRect(0,0, gp.ScreenWidth, gp.ScreenHeight);

            g.setColor(Color.white);
            g.setFont(arial_80);
            g.drawString("You Won !", 100, 100);

            gp.gameThread=null;
      ; }else{

       }

        g.setFont(arial_40);
        g.setColor(Color.white);
    }
}
