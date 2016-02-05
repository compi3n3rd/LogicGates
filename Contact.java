/*CONTACT - POINTS OF COMMUNICATION BETWEEN A CIRCUIT AND OUTSIDE*/
import java.util.*;
public class Contact{
  private Wire in, out;
  private boolean inbound;
  
  public Contact(Wire in, Wire out, boolean inbound){
    this.in = in;
    this.out = out;
    this.inbound = inbound;
  }
  
  //Formatted representation:
  //If 2 wires are different: (inner-wire)outer wire : Signal
  //If 2 wires are the same: wire : Signal
  @Override public String toString(){
    String repr = "";
    if (in.getName().equals(out.getName())){
      repr = String.format("%s:%s",in.getName(),in.getSignal());
    }
    else{
      repr = String.format(inbound ? "%s(%s):%s" : "(%s)%s:%s",in.getName(),out.getName(),out.getSignal());
    }
    return repr;
  }
  
  //Compare Contacts
  @Override public boolean equals(Object o){
    boolean equals = false;
    if (o instanceof Contact){
      Contact cont = (Contact) o;
      if (cont.getIn().equals(in) && cont.getOut().equals(out) && cont.getInbound()==inbound)
        equals = true;
    }
    return equals;
  }
  
  //SETTER AND GETTER METHODS
  public void setIn(Wire in){
    this.in = in;
  }
  public void setOut(Wire out){
    this.out = out;
  }
  public void setInbound(boolean inbound){
    this.inbound = inbound;
  }
  public Wire getIn(){
    return in;
  }
  public Wire getOut(){
    return out;
  }
  public boolean getInbound(){
    return inbound;
  }
}