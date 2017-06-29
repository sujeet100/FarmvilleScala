package com.farmville

import org.scalatest.{FlatSpec, Matchers}
import rx.lang.scala.{Observable, Subscriber}
import rx.lang.scala.schedulers.ComputationScheduler

/**
  * Created by sujit on 6/27/17.
  */
class FieldSpec extends FlatSpec with Matchers {

  object DummyCrop extends Crop("Dummy", 4)

  it should "create a farm with default number of fields" in {
    val farm: Farm = Farm(List(Field("1", None), Field("2", None), Field("3", None)), Silo(Nil))
    farm.fields.size should be(3)
  }


  it should "be able to plant wheat on empty field" in {
    val farm: Farm = Farm(List(Field("1", None), Field("2", None), Field("3", None)), Silo(Nil))
    farm.plant(Wheat)
    farm.fields should be(List(Field("1", Some(Wheat)), Field("2", None), Field("3", None)))
  }

  it should "be able to plant wheat on next empty field" in {
    val farm: Farm = Farm(List(Field("1", Some(Corn)), Field("2", None), Field("3", None)), Silo(Nil))
    farm.plant(Wheat)
    farm.fields should be(List(Field("1", Some(Corn)), Field("2", Some(Wheat)), Field("3", None)))
  }

  it should "be able to harvest a crop" in {
    val farm: Farm = Farm(List(Field("1", Some(Corn)), Field("2", None), Field("3", None)), Silo(Nil))
    farm.harvest(Field("1", Some(Corn)))
    farm.fields should be(List(Field("1", None), Field("2", None), Field("3", None)))
  }

  it should "notify farmer when the crop is ready for harvesting" in {
    val farm: Farm = Farm(List(Field("1", Some(DummyCrop)), Field("2", None), Field("3", None)), Silo(Nil))

    def dummyFarmer: Subscriber[Field] = Subscriber {
      case f@Field(id, Some(c), growth) => if (f.isReady) farm.harvest(f)
      case f@Field(id, None, growth) =>
    }

    farm.getFieldFeed
      .subscribeOn(ComputationScheduler())
      .subscribe(dummyFarmer)

    for (i <- 1 to 5)
      farm.updateState()

    farm.fields should be(List(Field("1", None), Field("2", None), Field("3", None)))

  }

  it should "store harvested crop in a silo" in {
    val silo: Silo = Silo(Nil)
    val farm: Farm = Farm(List(Field("1", Some(Corn)), Field("2", None), Field("3", None)), silo)
    farm.harvest(Field("1", Some(Corn)))
    farm.silo should be(Silo(List(Corn, Corn)))
  }

  it should "not be able to harvest if silo is full" in {
    val silo: Silo = Silo(List(Wheat, Wheat), 2)
    val farm: Farm = Farm(List(Field("1", Some(Corn)), Field("2", None), Field("3", None)), silo)
    farm.harvest(Field("1", Some(Corn)))
    farm.silo should be(Silo(List(Wheat, Wheat), 2))
  }
}
