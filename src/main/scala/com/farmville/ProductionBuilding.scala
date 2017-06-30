package com.farmville

import rx.lang.scala.Observable

/**
  * Created by sujit on 6/30/17.
  */
abstract class ProductionBuilding(silo: Silo, numberOfSlot: Int = 3, var slots: List[Slot] = List(Slot(None), Slot(None), Slot(None))) {

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

  def makeRecipe(recipe: Recipe): Unit = {
    if (canBeMade(recipe)) {
      getIngredients(recipe)
      val emptySLot = slots.find(x => x.recipe.isEmpty)
      emptySLot match {
        case Some(Slot(r, percentageCompleted)) => slots = slots.map(s => if (emptySLot.contains(s)) Slot(Some(recipe)) else s);
        case None => throw new RuntimeException("No empty slot")
      }

    } else {
      throw new RuntimeException("Not enough wheat to bake a bread")
    }
  }

  private def getIngredients(recipe: Recipe) = {
    recipe.ingredients.foreach(i => silo.withdraw(i))
  }

  private def canBeMade(recipe: Recipe) = {
    recipe.ingredients.forall(silo.hasEnough)
  }


}

abstract case class Recipe(timeInSeconds: Int, ingredients: List[(Crop, Int)]) {

}

object Bread extends Recipe(2, List((Wheat, 3)))

object CupCake extends Recipe(10, List((Wheat, 2), (Corn, 1)))

object Sugar extends Recipe(3, List((Sugarcane, 2)))

case class Slot(recipe: Option[Recipe], var percentageCompleted: Double = 0) {
  def isReady: Boolean = recipe.isDefined && percentageCompleted >= 100

}
