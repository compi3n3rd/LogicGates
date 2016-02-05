/*LOGIC GATE : GATE XOR - INHERITS FROM GATE*/
import java.util.*;
public class GateXor extends Gate{
  public GateXor(List<Wire> ins, Wire output){
    super("Xor",ins,output);
  }
  
  //Propagate using XOR logic
  //Return true if signals are changed, false otherwise
  @Override public boolean propagate(){
    Signal check = Signal.LO;
    boolean gate = true;
    int countHI = 0; int countX = 0;
    for (Wire s : getInputs()){
      if (s.getSignal() == Signal.HI){
        countHI++;
      }
      else if (s.getSignal() == Signal.X){
        countX++;
      }
    }
    if (countHI == 1 && countX == 0) check = Signal.HI;
    else if (countX > 0) check = Signal.X;
    if (getOutput().getSignal().equals(check))
      gate = false;
    getOutput().setSignal(check);
    return gate;
  }
  
  //Cast other to GateXor before using the inherited method
  @Override public boolean equals(Object other){
    boolean t = false;
    if (other instanceof GateXor){
      GateXor g = (GateXor) other;
      t = super.equals(g);
    }
    return t;
  }
}