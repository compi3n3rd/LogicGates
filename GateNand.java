/*LOGIC GATES: GATE NAND - INHERITS FROM GATE*/
import java.util.*;
public class GateNand extends Gate{
  public GateNand(List<Wire> ins, Wire output){
    super("Nand",ins,output);
  }
  //Propagate using AND followed by NOT Logic
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
    check = check.invert();
    if (getOutput().getSignal().equals(check))
      gate = false;
    getOutput().setSignal(check);
    return gate;
  }
  
  //Cast the object to GateNand to use the inherited method
  @Override public boolean equals(Object other){
    boolean t = false;
    if (other instanceof GateNand){
      GateNand g = (GateNand) other;
      t = super.equals(g);
    }
    return t;
  }
}