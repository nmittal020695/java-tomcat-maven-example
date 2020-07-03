import org.junit.Test;


import org.testng.Reporter;


import static org.junit.Assert.assertEquals;
public class Verify {
	
@Test
   public void testSet() {
      String str= "Hello World!";
     
      assertEquals("Hello World!",str);
      
   }
@Test
public void testSet1() {
   String str1= "Hello World";
  
   assertEquals("Hello world!",str1);
   
}
@Test

public void testSet2() {
    
    String str2= "this is left top align";
    
    assertEquals("this is left top align",str2);
 }
@Test

public void testSet3() {
    
    String str3= "this is right align";
    
    assertEquals("this is right top align",str3);
 }
@Test
public void testSet4() {
   String str4= "All word shows Bold";
  
   assertEquals("All word shows Bold",str4);
   
}

}
