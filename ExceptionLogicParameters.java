/*EXCEPTION - CALLED WHEN WRONG NUMBER OF INPUTS/OUTPUTS ARE PROVIDED*/
public class ExceptionLogicParameters extends RuntimeException{
  private boolean inputsRelated;//Differentiates input from ouput causes
  private int expected, found;
  
  public ExceptionLogicParameters(boolean inputsRelated, int expected, int found){
    this.inputsRelated = inputsRelated;
    this.expected = expected;
    this.found = found;
  }
  @Override public String toString(){
    String toString = String.format("expected %d, but found %d",expected,found);
    return toString;
  }
  
  //SETTER AND GETTER
  public boolean getInputsRelated(){
    return inputsRelated;
  }
  public int getExpected(){
    return expected;
  }
  public int getFound(){
    return found;
  }
  public void setInputsRelated(boolean inputsRelated){
    this.inputsRelated = inputsRelated;
  }
  public void setExpected(int expected){
    this.expected = expected;
  }
  public void setFound(int found){
    this.found = found;
  }
}