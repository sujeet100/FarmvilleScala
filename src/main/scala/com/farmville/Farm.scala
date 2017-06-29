package com.farmville

import rx.lang.scala.Observable

import scala.collection.immutable.Stream.Empty

/**
  * Created by sujit on 6/27/17.
  */
case class Farm(var fields: List[Field], silo: Silo) {

  def updateState(): Unit = {
    fields = fields.map {
      case f@Field(id, Some(c), g) => if (f.growth < 100) f.growth += (100d / c.growthTimeInSeconds); f
      case f@Field(id, None, g) => f
    }
    Thread.sleep(1000)
  }


  def harvest(field: Field): Unit = {
    val toBeHarvestedField = fields.find(f => f.id == field.id)
    toBeHarvestedField match {
      case Some(Field(id, Some(c), g)) =>
        fields = fields.map(f => if (f == field) Field(f.id, None) else f)
        for (i <- 1 to 2)
          silo.putCrop(c)
      case None =>
    }

  }

  def getFieldFeed: Observable[Field] = {
    Observable({
      subscriber =>
        while (true) {
          fields.foreach(subscriber.onNext)
          Thread.sleep(1000)
        }
    })
  }

  def plant(crop: Crop): Unit = {
    val emptyField = fields.find(_.isEmpty)
    emptyField match {
      case Some(Field(id, c, growth)) => fields = fields.map(f => if (emptyField.contains(f)) Field(f.id, Some(crop)) else f);
      case None => throw new RuntimeException("No empty field")
    }

  }

}
