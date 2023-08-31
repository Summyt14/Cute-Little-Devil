package com.dam46338.projeto.entities

enum class EntityType(val id: String,
                      val width: Int,
                      val height: Int,
                      val weight: Int) {
    Player("player", 32, 32, 80),
    Bear("bear", 40, 40, 150),
    PurpleBlob("purpleBlob", 40, 40, 150),
    RedSkull("redSkull", 40, 40, 150),
    TinySkull("tinySkull", 40, 40, 150),
    YellowSkull("yellowSkull", 40, 40, 150),
    Fireball("fireball", 32, 32, 0),
    MidasTouch("midasTouch", 32, 32, 0),
    BlackHole("blackHole", 32, 32, 0),
    Lightning("lightning", 32, 32, 0),
    OverpoweredShield("overpoweredShield", 32, 32, 0)
}