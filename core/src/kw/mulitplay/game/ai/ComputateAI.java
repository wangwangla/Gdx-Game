package kw.mulitplay.game.ai;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Set;

import kw.mulitplay.game.actor.PackActor;
import kw.mulitplay.game.constant.Constant;

public class ComputateAI {
    // 有没有翻起的牌
    private Array<PackActor> list;
    private int status;
    private Array<PackActor> actorsTemp;
    private Color computerColor;
    public ComputateAI(){

    }

    public PackActor[] excete(Array<PackActor> actors, Color color) {
        //use color
        this.computerColor = color;
        //所有的
        this.actorsTemp = actors;
        //找出符合当前用户的牌
        this.list = select(actors);
        PackActor actor = null;
        //kill me
        HashMap<PackActor, Array<PackActor>> meKill = canKill(list);
        HashMap<PackActor, Array<PackActor>> killMe = canKillMe(list);
        if (isAlreadyFan().size > 0) {
            //i can kill target
            if (meKill.size() > 0) {
                //kill me
                PackActor srcActor = canKillArray.get(getRandom(meKill.size()));
                Array<PackActor> packActorArray = meKill.get(srcActor);
                PackActor targetActor = packActorArray.get(getRandom(packActorArray.size));
                return new PackActor[]{srcActor, targetActor};
            }
//            else if (meKill.size()>0) {

                //i kil
//                int random = getRandom(killMe.size());
//                PackActor actor1 = canKillArray.get(random);
//                Array<PackActor> packActorArray = killMe.get(actor1);
//                PackActor actor2 = packActorArray.get(getRandom(packActorArray.size));
//                System.out.println("AI---- test be killss!!!");
//                return new PackActor[]{actor1,actor2};
//            } else {
                actor = fanpai();
                return new PackActor[]{actor};
//            }
        } else {
            //随机翻拍
            actor = fanpai();
            return new PackActor[]{actor};
        }
    }

    public void move(HashMap<PackActor, Array<PackActor>> killMe){
        //可以跑的路线
        //.有危险
        for (PackActor packActor : killMe.keySet()) {
//            canMove(packActor);
        }
    }

    private HashMap<PackActor,Array<Vector2>> canMove = new HashMap<PackActor,Array<Vector2>>();
    private Array<PackActor> actors = new Array<>();
    private void canMove(PackActor packActor,int arr[][]) {
        canMove.clear();
        actors.clear();
        int tempX = packActor.getTempX();
        int tempY = packActor.getTempY();
        Array<Vector2> temp = new Array<>();
        if (tempX-1>0&&arr[tempX-1][tempY] == 0) {
            temp.add(new Vector2(tempX-1,tempY));
        }
        if (tempX+1<arr[0].length&&arr[tempX+1][tempY] == 0) {
            temp.add(new Vector2(tempX+1,tempY));
        }
        if ((tempY-1)>=0&&arr[tempX][tempY-1] == 0) {
            temp.add(new Vector2(tempX,tempY-1));
        }
        if ((tempY+1)<arr.length&&arr[tempX][tempY+1] == 0) {
            temp.add(new Vector2(tempX,tempY+1));
        }
        if (temp.size>0) {
            canMove.put(packActor, temp);
            actors.add(packActor);
        }
    }

    public PackActor fanpai(){
        Array<PackActor> actors = new Array<>();
        for (PackActor actor : actorsTemp) {
            if (actor.getCurrentStatus()!= Constant.ZHENGMIAN) {
                actors.add(actor);
            }
        }

        try{
            int random = getRandom(actors.size);
            PackActor actor = actors.get(random);
            return actor;
        }catch (Exception e){
            System.out.println("=============errpr");
        }
        return null;
    }

    public int getRandom(int range){
        double random = Math.random();
        int v = (int)(random * range);
        return v;
    }


