package com.farmville

/**
  * Created by sujit on 6/28/17.
  */
case class Silo(var crops: List[Crop], capacity: Int = 10) {
  def putCrop(crop: Crop): Unit = {
    if (!isFull)
      crops = crop :: crops
  }

  def isFull: Boolean = crops.size == capacity

}
