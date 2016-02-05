// CHANGELOG
// 
// Sunday, April 5th 3:30am: major additions.
//   - many per-class tests
//   - file-based tests grouped into fewer tests (to balance grading weight better)
//   - moved all FeedbackCircuit code to honors section comment block
//   - includes some instance variables in a @Before block, hopefully doesn't have
//     the issues we had with static variables on the last project's test file.
//

/** Example of using unit tests for programming assignment 5.  This is
  * partially how your code will be graded.  Later in the class we will
  * write our own unit tests.  To run them on the command line, make
  * sure that the junit-cs211.jar is in the project directory.
  * 
  *  demo$ javac -cp .:junit-cs211.jar *.java     # compile everything
  *  demo$ java  -cp .:junit-cs211.jar P5Tests    # run tests
  * 
  * On windows replace : with ; (colon with semicolon)
  *  demo$ javac -cp .;junit-cs211.jar *.java     # compile everything
  *  demo$ java  -cp .;junit-cs211.jar P5Tests    # run tests
  */

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class P5Tests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("P5Tests");
  }
  
  /////////////////////////////////////////////////////////
  
  // Signal tests
  
  @Test (timeout=3000) public void signal1(){
    assertEquals(Signal.HI, Signal.HI);
    assertEquals(Signal.LO, Signal.LO);
    assertEquals(Signal.X,  Signal.X );
  }
  
  @Test (timeout=3000) public void signal2(){ assertEquals("1",Signal.HI.toString()); }
  @Test (timeout=3000) public void signal3(){ assertEquals("0",Signal.LO.toString()); }
  @Test (timeout=3000) public void signal4(){ assertEquals("X",Signal.X .toString()); }
  
  @Test (timeout=3000) public void signal5(){ assertEquals(Signal.HI,Signal.fromString('1')); }
  @Test (timeout=3000) public void signal6(){ assertEquals(Signal.LO,Signal.fromString('0')); }
  @Test (timeout=3000) public void signal7(){ assertEquals(Signal.X ,Signal.fromString('X')); }
  @Test (timeout=3000) public void signal8(){ assertEquals(Signal.X ,Signal.fromString('x')); }
  @Test (timeout=3000) public void signal9(){
    try {
      char c = ' ';
      Signal s = Signal.fromString(c);
      fail(String.format("shouldn't have gotten back Signal %s from char'%s'.",s,c));
    }
    catch (ExceptionLogicMalformedSignal e) { return; }
  }
  @Test (timeout=3000) public void signal10(){
    try {
      char c = 'h';
      Signal s = Signal.fromString(c);
      fail(String.format("shouldn't have gotten back Signal %s from char'%s'.",s,c));
    }
    catch (ExceptionLogicMalformedSignal e) { return; }
  }
  
  @Test (timeout=3000) public void signal11(){
    String inp = "110X";
    List<Signal> expecteds = Signal.fromString(inp);
    assertEquals(expecteds, Arrays.asList(new Signal[]{Signal.HI, Signal.HI, Signal.LO, Signal.X}));
  }
  
  
  @Test (timeout=3000) public void signal12(){
    String inp = "";
    List<Signal> expecteds = Signal.fromString(inp);
    assertEquals(expecteds, Arrays.asList(new Signal[]{}));
  }
  
  // this one is still okay - valid symbols and ignorable whitespace.
  @Test (timeout=3000) public void signal13(){
    String inp = "1 x \tX 00";
    List<Signal> expecteds = Signal.fromString(inp);
    assertEquals(expecteds, Arrays.asList(new Signal[]{Signal.HI, Signal.X, Signal.X, Signal.LO, Signal.LO}));
  }
  
  @Test (timeout=3000) public void signal14(){
    try {
      String inp = "1 x \tX BAD characters ! 00";
      List<Signal> expecteds = Signal.fromString(inp);
      fail("shouldn't have succeeded in reading past any bad characters.");
    }
    catch (ExceptionLogicMalformedSignal e){ return; }
  }
  
  @Test (timeout=3000) public void signal15(){
    List<Signal> originals = sigs0;
    assertEquals("0", Signal.toString(originals));
  }
  
  @Test (timeout=3000) public void signal16(){
    List<Signal> originals = Arrays.asList(new Signal[]{Signal.LO, Signal.HI, Signal.X, Signal.HI});
    assertEquals("01X1", Signal.toString(originals));
  }
  
  /////////////////////////////////////////////////////////
  
  // Wire tests
  
  @Test (timeout=3000) public void wire1(){
    Wire w1 = new Wire("boringName");
    Wire w2 = new Wire("special_name123");
    assertEquals("boringName",w1.getName());
    assertEquals("special_name123",w2.getName());
    assertEquals(Signal.X, w1.getSignal());
    assertEquals(Signal.X, w2.getSignal());
  }
  
  @Test (timeout=3000) public void wire2(){
    Wire w1 = new Wire("n");
    w1.setSignal(Signal.HI);
    assertEquals(Signal.HI, w1.getSignal());
  }
  
  @Test (timeout=3000) public void wire3(){
    Wire w1 = new Wire("old");
    w1.setName("new");
    assertEquals("new", w1.getName());
  }
  
  @Test (timeout=3000) public void wire4(){
    Wire w1 = new Wire("a");
    Wire w2 = new Wire("a");
    Wire w3 = new Wire("OTHER");
    
    assertTrue(w1.equals(w2));
    assertTrue(w2.equals(w1));
    
    assertFalse(w1.equals(w3));
    assertFalse(w2.equals(w3));
  }
  
  @Test (timeout=3000) public void wire5(){
    Wire w1 = new Wire("w");
    assertEquals("w:X",w1.toString());
  }
  
  @Test (timeout=3000) public void wire6(){
    Wire w1 = new Wire("w");
    w1.setSignal(Signal.LO);
    assertEquals("w:0",w1.toString());
    w1.setSignal(Signal.HI);
    assertEquals("w:1",w1.toString());
  }
  
  /////////////////////////////////////////////////////////
  
  List<Signal> sigs00, sigs01, sigs10, sigs11, sigs0X, sigsX0, sigs1X, sigsX1, sigsXX, sigs0, sigs1, sigsX;
  List<Wire> wires1, wires2, wires3, wires4;
  Gate gateInstance;
  
  @Before
  public void setup(){
    sigs00 = Arrays.asList(new Signal[]{Signal.LO, Signal.LO});
    sigs01 = Arrays.asList(new Signal[]{Signal.LO, Signal.HI});
    sigs10 = Arrays.asList(new Signal[]{Signal.HI, Signal.LO});
    sigs11 = Arrays.asList(new Signal[]{Signal.HI, Signal.HI});
    sigs0X = Arrays.asList(new Signal[]{Signal.LO, Signal.X });
    sigsX0 = Arrays.asList(new Signal[]{Signal.X , Signal.LO});
    sigs1X = Arrays.asList(new Signal[]{Signal.HI, Signal.X });
    sigsX1 = Arrays.asList(new Signal[]{Signal.X , Signal.HI});
    sigsXX = Arrays.asList(new Signal[]{Signal.X , Signal.X });
    
    sigs0 = Arrays.asList(new Signal[]{Signal.LO });
    sigs1 = Arrays.asList(new Signal[]{Signal.HI });
    sigsX = Arrays.asList(new Signal[]{Signal.X  });
    
    
    wires1 = Arrays.asList(new Wire[]{new Wire("a")});
    wires2 = Arrays.asList(new Wire[]{new Wire("a"), new Wire("b")});
    wires3 = Arrays.asList(new Wire[]{new Wire("a"), new Wire("b"), new Wire("c")});
    wires4 = Arrays.asList(new Wire[]{new Wire("a"), new Wire("b"), new Wire("c"), new Wire("d")});
    
    gateInstance = new Gate("gname", wires2, new Wire("out")){
      @Override public boolean propagate() { throw new RuntimeException("I'm just a Gate instance for testing non-propagate methods, please don't call propagate on me!"); }
    };
  }
  
  // Gate is abstract; make an arbitrary child class and test inherited methods that way.
  
  class GateThing extends Gate {
    public GateThing(List<Wire> i, Wire o){ super("gtname", i, o); }
    @Override public boolean propagate(){ throw new RuntimeException("don't call propagate on me."); }
  };
  
  // Gate tests.
  
  @Test (timeout=3000) public void gate_getInputs(){
    GateThing g = new GateThing(wires2,new Wire("out"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(expected, g.getInputs());
  }
  
  @Test (timeout=3000) public void gate_getOutput(){
    GateThing g = new GateThing(wires2,new Wire("out"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(new Wire("out"), g.getOutput());
  }
  
  @Test (timeout=3000) public void gate_setInputs(){
    GateThing g = new GateThing(wires2,new Wire("out"));
    g.setInputs(wires3);
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b"), new Wire("c")});
    assertEquals(expected, g.getInputs());
  }
  
  @Test (timeout=3000) public void gate_setOutput(){
    GateThing g = new GateThing(wires2,new Wire("out"));
    g.setOutput(new Wire("newout"));
    assertEquals(new Wire("newout"), g.getOutput());
  }
  
  @Test (timeout=3000) public void gate_name1(){
    GateThing g = new GateThing(wires2, new Wire("out"));
    assertEquals("gtname", g.getName());
  }
  @Test (timeout=3000) public void gate_name2(){
    GateThing g = new GateThing(wires2, new Wire("out"));
    g.setName("newname");
    assertEquals("newname", g.getName());
  }
  
  // check that feeding a List<Signal> updates the input wires' signals.
  @Test (timeout=3000) public void gate_feed1(){
    GateThing g = new GateThing(wires2, new Wire("out"));
    g.feed(sigs01);
    assertEquals(Signal.LO, g.getInputs().get(0).getSignal());
    assertEquals(Signal.HI, g.getInputs().get(1).getSignal());
  }
  
  // given too few parameters, should throw exception.
  @Test (timeout=3000) public void gate_feed2(){
    GateThing g = new GateThing(wires4, new Wire("out"));
    try {
      g.feed(sigs01);
      fail("should have thrown exception, given wrong number of parameters.");
    } catch (ExceptionLogicParameters e){
      assertEquals(4,e.getExpected());
      assertEquals(2,e.getFound());
    }
  }
  // given too few parameters, should throw exception.
  @Test (timeout=3000) public void gate_feed3(){
    GateThing g = new GateThing(wires4, new Wire("out"));
    try {
      g.feed("01");
      fail("should have thrown exception, given wrong number of parameters.");
    } catch (ExceptionLogicParameters e){
      assertEquals(4,e.getExpected());
      assertEquals(2,e.getFound());
    }
  }
  // given too few parameters, constructor should throw exception.
  @Test (timeout=3000) public void gate_feed4(){
    try {
      GateThing g = new GateThing(new ArrayList<Wire>(), new Wire("out"));
      fail("should have thrown exception, given wrong number of parameters.");
    } catch (ExceptionLogicParameters e){ }
  }
  
  @Test (timeout=3000) public void gate_feed5(){
    GateThing g = new GateThing(wires4, new Wire("out"));
    g.feed("0X11");
    assertEquals(Signal.LO, g.getInputs().get(0).getSignal());
    assertEquals(Signal.X , g.getInputs().get(1).getSignal());
    assertEquals(Signal.HI, g.getInputs().get(2).getSignal());
    assertEquals(Signal.HI, g.getInputs().get(3).getSignal());
  }
  
  @Test (timeout=3000) public void gate_read1(){
    GateThing g = new GateThing(wires4, new Wire("out"));
    assertEquals(sigsX,g.read());
  }
  @Test (timeout=3000) public void gate_read2(){
    GateThing g = new GateThing(wires4, new Wire("out"));
    Wire w = new Wire("out");
    w.setSignal(Signal.HI);
    g.setOutput(w);
    assertEquals(sigs1,g.read());
  }
  
  @Test (timeout=3000) public void gate_toString1(){
    GateThing g = new GateThing(wires3, new Wire("out"));
    String expected = "gtname( [a:X, b:X, c:X] | out:X )";
    assertEquals(expected, g.toString());
  }
  
  // after some modifications to the gate, do we still get the right string?
  // relies upon Wire::setSignal(Signal), Gate::setOutput(Wire), Gate::setName(String), and Gate::feed(String).
  @Test (timeout=3000) public void gate_toString2(){
    GateThing g = new GateThing(wires2, new Wire("out"));
    g.setName("NAME");
    Wire w = new Wire("w");
    w.setSignal(Signal.HI);
    g.setOutput(w);
    g.feed("01");
    String expected = "NAME( [a:0, b:1] | w:1 )";
    assertEquals(expected, g.toString());
  }
  
  // equals.should be comparing the names, inputs/output wires (via wires' equals() method).
  @Test (timeout=3000) public void gate_equals(){
    GateThing g1 = new GateThing(wires2, new Wire("out"));
    List<Wire> ws2 = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    GateThing g2 = new GateThing(ws2, new Wire("out"));
    assertEquals(g1,g2);
  }
  
  /////////////////////////////////////////////////////////
  
  // GateAnd tests.
  
  // does your constructor construct? Check a few Gate-inherited methods.
  @Test (timeout=3000) public void gateand1(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(expected, g.getInputs());
    
    expected.get(0).setSignal(Signal.LO);
    expected.get(1).setSignal(Signal.HI);
    
    g.feed("01");
    assertEquals(expected, g.getInputs());
  }
  
  
  @Test (timeout=3000) public void gateand_00(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("00");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gateand_01(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("01");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gateand_10(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("10");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  @Test (timeout=3000) public void gateand_11(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("11");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gateand_X0(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("X0");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }    
  
  @Test (timeout=3000) public void gateand_1X(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("1X");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }    
  
  @Test (timeout=3000) public void gateand_XX(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    g.feed("XX");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }
  
  
  @Test (timeout=3000) public void gateand_111(){
    GateAnd g = new GateAnd(wires3, new Wire("outa"));
    g.feed("111");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gateand_1101(){
    GateAnd g = new GateAnd(wires4, new Wire("outa"));
    g.feed("1101");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gateand_propagate_results(){
    GateAnd g = new GateAnd(wires2, new Wire("outa"));
    // output : X -> LO
    g.feed("00");
    boolean ans = g.propagate();
    assertTrue(ans);
    // output: LO -> LO
    ans = g.propagate();
    assertFalse(ans);
    //output: LO -> HI
    g.feed("11");
    ans = g.propagate();
    assertTrue(ans);
  }
  
  /////////////////////////////////////////////////////////
  
  // GateOr Tests
  
  // does your constructor construct? Check a few Gate-inherited methods.
  @Test (timeout=3000) public void gateor1(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(expected, g.getInputs());
    
    expected.get(0).setSignal(Signal.LO);
    expected.get(1).setSignal(Signal.HI);
    
    g.feed("01");
    assertEquals(expected, g.getInputs());
  }
  
  
  @Test (timeout=3000) public void gateor_00(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("00");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gateor_01(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("01");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gateor_10(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("10");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  @Test (timeout=3000) public void gateor_11(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("11");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gateor_X0(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("X0");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }    
  
  @Test (timeout=3000) public void gateor_1X(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("1X");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }    
  
  @Test (timeout=3000) public void gateor_XX(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    g.feed("XX");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }
  
  @Test (timeout=3000) public void gateor_111(){
    GateOr g = new GateOr (wires3, new Wire("outa"));
    g.feed("111");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gateor_0010(){
    GateOr g = new GateOr (wires4, new Wire("outa"));
    g.feed("0010");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gateor_propagate_results(){
    GateOr g = new GateOr (wires2, new Wire("outa"));
    // output : X -> LO
    g.feed("00");
    boolean ans = g.propagate();
    assertTrue(ans);
    // output: LO -> LO
    ans = g.propagate();
    assertFalse(ans);
    //output: LO -> HI
    g.feed("11");
    ans = g.propagate();
    assertTrue(ans);
  }
  
  /////////////////////////////////////////////////////////
  
  // GateNot Tests
  // does your constructor construct? Check a few Gate-inherited methods.
  @Test (timeout=3000) public void gatexor_1(){
    GateNot g = new GateNot (new Wire("inw"), new Wire("outa"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("inw")});
    assertEquals(expected, g.getInputs());
    
    expected.get(0).setSignal(Signal.LO);
    
    g.feed("0");
    assertEquals(expected, g.getInputs());
  }
  
  
  @Test (timeout=3000) public void gatenot_0(){
    GateNot g = new GateNot (new Wire("inw"), new Wire("outa"));
    g.feed("0");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatenot_1(){
    GateNot g = new GateNot (new Wire("inw"), new Wire("outa"));
    g.feed("1");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatenot_X(){
    GateNot g = new GateNot (new Wire("inw"), new Wire("outa"));
    g.feed("X");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }
  
  @Test (timeout=3000) public void gatenot_propagate_results(){
    GateNot g = new GateNot (new Wire("inw"), new Wire("outa"));
    // output : X -> LO
    g.feed("1");
    boolean ans = g.propagate();
    assertTrue(ans);
    // output: LO -> LO
    ans = g.propagate();
    assertFalse(ans);
    //output: LO -> HI
    g.feed("0");
    ans = g.propagate();
    assertTrue(ans);
  }
  
  /////////////////////////////////////////////////////////
  
  // GateXor Tests
  // does your constructor construct? Check a few Gate-inherited methods.
  @Test (timeout=3000) public void gatexor1(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(expected, g.getInputs());
    
    expected.get(0).setSignal(Signal.LO);
    expected.get(1).setSignal(Signal.HI);
    
    g.feed("01");
    assertEquals(expected, g.getInputs());
  }
  
  
  @Test (timeout=3000) public void gatexor_00(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("00");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatexor_01(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("01");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatexor_10(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("10");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  @Test (timeout=3000) public void gatexor_11(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("11");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatexor_X0(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("X0");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }    
  
  @Test (timeout=3000) public void gatexor_1X(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("1X");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }    
  
  @Test (timeout=3000) public void gatexor_XX(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    g.feed("XX");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }
  
  @Test (timeout=3000) public void gatexor_011(){
    GateXor g = new GateXor (wires3, new Wire("outa"));
    g.feed("011");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatexor_0010(){
    GateXor g = new GateXor (wires4, new Wire("outa"));
    g.feed("0010");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatexor_propagate_results(){
    GateXor g = new GateXor (wires2, new Wire("outa"));
    // output : X -> LO
    g.feed("00");
    boolean ans = g.propagate();
    assertTrue(ans);
    // output: LO -> LO
    ans = g.propagate();
    assertFalse(ans);
    //output: LO -> HI
    g.feed("10");
    ans = g.propagate();
    assertTrue(ans);
  }
  
  
  /////////////////////////////////////////////////////////
  
  // GateNand Tests
  // does your constructor construct? Check a few Gate-inherited methods.
  @Test (timeout=3000) public void gatenand_1(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(expected, g.getInputs());
    
    expected.get(0).setSignal(Signal.LO);
    expected.get(1).setSignal(Signal.HI);
    
    g.feed("01");
    assertEquals(expected, g.getInputs());
  }
  
  
  @Test (timeout=3000) public void gatenand_00(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("00");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatenand_01(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("01");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatenand_10(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("10");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  @Test (timeout=3000) public void gatenand_11(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("11");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatenand_X0(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("X0");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }    
  
  @Test (timeout=3000) public void gatenand_1X(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("1X");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }    
  
  @Test (timeout=3000) public void gatenand_XX(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    g.feed("XX");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }
  
  @Test (timeout=3000) public void gatenand_011(){
    GateNand g = new GateNand (wires3, new Wire("outa"));
    g.feed("011");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatenand_0010(){
    GateNand g = new GateNand (wires4, new Wire("outa"));
    g.feed("0010");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatenand_propagate_results(){
    GateNand g = new GateNand (wires2, new Wire("outa"));
    // output : X -> LO
    g.feed("11");
    boolean ans = g.propagate();
    assertTrue(ans);
    // output: LO -> LO
    ans = g.propagate();
    assertFalse(ans);
    //output: LO -> HI
    g.feed("10");
    ans = g.propagate();
    assertTrue(ans);
  }
  
  
  /////////////////////////////////////////////////////////
  
  // GateNor Tests
  // does your constructor construct? Check a few Gate-inherited methods.
  @Test (timeout=3000) public void gatenor_1(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    List<Wire> expected = Arrays.asList(new Wire[]{new Wire("a"),new Wire("b")});
    assertEquals(expected, g.getInputs());
    
    expected.get(0).setSignal(Signal.LO);
    expected.get(1).setSignal(Signal.HI);
    
    g.feed("01");
    assertEquals(expected, g.getInputs());
  }
  
  
  @Test (timeout=3000) public void gatenor_00(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("00");
    boolean ans = g.propagate();
    assertEquals(sigs1, g.read());
  }
  
  @Test (timeout=3000) public void gatenor_01(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("01");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatenor_10(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("10");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  @Test (timeout=3000) public void gatenor_11(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("11");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatenor_X0(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("X0");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }    
  
  @Test (timeout=3000) public void gatenor_1X(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("1X");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }    
  
  @Test (timeout=3000) public void gatenor_XX(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    g.feed("XX");
    boolean ans = g.propagate();
    assertEquals(sigsX, g.read());
  }
  
  @Test (timeout=3000) public void gatenor_011(){
    GateNor g = new GateNor (wires3, new Wire("outa"));
    g.feed("011");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatenor_0010(){
    GateNor g = new GateNor (wires4, new Wire("outa"));
    g.feed("0010");
    boolean ans = g.propagate();
    assertEquals(sigs0, g.read());
  }
  
  @Test (timeout=3000) public void gatenor_propagate_results(){
    GateNor g = new GateNor (wires2, new Wire("outa"));
    // output : X -> HI
    g.feed("00");
    boolean ans = g.propagate();
    assertTrue(ans);
    // output: HI -> HI
    ans = g.propagate();
    assertFalse(ans);
    //output: HI -> LO
    g.feed("10");
    ans = g.propagate();
    assertTrue(ans);
  }
  
  
  /////////////////////////////////////////////////////////
  
  // Contact tests.
  
  @Test (timeout=3000) public void contact_1(){
    Contact c = new Contact(new Wire("in"), new Wire("out"), true);
    assertEquals(new Wire("in"), c.getIn());
    assertEquals(new Wire("out"), c.getOut());
    assertEquals(true, c.getInbound());
  }
  
  @Test (timeout=3000) public void contact_2(){
    Contact c = new Contact(new Wire("in"), new Wire("out"), true);
    c.setIn(new Wire("in2"));
    
    assertFalse(new Wire("in").equals(c.getIn()));
    assertEquals(new Wire("in2"),c.getIn());
    
    c.setOut(new Wire("out2"));
    assertEquals(new Wire("out2"),c.getOut());
    
    c.setInbound(false);
    assertEquals(false,c.getInbound());
  }
  
  @Test (timeout=3000) public void contact_toString1(){
    assertEquals("A(B):X",new Contact(new Wire("A"), new Wire("B"),true).toString());
  }
  @Test (timeout=3000) public void contact_toString2(){
    assertEquals("(A)B:X",new Contact(new Wire("A"), new Wire("B"),false).toString());
  }
  @Test (timeout=3000) public void contact_toString3(){
    assertEquals("A:X",new Contact(new Wire("A"), new Wire("A"),true).toString());
    assertEquals("A:X",new Contact(new Wire("A"), new Wire("A"),false).toString());
  }
  @Test (timeout=3000) public void contact_toString4(){
    Wire a1 = new Wire("A");
    a1.setSignal(Signal.LO);
    Wire a2 = new Wire("A");
    a2.setSignal(Signal.LO);
    assertEquals("A:0",new Contact(a1, a2,true).toString());
  }
  @Test (timeout=3000) public void contact_toString5(){
    Wire a1 = new Wire("A");
    a1.setSignal(Signal.HI);
    Wire a2 = new Wire("A");
    a2.setSignal(Signal.HI);
    assertEquals("A:1",new Contact(a1, a2,true).toString());
  }
  
  @Test (timeout=3000) public void contact_equals(){
    Contact c1 = new Contact(new Wire("A"), new Wire("B"), true);
    Contact c2 = new Contact(new Wire("A"), new Wire("B"), true);
    Contact c3 = new Contact(new Wire("A"), new Wire("B"), false);
    Contact c4 = new Contact(new Wire("NO"), new Wire("nono!"), true);
    assertEquals(c1, c2);
    assertFalse(c1.equals(c3));
    assertFalse(c2.equals(c4));
    assertFalse(c4.equals(c1));
  }
  
  /////////////////////////////////////////////////////////
  
  // Circuit tests
  List<Logic> componentsArg;
  List<Contact> inputContactsArg, outputContactsArg;
  List<Wire>innerWiresArg;
  List<String> importablesArg;
  Circuit simpleCircuit, simpleCircuit2, vanillaCircuit, andy;
  @Before
  public void prepareCircuits(){
    
    Wire a = new Wire("A");
    Wire b = new Wire("B");
    Wire c = new Wire("C");
    Wire out = new Wire("out");
    Wire inner = new Wire("inner");
    GateOr  gate_or  = new GateOr (new ArrayList<Wire>(Arrays.asList(new Wire[]{a,b})), inner);
    GateAnd gate_and = new GateAnd(new ArrayList<Wire>(Arrays.asList(new Wire[]{inner,c})),out);
    
    componentsArg = new ArrayList<Logic>(Arrays.asList(new Logic[]{gate_or, gate_and}));
    
    Contact ca = new Contact(a,a,true);
    Contact cb = new Contact(b,b,true);
    Contact cc = new Contact(c,c,true);
    Contact cout = new Contact(out,out,false);
    inputContactsArg = new ArrayList<Contact>(Arrays.asList(new Contact[]{ca,cb,cc}));
    outputContactsArg = new ArrayList<Contact>(Arrays.asList(new Contact[]{cout}));
    
    innerWiresArg = new ArrayList<Wire>(Arrays.asList(new Wire[]{a,b,c,out,inner}));
    importablesArg = new ArrayList<String>();
    
    simpleCircuit = new Circuit("simpleCircuit", componentsArg, inputContactsArg, outputContactsArg, innerWiresArg, importablesArg);
    simpleCircuit2 = new Circuit("simpleCircuit2", componentsArg, inputContactsArg, outputContactsArg, innerWiresArg, importablesArg);
    
    vanillaCircuit = new Circuit("cname",new ArrayList<Logic>(), new ArrayList<Contact>(), new ArrayList<Contact>(), new ArrayList<Wire>(), new ArrayList<String>());
  }
  
  // building a circuit by hand, without reading files or using other constructors, is a real pain.
  // but it's a static method, which means student files will be a bit more forgiven during testing.
  // this is equivalent to the samples/andy.txt circuit.
  public static Circuit getAndy(){
    // andy circuit's wires
    Wire andyTemp = new Wire("temp");
    Wire andyOrary = new Wire("orary");
    Wire andyOut = new Wire("out");
    Wire andyTemp1 = new Wire("temp1");
    Wire andyTemp2 = new Wire("temp2");
    
    // first notnot's wires
    Wire n1in = new Wire("in");
    Wire n1out = new Wire("out");
    Wire n1temp = new Wire("temp");
    
    // second notnot's wires
    Wire n2in = new Wire("in");
    Wire n2out = new Wire("out");
    Wire n2temp = new Wire("temp");
    
    // first notnot's gates
    GateNot n1 = new GateNot(n1in,   n1temp);
    GateNot n2 = new GateNot(n1temp, n1out );
    
    // secont notnot's gates
    GateNot n3 = new GateNot(n2in,   n2temp);
    GateNot n4 = new GateNot(n2temp, n2out );
    
    // first notnot's contacts
    Contact n1c1 = new Contact(andyTemp, n1in, true);
    Contact n1c2 = new Contact(n1out, andyTemp1, false);
    
    // second notnot's contacts
    Contact n2c1 = new Contact(andyOrary, n2in, true);
    Contact n2c2 = new Contact(n2out, andyTemp2, false);
    
    // andy's contacts
    Contact ac1 = new Contact(andyTemp,  andyTemp , true  );
    Contact ac2 = new Contact(andyOrary, andyOrary, true  );
    Contact ac3 = new Contact(andyOut,   andyOut  , false );
    
    
    
    Circuit notnot1 = new Circuit("notnot",
                                  new ArrayList<Logic>(Arrays.asList(new Logic[]{n1, n2})),
                                  new ArrayList<Contact>(Arrays.asList(new Contact[]{n1c1})),
                                  new ArrayList<Contact>(Arrays.asList(new Contact[]{n1c2})),
                                  new ArrayList<Wire>(Arrays.asList(new Wire[]{n1in, n1out, n1temp})),
                                  new ArrayList<String>()
                                 );
    
    Circuit notnot2 = new Circuit("notnot",
                                  Arrays.asList(new Logic[]{n3, n4}),
                                  Arrays.asList(new Contact[]{n2c1}),
                                  Arrays.asList(new Contact[]{n2c2}),
                                  Arrays.asList(new Wire[]{n2in, n2out, n2temp}),
                                  Arrays.asList(new String[]{})
                                 );
    
    GateAnd innerAnd = new GateAnd(Arrays.asList(new Wire[]{andyTemp1, andyTemp2}), andyOut);
    
    Circuit andy = new Circuit("andy",
                                  new ArrayList<Logic>  (Arrays.asList(new Logic[]{notnot1, notnot2, innerAnd})),
                                  new ArrayList<Contact>(Arrays.asList(new Contact[]{ac1, ac2})),
                                  new ArrayList<Contact>(Arrays.asList(new Contact[]{ac3})),
                                  new ArrayList<Wire>   (Arrays.asList(new Wire[]{andyTemp, andyOrary, andyOut, andyTemp1, andyTemp2})),
                                  new ArrayList<String> (Arrays.asList(new String[]{"notnot"}))
                              );
    return andy;
  }
  
  @Test (timeout=3000) public void circuit_1(){
    simpleCircuit.setComponents(Arrays.asList(new Logic[]{}));
    assertEquals(Arrays.asList(new Logic[]{}), simpleCircuit.getComponents());
  }
  
  @Test (timeout=3000) public void circuit_2(){
    List<Contact> ins = Arrays.asList(new Contact[]{new Contact(new Wire("A"), new Wire("B"), true)});
    simpleCircuit.setInputs(ins);
    assertEquals(ins, simpleCircuit.getInputs());
  }
  
  @Test (timeout=3000) public void circuit_3(){
    List<Contact> outs = Arrays.asList(new Contact[]{new Contact(new Wire("A"), new Wire("B"), true)});
    simpleCircuit.setOutputs(outs);
    assertEquals(outs, simpleCircuit.getOutputs());
  }
  
  @Test (timeout=3000) public void circuit_4(){
    List<Wire> inners = Arrays.asList(new Wire[]{new Wire("X"), new Wire("Y"), new Wire("Z")});
    simpleCircuit.setInnerWires(inners);
    assertEquals(inners, simpleCircuit.getInnerWires());
  }
  
  @Test (timeout=3000) public void circuit_5(){
    List<String> imps = Arrays.asList(new String[]{"half", "full", "ripple"});
    simpleCircuit.setImportables(imps);
    assertEquals(imps, simpleCircuit.getImportables());
  }
  
  @Test (timeout=3000) public void circuit_6(){
    simpleCircuit.setName("newnamehere");
    assertEquals("newnamehere", simpleCircuit.getName());
  }
  
  @Test (timeout=3000) public void circuit_getCircuitScanner() throws IOException {
    Scanner sc = vanillaCircuit.getCircuitScanner("and");
    String expected = "A B -> out\n\nAND A B -> out";
    String found = sc.useDelimiter("\\Z").next(); // note, this removes the last newline.
    assertEquals(expected, found);
  }
  
  @Test (timeout=3000) public void circuit_parseImportLine1(){
    vanillaCircuit.parseImportLine("IMPORT a");
    assertEquals(Arrays.asList(new String[]{"a"}), vanillaCircuit.getImportables());
  }
  
  @Test (timeout=3000) public void circuit_parseImportLine2(){
    vanillaCircuit.parseImportLine("IMPORT a b c half full ripple4");
    assertEquals(Arrays.asList(new String[]{"a","b","c","half","full","ripple4"}), vanillaCircuit.getImportables());
  }
  
  @Test (timeout=3000) public void circuit_parseContactsLine1(){
    List<Contact> empty = Arrays.asList(new Contact[]{});
    assertEquals(empty, vanillaCircuit.getInputs());
    assertEquals(empty, vanillaCircuit.getOutputs());
    List<Contact> ins = Arrays.asList(new Contact[]{
      new Contact(new Wire("A"), new Wire("A"), true),
        new Contact(new Wire("B"), new Wire("B"), true),
        new Contact(new Wire("C"), new Wire("C"), true)}
    );
    List<Contact> outs = Arrays.asList(new Contact[]{
      new Contact(new Wire("D"), new Wire("D"), false)}
    );
    
    vanillaCircuit.parseContactsLine("A B C -> D");
    assertEquals(ins , vanillaCircuit.getInputs ());
    assertEquals(outs, vanillaCircuit.getOutputs());
  }
  
  @Test (timeout=3000) public void circuit_parseContactsLine2(){
    List<Contact> empty = Arrays.asList(new Contact[]{});
    assertEquals(empty, vanillaCircuit.getInputs());
    assertEquals(empty, vanillaCircuit.getOutputs());
    List<Contact> ins = Arrays.asList(new Contact[]{
      new Contact(new Wire("A"), new Wire("A"), true),
        new Contact(new Wire("B"), new Wire("B"), true),
        new Contact(new Wire("C"), new Wire("C"), true)}
    );
    List<Contact> outs = Arrays.asList(new Contact[]{
      new Contact(new Wire("D"), new Wire("D"), false),
        new Contact(new Wire("E"), new Wire("E"), false)}
    );
    
    List<Wire> inners = Arrays.asList(new Wire[]{new Wire("A"), new Wire("B"), new Wire("C"), new Wire("D"), new Wire("E")});
    vanillaCircuit.parseContactsLine("A B C -> D E");
    assertEquals(ins , vanillaCircuit.getInputs ());
    assertEquals(outs, vanillaCircuit.getOutputs());
    assertEquals(inners, vanillaCircuit.getInnerWires());
  }
  
  @Test (timeout=3000) public void circuit_findWire1(){
    Wire f = simpleCircuit.findWire("A");
    assertEquals(new Wire("A"), f);
  }
  
  @Test (timeout=3000) public void circuit_findWire2(){
    Wire f = simpleCircuit.findWire("NOTPRESENT");
    assertNull(f);
  }
  @Test (timeout=3000) public void circuit_findWire3(){
    Wire f = simpleCircuit.findWire("inner");
    assertEquals(new Wire("inner"), f);
    f = simpleCircuit.findWire("out");
    assertEquals(new Wire("out"), f);
  }
  
  @Test (timeout=3000) public void circuit_findWire4(){
    Wire w = getAndy().findWire("temp");
    assertEquals(new Wire("temp"), w);
    w = getAndy().findWire("garbage");
    assertNull(w);
  }
  @Test (timeout=3000) public void circuit_hookup1(){
    assertEquals(Arrays.asList(
                               new Contact[]{
                                 new Contact(new Wire("A"),new Wire("A"),true),
                                 new Contact(new Wire("B"),new Wire("B"),true),
                                 new Contact(new Wire("C"),new Wire("C"),true)
                               }
                              ),
                 simpleCircuit.getInputs()
                );
    simpleCircuit.hookUp(Arrays.asList(new Wire[]{new Wire("newA"), new Wire("newB"), new Wire("newC")}),
                         Arrays.asList(new Wire[]{new Wire("newOut")}));
    assertEquals(new Wire("newA"), simpleCircuit.getInputs().get(0).getIn());
    assertEquals(new Wire("newB"), simpleCircuit.getInputs().get(1).getIn());
    assertEquals(new Wire("newC"), simpleCircuit.getInputs().get(2).getIn());
    assertEquals(new Wire("newOut"), simpleCircuit.getOutputs().get(0).getOut());
  }
  
  @Test (timeout=3000) public void circuit_hookup2(){
    try {
      simpleCircuit.hookUp(Arrays.asList(new Wire[]{new Wire("newA")}), // too short!
                           Arrays.asList(new Wire[]{new Wire("newOut")}));
      fail("should have complained about hookUp lengths.");
    }
    catch (ExceptionLogicParameters e){
      assertEquals(3, e.getExpected());
      assertEquals(1, e.getFound());
      assertTrue(e.getInputsRelated());
    }
  }
  
  @Test (timeout=3000) public void circuit_parseComponentLine_1() throws IOException{
    String line = "OR A B out -> brandNewWire";
    simpleCircuit.parseComponentLine(line);
    
    // did we get a new innerWires value added?
    assertEquals(6, simpleCircuit.getInnerWires().size());
    assertEquals(new Wire("brandNewWire"), simpleCircuit.getInnerWires().get(5));
    
    // did we get a new (third) component added? Previously, had an OR and an AND gate in the list only.
    assertEquals(3, simpleCircuit.getComponents().size());
    assertTrue(simpleCircuit.getComponents().get(2) instanceof GateOr);
    assertEquals(Arrays.asList(new Wire[]{new Wire("A"), new Wire("B"), new Wire("out")}), ((GateOr)simpleCircuit.getComponents().get(2)).getInputs());
    assertEquals(new Wire("brandNewWire"), ((GateOr)simpleCircuit.getComponents().get(2)).getOutput());
  }  
  
  @Test (timeout=3000) public void circuit_parseComponentLine_2() throws IOException{
    String line = "notnot temp -> newOUT";
    Circuit andy = getAndy();
    andy.parseComponentLine(line);
    
    // did we get a new innerWires value added?
    assertEquals(6, andy.getInnerWires().size());
    assertEquals(new Wire("newOUT"), andy.getInnerWires().get(5));
    
    // did we get a new (third) component added? Previously, had an OR and an AND gate in the list only.
    assertEquals(4, andy.getComponents().size());
    assertTrue(andy.getComponents().get(3) instanceof Circuit);
    assertEquals("notnot", ((Circuit)andy.getComponents().get(3)).getName());
    assertEquals(Arrays.asList(new Contact[]{new Contact(new Wire("temp"),new Wire("in"),true)}), ((Circuit)andy.getComponents().get(3)).getInputs());
    assertEquals(Arrays.asList(new Contact[]{new Contact(new Wire("out"),new Wire("newOUT"),false)}), ((Circuit)andy.getComponents().get(3)).getOutputs());
  }
  @Test (timeout=3000) public void circuit_parseComponentLine_3() throws IOException{
    String line = "OR -> brandNewWire";
    try {
      simpleCircuit.parseComponentLine(line);
      fail("should have complained that OR receieved no arguments.");
    }
    catch (ExceptionLogicParameters e){ }
  }  
  
  
  @Test (timeout=3000) public void circuit_feed1() {
    simpleCircuit.feed(Arrays.asList(new Signal[]{Signal.HI, Signal.LO, Signal.X}));
    assertEquals(Signal.HI, simpleCircuit.getInputs().get(0).getIn().getSignal());
    assertEquals(Signal.LO, simpleCircuit.getInputs().get(1).getIn().getSignal());
    assertEquals(Signal.X , simpleCircuit.getInputs().get(2).getIn().getSignal());
  }
  @Test (timeout=3000) public void circuit_feed2() {
    simpleCircuit.feed("10X");
    assertEquals(Signal.HI, simpleCircuit.getInputs().get(0).getIn().getSignal());
    assertEquals(Signal.LO, simpleCircuit.getInputs().get(1).getIn().getSignal());
    assertEquals(Signal.X , simpleCircuit.getInputs().get(2).getIn().getSignal());
  }
  @Test (timeout=3000) public void circuit_feed3() {
    try {
      simpleCircuit.feed("10X1");
      fail("shouldn't have succeeded in feeding four signals to a three-input circuit.");
    } catch (ExceptionLogicParameters e){ return; }
  }
  
  @Test (timeout=3000) public void circuit_propagate1() {
    simpleCircuit.feed("101");
    boolean ans = simpleCircuit.propagate();
    assertEquals(Signal.HI, simpleCircuit.getOutputs().get(0).getOut().getSignal());
  }
  @Test (timeout=3000) public void circuit_propagate2() {
    simpleCircuit.feed("101");
    boolean ans = simpleCircuit.propagate();
    assertTrue(ans);
    ans = simpleCircuit.propagate();
    assertFalse(ans);
  }
  @Test (timeout=3000) public void circuit_propagate3() {
    simpleCircuit.feed("110");
    boolean ans = simpleCircuit.propagate();
    assertEquals(Signal.LO, simpleCircuit.getOutputs().get(0).getOut().getSignal());
  }
  
  @Test (timeout=3000) public void circuit_propagate4() {
    simpleCircuit.feed("X00");
    boolean ans = simpleCircuit.propagate();
    assertEquals(Signal.LO, simpleCircuit.getOutputs().get(0).getOut().getSignal());
  }
  
  @Test (timeout=3000) public void circuit_propagate5() {
    simpleCircuit.feed("10X");
    boolean ans = simpleCircuit.propagate();
    assertEquals(Signal.X, simpleCircuit.getOutputs().get(0).getOut().getSignal());
  }
  
  @Test (timeout=3000) public void circuit_propagate6() {
    Circuit andy = getAndy();
    andy.feed("00");
    andy.propagate();
    assertEquals(Signal.LO, andy.getOutputs().get(0).getOut().getSignal());
  }
  @Test (timeout=3000) public void circuit_propagate7() {
    Circuit andy = getAndy();
    andy.feed("10");
    andy.propagate();
    assertEquals(Signal.LO, andy.getOutputs().get(0).getOut().getSignal());
    andy.feed("01");
    andy.propagate();
    assertEquals(Signal.LO, andy.getOutputs().get(0).getOut().getSignal());
  }
  @Test (timeout=3000) public void circuit_propagate8() {
    Circuit andy = getAndy();
    andy.feed("11");
    andy.propagate();
    assertEquals(Signal.HI, andy.getOutputs().get(0).getOut().getSignal());
  }
  @Test (timeout=3000) public void circuit_propagate9() {
    Circuit andy = getAndy();
    andy.feed("0X");
    andy.propagate();
    assertEquals(Signal.LO, andy.getOutputs().get(0).getOut().getSignal());
    andy.feed("X0");
    andy.propagate();
    assertEquals(Signal.LO, andy.getOutputs().get(0).getOut().getSignal());
    andy.feed("XX");
    andy.propagate();
    assertEquals(Signal.X, andy.getOutputs().get(0).getOut().getSignal());
  }
  
  @Test (timeout=3000) public void circuit_read1() {
    simpleCircuit.feed("101");
    boolean ans = simpleCircuit.propagate();
    assertEquals(sigs1, simpleCircuit.read());
  }

  @Test (timeout=3000) public void circuit_read2() {
    simpleCircuit.feed("110");
    boolean ans = simpleCircuit.propagate();
    assertEquals(sigs0, simpleCircuit.read());
  }
  
  @Test (timeout=3000) public void circuit_read3() {
    simpleCircuit.feed("X00");
    boolean ans = simpleCircuit.propagate();
    assertEquals(sigs0, simpleCircuit.read());
  }
  
  @Test (timeout=3000) public void circuit_read4() {
    simpleCircuit.feed("10X");
    boolean ans = simpleCircuit.propagate();
    assertEquals(sigsX, simpleCircuit.read());
  }
  
  @Test (timeout=3000) public void circuit_read5() {
    Circuit andy = getAndy();
    andy.feed("00");
    andy.propagate();
    assertEquals(sigs0, andy.read());
  }
  @Test (timeout=3000) public void circuit_read6() {
    Circuit andy = getAndy();
    andy.feed("10");
    andy.propagate();
    assertEquals(sigs0, andy.read());
    andy.feed("01");
    andy.propagate();
    assertEquals(sigs0, andy.read());
  }
  @Test (timeout=3000) public void circuit_read7() {
    Circuit andy = getAndy();
    andy.feed("11");
    andy.propagate();
    assertEquals(sigs1, andy.read());
  }
  @Test (timeout=3000) public void circuit_read8() {
    Circuit andy = getAndy();
    andy.feed("0X");
    andy.propagate();
    assertEquals(sigs0, andy.read());
    andy.feed("X0");
    andy.propagate();
    assertEquals(sigs0, andy.read());
    andy.feed("XX");
    andy.propagate();
    assertEquals(sigsX, andy.read());
  }
  
  @Test (timeout=3000) public void circuit_andy() throws IOException {
   Circuit studentAndy = new Circuit("andy");
   Circuit handCodedAndy = getAndy();
   
   assertEquals(handCodedAndy.getInputs(), studentAndy.getInputs());
   assertEquals(handCodedAndy.getOutputs(), studentAndy.getOutputs());
   assertEquals(handCodedAndy.getInnerWires(), studentAndy.getInnerWires());
   assertEquals(handCodedAndy.getImportables(), studentAndy.getImportables());
   assertEquals(handCodedAndy.getName(), studentAndy.getName());
   
   // don't have .equals() for circuits, use string representation.
   assertEquals(handCodedAndy.getComponents().toString(), studentAndy.getComponents().toString());
  }
  
  @Test (timeout=3000) public void circuit_indent1() {
    String sub = "a b c\nd!\n";
    assertEquals("  a b c\n  d!\n", Circuit.indent(sub));
  }
  @Test (timeout=3000) public void circuit_indent2() {
    String sub = "  a\n    b\n  c\n";
    assertEquals("    a\n      b\n    c\n", Circuit.indent(sub));
  }
  
  @Test (timeout=3000) public void circuit_toString() throws IOException {
    assertEquals(new Circuit("andy").toString(), getAndy().toString());
  }
  
  /////////////////////////////////////////////////////////
  
  // GateSim can't really be tested... its only effect is to write to System.out,
  // and we can't really monitor that with a unit test.  :-/
//  @Test (timeout=3000) public void gatesim() throws IOException {
//    PrintStream ps = new PrintStream("testing_outfile.txt");
//    PrintStream oldSysout = System.out;
//    System.out = ps; // nope. it's final.
//    GateSim.main(new String[]{"and", "11"});
//    ps.close();
//    System.out = oldSysout;
//    Scanner sc = new Scanner("testing_outfile.txt");
//    String s = sc.next();
//    assertEquals("1", s);
//  }
  
  /////////////////////////////////////////////////////////
  
  // ExceptionLogicMalformedSignal tests.
  @Test (timeout=3000) public void malformedSignal1() {
   ExceptionLogicMalformedSignal e = new ExceptionLogicMalformedSignal('c', "I didn't like that character.");
   assertTrue('c'==e.getBad());
  }
  
  @Test (timeout=3000) public void malformedSignal2() {
   ExceptionLogicMalformedSignal e = new ExceptionLogicMalformedSignal('Z', "short");
   e.setBad('q');
   e.setMsg("NEW MSG");
   assertTrue('q'==e.getBad());
   assertEquals("NEW MSG", e.getMsg());
  }
  
  @Test (timeout=3000) public void malformedSignal3() {
    ExceptionLogicMalformedSignal e = new ExceptionLogicMalformedSignal('Z', "short");
    try {
      throw e;
    } catch (ExceptionLogicMalformedSignal ex){ }
  }
  
  /////////////////////////////////////////////////////////
  
  // ExceptionLogicParameters tests.
  @Test (timeout=3000) public void ELParams1() {
    ExceptionLogicParameters e = new ExceptionLogicParameters(true, 2, 4);
    assertEquals(true, e.getInputsRelated());
    assertEquals(2, e.getExpected());
    assertEquals(4, e.getFound());
  }
  
  @Test (timeout=3000) public void ELParams2() {
    ExceptionLogicParameters e = new ExceptionLogicParameters(true, 2, 4);
    e.setInputsRelated(false);
    e.setExpected(5);
    e.setFound(3);
    assertEquals(false, e.getInputsRelated());
    assertEquals(5, e.getExpected());
    assertEquals(3, e.getFound());
  }
  
  @Test (timeout=3000) public void ELParams3() {
    ExceptionLogicParameters e = new ExceptionLogicParameters(true, 2, 4);
    try {
      throw e;
    } catch (ExceptionLogicParameters ex) { }
  }
  
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  
  /////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////
  // FULL-FILE TESTS
  /////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////
  
  // some helper code that lets us test circuits and feedback circuits.
  
  public static boolean assertOutsMatch(List<Wire> outs, List<Signal> sigs){
    if (outs.size() != sigs.size()) { fail (String.format("wrong number of output wires (%d) vs. signals (%d).",outs.size(), sigs.size())); }
    for (int i=0; i<outs.size(); i++){
      if (outs.get(i).getSignal() != sigs.get(i)) return false;
    }
    return true;
  }
  
  public static void test_circuit(Circuit c, String inputs, String expectedOutputs){
    List<Signal> sigIns = Signal.fromString(inputs);
    List<Signal> sigOuts = c.inspect(sigIns);
    String actualOuts = Signal.toString(sigOuts);
    assertEquals(expectedOutputs, actualOuts);
  }
  public static void test_circuit(String filename, String inputs, String expectedOutputs){
    try {
      test_circuit(new Circuit(filename), inputs, expectedOutputs);
    } catch (IOException e) { fail("IOException when opening "+filename+" to read a Circuit." + e); }
  }
  
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_not_0() { test_circuit("not", "0", "1"); }
  @Test (timeout=3000) public void file_not_1() { test_circuit("not", "1", "0"); }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_not_X() { test_circuit("not", "X", "X"); }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_and_00() { test_circuit("and", "00", "0"); }
  @Test (timeout=3000) public void file_and_01() { test_circuit("and", "01", "0"); }
  @Test (timeout=3000) public void file_and_10() { test_circuit("and", "10", "0"); }
  @Test (timeout=3000) public void file_and_11() { test_circuit("and", "11", "1"); }
  
  //------------------------------------------------------------------------------------------
  
  @Test (timeout=3000) public void file_and_2_Xs() {
    test_circuit("and", "0X", "0");
    test_circuit("and", "X0", "0"); 
    test_circuit("and", "1X", "X");
    test_circuit("and", "X1", "X");
    test_circuit("and", "XX", "X"); 
  }
  
  //------------------------------------------------------------------------------------------
  
  @Test (timeout=3000) public void file_and_single() {
    test_circuit("and1", "0", "0");
    test_circuit("and1", "1", "1");
    test_circuit("and1", "X", "X");
  }
  
  //------------------------------------------------------------------------------------------
  
  // Exhaustive 3-inputs test of an AND gate.
  
  @Test (timeout=3000) public void file_and_3s() {
    test_circuit("and_3in", "000", "0");
    test_circuit("and_3in", "001", "0");
    test_circuit("and_3in", "010", "0");
    test_circuit("and_3in", "011", "0");
    test_circuit("and_3in", "100", "0");
    test_circuit("and_3in", "101", "0");
    test_circuit("and_3in", "110", "0");
    test_circuit("and_3in", "111", "1");
  }
  
  @Test (timeout=3000) public void file_and_3_Xs() {
    test_circuit("and_3in", "00X", "0");
    test_circuit("and_3in", "01X", "0");
    test_circuit("and_3in", "0X0", "0");
    test_circuit("and_3in", "0X1", "0");
    test_circuit("and_3in", "0XX", "0");
    test_circuit("and_3in", "10X", "0");
    test_circuit("and_3in", "11X", "X");
    test_circuit("and_3in", "1X0", "0");
    test_circuit("and_3in", "1X1", "X");
    test_circuit("and_3in", "1XX", "X");
    test_circuit("and_3in", "X00", "0");
    test_circuit("and_3in", "X01", "0");
    test_circuit("and_3in", "X0X", "0");
    test_circuit("and_3in", "X10", "0");
    test_circuit("and_3in", "X11", "X");
    test_circuit("and_3in", "X1X", "X");
    test_circuit("and_3in", "XX0", "0");
    test_circuit("and_3in", "XX1", "X");
    test_circuit("and_3in", "XXX", "X");
  }

//==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_or() {
    test_circuit("or", "00", "0");
    test_circuit("or", "01", "1");
    test_circuit("or", "10", "1");
    test_circuit("or", "11", "1");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_or_Xs() {
    test_circuit("or", "0X", "X");
    test_circuit("or", "X0", "X");
    test_circuit("or", "1X", "1");
    test_circuit("or", "X1", "1");
    test_circuit("or", "XX", "X");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_xor() {
    test_circuit("xor", "00", "0");
    test_circuit("xor", "01", "1");
    test_circuit("xor", "10", "1");
    test_circuit("xor", "11", "0");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_xor_Xs() {
    test_circuit("xor", "0X", "X");
    test_circuit("xor", "X0", "X");
    test_circuit("xor", "1X", "X");
    test_circuit("xor", "X1", "X");
    test_circuit("xor", "XX", "X");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_nand() {
    test_circuit("nand", "00", "1");
    test_circuit("nand", "01", "1");
    test_circuit("nand", "10", "1");
    test_circuit("nand", "11", "0");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_nand_Xs() {
    test_circuit("nand", "0X", "1");
    test_circuit("nand", "X0", "1");
    test_circuit("nand", "1X", "X");
    test_circuit("nand", "X1", "X");
    test_circuit("nand", "XX", "X");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_nor() {
    test_circuit("nor", "00", "1");
    test_circuit("nor", "01", "0");
    test_circuit("nor", "10", "0");
    test_circuit("nor", "11", "0");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_nor_Xs() {
    test_circuit("nor", "0X", "X");
    test_circuit("nor", "X0", "X");
    test_circuit("nor", "1X", "0");
    test_circuit("nor", "X1", "0");
    test_circuit("nor", "XX", "X");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_1() {
    test_circuit("1", "000", "0");
    test_circuit("1", "001", "1");
    test_circuit("1", "010", "0");
    test_circuit("1", "011", "1");
    test_circuit("1", "100", "0");
    test_circuit("1", "101", "1");
    test_circuit("1", "110", "1");
    test_circuit("1", "111", "1");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_1_xs() {
    test_circuit("1", "00X", "X");
    test_circuit("1", "0X0", "0");
    test_circuit("1", "X00", "0");
    test_circuit("1", "01X", "X");
    test_circuit("1", "0X1", "1");
    test_circuit("1", "X01", "1");
    test_circuit("1", "10X", "X");
    test_circuit("1", "1X0", "X");
    test_circuit("1", "X10", "X");
    test_circuit("1", "11X", "1");
    test_circuit("1", "1X1", "1");
    test_circuit("1", "X11", "1");
    test_circuit("1", "0XX", "X");
    test_circuit("1", "1XX", "X");
    test_circuit("1", "X0X", "X");
    test_circuit("1", "X1X", "X");
    test_circuit("1", "XX0", "X");
    test_circuit("1", "XX1", "1");
    test_circuit("1", "XXX", "X");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_2() {
    test_circuit("2", "000", "0");
    test_circuit("2", "001", "0");
    test_circuit("2", "010", "0");
    test_circuit("2", "011", "1");
    test_circuit("2", "100", "0");
    test_circuit("2", "101", "1");
    test_circuit("2", "110", "0");
    test_circuit("2", "111", "1");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_2_Xs() {
    test_circuit("2", "00X", "0");
    test_circuit("2", "0X0", "0");
    test_circuit("2", "X00", "0");
    test_circuit("2", "01X", "X");
    test_circuit("2", "0X1", "X");
    test_circuit("2", "X01", "X");
    test_circuit("2", "10X", "X");
    test_circuit("2", "1X0", "0");
    test_circuit("2", "X10", "0");
    test_circuit("2", "11X", "X");
    test_circuit("2", "1X1", "1");
    test_circuit("2", "X11", "1");
    test_circuit("2", "0XX", "X");
    test_circuit("2", "1XX", "X");
    test_circuit("2", "X0X", "X");
    test_circuit("2", "X1X", "X");
    test_circuit("2", "XX0", "0");
    test_circuit("2", "XX1", "X");
    test_circuit("2", "XXX", "X");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_halfadder() {
    test_circuit("halfadder", "00", "00");
    test_circuit("halfadder", "01", "01");
    test_circuit("halfadder", "10", "01");
    test_circuit("halfadder", "11", "10");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_halfadder_Xs() {
    test_circuit("halfadder", "0X", "0X");
    test_circuit("halfadder", "X0", "0X");
    test_circuit("halfadder", "1X", "XX");
    test_circuit("halfadder", "X1", "XX");
    test_circuit("halfadder", "XX", "XX");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_fulladder_basic() {
    test_circuit("fulladder_basic", "000", "00");
    test_circuit("fulladder_basic", "001", "01");
    test_circuit("fulladder_basic", "010", "01");
    test_circuit("fulladder_basic", "011", "10");
    test_circuit("fulladder_basic", "100", "01");
    test_circuit("fulladder_basic", "101", "10");
    test_circuit("fulladder_basic", "110", "10");
    test_circuit("fulladder_basic", "111", "11");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_fulladder_basic_Xs() {
    test_circuit("fulladder_basic", "00X", "0X");
    test_circuit("fulladder_basic", "0X0", "0X");
    test_circuit("fulladder_basic", "X00", "0X");
    test_circuit("fulladder_basic", "01X", "XX");
    test_circuit("fulladder_basic", "0X1", "XX");
    test_circuit("fulladder_basic", "X01", "XX");
    test_circuit("fulladder_basic", "10X", "XX");
    test_circuit("fulladder_basic", "1X0", "XX");
    test_circuit("fulladder_basic", "X10", "XX");
    test_circuit("fulladder_basic", "11X", "1X");
    test_circuit("fulladder_basic", "1X1", "XX");
    test_circuit("fulladder_basic", "X11", "XX");
    test_circuit("fulladder_basic", "0XX", "XX");
    test_circuit("fulladder_basic", "1XX", "XX");
    test_circuit("fulladder_basic", "X0X", "XX");
    test_circuit("fulladder_basic", "X1X", "XX");
    test_circuit("fulladder_basic", "XX0", "XX");
    test_circuit("fulladder_basic", "XX1", "XX");
    test_circuit("fulladder_basic", "XXX", "XX");
  }
  
  //==========================================================================================
  
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_fulladder() {
    test_circuit("fulladder", "000", "00");
    test_circuit("fulladder", "001", "01");
    test_circuit("fulladder", "010", "01");
    test_circuit("fulladder", "011", "10");
    test_circuit("fulladder", "100", "01");
    test_circuit("fulladder", "101", "10");
    test_circuit("fulladder", "110", "10");
    test_circuit("fulladder", "111", "11");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_fulladder_Xs() {
    test_circuit("fulladder", "00X", "0X");
    test_circuit("fulladder", "0X0", "0X");
    test_circuit("fulladder", "X00", "0X");
    test_circuit("fulladder", "01X", "XX");
    test_circuit("fulladder", "0X1", "XX");
    test_circuit("fulladder", "X01", "XX");
    test_circuit("fulladder", "10X", "XX");
    test_circuit("fulladder", "1X0", "XX");
    test_circuit("fulladder", "X10", "XX");
    test_circuit("fulladder", "11X", "1X");
    test_circuit("fulladder", "1X1", "XX");
    test_circuit("fulladder", "X11", "XX");
    test_circuit("fulladder", "0XX", "XX");
    test_circuit("fulladder", "1XX", "XX");
    test_circuit("fulladder", "X0X", "XX");
    test_circuit("fulladder", "X1X", "XX");
    test_circuit("fulladder", "XX0", "XX");
    test_circuit("fulladder", "XX1", "XX");
    test_circuit("fulladder", "XXX", "XX");
  }
  
  //==========================================================================================
  
  @Test (timeout=3000) public void file_mux_4_to_1() {
    test_circuit("mux_4_to_1", "1000 00", "1");
    test_circuit("mux_4_to_1", "0111 00", "0");
    test_circuit("mux_4_to_1", "0100 10", "1");
    test_circuit("mux_4_to_1", "1011 10", "0");
    test_circuit("mux_4_to_1", "0010 01", "1");
    test_circuit("mux_4_to_1", "1101 01", "0");
    test_circuit("mux_4_to_1", "0001 11", "1");
    test_circuit("mux_4_to_1", "1110 11", "0");
  }
  // check various X's.
  @Test (timeout=3000) public void file_mux_4_to_1_Xs(){
    test_circuit("mux_4_to_1", "X000 00", "X");
    test_circuit("mux_4_to_1", "X111 00", "X");
    test_circuit("mux_4_to_1", "0X00 10", "X");
    test_circuit("mux_4_to_1", "1X11 10", "X");
    test_circuit("mux_4_to_1", "00X0 01", "X");
    test_circuit("mux_4_to_1", "000X 11", "X");
    test_circuit("mux_4_to_1", "XXXX 01", "X");
    
    test_circuit("mux_4_to_1", "0000 XX", "0"); // all the AND gates will be low, so the OR gate will definitely be low.
    test_circuit("mux_4_to_1", "1111 XX", "X"); // 
    
  }
  
  //==========================================================================================
  
  // not exhaustive.
  
  @Test (timeout=3000) public void file_ripple4() {
    test_circuit("ripple4", "0000 0000 0", "00000");
    test_circuit("ripple4", "0100 1100 0", "10100");
    test_circuit("ripple4", "0100 1100 1", "01100");
    test_circuit("ripple4", "1110 1001 0", "00001");
    test_circuit("ripple4", "1110 1001 1", "10001");
  }
  
  //==========================================================================================
  // Exhaustive. 
  
  @Test (timeout=3000) public void file_nand_derived() {
    test_circuit("nand_derived", "00", "1");
    test_circuit("nand_derived", "01", "1");
    test_circuit("nand_derived", "10", "1");
    test_circuit("nand_derived", "11", "0");
  }
  //------------------------------------------------------------------------------------------
  @Test (timeout=3000) public void file_nand_derived_Xs() {
    test_circuit("nand_derived", "0X", "1");
    test_circuit("nand_derived", "X0", "1");
    test_circuit("nand_derived", "1X", "X");
    test_circuit("nand_derived", "X1", "X");
    test_circuit("nand_derived", "XX", "X");
  }
  
  //==========================================================================================  
  //==========================================================================================
  //==========================================================================================
  
  /*** BEGIN HONORS TESTS. Add a star-slash at the end of this text
    * to turn on honors section tests. 
  
  //==========================================================================================
  //==========================================================================================
  //==========================================================================================
  
  public static void test_feedback_circuit(FeedbackCircuit c, String inputs, String expectedOutputs){
    List<Signal> sigIns = Signal.fromString(inputs);
    List<Signal> sigOuts = c.inspect(sigIns);
    String actualOuts = Signal.toString(sigOuts);
    assertEquals(expectedOutputs, actualOuts);
  }
  public static void test_feedback_circuit(String filename, String inputs, String expectedOutputs){
    try {
      test_feedback_circuit(new FeedbackCircuit(filename), inputs, expectedOutputs);
    } catch (IOException e) { fail("IOException when opening "+filename+" to read a Circuit." + e); }
  }
  
  
  // this is a "not(SR) NAND" latch circuit.
  // http://en.wikipedia.org/wiki/Flip-flop_(electronics)#SR_NAND_latch
  @Test (timeout=3000) public void file_fb_nSnR_nand() {
    test_feedback_circuit("fb_nSnR_nand", "00", "11");// not allowed in regular circuits (they don't like Q and nQ both being 1).
    test_feedback_circuit("fb_nSnR_nand", "01", "10");
    test_feedback_circuit("fb_nSnR_nand", "10", "01");
    test_feedback_circuit("fb_nSnR_nand", "11", "XX");
  }
  @Test (timeout=3000) public void file_fb_nSnR_nand_Xs() {
    test_feedback_circuit("fb_nSnR_nand", "0X", "1X");
    test_feedback_circuit("fb_nSnR_nand", "1X", "XX");
    test_feedback_circuit("fb_nSnR_nand", "X0", "X1");
    test_feedback_circuit("fb_nSnR_nand", "X1", "XX");
    test_feedback_circuit("fb_nSnR_nand", "XX", "XX");
  }
  
  // this is an "SR NOR" latch circuit.
  // It has a "Set" and "Reset" wire. A high reset means reset Q=0 (and thus nQ=1).
  // While reset stays low, if we put "set" to high, it sets Q=1 (and nQ=0). 
  // returning "set" to low, Q stays at its previous value (Q=1 in the current discussion).
  // not until we put "reset" high again will Q=0.
  //
  // http://en.wikipedia.org/wiki/Flip-flop_(electronics)#SR_NOR_latch
  
  @Test (timeout=3000) public void file_fb_SR_nor_v1() throws IOException {
    FeedbackCircuit fc = new FeedbackCircuit("fb_SR_nor");
    
    // reset the Q value.
    fc.feed(Signal.fromString("01"));
    fc.propagate();
    // should have Q=0 (and nQ=1).
    assertEquals("01",Signal.toString(fc.read()));
  }
  
  @Test (timeout=3000) public void file_fb_SR_nor_v2() throws IOException {
    FeedbackCircuit fc = new FeedbackCircuit("fb_SR_nor");
    
    // reset the Q value.
    fc.feed(Signal.fromString("01"));
    fc.propagate();
    // should have Q=0 (and nQ=1).
    assertEquals("01",Signal.toString(fc.read()));
    
    // feed S=0, R=0.
    fc.feed(Signal.fromString("00"));
    fc.propagate();
    
    // should still have Q=0 (and nQ=1).
    assertEquals("01",Signal.toString(fc.read()));
  }
  
  @Test (timeout=3000) public void file_fb_SR_nor_v3() throws IOException {
    FeedbackCircuit fc = new FeedbackCircuit("fb_SR_nor");
  
    // reset the Q value.
    fc.feed(Signal.fromString("01"));
    fc.propagate();
    // should have Q=0 (and nQ=1).
    assertEquals("01",Signal.toString(fc.read()));
    
    // feed S=0, R=0.
    fc.feed(Signal.fromString("00"));
    fc.propagate();
    
    // should still have Q=0 (and nQ=1).
    assertEquals("01",Signal.toString(fc.read()));
    
    // feed S=1, R=0.
    fc.feed(Signal.fromString("10"));
    fc.propagate();
    assertEquals("10",Signal.toString(fc.read()));
    
    fc.feed(Signal.fromString("00"));
    fc.propagate();
    assertEquals("10",Signal.toString(fc.read()));
    
    fc.feed(Signal.fromString("01"));
    fc.propagate();
    assertEquals("01",Signal.toString(fc.read()));
    
    fc.feed(Signal.fromString("00"));
    fc.propagate();
    assertEquals("01",Signal.toString(fc.read()));
  }
  
  @Test (timeout=3000) public void file_memory1_1() throws IOException {
   FeedbackCircuit fc = new FeedbackCircuit("memory1");
   assertEquals("1",fc.inspect("11"));
   assertEquals("1",fc.inspect("10"));   // this time, "10" -> "1".
  }
  @Test (timeout=3000) public void file_memory1_2() throws IOException {
   FeedbackCircuit fc = new FeedbackCircuit("memory1");
   assertEquals("0",fc.inspect("00"));
   assertEquals("0",fc.inspect("10"));   // this time, "10" -> "0".
  }
  @Test (timeout=3000) public void file_memory1_3() throws IOException {
   FeedbackCircuit fc = new FeedbackCircuit("memory1");
   assertEquals("0",fc.inspect("00"));
   assertEquals("0",fc.inspect("10"));
   assertEquals("1",fc.inspect("11"));
   assertEquals("0",fc.inspect("01"));
   assertEquals("0",fc.inspect("00"));
   assertEquals("1",fc.inspect("11"));
   assertEquals("0",fc.inspect("01"));
  }
  
  @Test (timeout=3000) public void file_memory2_1() throws IOException {
    FeedbackCircuit fc = new FeedbackCircuit("memory2");
    assertEquals("1",fc.inspect("11"));
    assertEquals("1",fc.inspect("01"));
  }
  @Test (timeout=3000) public void file_memory2_2() throws IOException {
    FeedbackCircuit fc = new FeedbackCircuit("memory2");
    assertEquals("0",fc.inspect("00"));
    assertEquals("0",fc.inspect("01"));
  }
  
  // END HONORS SECTION ***/
  
}
