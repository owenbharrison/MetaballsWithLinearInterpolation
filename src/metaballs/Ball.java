package metaballs;
import processing.core.*;

public class Ball extends PApplet{
  PVector pos, vel;
  float radius = random(25, 35);
  
  public Ball(float x, float y){
    pos = new PVector(x, y);
    vel = PVector.random2D().mult(3);
  }
  
  void update(){
    pos.add(vel);
  }
  
  void checkWalls(int w, int h){
    if(pos.x<radius||pos.x>w-radius)vel.x*=-1;
    if(pos.y<radius||pos.y>h-radius)vel.y*=-1;
  }
}

