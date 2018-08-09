/*
 * Jumper class
 * Jumper is created when user input height and weight data and it should persist app relaunch
 */

import java.util.Date;

class Jumper {
    String name;
    String gender;
    float height; // jumper's height
    float weight; // jumper's weight

    float jumpingspeed; // jumps per minute
    float caloriespermin;
    float caloriestotal; // = jumpingspeed x caloriespermin
    int   totaljumps;

    Jumpsound mjumpsound; // recorded jumpsound data
    Date  startTime; // when jumper starts counting
    Date  endTime; // when jumper stops counting

    public Jumper(float height, float weight) {
        this.height = height;
        this.weight = weight;
    }


    void set(float caloriesunit) {
        this.caloriespermin = caloriesunit;
    }





}
