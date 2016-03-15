package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Observable;
import java.util.Set;
import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

public class MainEngine extends Observable implements IMainEngine {
	/* Constants */
	final static int L = 20;

	/* Game Component */
	private Set<Ball> ballSet;

	/** TODO Temporarily Line, REMOVE\CHANGE before final release **/
	public Ball ball;// Using one ball to test, this is the only ball for now

	private Map<String, AGizmoComponent> gizmos;

	private Walls gws;

	/* Game Mechanic */
	private PhysicsConfig physicsSettings;
	// TODO might change SpecialCollisionHandler to be static-based too
	private CollisionHandler collisionHandler;
	private Connections customConnections;

	/* Run time values */
	/** How frequent each tick of the ball is. Essentially this is frame per seconds; the lower this value, 
	 * the smoother the animation will be, but also more computationally expensive. **/
	private double moveTime = 1/60;			// 60 fps
	
	private boolean isPlaying; // used to tell Keyboard ActionListeners when
								// they should be active (only when the game is
								// running)


	public MainEngine() {
		gizmos = new HashMap<String, AGizmoComponent>();

		physicsSettings = new PhysicsConfig();
		customConnections = new Connections();

		/** TODO Temporarily Line, REMOVE\CHANGE before final release **/
//		ball = new Ball("Ball", Color.RED, 50, 50, new Angle(45), 50);
	}

	@Override
	public void moveBalls() {
		// Friction		- from 	6.170 Final Project  Gizmoball
		double mu1 = physicsSettings.getFrictionCoef1();
		double mu2 = physicsSettings.getFrictionCoef2();

		// Gravity		- from 	6.170 Final Project  Gizmoball
		double gravity = physicsSettings.getGravity();

		
		List<CollisionDetails> collisionList = calcTimesUntilCollision();		// called to get a list of collisions

		
		// Temp variables setup
		Ball ball;
		double frictionScale;
		double tuc;
		AGizmoComponent collider;
		
		
		for (CollisionDetails cd : collisionList) {
			ball = cd.getBall();
			
			// Apply friction to Ball
			frictionScale = 1 - mu1 * moveTime - ball.getVelo().length() * mu2 * moveTime;
			ball.setVelo(ball.getVelo().times(frictionScale));
			
			// Apply gravity to Ball
			ball.setVelo(ball.getVelo().plus(new Vect(Angle.DEG_90, gravity * moveTime)));
			
			tuc = cd.getTuc();		// i.e. what is the time to the nearest future collision...?
			
			if (tuc > moveTime) {
				// No collision ...
				ball = moveBallAtCurrentVelo(ball, moveTime);
			} else {
				// We've got a collision in tuc, so move the ball until it directly touches the collider
				ball = moveBallAtCurrentVelo(ball, tuc);	

				collider = getGizmo(cd.getColliderName());
				// Now handle the collision
				collisionHandler.handleCollision(cd, collider);
			}

		}

		// Notify observers ... redraw updated view
		update();

	}

	public Ball moveBallAtCurrentVelo(Ball ball, double time) {
		double newX = 0.0;
		double newY = 0.0;
		double xVel = ball.getVelo().x();
		double yVel = ball.getVelo().y();
		newX = ball.getPreciseX() + (xVel * time);
		newY = ball.getPreciseY() + (yVel * time);
		ball.setPreciseX(newX);
		ball.setPreciseY(newY);
		return ball;
	}

