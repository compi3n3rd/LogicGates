/*LOGIC GATE: GATE NOR - INHERITS FROM GATE*/
import java.util.*;
public class GateNor extends Gate{
  public GateNor(List<Wire> ins, Wire output){
    super("Nor",ins,output);
  }
  
  //Propagate signals using OR followed by NOT logic
  //Return true if signals are changed, false otherwise
  @Override public boolean propagate(){
    Signal check = Signal.LO;
    boolean gate = true;
    int countHI = 0; int countX = 0;
    for (Wire s : getInputs()){
      if (s.getSignal() == Signal.HI){
        countHI++;
        break;
      }
      else if (s.getSignal() == Signal.X){
        countX++;
      }
    }
    if (countHI > 0) check = Signal.HI;
    else if (countHI == 0 && countX > 0) check = Signal.X;
    check = check.invert();
    if (getOutput().getSignal().equals(check))
      gate = false;
    getOutput().setSignal(check);

    return gate;
  }
  
  //Cast other to GateNor to use the inherited method
  @Override public boolean equals(Object other){
    boolean t = false;
    if (other instanceof GateNor){
      GateNor g = (GateNor) other;
      t = super.equals(g);
    }
    return t;
  }
}