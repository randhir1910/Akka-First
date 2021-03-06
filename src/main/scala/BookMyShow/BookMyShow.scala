package BookMyShow

import akka.actor.{ ActorSystem, Props}
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory

object BookMyShow extends App {
  val config = ConfigFactory.parseString(
    """
      |akka.actor.deployment {
      | /poolRouter {
      |   router = round-robin-pool
      |   nr-of-instances = 5
      | }
      |}
    """.stripMargin
  )
  val system = ActorSystem("BookMyShow",config)
  val bookingActorRef = system.actorOf(Props[BookingActor])
  val userRef1 = system.actorOf(FromConfig.props(Props(new UserActor(bookingActorRef))),"poolRouter")

/*start 5 child of actor in which 4 requests for same seat.
* THERE IS 4 SEATS ----- A,B,C,D
*
* */

  userRef1 ! "A"
  userRef1 ! "A"
  userRef1 ! "A"
  userRef1 ! "A"
  userRef1 ! "B" // request for different seat


}
