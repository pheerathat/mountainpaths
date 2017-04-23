import java.util.*;
import java.io.*;
import java.awt.*;

public class MapDataDrawer
{
  
  private int[][] grid;
  
  public MapDataDrawer(String filename, int rows, int cols){
    // initialize grid 
    //read the data from the file into the grid
    try{
      File f=new File(filename);
      grid=new int[rows][cols];
      Scanner sc=new Scanner(f);
      for(int i=0;i<rows;i++){
        for(int j=0;j<cols;j++){
          grid[i][j]=sc.nextInt();
        }
      }
    }catch(FileNotFoundException e){
      System.out.println("File not found.");
    }
  }
  
  /**
   * @return the min value in the entire grid
   */
  public int findMinValue(){
    int min=grid[0][0];
    for(int i=0;i<grid.length;i++){
      for(int j=0;j<grid[0].length;j++){
        if(grid[i][j]<min) min=grid[i][j];
      }
    }
    return min;    
  }
  /**
   * @return the max value in the entire grid
   */
  public int findMaxValue(){
    int max=grid[0][0];
    for(int i=0;i<grid.length;i++){
      for(int j=0;j<grid[0].length;j++){
        if(grid[i][j]>max) max=grid[i][j];
      }
    }
    return max; 
  }
  
  /**
   * @param col the column of the grid to check
   * @return the index of the row with the lowest value in the given col for the grid
   */
  public  int indexOfMinInCol(int col){
    int min=grid[0][col];
    int index=0;
    for(int i=0;i<grid.length;i++){
      if(grid[i][col]<min){
        min=grid[i][col];
        index=i;
      }
    }
    return index;
  }
  
  /**
   * Draws the grid using the given Graphics object.
   * Colors should be grayscale values 0-255, scaled based on min/max values in grid
   */
  public void drawMap(Graphics g){
    int max = findMaxValue();
    int deltaValue = max-findMinValue();
    for(int i=0;i<grid.length;i++){
      for(int j=0;j<grid[0].length;j++){
        int c=(int)Math.round(255-(max-grid[i][j])/((deltaValue)/255));
        g.setColor(new Color(c,c,c));
        g.fillRect(j,i,1,1);
      }
    }
  }
  
  /**
   * Find a path from West-to-East starting at given row.
   * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
   * @return the total change in elevation traveled from West-to-East
   */
  public int drawLowestElevPath(Graphics g, int row){
    Random rd=new Random();
    int now=row,sum=0;
    int[] r=new int[3];
    for(int i=0;i<grid[row].length;i++){
      if(i!=0){
        if(now==0){
          r[0]=Math.abs(grid[now][i-1]-grid[now][i]);
          r[1]=Math.abs(grid[now][i-1]-grid[now+1][i]);
          if(r[1]==r[0]) now=rd.nextInt(1)+now;
          else if(r[1]<r[0]) now++;
        }else if(now==grid.length-1){
          r[0]=Math.abs(grid[now][i-1]-grid[now][i]);
          r[1]=Math.abs(grid[now][i-1]-grid[now-1][i]);
          if(r[1]==r[0]) now=rd.nextInt(1)+(now-1);
          else if(r[1]<r[0]) now--;
        }else{
          r[0]=Math.abs(grid[now][i-1]-grid[now][i]);
          r[1]=Math.abs(grid[now][i-1]-grid[now-1][i]);
          r[2]=Math.abs(grid[now][i-1]-grid[now+1][i]);
          int min=Math.min(Math.min(r[0],r[1]),r[2]);
          if(r[0]==r[1] && r[0]==r[2]) now=rd.nextInt(2)+(now-1);
          else if(r[0]==r[1] && r[0]==min) now=rd.nextInt(1)+(now-1);
          else if(r[0]==r[2] && r[0]==min) now=rd.nextInt(1)+now;
          else if(r[1]==r[2] && r[1]==min) now=(rd.nextInt(1)*2)+(now-1);
          else if(r[1]==min) now--;
          else if(r[2]==min) now++;
        }
      }
      sum+=grid[now][i];
      g.fillRect(i,now,1,1);
    }
    return sum;
  }
  
  /**
   * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
   */
  public int indexOfLowestElevPath(Graphics g){
    int minSum=drawLowestElevPath(g,0);
    int minRow=0;
    for(int i=1;i<grid.length;i++){
      if(drawLowestElevPath(g,i) <= minSum) {
        minSum=drawLowestElevPath(g,i);
        minRow=i;
      }
    }
    return minRow;
    
  }
  
  
}