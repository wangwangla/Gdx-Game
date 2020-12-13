package kw.mulitplay.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import java.util.HashMap;

import kw.mulitplay.game.Constant;
import kw.mulitplay.game.asset.FontResource;
import kw.mulitplay.game.screen.data.GameData;

public class PackActor extends Group {
    private Image bg;    // 背景
    private Image fanPack;   // 反面
    private Image zhengPack;     //正面
    private Label animalLabel;      //動物名字
    private int num = 0;   //num
    private String name;
    private Color useColor ;
    private Color redColor = Color.RED;
    private Color blackColor = Color.BLACK;
    private int tempX;
    private int tempY;
    private short currentStatus;
    private short ower;
    public PackActor(){
        setSize(160,185);
        Image image = new Image(new Texture("white.png"));
        addActor(image);
        setVisible(false);
    }

    public PackActor(GameData data, Integer num){
        name = num+"";
        setName(name);
        currentStatus = Constant.FANMIAN;
        useColor = blackColor;
        ower = 0;   //默認為0  ，為黑色
        if (num>10){
            num -= 10;
            useColor = redColor;
            ower = 1;
        }
        this.num = num;
        bg = new Image(new Texture("white.png"));
        addActor(bg);
        fanPack = new Image(new Texture("pckback.png"));
        setSize(fanPack.getWidth(),fanPack.getHeight());
        bg.setSize(getWidth(),getHeight());
        fanPack.setPosition(getWidth()/2,getHeight()/2, Align.center);
        zhengPack = new Image(new Texture("animal/"+num+".png"));
        addActor(zhengPack);
        zhengPack.setPosition(getWidth(),0,Align.bottomRight);
        name = data.getAnimalData().get(num);
        animalLabel = new Label("x",new Label.LabelStyle(){{font = FontResource.font;}});
        addActor(animalLabel);
        animalLabel.setColor(useColor);
        animalLabel.setAlignment(Align.center);
        animalLabel.setFontScale(5);
        animalLabel.setPosition(20,getHeight()-20, Align.topLeft);
        addActor(fanPack);
    }

    public int getTempX() {
        return tempX;
    }

    public int getTempY() {
        return tempY;
    }

    public void setXY(int x, int y) {
        this.tempX = x;
        this.tempY = y;
        setPosition(x*(getWidth()+5),y*(getHeight()+7));
    }

    public short getCurrentStatus() {
        return currentStatus;
    }

    public void open(){
        currentStatus = Constant.ZHENGMIAN;
        fanPack.setVisible(false);
    }

    public void setListener(PackActor.TachListener listener) {
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                listener.action(PackActor.this);
            }
        });
    }

    public int getNum() {
        return num;
    }

    public void appear(){
        remove();
    }

    public interface TachListener{
        public void action(PackActor actor);
    }

    public short getOwer() {
        return ower;
    }

    public void setAnimalScale(float scale){
        setOrigin(Align.center);
        setScale(scale,scale);
    }
}