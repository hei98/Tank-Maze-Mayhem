package com.mygdx.tank.model.components;

public class KillDeathComponent implements Component{
    public int kills;
    public int deaths;
    public float ratio;

    public KillDeathComponent(){
        this.kills = 0;
        this.deaths = 0;
        this.ratio = 0;
    }
}
