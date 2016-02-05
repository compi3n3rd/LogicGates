/*CIRCUIT - creates a complete circuit that receives and propagates signals*/
import java.util.*;
import java.io.*;
public class Circuit implements Logic{
  private List<Logic> components;
  private List<Contact> inputs, outputs;
  private List<Wire> innerWires;
  private List<String> importables;
  private String name;
  
  //Simple constructor that receives all required components of the circuit
  public Circuit (String circuitName, List<Logic> components, List<Contact> inputs, List<Contact> outputs, List<Wire> innerWires, List<String> importables){
    this.name = circuitName;
    this.components = components;
    this.inputs = inputs;
    this.outputs = outputs;
    this.innerWires = innerWires;
    this.importables = importables;
  }
  //Constructor that creates a circuit from a given file
  public Circuit(String circuitName) throws IOException{
    components = new ArrayList<Logic>();
    this.name = circuitName; String str;
    //Scanning through the file
    Scanner ins = getCircuitScanner(circuitName);
    //Generating an ArrayList containing lines in the file
    List<String> input = new ArrayList<String> ();
    while (ins.hasNextLine()){
      str = ins.nextLine();
      if (!str.equals(""))
        input.add(str);
    }
    //Split the first line and check if it's a call to import
    //Calls to helper methods vary depending on this
    String[] firstLine = input.get(0).split("\\s");
    if (firstLine[0].equals("IMPORT")){
      parseImportLine(input.get(0));
      parseContactsLine(input.get(1));
      for (int i = 2; i < input.size(); i++){
        parseComponentLine(input.get(i));
      }
    }
    //Initiating importables to null if there's no call to import
    else{
      importables = new ArrayList<String>(0);
      parseContactsLine(input.get(0));
      for (int i = 1; i < input.size(); i++){
        parseComponentLine(input.get(i));
      }
    }
  }
  //Open up the file and create a scanner; throw an IOException if file not found
  public Scanner getCircuitScanner(String circuitName) throws IOException{
    Scanner input = new Scanner(new File("samples/"+circuitName+".txt"));
    return input;
  }
  //add all the names in the import line to importables
  public void parseImportLine(String line){
    String str = "";
    importables = new ArrayList<String>();
    String[] sA = line.split("\\s");
    for (int i=1; i < sA.length; i++){
      importables.add(sA[i]);
    }
  }
  //HELPER METHOD - return a list if wires from
  //the string given
  public List<Wire> parseString(String s){
    List<Wire> w = new ArrayList<Wire>();
    if (s.length() > 0){
      String[] stringArray = s.split("\\s");
      for (String t : stringArray){
        w.add(new Wire(t));
      }
    }
    return w;
  }
  //Create a list of wires from the contact line
  public void parseContactsLine(String line){
    inputs = new ArrayList<Contact>();
    outputs = new ArrayList<Contact>();
    //split the string to 2 parts, clean up unnecessary
    //whitespaces
    String left = "";String right = "";
    String[] cleaned = line.split("\\->");
    left = cleaned[0].trim();
    right = cleaned[1].trim();
    //Adding these wires to innerWires
    innerWires = parseString(left);
    //Creating new Contacts and attach them to these wires
    for (Wire wire : innerWires){
      inputs.add(new Contact(wire,wire,true));
    }
    List<Wire> nW = parseString(right);
    for (Wire wir : nW){
      innerWires.add(wir);
      outputs.add(new Contact(wir,wir,false));
    }
  }
  //Look up a wire from the given name inside innerWires
  public Wire findWire(String name){
    Wire result = null;
    for (Wire w : innerWires){
      if (w.getName().equals(name))
        result = w;
    }
    return result;
  }
  //Take 2 lists, replace the outside wires of a circuit with
  //wires in these lists
  public void hookUp(List<Wire> inWires, List<Wire> outWires){
    if (inWires.size() == inputs.size() && outWires.size() == outputs.size()){
      for (int i=0; i<inputs.size();i++){
        inputs.get(i).setIn(inWires.get(i));
      }
      for (int j=0; j<outputs.size();j++){
        outputs.get(j).setOut(outWires.get(j));
      }
    }
    //exception thrown when fewer/more wires are given in inWires/outWires
    else throw new ExceptionLogicParameters(true,inputs.size(),inWires.size());
  }
  //Create components inside a circuit - including gates and subcircuits
  public void parseComponentLine(String line) throws IOException{
    Scanner input = new Scanner(line);
    String compType = input.next();//component type (circuit or gate)
    
    //These 2 lists contain wires created from component lines
    List<Wire> inWires = new ArrayList<Wire>();
    List<Wire> outWires = new ArrayList<Wire>();
    
    //Clean up the string and turn them into wires
    String newWires = input.nextLine().trim();
    String left = "";String right = "";
    String[] stringArray = newWires.split("\\->");
    left = stringArray[0].trim();
    right = stringArray[1].trim();
    
    inWires = parseString(left);
    outWires = parseString(right);
    
    //Check if input/output lengths given are mismatched
    if (inWires.size() == 0) throw new ExceptionLogicParameters(true,1,0);
    if (outWires.size() == 0) throw new ExceptionLogicParameters(false,1,0);
    
    //This checks to see if any new wire has the same name as a current wire
    //If so, the current wire is used and the new wire is discarded
    for (int i=0; i < inWires.size();i++){
      if (findWire(inWires.get(i).getName()) != null){
        inWires.set(i,findWire(inWires.get(i).getName()));
      }
      else{
        innerWires.add(inWires.get(i));
      }
    }
    for (int j=0; j < outWires.size();j++){
      if (findWire(outWires.get(j).getName()) != null){
        outWires.set(j,findWire(outWires.get(j).getName()));
      }
      else{
        innerWires.add(outWires.get(j));
      }
    }
    
    //If the line calls for a subcircuit to be created
    //Subcircuit created and hooked up to current wires
    if (importables.contains(compType)){
      Circuit newComponent = new Circuit(compType);
      newComponent.hookUp(inWires,outWires);
      this.components.add(newComponent);
    }
    //If the call is for a new gate, gate is created and add
    //to components
    else{
      if (compType.equals("NOT")){
        if (inWires.size() == 1){
          components.add(new GateNot(inWires.get(0),outWires.get(0)));
        }
        else{
          throw new ExceptionLogicParameters(true,1,inWires.size());
        }
      }
    else if (compType.equals("AND")){
      components.add(new GateAnd(inWires,outWires.get(0)));
    }
    else if (compType.equals("NAND")){
      components.add(new GateNand(inWires,outWires.get(0)));
    }
    else if (compType.equals("OR")){
      components.add(new GateOr(inWires,outWires.get(0)));
    }
    else if (compType.equals("XOR")){
      components.add(new GateXor(inWires,outWires.get(0)));
    }
    else if (compType.equals("NOR")){
      components.add(new GateNor(inWires,outWires.get(0)));
    }
   }
  }
  
