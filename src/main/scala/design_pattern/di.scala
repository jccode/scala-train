package design_pattern

trait Repository {
  def save(user: User)
}

trait DatabaseRepository extends Repository {
  override def save(user: User): Unit = {
    println("save user!")
  }
}

trait UserService {
  self: Repository =>

  def create(user: User): Unit = {
    save(user)
  }

}

class User

object di {
  def main(args: Array[String]): Unit = {
    val userService = new UserService with DatabaseRepository
    userService.create(new User)
  }
}


