package gd.samples

import akka.actor.Actor

/**
  * Created by henry on 5/14/17.
  */
class SampleAggRoot extends Actor with akka.actor.ActorLogging {

  override def preStart(): Unit = {
    log.info("SampleAggRoot up")
  }

  def receive: Receive = {
    case "backoff" => context stop self
    case s: String => log.info(s)
  }
}
