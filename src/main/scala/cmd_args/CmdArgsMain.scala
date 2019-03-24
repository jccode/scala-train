package cmd_args


/**
  * Testing
  *
  *     sbt run arg1 arg2
  *
  *
  *
  */
object CmdArgsMain extends App {

  println(s"You pass ${args.length} arguments. ${args.mkString(",")}")

}
