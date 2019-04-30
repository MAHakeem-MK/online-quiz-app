package com.mahakeemmk.quizapp.model

class Plant(var genus:String,var species:String,var common:String,var pinctureName:String,var discription:String) {
    override fun toString(): String {
        return genus + " " + species
    }
}