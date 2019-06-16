val ss = Array("Hello", "world", "foo", "bar")
ss.zipWithIndex
ss.zipWithIndex.map(t => (t._2, t._1))
ss.zipWithIndex.map(t => (t._2, t._1)).toMap
