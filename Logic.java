/*LOGIC INTERFACE - IMPLEMENTED BY CIRCUIT AND GATE*/
import java.util.*;
public interface Logic{
  public abstract void feed(List<Signal> inSignals);
  
  public abstract void feed(String inSignals);
  
  public abstract boolean propagate();
  
  public abstract List<Signal> read();
  
  public abstract List<Signal> inspect(List<Signal> inputs);
  
  public abstract String inspect(String input);
}