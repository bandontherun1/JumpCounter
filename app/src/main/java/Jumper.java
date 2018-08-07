/*
 * Jumper class
 * Jumper is created when user input height and weight data and it should persist app relaunch
 */

import java.util.Date;

class Jumper {
    float height; // jumper's height
    float weight; // jumper's weight
    float jumpingspeed; // jumps per minute
    float caloriespermin;
    float caloriestotal; // = jumpingspeed x caloriespermin
    int   totaljumps;
    Jumpsound mjumpsound; // recorded jumpsound data
    Date  startTime; // when jumper starts counting
    Date  endTime; // when jumper stops counting

    Jumper init(float height, float weight) {
        mjumper = new Jumper;

        if (!myjumper) {
            mjumper.height = height;
            mjumper.weight = weight;
        }

        return mjumper;

    }

    void set(float caloriesunit) {
        this.caloriespermin = caloriesunit;
    }





}
