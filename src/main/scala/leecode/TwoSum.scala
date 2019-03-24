package leecode

object TwoSum {

  /**
    * 蛮力
    * 时间复杂度:O(n*n), 空间复杂度:O(1)
    *
    * @param nums
    * @param target
    * @return
    */
  def twoSum(nums: Array[Int], target: Int): Array[Int] = {
    if (nums.length < 2)
      return Array.empty

    for (i <- 0 until (nums.length-1)) {
      for (j <- i+1 until nums.length) {
        if (nums(i) + nums(j) == target)
          return Array(i, j)
      }
    }

    Array.empty
  }

  def main(args: Array[String]): Unit = {
    println(twoSum(Array(2, 8, 11, 15, 7), 9).mkString(","))
    println(twoSum2(Array(2, 8, 11, 15, 7), 9).mkString(","))
    println(twoSum3(Array(2, 8, 11, 15, 7), 9).mkString(","))
  }


  /**
    * 两遍哈希表
    * 时间复杂度: O(n),  空间复杂度: O(n)
    *
    * @param nums
    * @param target
    * @return
    */
  def twoSum2(nums: Array[Int], target: Int): Array[Int] = {
    if (nums.length < 2)
      return Array.empty

    val map = nums.zipWithIndex.toMap
    for (i <- 0 until (nums.length - 1)) {
      val opt = map.get(target - nums(i))
      if (opt.isDefined && opt.get != i) {
        return Array(i, opt.get)
      }
    }

    Array.empty
  }

  /**
    * 一遍哈希表
    * 时间复杂度: O(n),  空间复杂度: O(n)
    *
    * @param nums
    * @param target
    * @return
    */
  def twoSum3(nums: Array[Int], target: Int): Array[Int] = {
    if (nums.length < 2)
      return Array.empty

    import scala.collection.mutable
    val map = new mutable.HashMap[Int, Int]()
    for (i <- nums.indices) {
      val opt = map.get(target - nums(i))
      if (opt.isDefined) {
        return Array(opt.get, i)
      }
      //map.put(nums(i), i)
      map += (nums(i) -> i)
    }

    Array.empty
  }
}
