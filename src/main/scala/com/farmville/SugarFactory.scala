package com.farmville

import rx.lang.scala.Observable

/**
  * Created by sujit on 6/29/17.
  */
case class SugarFactory(silo: Silo, numberOfSlot: Int = 3, factorySlots: List[Slot] = List(Slot(None), Slot(None), Slot(None))) extends ProductionBuilding(silo, numberOfSlot, factorySlots)