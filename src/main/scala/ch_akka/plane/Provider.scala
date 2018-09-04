package ch_akka.plane

import akka.actor.Actor

/**
  * PilotProvider
  *
  */
trait PilotProvider {
  def newPilot: Actor = new Pilot
  def newCoPilot: Actor = new CoPilot
  def newAutoPilot: Actor = new AutoPilot
}

trait LeadFlightAttendantProvider {
  def newLeadFlightAttendant: Actor = LeadFlightAttendant()
}

trait AltimeterProvider {
  def newAltimeter: Actor = Altimeter()
}