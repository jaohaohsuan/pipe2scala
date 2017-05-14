package gd.samples

import akka.actor.{Actor, ActorInitializationException, ActorLogging, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration._
/**
  * Created by henry on 5/14/17.
  */
class SingletonGuardian extends Actor with ActorLogging {

  import akka.actor.SupervisorStrategy._

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ActorInitializationException =>
      log.warning("ActorInitializationException")
      Restart
    case _: Exception =>
      log.warning("Unexpected Exception")
      Escalate
  }

  def receive: Receive = {
    case (p: Props, name: String) => sender() ! context.actorOf(p, name)
  }
}
