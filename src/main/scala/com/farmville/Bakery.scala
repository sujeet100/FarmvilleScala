package com.farmville

import rx.lang.scala.Observable

/**
  * Created by sujit on 6/29/17.
  */
case class Bakery(silo: Silo, numberOfSlot: Int = 3, var slots: List[Slot] = List(Slot(None), Slot(None), Slot(None))) {
  def updateState() = {
    slots.foreach(s => if (s.recipe.isDefined) s.percentageCompleted += (100d / s.recipe.get.timeInSeconds))
    Thread.sleep(1000)
  }

  def getStatusFeed: Observable[Slot] = {
    Observable({
      subscriber =>
        while (true) {
          slots.foreach(subscriber.onNext)
          Thread.sleep(1000)
        }
    })
  }

  def makeBread: Unit = {
    if (silo.crops.count(_ == Wheat) == 3) {
      val remainingWheat = silo.crops.filter(x => x == Wheat).drop(3)
      silo.crops = silo.crops.filter(x => x != Wheat)
      remainingWheat foreach (w => silo.crops = w :: silo.crops)
      val emptySLot = slots.find(x => x.recipe.isEmpty)
      emptySLot match {
        case Some(Slot(recipe, percentageCompleted)) => slots = slots.map(s => if (emptySLot.contains(s)) Slot(Some(Bread)) else s);
        case None => throw new RuntimeException("No empty slot")
      }

    } else {
      throw new RuntimeException("Not enough wheat to bake a bread")
    }
  }

}

abstract case class Recipe(timeInSeconds: Int)

object Bread extends Recipe(2)

case class Slot(recipe: Option[Recipe], var percentageCompleted: Double = 0) {
  def isReady: Boolean = recipe.isDefined && percentageCompleted >= 100

}
