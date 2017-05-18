package com.kampoz.sketchat.helper;

/**
 * Created by wasili on 2017-05-18.
 */

public class MyColorRGB {
  private int red;
  private int blue;
  private int green;

  public MyColorRGB(int red, int green, int blue){
    if(blue>=0 && blue<=255 && green>=0 && green<=255 && red>=0 && red<=255){
      this.blue = blue;
      this.green = green;
      this.red = red;
    }
    else
      throw new IllegalArgumentException("Wrong color value");
  }

  public int getBlue() {
    return blue;
  }

  public void setBlue(int blue) {
    if(blue>=0 && blue<=255)
      this.blue = blue;
    else
      throw new IllegalArgumentException("Blue wrong value");
  }

  public int getGreen() {
    return green;
  }

  public void setGreen(int green) {
    if(green>=0 && green<=255)
      this.green = green;
    else
      throw new IllegalArgumentException("Green wrong value");
  }

  public int getRed() {
    return red;
  }

  public void setRed(int red) {
    if(red>=0 && red<=255)
      this.red = red;
    else
      throw new IllegalArgumentException("Red wrong value");
  }
}
