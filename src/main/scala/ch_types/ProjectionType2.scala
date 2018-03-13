package ch_types

/**
  * ProjectionType2
  *
  * @author 01372461
  */
object ProjectionType2 extends App {

}

trait ComputerFactory[T] {
  def manufacture(): Computer[T]
}

trait Computer[T] {
  type BrandType
}

trait Brand

class ThinkPad extends Brand

class ThinkPadT450 extends Computer[ThinkPad] {
  override final type BrandType = ThinkPad

}

