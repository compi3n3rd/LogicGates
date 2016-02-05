/*SIGNAL ENUMERATION - PROVIDES SIGNALS TO THE WIRES
 * THERE ARE HIGH (HI), LOW (LO) AND UNKNOWN (X) SIGNAL*/
import java.util.*;
public enum Signal{
  HI, LO, X;
  
  //Return an inverted signal
  public Signal invert(){
    Signal invert = X;
    if (this == HI) invert = LO;
    else if (this == LO) invert = HI;
    return invert;
  }
  
  //Convert any character to a signal
  //Exception thrown when an invalid character is given
  public static Signal fromString(char c) throws RuntimeException{
    Signal sig = X;
    if (c == '1') sig = HI;
    else if (c == '0') sig = LO;
    else if (c == 'X' || c == 'x') sig = X;
    else throw new ExceptionLogicMalformedSignal(c,"bad characters present");
    return sig;
  }
  
  //Convert every single character in a given string to a list of signals
  public static List<Signal> fromString(String inps){
    List<Signal> list = new ArrayList<Signal> ();
    String charactersOnly = inps.replaceAll("\\s","");
    for (char c : charactersOnly.toCharArray()){
      list.add(Signal.fromString(c));
    }
    return list;
  }
  
  //Provide string representations for the signals
  @Override public String toString(){
    String repr = "X";
    if (this == HI) repr = "1";
    else if (this == LO) repr = "0";
    return repr;
  }
  //Overloaded toString method
  public static String toString(List<Signal> sig){
    String listRepr = "";
    for (Signal s : sig)
      listRepr += s.toString();
    return listRepr;
  }
  
}