  //Feed a list of signals into the circuit - exception thrown when
  //input lengths are mismatched
  @Override public void feed(List<Signal> inputsW){
    if (inputsW.size() == inputs.size()){
      for (int i=0; i < inputs.size(); i++){
        inputs.get(i).getIn().setSignal(inputsW.get(i));
      }
    }
    else{
      throw new ExceptionLogicParameters(true,inputs.size(),inputsW.size());
    }
  }
  
  //Feed a string of signal into the circuit
  @Override public void feed(String s){
    List<Signal> sigList = Signal.fromString(s);
    feed(sigList);
  }
  
  //propagate transfers signals through the inbound and outbound Contacts
  //Propagate on each component is also call
  //Order: inbound contacts - components - outbound contacts
  @Override public boolean propagate(){
    boolean p = false;
    List<Signal> first = read();
    for (Contact c : inputs){
      c.getOut().setSignal(c.getIn().getSignal());
    }
    for (Logic component : components){
      component.propagate();
    }
    for (Contact cOut : outputs){
      if(cOut.getOut().getSignal() != cOut.getIn().getSignal()){
        cOut.getOut().setSignal(cOut.getIn().getSignal());
      }
    }
    List<Signal> second = read();
    for (int i = 0; i < first.size(); i++){
      if (!first.get(i).equals(second.get(i))){
        p = true;
      }
    }
    return p;
  }
  
  //Return a simple list of signal from ouputs
  @Override public List<Signal> read(){
    List<Signal> list = new ArrayList<Signal>();
    for (Contact sig : outputs){
      list.add(sig.getOut().getSignal());
    }
    return list;
  }
  
  //Return a string representation of the entire circuit
  //Indentations are followed
  @Override public String toString(){
    StringBuilder stringRepr = new StringBuilder();
    stringRepr.append(name+" : "+inputs+" -> "+outputs+"\n");
    for (Logic comp : components){
      stringRepr.append(indent(comp.toString()));
    }
    return stringRepr.toString();
  }
  
  //Indented any string by 2 spaces - format purposes
  public static String indent(String s){
    String indented = "";
    String aux = "";
    Scanner str = new Scanner(s);
    while (str.hasNextLine()){
      aux = str.nextLine();
      indented += "  " + aux + "\n";
    }
    return indented;
  }
  
  //2 inspect methods call 3 methods below for easy and quick testing
  @Override public List<Signal> inspect(List<Signal> inputs){
    feed(inputs);
    propagate();
    return read();
  }
  @Override public String inspect(String input){
    feed(input);
    propagate();
    return read().toString();
  }
  
  //GETTER AND SETTER - provides way to retrieve/update internal fields
  public void setComponents(List<Logic> components){
    this.components = components;
  }
  public List<Logic> getComponents(){
    return components;
  }
  public void setInputs(List<Contact> inputs){
    this.inputs = inputs;
  }
  public List<Contact> getInputs(){
    return inputs;
  }
  public void setOutputs(List<Contact> outputs){
    this.outputs = outputs;
  }
  public List<Contact> getOutputs(){
    return outputs;
  }
  public void setInnerWires(List<Wire> innerWires){
    this.innerWires = innerWires;
  }
  public List<Wire> getInnerWires(){
    return innerWires;
  }
  public void setImportables(List<String> importables){
    this.importables = importables;
  }
  public List<String> getImportables(){
    return importables;
  }
  public void setName(String name){
    this.name = name;
  }
  public String getName(){
    return name;
  }
}