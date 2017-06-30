package com.farmville

/**
  * Created by sujit on 6/27/17.
  */
abstract case class Crop(cropName: String, growthTimeInSeconds: Int) {

}

object Wheat extends Crop("Wheat", 120)

object Corn extends Crop("Corn", 180)

object Sugarcane extends Crop("Sugarcane", 60)

