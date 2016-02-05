/*LOGIC GATE : GATE NOT - INHERITS FROM GATE*/
import java.util.*;
//GateNot only takes one input argument as opposed to multiple inputs in other Gates.
public class GateNot extends Gate{
  public GateNot(Wire input, Wire output){
    super("Not",Arrays.asList(new Wire[]{input}),output);
  }
  
  //Propagate using NOT logic
  //Return true if signal is changed, false otherwise
  @Override public boolean propagate(){
    boolean gateVal = true;
    Wire newWire = getInputs().get(0);
    Signal sig = newWire.getSignal();
    sig = sig.invert();
    if (getOutput().getSignal().equals(sig))
      gateVal = false;
    getOutput().setSignal(sig);
    return gateVal;
  }
  
  //Cast other to GateNot before using the inherited method
  @Override public boolean equals(Object other){
    boolean t = false;
    if (other instanceof GateNot){
      GateNot g = (GateNot) other;
      t = super.equals(g);
    }
    return t;
  }
}