package testing;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;
import model.*;
import physics.Angle;
public class BallTests {
	Angle test1 = new Angle(25);
	Angle test2 = new Angle(75);
	
	Ball a = new Ball("A1", Color.red, 2.0, 2.0, test1, 3.0);
	Ball b = new Ball("B1", Color.blue, 2.5, 2.5, test2, 3.5);
	Ball c = new Ball("C1", Color.red, 2.0, 2.0, test1, 3.0);
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
