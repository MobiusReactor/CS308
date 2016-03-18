package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashSet;
import java.util.Set;
import physics.Circle;
import physics.LineSegment;

public class SquareBumper extends AStationaryGizmo implements ILineSegmentCollider {
	
/* Collections used to optimise up game performance */
	/** The visual representation of the Gizmo. Used by drawing code to determine 
	 * what a Gizmo will look like on screen. **/
	private Shape drawingShape;
	/** A set of Circles belonging to this Gizmo. They act as collision detectors
	 * with a ball, often at the edges of a shape. **/
	protected Set<Circle> circleSet;
	/**
	 * A set of Line Segments around the edge of the absorber, which will act as
	 * the collision detector with a ball
	 **/
	private Set<LineSegment> ls;
	
	
	public SquareBumper(String name, int grid_tile_x, int grid_tile_y, Color color) {
		super(name, grid_tile_x * MainEngine.L, grid_tile_y * MainEngine.L, color);
		
		
		// Collection-speed up initialisation 
		circleSet = new HashSet<Circle>();
		ls = new HashSet<LineSegment>();
		updateCollections();
	}

	
	
/* Square's Collection-sped up methods (Circle, DrawingShape, LineSegment) */
	@Override
	public Shape getDrawingShape() {
		return drawingShape;
	}
	
	private void setupDrawingShape() {
		drawingShape = new Rectangle(MainEngine.L, MainEngine.L);
	}

	@Override
	public Set<Circle> getCircles() {
		return circleSet;
	}
	
	private void setupCircles() {
		int lCorner_X = getX();
		int rCorner_X = getX() + MainEngine.L;
		int tCorner_Y = getY();
		int bCorner_Y = getY() + MainEngine.L;

		Circle topleft = new Circle(lCorner_X, tCorner_Y, 0);
		Circle topright = new Circle(rCorner_X, tCorner_Y, 0);
		Circle bottomright = new Circle(rCorner_X, bCorner_Y, 0);
		Circle bottomleft = new Circle(lCorner_X, bCorner_Y, 0);

		circleSet.add(topleft);
		circleSet.add(topright);
		circleSet.add(bottomright);
		circleSet.add(bottomleft);
	}
	
	@Override
	public Set<LineSegment> getLineSeg() {
		return ls;
	}
	
	/**
	 * The setup of the Line Segment collection. Gizmo component that rely on
	 * Line Segments for collision detection should set up their Line Segment
	 * objects here.
	 * 
	 * @modify this
	 * @effect Fill the collection which hold all the Line Segments in this
	 *         class with appropriate objects
	 */
	private void setupLineSeg() {
		int lCorner_X = getX();
		int rCorner_X = getX() + MainEngine.L;
		int tCorner_Y = getY();
		int bCorner_Y = getY() + MainEngine.L;

		LineSegment top = new LineSegment(lCorner_X, tCorner_Y, rCorner_X, tCorner_Y);
		LineSegment right = new LineSegment(rCorner_X, tCorner_Y, rCorner_X, bCorner_Y);
		LineSegment bottom = new LineSegment(rCorner_X, bCorner_Y, lCorner_X, bCorner_Y);
		LineSegment left = new LineSegment(lCorner_X, bCorner_Y, lCorner_X, tCorner_Y);

		ls.add(top);
		ls.add(right);
		ls.add(bottom);
		ls.add(left);
	}

	public void updateCollections() {
		setupDrawingShape();
		setupLineSeg();
		setupCircles();
	}
	
	
/* Regular methods implementation */
	@Override
	public void triggerAction() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean rotate(int degree) {
		// Square shouldn't be rotatable so this method doesn't need to do anything
		return true;
	}

/* Overwritten methods */
	@Override
	public boolean move(int grid_tile_x, int grid_tile_y) {
		// TODO Validation
		
		super.move(grid_tile_x * MainEngine.L, grid_tile_y * MainEngine.L);
		
		updateCollections();
		
		return false;
	}
	
	@Override
	public String toString(){
		return "Square " + super.toString();
	}
}
