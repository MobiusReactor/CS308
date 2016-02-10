import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;

public class SquareBumper extends Gizmo {
	public SquareBumper(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public void rotate() {
	}

	public Color getColor() {
		return Color.RED;
	}

	@Override
	public Shape getShape() {
		return new Rectangle(Const.L, Const.L);
	}
}