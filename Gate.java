/*LOGIC GATES: PARENT CLASS - PROVIDES DEFAULT METHODS SHARED BY ALL LOGIC GATES - LOGIC STRUCTURE*/
import java.util.*;
public abstract class Gate implements Logic{
  private String name;
  private List<Wire> inputs;
  private Wire output;
  
  public Gate(String name, List<Wire> ins, Wire out){
    this.name = name;
    this.output = out;
    if (ins.size() > 0)
      this.inputs = ins;
    else throw new ExceptionLogicParameters (true,1,0);
  }
  
  //Take in a list of Signals and feed it to the gate
  //ExceptionLogicParameters thrown when wrong number of inputs are given
  @Override public void feed(List<Signal> inSignals){
    if (inSignals.size() == inputs.size()){
      for (int i=0; i < inputs.size(); i++){
        Wire w = inputs.get(i);
        w.setSignal(inSignals.get(i));
        inputs.set(i, w);
      }
    }
    else throw new ExceptionLogicParameters(true,inputs.size(),inSignals.size());
  }
  
  //Overloaded method - take only a string and convert it to signals
  @Override public void feed(String inSignals){
    List<Signal> inSigs = Signal.fromString(inSignals);
    feed(inSigs);
  }
  
  //Return a list of current signals
  @Override public List<Signal> read(){
    List<Signal> list = new ArrayList<Signal>();
    list.add(output.getSignal());
    return list;
  }
  
  //Feed, propagate and read signals all at once
  @Override public List<Signal> inspect(List<Signal> inputs){
    feed(inputs);
    propagate();
    return read();
  }
  //Overloaded inspect - takes a string instead of list
  @Override public String inspect(String input){
    feed(input);
    propagate();
    return read().toString();
  }
  
  @Override public String toString(){
    String str = String.format("%s( %s | %s )", name, inputs, output);
    return str;
  }
  
  //General equals method - upcasting might be implemented by subclasses
  @Override public boolean equals(Object other){
    boolean equals = false;
    if (other instanceof Gate){
      Gate g = (Gate) other;
      if (g.getName().equals(name) && g.getOutput().equals(output) && g.getInputs().equals(inputs))
        equals = true;
    }
    return equals;
  }
  
  //SETTER AND GETTER
  public List<Wire> getInputs(){
    return inputs;
  }
  public Wire getOutput(){
    return output;
  }
  public String getName(){
    return name;
  }
  public void setInputs(List<Wire> inputs){
    this.inputs = inputs;
  }
  public void setOutput(Wire output){
    this.output = output;
  }
  public void setName(String name){
    this.name = name;
  }
}