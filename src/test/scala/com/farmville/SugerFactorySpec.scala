package com.farmville

import org.scalatest.{FlatSpec, Matchers}
import rx.lang.scala.Subscriber
import rx.lang.scala.schedulers.ComputationScheduler

/**
  * Created by sujit on 6/29/17.
  */
class SugerFactorySpec extends FlatSpec with Matchers {

  it should "be able to produce a sugar if silo has 2 sugarcane" in {
    val silo: Silo = Silo(List(Sugarcane, Sugarcane, Wheat))
    val sugarFactory: SugarFactory = SugarFactory(silo)
    sugarFactory.makeRecipe(Sugar)
    silo should be(Silo(List(Wheat)))

  }


  it should "notify farmer when the sugar is ready" in {
    var readyRecipes: Set[Slot] = Set()

    val dummyFarmer: Subscriber[Slot] = Subscriber {
      case s@Slot(Some(recipe), percentageCompleted) => if (s.isReady) readyRecipes += s;
      case s@Slot(None, percentageCompleted) =>
    }


    val silo: Silo = Silo(List(Sugarcane, Sugarcane, Wheat))
    val sugarcaneFactory: SugarFactory = SugarFactory(silo)

    sugarcaneFactory.getStatusFeed
      .subscribeOn(ComputationScheduler())
      .subscribe(dummyFarmer)

    sugarcaneFactory.makeRecipe(Sugar)

    for (i <- 1 to 3)
      sugarcaneFactory.updateState()

    //sugarcaneFactory.silo should be(Silo(Nil))
    readyRecipes should be(Set(Slot(Some(Sugar), 100)))

  }

}
