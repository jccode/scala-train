package ch_akka.plane

import akka.actor.{Actor, ActorRef}

/**
  * PilotProvider
  *
  */
trait PilotProvider {
  def newPilot(plane: ActorRef, autopilot: ActorRef): Actor = new Pilot(plane, autopilot)
  def newCoPilot: Actor = new CoPilot
  def newAutoPilot: Actor = new AutoPilot
}

trait LeadFlightAttendantProvider {
  def newLeadFlightAttendant: Actor = LeadFlightAttendant()
}

trait AltimeterProvider {
  def newAltimeter: Actor = Altimeter()
}