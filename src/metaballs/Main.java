package metaballs;
import processing.core.*;
import processing.event.*;

import java.util.ArrayList;

public class Main extends PApplet{
	float[][] valField;
	int[][] stateField;
	int res = 10;
	int cols, rows;
	ArrayList<Ball> balls;
	float percent = 100;
	
	public static void main(String[] args){
		PApplet.main(new String[] {"metaballs.Main"});
	}
	
	public void settings(){
		size(1000, 800);
	}

	public void setup(){
	  cols = width/res+1;
	  rows = height/res+1;
	  valField = new float[cols][rows];
	  stateField = new int[cols][rows];
	  for(int i=0;i<cols;i++){
	    for(int j=0;j<rows;j++){
	      valField[i][j] = random(5);
	    }
	  }
	  balls = new ArrayList<Ball>();
	  for(int i=0;i<15;i++){
	    balls.add(new Ball(random(50, width-50), random(50, height-50)));
	  }
	}

  public void mousePressed(){
	  balls.add(new Ball(mouseX, mouseY));
	}
  
  public void keyPressed(){
  	if(key=='w'){
  		percent ++;
    }
  	if(key=='s'){
  		percent --;
  	}
  	if(percent<1)percent = 1;
  	if(percent>200)percent = 200;
  }
  
  public void mouseWheel(MouseEvent e){
  	res+=e.getCount();
  	if(res<1)res = 1;
  	if(res>20)res = 20;
  	cols = width/res+1;
	  rows = height/res+1;
	  valField = new float[cols][rows];
	  stateField = new int[cols][rows];
	  for(int i=0;i<cols;i++){
	    for(int j=0;j<rows;j++){
	      valField[i][j] = random(5);
	    }
	  }
  }

	public void draw(){
	  background(170);
	  for(Ball b:balls){
	    b.update();
	    b.checkWalls(width, height);
	  }
	  for(int i=0;i<cols;i++){
	    for(int j=0;j<rows;j++){
	      float mb = metaballValue(i*res, j*res);
	      valField[i][j] = mb;
	      stateField[i][j] = mb>1?1:0;
	    }
	  }
	  for(int i=0;i<cols-1;i++){
	    for(int j=0;j<rows-1;j++){
	      int x=i*res;
	      int y=j*res;
	      int state = stateField[i][j]*8+stateField[i+1][j]*4+stateField[i+1][j+1]*2+stateField[i][j+1];
	      PVector a = new PVector(x,y);
	      PVector b = new PVector(x+res,y);
	      PVector c = new PVector(x+res,y+res);
	      PVector d = new PVector(x,y+res);
	      switch(state){
	        case 1:case 14:
	          line(a.x, lerpBetweenY(a,d), lerpBetweenX(d, c), d.y);
	          break;
	        case 2:case 13:
	          line(lerpBetweenX(d, c), c.y, b.x, lerpBetweenY(b, c));
	          break;
	        case 3:case 12:
	          line(a.x, lerpBetweenY(a,d), b.x, lerpBetweenY(b, c));
	          break;
	        case 4:case 11:
	          line(lerpBetweenX(a, b), a.y, b.x, lerpBetweenY(b, c));
	          break;
	        case 5:
	          line(lerpBetweenX(a, b), a.y, a.x, lerpBetweenY(a, d));
	          line(b.x, lerpBetweenY(b, c), lerpBetweenX(d, c), c.y);
	          break; 
	        case 6:case 9:
	          line(lerpBetweenX(a,b), a.y, lerpBetweenX(d, c), c.y);
	          break;
	        case 7: case 8:
	          line(lerpBetweenX(a, b), a.y, a.x, lerpBetweenY(a, d));
	          break;
	        case 10:
	          line(lerpBetweenX(a, b), a.y, b.x, lerpBetweenY(b, c));
	          line(a.x, lerpBetweenY(a,d), lerpBetweenX(d, c), d.y);
	          break;
	      }
	    }
	  }
	  push();
	  textAlign(CENTER, CENTER);
	  text("Grid Size: "+res, width/2, 10);
	  pop();
	  surface.setTitle("Marching Squares FPS: "+round(frameRate));
	}

	public float metaballValue(float x, float y){
	  float sum = 0;
	  for(Ball b:balls){
	  	float r = b.radius * (percent/100);
	    sum += pow(r, 2)/(pow(x-b.pos.x,2)+pow(y-b.pos.y,2));
	  }
	  return sum;
	}

	public float lerp(float x0, float x1, float y0, float y1, float x){
	  return y0+(y1-y0)*(x-x0)/(x1-x0);
	}

	public float lerpBetweenY(PVector b, PVector d){
	  return b.y+(d.y-b.y)*(1-metaballValue(b.x, b.y))/(metaballValue(d.x,d.y)-metaballValue(b.x,b.y));
	}

	public float lerpBetweenX(PVector b, PVector d){
	  return b.x+(d.x-b.x)*(1-metaballValue(b.x, b.y))/(metaballValue(d.x,d.y)-metaballValue(b.x,b.y));
	}
}