package model;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import enums.Connections_Status;

public class Connections {

	private Map<Integer, Set<AGizmoComponent>> keyPressedMap;
	private Map<Integer, Set<AGizmoComponent>> keyReleasedMap;

	private Map<AGizmoComponent, Set<AGizmoComponent>> triggerMap;

	public Connections(Map<Integer, Set<AGizmoComponent>> kPressedMap, Map<Integer, Set<AGizmoComponent>> kReleasedMap, Map<AGizmoComponent, Set<AGizmoComponent>> gtMap) {
		keyPressedMap = kPressedMap;
		keyReleasedMap = kReleasedMap;
		triggerMap = gtMap;
	}

	public Connections() {
		keyPressedMap = new HashMap<Integer, Set<AGizmoComponent>>();
		keyReleasedMap = new HashMap<Integer, Set<AGizmoComponent>>();
		triggerMap = new HashMap<AGizmoComponent, Set<AGizmoComponent>>();
	}

	/**
	 * TODO
	 * 
	 * @param keyCode
	 * @param gizmo
	 * @return
	 */
	public Connections_Status.AC addKeyConnection(int keyCode, int type, AGizmoComponent gizmo) {
		// parameter validation
		if (gizmo == null ||
				KeyEvent.getKeyText(keyCode).contains("Unknown keyCode") // check if keyCode refers to a fake key
		) {
			return Connections_Status.AC.INVALID_ARG;
		}


		Set<AGizmoComponent> gizmoSet;

		if (type == KeyEvent.KEY_PRESSED) {
			gizmoSet = keyPressedMap.get(keyCode);
		} else {
			gizmoSet = keyReleasedMap.get(keyCode);
		}

		if (gizmoSet == null) { // i.e. check if there is no recorded connection for this key yet
			gizmoSet = new HashSet<AGizmoComponent>();
		}


		if (!gizmoSet.add(gizmo)) { // i.e. does connection already exist...?
			return Connections_Status.AC.CONNECTION_EXIST;
		}

		if (type == KeyEvent.KEY_PRESSED) {
			keyPressedMap.put(keyCode, gizmoSet);
		} else {
			keyReleasedMap.put(keyCode, gizmoSet);
		}

		return Connections_Status.AC.OK;
	}

	public Connections_Status.RC removeKeyConnection(int keyCode, int type, AGizmoComponent gizmo) {
		// parameter validation
		if (gizmo == null ||
				KeyEvent.getKeyText(keyCode).contains("Unknown keyCode") // check if keyCode refers to a fake key
		) {
			return Connections_Status.RC.INVALID_ARG;
		}

		Set<AGizmoComponent> gizmoSet;

		if (type == KeyEvent.KEY_PRESSED) {
			gizmoSet = keyPressedMap.get(keyCode);
		} else {
			gizmoSet = keyReleasedMap.get(keyCode);
		}

		if (gizmoSet == null)
			return Connections_Status.RC.CONNECTION_NOT_EXIST;
		else {
			if (!gizmoSet.remove(gizmo)) { // i.e. check if there is no recorded connection for this key yet
				return Connections_Status.RC.CONNECTION_NOT_EXIST;
			}
			keyPressedMap.put(keyCode, gizmoSet);
		}

		return Connections_Status.RC.OK;
	}

	public Set<AGizmoComponent> getKeyConnections(int keyCode, int type) {
		Set<AGizmoComponent> gizmoSet;

		if (type == KeyEvent.KEY_PRESSED) {
			gizmoSet = keyPressedMap.get(keyCode);
		} else {
			gizmoSet = keyReleasedMap.get(keyCode);
		}

		if (gizmoSet != null) {
			return gizmoSet;
		} else {
			return java.util.Collections.emptySet();
		}
	}

