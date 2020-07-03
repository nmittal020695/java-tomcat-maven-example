package testpackage;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.google.common.base.Verify;

public class VerifyRunner {
	public static void main(String[] args) {
	      Result result = JUnitCore.runClasses(Verify.class);
	      for (Failure failure : result.getFailures()) {
	         System.out.println(failure.toString());
	      }
	      System.out.println("Result=="+result.wasSuccessful());

}
}

