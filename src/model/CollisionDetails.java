package model;

import physics.Vect;

public class CollisionDetails {

	private double tuc; //time until collision
	private Vect velo;
	private Ball b; // identify which ball is involved in a collision & provide access to the object
	private String clderName; // identify which Gizmo Component is involved in a collision

	public CollisionDetails(double t, Vect v, Ball ball, String colliderID) {
		tuc = t;
		velo = v;
		clderName = colliderID;
		b = ball;
	}

	public double getTuc() {
		return tuc;
	}

	public Vect getVelo() {
		return velo;
	}

	public Ball getBall() {
		return b;
	}

	public String getColliderName() {
		return clderName;
	}

}
