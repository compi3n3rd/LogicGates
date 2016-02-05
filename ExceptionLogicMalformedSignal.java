/*EXCEPTION - CALLED WHEN UNSUPPORTED CHARACTER REPRESENTATION OF A SIGNAL IS PROVIDED*/
public class ExceptionLogicMalformedSignal extends RuntimeException{
  private char bad;
  private String msg;
  
  public ExceptionLogicMalformedSignal(char bad, String msg){
    this.bad = bad;
    this.msg = msg;
  }
  @Override public String toString(){
    return msg;
  }
  
  //SETTER AND GETTER
  public char getBad(){
    return bad;
  }
  public String getMsg(){
    return msg;
  }
  public void setBad(char bad){
    this.bad = bad;
  }
  public void setMsg(String msg){
    this.msg = msg;
  }
}