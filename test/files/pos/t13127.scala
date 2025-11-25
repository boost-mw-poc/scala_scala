import scala.annotation.unchecked.uncheckedOverride

trait Hordering[T] extends java.util.Comparator[T] {
  // overriding on java 26 and later, not overriding before
  @uncheckedOverride def max[U <: T](x: U, y: U): U = x
  @uncheckedOverride def min[U <: T](x: U, y: U): U = x 
}

trait Base[T] {
  def max[U <: T](x: U, y: U): U = x

  def mux(x: String = "ex") = x
}

trait Sub[T] extends Base[T] {
  @uncheckedOverride def max[U <: T](x: U, y: U): U = x // overriding ok
  @uncheckedOverride def min[U <: T](x: U, y: U): U = x // not overriding ok

  // overriding default
  @uncheckedOverride def mux(x: String = "nox") = x

  def test = mux()
}

// this fails to compile on Java 26, see https://github.com/scala/scala/pull/11175#issuecomment-3526694773
trait OverrideOrdering[T] extends scala.math.Ordering[T] {
  override def max(x: T, y: T): T = x
}