	public void clearKeyConnection(int keyCode, AGizmoComponent g, int type) {
		Set<AGizmoComponent> gizmoSet;

		if (type == KeyEvent.KEY_PRESSED) {
			gizmoSet = keyPressedMap.get(keyCode);
		} else {
			gizmoSet = keyReleasedMap.get(keyCode);
		}

		if (gizmoSet != null) {
			gizmoSet.remove(g);
		}
	}

	public void clearALLKeyConnection(int type) {
		if (type == KeyEvent.KEY_PRESSED) {
			keyPressedMap.clear();
		} else {
			keyReleasedMap.clear();
		}
	}


	public Connections_Status.AC addGizmoTriggerConnection(AGizmoComponent triggeredGizmo, AGizmoComponent reactionGizmo) {
		// parameter validation
		if (triggeredGizmo == null || reactionGizmo == null) {
			return Connections_Status.AC.INVALID_ARG;
		}


		Set<AGizmoComponent> gizmoSet = triggerMap.get(triggeredGizmo);
		if (gizmoSet == null) { // i.e. check if there is no recorded connection for this key yet
			gizmoSet = new HashSet<AGizmoComponent>();
		}


		if (!gizmoSet.add(reactionGizmo)) { // i.e. does connection already exist...?
			return Connections_Status.AC.CONNECTION_EXIST;
		}

		triggerMap.put(triggeredGizmo, gizmoSet);

		return Connections_Status.AC.OK;
	}

	public Connections_Status.RC removeGizmoTriggerConnection(AGizmoComponent triggeredGizmo, AGizmoComponent reactionGizmo) {
		// parameter validation
		if (triggeredGizmo == null || reactionGizmo == null) {
			return Connections_Status.RC.INVALID_ARG;
		}

		Set<AGizmoComponent> gizmoSet = triggerMap.get(triggeredGizmo);

		if (gizmoSet == null)
			return Connections_Status.RC.CONNECTION_NOT_EXIST;
		else {
			if (!gizmoSet.remove(reactionGizmo)) { // i.e. check if there is no recorded connection for this key yet
				return Connections_Status.RC.CONNECTION_NOT_EXIST;
			}
			triggerMap.put(triggeredGizmo, gizmoSet);
		}

		return Connections_Status.RC.OK;
	}

	public Set<AGizmoComponent> getGizmoTriggerConnections(AGizmoComponent gizmo) {
		return triggerMap.get(gizmo);
	}

	public void clearTriggerConnection(AGizmoComponent gizmo) {
		Set<AGizmoComponent> gizmoSet = triggerMap.get(gizmo);
		gizmoSet.clear();
	}

	public void clearALLGizmoTriggerConnection() {
		triggerMap.clear();
	}

	public void removeAllKeyBindings(AGizmoComponent gizmo) {
		for (Map.Entry<Integer, Set<AGizmoComponent>> e : keyPressedMap.entrySet()) {
			for (AGizmoComponent g : e.getValue()) {
				if (g.getGizmoID().equals(gizmo.getGizmoID())) {
					removeKeyConnection(e.getKey(), KeyEvent.KEY_PRESSED, gizmo);
				}
			}
		}
		for (Map.Entry<Integer, Set<AGizmoComponent>> e : keyReleasedMap.entrySet()) {
			for (AGizmoComponent g : e.getValue()) {
				if (g.getGizmoID().equals(gizmo.getGizmoID())) {
					removeKeyConnection(e.getKey(), KeyEvent.KEY_RELEASED, gizmo);
				}
			}
		}
	}

	public void removeAllGizmoConnections(AGizmoComponent gizmo) {
		triggerMap.remove(gizmo);
	}


	public Map<Integer, Set<AGizmoComponent>> getKeyPressBindings() {
		return keyPressedMap;
	}

	public Map<Integer, Set<AGizmoComponent>> getKeyReleaseBindings() {
		return keyReleasedMap;
	}

	public Map<AGizmoComponent, Set<AGizmoComponent>> getConnections() {
		return triggerMap;
	}

//	keyPressedMap = kPressedMap;
//	keyReleasedMap = kReleasedMap;
//	triggerMap = gtMap;
}
