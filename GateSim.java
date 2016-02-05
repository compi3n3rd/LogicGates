/*GATE SIMULATION - CONTAINING THE MAIN PROGRAM*/
import java.util.*;
import java.io.*;
public class GateSim{
  //Catch all exceptions that are thrown
  public static void main(String[] args) throws Exception{
    try{
      Circuit c = new Circuit(args[0]);
      c.feed(args[1]);
      c.propagate();
      String s = Signal.toString(c.read());
      System.out.println(s);
    }
    catch (Exception e){
      System.out.println("couldn't perform simulation");
    }
  }
}