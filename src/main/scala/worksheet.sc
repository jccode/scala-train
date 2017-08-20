val p = "akka://default/user/$$d/group-group1/device-device1"
val arr = p.split('/')
arr.dropRight(1).mkString("/")
arr.mkString("/")

