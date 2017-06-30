package com.farmville

import rx.lang.scala.Observable

/**
  * Created by sujit on 6/29/17.
  */
case class Bakery(silo: Silo,
                  numberOfSlot: Int = 3,
                  var bakerySlots: List[Slot] = List(Slot(None), Slot(None), Slot(None)))
  extends ProductionBuilding(silo, numberOfSlot, bakerySlots)

