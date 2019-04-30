package com.mahakeemmk.quizapp.model

import org.json.JSONObject

class PlantParser {
    fun parseJson(json:String):ArrayList<Plant> {
        var allPlants:ArrayList<Plant> = ArrayList()
        var jsonArray = JSONObject(json).getJSONArray("values")
        var index = 0
        while (index < jsonArray.length()) {
            var plantObject = jsonArray[index] as JSONObject
            with(plantObject) {
                allPlants.add(Plant(getString("genus"),getString("species"),getString("common"),getString("picture_name"),getString("description")))
            }
            index++
        }
        return allPlants
    }
}