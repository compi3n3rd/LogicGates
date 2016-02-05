/*LOGIC GATES: GATE AND - INHERITS FROM GATE*/
import java.util.*;
public class GateAnd extends Gate{
  public GateAnd(List<Wire> ins, Wire output){
    super("And",ins,output);
  }
  
  //Propagate signals by using AND Logic
  //Directly modify signal in the wire
  //Return true if signal is changed, false otherwise
  @Override public boolean propagate(){
    Signal check = Signal.X;
    boolean gate = true;
    int countLO = 0; int countHI = 0; int countX = 0;
    for (Wire s : getInputs()){
      if (s.getSignal() == Signal.LO){
        countLO++;
      }
      else if (s.getSignal() == Signal.X){
        countX++;
      }
      else if (s.getSignal() == Signal.HI){
        countHI++;
      }
    }
    if (countLO > 0) check = Signal.LO;
    else if (countHI>0 && countX == 0) check = Signal.HI;
    //Checking if the signal is changed
    if (getOutput().getSignal().equals(check))
      gate = false;
    getOutput().setSignal(check);
    return gate;
  }
  
  //Cast the object to GateAnd to use the default equals method
  @Override public boolean equals(Object other){
    boolean t = false;
    if (other instanceof GateAnd){
      GateAnd g = (GateAnd) other;
      t = super.equals(g);
    }
    return t;
  }
}