	private List<CollisionDetails> calcTimesUntilCollision() {

		List<CollisionDetails> collisionList = new ArrayList<CollisionDetails>();
		
		for (Ball ball : ballSet) {
			// Find Time Until Collision and also, if there is a collision, the
			// new
			// speed vector.
			// Create a physics.Circle from Ball
			Circle ballCircle = ball.getCircle();
			Vect ballVelocity = ball.getVelo();
			Vect newVelo = new Vect(0, 0);

			// Now find shortest time to hit a vertical line or a wall line
			double shortestTime = Double.MAX_VALUE;
			double time = 0.0;

			String colliderID = null; // the Gizmo component that the ball will
										// collide with in a collision
										// prediction


			// Time to collide with 4 walls
			ArrayList<LineSegment> lss = gws.getLineSegments();
			for (LineSegment line : lss) {
				time = Geometry.timeUntilWallCollision(line, ballCircle, ballVelocity);
				if (time < shortestTime) {
					shortestTime = time;
					newVelo = Geometry.reflectWall(line, ballVelocity, 1.0);
					colliderID = "Wall";
				}
			}


			// Time to collide with all Gizmos
			Set<Circle> circleSet;
			Set<LineSegment> lsSet;

			Collection<AGizmoComponent> allGizmos = getAllGizmos();

			for (AGizmoComponent gizmo : allGizmos) {
				circleSet = gizmo.getCircles();

				// Checking collision with all the Circles
				for (Circle circle : circleSet) {
					time = Geometry.timeUntilCircleCollision(circle, ballCircle, ballVelocity);
					if (time < shortestTime) {
						shortestTime = time;
						newVelo = Geometry.reflectCircle(circle.getCenter(), ballCircle.getCenter(), ballVelocity, 1.0);
						colliderID = gizmo.getGizmoID();
					}
				}

				if (gizmo instanceof ILineSegmentCollider) {
					lsSet = ((ILineSegmentCollider) gizmo).getLineSeg();

					// Checking collision with all the Line Segments
					for (LineSegment line : lsSet) {
						time = Geometry.timeUntilWallCollision(line, ballCircle, ballVelocity);
						if (time < shortestTime) {
							shortestTime = time;
							newVelo = Geometry.reflectWall(line, ballVelocity, 1.0);
							colliderID = ((AGizmoComponent) gizmo).getGizmoID();
						}
					}
				}
			}

			CollisionDetails cd = new CollisionDetails(shortestTime, newVelo, ball, colliderID);
			collisionList.add(cd);
		}
		
		return collisionList;
	}
	
	@Override
	public double getMoveTime(){
		return moveTime;
	}
	
	
	@Override
	public void setBallSpeed(Ball b, Vect velo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addGizmo(AGizmoComponent gizmo) {
		// TODO Validation

		AGizmoComponent g = getGizmoAt(gizmo.getX() / L, gizmo.getY() / L);

		if (g != null) {
			removeGizmo(g);
		}
		gizmos.put(gizmo.getGizmoID(), gizmo);

		update();

		return false;
	}

	@Override
	public AGizmoComponent getGizmoAt(int x, int y) {
		for (AGizmoComponent g : gizmos.values()) {
			if (g.getX() / L == x && g.getY() / L == y) {
				return g;
			}

			if (g instanceof Flipper) {
				if (g.getX() / L >= x - 1 && g.getX() / L <= x && g.getY() / L >= y - 1 && g.getY() / L <= y) {
					return g;
				}
			}
		}
		return null;
	}

	public AGizmoComponent getGizmo(String name) {
		return gizmos.get(name);
	}

	public boolean rotateGizmo(int x, int y, int degree) {
		// TODO Validation
		return false;
	}

	@Override
	public boolean removeGizmo(AGizmoComponent gizmo) {
		// TODO Handle null
		// TODO remove connections

		gizmos.remove(gizmo.getGizmoID());
		update();

		return !(gizmos.containsValue(gizmo));
	}

	@Override
	public Collection<AGizmoComponent> getAllGizmos() {
		return gizmos.values();
	}

	@Override
	public Map<String, AGizmoComponent> getGizmosMap() {
		return gizmos;
	}

	@Override
	public void loadFile(String filePath) {
		gizmos = new HashMap<String, AGizmoComponent>();		// get rid of all existing Gizmos
		SaveDataEngine.loadFile(filePath, this);
	}

	@Override
	public void saveFile(String filePath) {
		SaveDataEngine.saveFile(filePath, this);
	}

	/** TODO Temporarily Line, REMOVE\CHANGE before final release **/
	public Ball getBall() {
		return ball;
	}

	public void update() {
		setChanged();
		notifyObservers();
	}

	@Override
	public void rotateGizmo(AGizmoComponent gizmo, int degree) {
		// TODO handle null
		gizmo.rotate(degree);
		update();
	}
}
