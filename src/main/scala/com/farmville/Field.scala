package com.farmville

/**
  * Created by sujit on 6/27/17.
  */
case class Field(id: String, crop: Option[Crop], var growth: Double = 0) {
  def isReady: Boolean = growth >= 100
  def isEmpty: Boolean = crop.isEmpty
}
