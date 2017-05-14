package gd.samples

import akka.actor.{ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.cluster.singleton.ClusterSingletonManager
import akka.cluster.singleton.ClusterSingletonManagerSettings

/**
  * Created by henry on 3/24/17.
  */
object App1 {
  def main(args: Array[String]): Unit = {

    val appName = "App1"
    val system = ActorSystem(appName)
    println("App1 is up and running")

    val singletonGuardian = system.actorOf(Props[SingletonGuardian], "singletonGuardian")

    val propsOfSampleAggRoot = (ClusterSingletonManager.props(
      singletonProps = Props[SampleAggRoot],
      terminationMessage = "backoff",
      settings = ClusterSingletonManagerSettings(system).withRole("domain").withSingletonName("ss")
    ), "sampleAggRoot")

    singletonGuardian ! propsOfSampleAggRoot

    system.registerOnTermination {
      println("All actors have been destroyed.")
    }

    sys.addShutdownHook {
      system.terminate()
      Await.result(system.whenTerminated,Duration.Inf)
      println(s"$appName has been shutdown gracefully.")
    }
  }
}
