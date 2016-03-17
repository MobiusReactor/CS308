package model;

import java.util.Collection;
import java.util.Map;
import java.util.Observer;
import physics.Vect;

public interface IMainEngine {

	public void moveBalls();

	public double getMoveTime();
	
	public void setBallSpeed(Ball b, Vect velo);

	public void start();

	public void stop();

	public boolean isPlaying();
	
	public boolean addGizmo(AGizmoComponent gizmo);

	public boolean removeGizmo(AGizmoComponent gizmo);

	public void rotateGizmo(AGizmoComponent gizmo, int degree);

	public AGizmoComponent getStationaryGizmoAt(int x, int y);
	
	public boolean moveGizmo(AGizmoComponent gizmo, int x, int y);

	public Map<String, AGizmoComponent> getGizmosMap();
	
	public Collection<AGizmoComponent> getAllGizmos();

	public void loadFile(String filePath);

	public void saveFile(String filePath);

	/**
	 * Set the preferred Wall width & height for the Gizmo Board (pixels). 
	 * This method will change the Wall object inside the MainEngine to the
	 * appropriate height/width. 
	 * @param width - the new width for the walls of the board, in pixels.
	 * @param height - the new height for the walls of the board, in pixels.
	 */
	public void setWallDimensions(int width, int height);
	
	public void addObserver(Observer o);

	/** TODO Temporarily Line, REMOVE\CHANGE before final release **/
	public Ball getBall();
}