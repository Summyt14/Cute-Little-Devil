package com.dam46338.projeto.world

enum class TileType(val id: Int,
                    val isCollidable: Boolean,
                    val tileName: String,
                    val isCollectible: Boolean = false,
                    val damage: Float = 0f) {
    Air(-1, false, "Air"),
    Tile19(19, true, "Tile19"),
    Tile20(20, true, "Tile20"),
    Tile21(21, true, "Tile21"),
    Tile94(94, true, "Tile94"),
    Tile95(95, true, "Tile95"),
    Tile96(96, true, "Tile96"),
    Tile97(97, true, "Tile97"),
//    Tile7(7, false, "Tile7"),
//    Tile8(8, false, "Tile8"),
//    Tile9(9, false, "Tile9"),
//    Tile14(14, false, "Tile14"),
//    Tile15(15, false, "Tile15"),
//    Tile16(16, false, "Tile16"),
    Coin(151, false, "Coin", true),
    Cake(180, false, "Cake", true),
    Apple(182, false, "Apple", true),
    Chicken(183, false, "Chicken", true),
    Chest(228, false, "Chest", true),
    Key(240, false, "Key", true);

    companion object {
        private val tileMap = mutableMapOf<Int, TileType>()
        const val tileSize = 32

        init {
            for (tileType in values()) {
                tileMap[tileType.id] = tileType
            }
        }

        fun getTileTypeById(id: Int): TileType? {
            return tileMap[id]
        }
    }
}