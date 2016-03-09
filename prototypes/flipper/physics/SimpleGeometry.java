package flipper.physics;import java.lang.Double; // import statement added to mollify javadocimport java.util.Iterator;import flipper.physics.Geometry.DoublePair;import flipper.physics.Geometry.VectPair;/** * SimpleGeometry alters the behavior of GeometyImpl by removing the * doughnut optimizations.  This often reduces the running time of the * timeUntilRotating* methods, but <b>will</b> reduce accuracy unless * a small maximumForesight is used.  Most callers will not use * SimpleGeometry directly, but will use the singleton Geometry * instead. * * <p> *  * The doughnut optimizations are used by the timeUntilRotating* * methods to narrow the possible times during which a collision might * happen, in order to narrow the search space and improve accuracy. * * <p> * * When dought optimizations are disabled, the timeUntilRotating* * methods will always evaluate at least <code>searchSlices</code> * data points between 0 and <code>maximumForesight</code> to search * for a root.  This will be faster for the cases where the doughnut * calculations do not lead to a useful decrease in the time being * searched, but will be slower for the cases where the doughnut * optimizations would have deduced that no collision was possible. * * @see flipper.physics.GeometryImpl * @see flipper.physics.Geometry **/public class SimpleGeometry  extends GeometryImpl{  /**   * @requires (maximumForesight >= 0.0) && (searchSlices >= 1) &&   * ((searchSlices >= 200) || (maximumForesight / searchSlices <= 0.01))   *   * @effects Constructs a SimpleGeometry with the specified tuning   * parameters, which are described in the class overview of   * GeometryImpl.   *   * @see flipper.physics.GeometryImpl   **/    public SimpleGeometry(double maximumForesight, int searchForCollisionSlices) {    super(maximumForesight, searchForCollisionSlices);    if (!((searchSlices >= 200) || ((maximumForesight / searchSlices) <= 0.01))) {      throw new IllegalArgumentException();    }  }  /**   * @effects performs the operation in a more conservative way than   * the superclass implementation (omits the doughnut optimizations).   **/  protected IntervalList restrictSearchInterval(IntervalList intervals,						double inner_radius,						double outer_radius,						double phi_1,						double phi_2,						double omega,						Vect center,						Circle ball,						Vect velocity)  {    // Compute the interval where we are in the outer circle    Circle outer_plus_ball =      new Circle(center, outer_radius + ball.getRadius());    DoublePair dp = timeUntilCircleCollision(outer_plus_ball,					     ball.getCenter(), velocity);    // If we never hit, we have no interval    if (!dp.areFinite()) {      return new IntervalList();    }    // Limit to the outer circle time    intervals.restrictToInterval(dp.d1, dp.d2);    return intervals;  }}