    public Array<PackActor> select(Array<PackActor> actors){
        Array<PackActor> actorsTemp = new Array<>();
        for (PackActor actor : actors) {
            if (actor.getUseColor() == computerColor){
                actorsTemp.add(actor);
            }
        }
        return actorsTemp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Array<PackActor> isAlreadyFan(){
        Array<PackActor> fan = new Array<>();
        for (PackActor actor : list) {
            if (actor.getCurrentStatus() == Constant.ZHENGMIAN){
                fan.add(actor);
            }
        }
        return fan;
    }

    private Array<PackActor> canKillArray;
    private Array<PackActor> canKillMeArray;
    // 有没有可以吃的牌
    public HashMap<PackActor,Array<PackActor>> canKill(Array<PackActor> actors){
        //can kill target src
        canKillArray = new Array<>();
        //select can kill
        Array<PackActor> canKillAtor = selectCanKill(actors);
        //can kill data    src ---> target
        HashMap<PackActor,Array<PackActor>> canKillHashMap = new HashMap<>();
        for (int i = 0; i < canKillAtor.size; i++) {
            PackActor actor = canKillAtor.get(i);
            Array<PackActor> arrayActor = getArrayActor(actor);
            if (arrayActor.size>0){
                Array<PackActor> packActorArray = new Array<>();
                //可以进行kill
                for (PackActor packActor : arrayActor) {
                    if (kill(actor,packActor)) {
                        packActorArray.add(packActor);
                    }
                }
                if (packActorArray.size>0) {
                    canKillHashMap.put(actor, packActorArray);
                    if (!canKillArray.contains(actor,false)) {
                        canKillArray.add(actor);
                    }
                }
            }
        }
        return canKillHashMap;
    }

    private Array<PackActor> selectCanKill(Array<PackActor> actors) {
        Array<PackActor> actors1 = new Array<>();
        for (PackActor actor : actors) {
            if (actor.getStatus()) {
                actors1.add(actor);
            }
        }
        return actors1;
    }

    // 有没有可以杀我的
    public HashMap<PackActor,Array<PackActor>> canKillMe(Array<PackActor> actors){
        //我自己的牌
        Array<PackActor> valueActor = selectCanKill(actors);
        canKillMeArray = new Array<>();
        HashMap<PackActor,Array<PackActor>> hashMap = new HashMap<>();
        for (int i = 0; i < valueActor.size; i++) {
            //我的牌，可以想吃的
            PackActor actor = valueActor.get(i);
            Array<PackActor> arrayActor = getArrayActor(actor);
            if (arrayActor.size>0){
                Array<PackActor> packActorArray = new Array<>();
                //可以进行kill
                for (PackActor packActor : arrayActor) {
                    if (kill(packActor,actor)) {
                        packActorArray.add(packActor);
                    }
                }
                if (packActorArray.size>0) {
                    hashMap.put(actor, packActorArray);
                    canKillArray.add(actor);
                }
            }
        }
        return hashMap;
    }


    public boolean kill(PackActor last,PackActor target){
        if (!target.isLive())return false;
        int num = target.getNum();
        if (last.getUseColor() != target.getUseColor())
        if (target.getNum() == 10&& last.getNum()==1){

        }else if ((last.getNum() == 10&&num==1)||num>=last.getNum()){
            return true;
        }
        return false;
    }

    //移动之后会不会有危险

    //随机翻

    //根据位置获取元素
    public Array<PackActor> getArrayActor(PackActor actor){
        Array<PackActor> actors = new Array<>();
        for (PackActor actorTemp :actorsTemp) {
            if (actorTemp.getStatus()){
                if (comparePosition(
                        actorTemp.getTempX(),
                        actorTemp.getTempY(),
                        actor.getTempX()-1,
                        actor.getTempY())) {
                    actors.add(actorTemp);
                }else if (comparePosition(
                        actorTemp.getTempX(),
                        actorTemp.getTempY(),
                        actor.getTempX()+1,
                        actor.getTempY())) {
                    actors.add(actorTemp);
                }else if (comparePosition(
                        actorTemp.getTempX(),
                        actorTemp.getTempY(),
                        actor.getTempX(),
                        actor.getTempY()-1)) {
                    actors.add(actorTemp);
                }else if (comparePosition(
                        actorTemp.getTempX(),
                        actorTemp.getTempY(),
                        actor.getTempX(),
                        actor.getTempY()+1)) {
                    actors.add(actorTemp);
                }
            }
        }
        return actors;
    }

    public PackActor getActor(int x,int y){
        for (PackActor actor : list) {
            if (actor.getStatus()&&comparePosition(x,y,actor.getTempX(),actor.getTempY())){
                return actor;
            }
        }
        return null;
    }

    public boolean comparePosition(int x,int y,int targetX,int targetY){
        if (x == targetX){
            if (y == targetY){
                return true;
            }
        }
        return false;
    }

//    canMove(){
//
//    }
}
