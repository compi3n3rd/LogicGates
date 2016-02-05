/*WIRE - CONSTRUCTING WIRES TO BE USED IN THE CIRCUIT*/
public class Wire{
  private Signal signal;
  private String name;
  //Wire starts out with unknown signal (X)
  public Wire(String name){
    this.signal = Signal.X;
    this.name = name;
  }
  //toString repr
  @Override public String toString(){
    return name +":"+ signal.toString();
  }
  //Compare Wire objects
  @Override public boolean equals(Object other){
    if (!(other instanceof Wire)){
      return false;
    }
    Wire w = (Wire) other;
    return w.getSignal() == signal && w.getName().equals(name);
  }
  
  //GETTER AND SETTER
  public Signal getSignal(){
    return signal;
  }
  public String getName(){
    return name;
  }
  public void setSignal(Signal signal){
    this.signal = signal;
  }
  public void setName(String name){
    this.name = name;
  }
}