package model;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import physics.*;

public class Flipper extends AStationaryGizmo implements ILineSegmentCollider {
	/** Angle of rotation of flipper relative to start point, used during gameplay. 
	 * Separate from rotationAngle, which defines the rotation of the flipper within 
	 * bounding box. **/
	public double gameplayRotation; 
	public boolean flippingForward;
	public boolean leftFlipper;
	private int flipSpeed;

	private long flipTime;
	private long startedFlipping;
	
	/** A set of Circles belonging to this Gizmo. They act as collision detectors
	 * with a ball, often at the edges of a shape. **/
	protected Set<Circle> circleSet;
	/**
	 * A set of Line Segments around the edge of the absorber, which will act as
	 * the collision detector with a ball
	 **/
	private Set<LineSegment> ls;

	

	public Flipper(String name, int grid_tile_x, int grid_tile_y, Color color, boolean leftFlipper) {
		super(name, grid_tile_x * MainEngine.L, grid_tile_y * MainEngine.L, color);

		bmWidth = 2;
		bmHeight = 2;
		

		this.gameplayRotation = 0;
		this.flippingForward = false;
		this.leftFlipper = leftFlipper;
		if (!leftFlipper) {
//			this.setX((this.getX() + MainEngine.L + (MainEngine.L / 2)));
		}

	}

	
/* Flipper's non-sped up get methods */
	@Override
	public Shape getDrawingShape() {
		// rotation += 15;
		RoundRectangle2D.Double r = new RoundRectangle2D.Double(0, 0, 0.5 * MainEngine.L, 2 * MainEngine.L,
				0.5 * MainEngine.L, 0.5 * MainEngine.L);
		AffineTransform transform = new AffineTransform();

		// Apply flipper rotation
		transform.rotate(Math.toRadians(gameplayRotation), r.getX() + 5, r.getY() + 5);
		if (leftFlipper) {
			try {
				// System.out.println("inverting");
				transform.invert();
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Rotate to proper orientation
		transform.rotate(Math.toRadians(rotationAngle), MainEngine.L, MainEngine.L);

		// Position right flipper at RHS of bounding box
		if (!leftFlipper) {
			transform.translate(30, 0);
		}

		return transform.createTransformedShape(r);
	}

	@Override
	public Set<Circle> getCircles() {
		return circleSet;
	}
	
	private void setupCircles() {
		int lCorner_X = getX();
		int rCorner_X = getX() + MainEngine.L * 2;
		int tCorner_Y = getY();
		int bCorner_Y = getY() + MainEngine.L * 2;
		
		if(leftFlipper){
			Circle top = new Circle(lCorner_X + 5, tCorner_Y + 5, 5);
			Circle bottom = new Circle(lCorner_X + 5, bCorner_Y - 5, 5);

			circleSet.add(top);
			circleSet.add(bottom);
		}
		else {
			Circle top = new Circle(rCorner_X - 5, tCorner_Y + 5, 5);
			Circle bottom = new Circle(rCorner_X - 5, bCorner_Y - 5, 5);

			circleSet.add(top);
			circleSet.add(bottom);
		}
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
		int rCorner_X = getX() + MainEngine.L * 2;
		int tCorner_Y = getY();
		int bCorner_Y = getY() + MainEngine.L * 2;

		if(leftFlipper){
			LineSegment left = new LineSegment(lCorner_X, tCorner_Y + 5, lCorner_X, bCorner_Y - 5);
			LineSegment right = new LineSegment(lCorner_X + 10, tCorner_Y + 5, lCorner_X + 10, bCorner_Y - 5);

			ls.add(right);
			ls.add(left);
		}
		else {
			LineSegment left = new LineSegment(rCorner_X - 10, tCorner_Y + 5, rCorner_X - 10, bCorner_Y - 5);
			LineSegment right = new LineSegment(rCorner_X, tCorner_Y + 5, rCorner_X, bCorner_Y - 5);

			ls.add(right);
			ls.add(left);
		}
	}

	public void updateCollections() {
		setupLineSeg();
		setupCircles();
	}

	
	
/* Regular methods implementation */
	@Override
	public void triggerAction() {
		// TODO Auto-generated method stub
		flippingForward = true;
		startedFlipping = System.nanoTime();
	}
	
	@Override
	public boolean rotate(int degree) {
		// TODO Validation
		rotationAngle = (rotationAngle + degree) % 360;

		return false;
	}

/* Overwritten methods */
	@Override
	public boolean move(int grid_tile_x, int grid_tile_y) {
		// TODO Validation
		
		super.move(grid_tile_x * MainEngine.L, grid_tile_y * MainEngine.L);

		return false;
	}

	@Override
	public String toString() {
		String s;

		if (leftFlipper) {
			s = "LeftFlipper " + super.toString();
		} else {
			s = "RightFlipper " + super.toString();
		}

		for (int i = rotationAngle; i > 0; i -= 90) {
			s += "\nRotate " + getGizmoID();
		}

		return s;
	}

	
/* Flipper exclusive methods */
	public void update() {
		if (flippingForward) {
			gameplayRotation += flipSpeed;
		} else {
			gameplayRotation -= flipSpeed;
		}

		if (gameplayRotation > 90) {
			gameplayRotation = 90;
		} else if (gameplayRotation < 0) {
			gameplayRotation = 0;
		}

		if (System.nanoTime() - startedFlipping > flipTime) {
			flippingForward = false;
		}
		// Circle circle = new physics.Circle(new physics.Vect(this.getX(),
		// this.getY()), 10);

	}
	
	// use this method TWICE for each flipper for a full 90-degree rotation
	public void rotate45degrees(){
		Vect rotationPoint;
		double SQRT = Math.sqrt(0.5); // calculation for 45-degree angle
		if (flippingForward){
		if (leftFlipper) {
			rotationPoint = new Vect(getX() + 5, getY() + 5);
			for (Circle each : circleSet){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, SQRT)); // rotate 45 degrees
			}
			for (LineSegment each : ls){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, SQRT)); // rotate 45 degrees
			}
		}
		else {
			rotationPoint = new Vect(getX() + MainEngine.L * 2 - 5, getY() + 5);
			for (Circle each : circleSet){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, -SQRT)); // rotate 315 degrees
			}
			for (LineSegment each : ls){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, -SQRT)); // rotate 315 degrees
			}
		}
		}
	}
	
	// use this method TWICE for each flipper for a full 90-degree rotation BACKWARDS
	public void rotateback45degrees(){
		Vect rotationPoint;
		double SQRT = Math.sqrt(0.5); // calculation for 45-degree angle
		if (!flippingForward){
		if (leftFlipper) {
			rotationPoint = new Vect(getX() + 5, getY() + 5);
			for (Circle each : circleSet){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, -SQRT)); // rotate 315 degrees
			}
			for (LineSegment each : ls){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, -SQRT)); // rotate 315 degrees
			}
		}
		else {
			rotationPoint = new Vect(getX() + MainEngine.L * 2 - 5, getY() + 5);
			for (Circle each : circleSet){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, SQRT)); // rotate 45 degrees
			}
			for (LineSegment each : ls){
				Geometry.rotateAround(each, rotationPoint, new Angle(SQRT, SQRT)); // rotate 45 degrees
			}
		}
		}
	}

	public Vect getRotationPoint(){
		if (leftFlipper) {return new Vect(getX() + 5, getY() + 5);}
		else return new Vect(getX() + MainEngine.L * 2 - 5, getY() + 5);
	}
	
	// returns true if left flipper, false - if right flipper
	public boolean isLeftFlipper(){
		return leftFlipper;
	}
}
