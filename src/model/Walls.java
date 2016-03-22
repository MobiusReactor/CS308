package model;

import java.util.HashSet;
import java.util.Set;

import physics.LineSegment;

public class Walls {

	private int xpos1;
	private int ypos1;
	private int ypos2;
	private int xpos2;
	
	private Set<LineSegment> ls;

	// Walls are the enclosing Rectangle - defined by top left corner and bottom
	// right
	public Walls(int x1, int y1, int x2, int y2) {
		xpos1 = x1;
		ypos1 = y1;
		xpos2 = x2;
		ypos2 = y2;

		ls = new HashSet<LineSegment>();
		LineSegment l1 = new LineSegment(xpos1, ypos1, xpos2, ypos1);
		LineSegment l2 = new LineSegment(xpos1, ypos1, xpos1, ypos2);
		LineSegment l3 = new LineSegment(xpos2, ypos1, xpos2, ypos2);
		LineSegment l4 = new LineSegment(xpos1, ypos2, xpos2, ypos2);
		ls.add(l1);
		ls.add(l2);
		ls.add(l3);
		ls.add(l4);
	}

	public Set<LineSegment> getLineSegments() {
		return ls;
	}
	
	public int getWidthInL(){
		return (xpos2 - xpos1) / MainEngine.L;
	}
	
	public int getHeightInL(){
		return (ypos2 - ypos1) / MainEngine.L;

	}

}
