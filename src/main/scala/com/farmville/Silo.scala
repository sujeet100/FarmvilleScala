package com.farmville

/**
  * Created by sujit on 6/28/17.
  */
case class Silo(var crops: List[Crop], capacity: Int = 10) {
  def withdraw(ingredient: (Crop, Int)): Unit = {
    val remaining = crops.filter(c => c == ingredient._1).drop(ingredient._2)
    crops = crops.filter(c => c != ingredient._1)
    remaining.foreach(r => crops = r :: crops)
  }

  def hasEnough(ingredient: (Crop, Int)): Boolean = {
    crops.count(x => x == ingredient._1) >= ingredient._2
  }

  def putCrop(crop: Crop): Unit = {
    if (!isFull)
      crops = crop :: crops
  }

  def isFull: Boolean = crops.size == capacity

}
