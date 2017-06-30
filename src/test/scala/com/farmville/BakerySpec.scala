package com.farmville

import org.scalatest.{FlatSpec, Matchers}
import rx.lang.scala.Subscriber
import rx.lang.scala.schedulers.ComputationScheduler

/**
  * Created by sujit on 6/29/17.
  */
class BakerySpec extends FlatSpec with Matchers {

  it should "be able to produce a bread if silo has 3 wheat" in {
    val silo: Silo = Silo(List(Wheat, Wheat, Wheat))
    val bakery: Bakery = Bakery(silo)
    bakery.makeRecipe(Bread)
    silo should be(Silo(Nil))

  }

  it should "be able to produce a cupcake if silo has 2 wheat and 1 corn" in {
    val silo: Silo = Silo(List(Wheat, Wheat, Corn))
    val bakery: Bakery = Bakery(silo)
    bakery.makeRecipe(CupCake)
    silo should be(Silo(Nil))

  }

  it should "notify farmer when the bread is ready" in {
    var readyRecipes: Set[Slot] = Set()

    val dummyFarmer: Subscriber[Slot] = Subscriber {
      case s@Slot(Some(recipe), percentageCompleted) => if (s.isReady) readyRecipes += s;
      case s@Slot(None, percentageCompleted) =>
    }


    val silo: Silo = Silo(List(Wheat, Wheat, Wheat))
    val bakery: Bakery = Bakery(silo)

    bakery.getStatusFeed
      .subscribeOn(ComputationScheduler())
      .subscribe(dummyFarmer)

    bakery.makeRecipe(Bread)

    for (i <- 1 to 2)
      bakery.updateState()

    //bakery.silo should be(Silo(Nil))
    readyRecipes should be(Set(Slot(Some(Bread), 100)))

  }

}
