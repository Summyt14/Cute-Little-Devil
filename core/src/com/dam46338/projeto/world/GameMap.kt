package com.dam46338.projeto.world

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.dam46338.projeto.MyGdxGame
import com.dam46338.projeto.entities.Entity
import com.dam46338.projeto.entities.Player
import com.dam46338.projeto.utils.Controller
import kotlin.math.ceil

abstract class GameMap(val main: MyGdxGame, val tiledMap: TiledMap,
                       val controller: Controller,
                       val animations: MutableMap<String, MutableMap<String, Animation<TextureRegion>>>) {
    val entities: ArrayList<Entity> = ArrayList()
    val player: Player = Player(1, 7, this)
    var keyCollected: Boolean = false
    var chestCollected: Boolean = false
    private val gravity = -13f

    open fun update(delta: Float) {
        entities.forEach { entity -> entity.update(delta, gravity) }

        if (chestCollected) {
            main.setNumCoins(main.getNumCoins() + player.numCoins)
            main.setScreen(MyGdxGame.Screens.Initial)
            //main.setScreen(MyGdxGame.Screens.Game, getLevelNum() + 1)
        }
    }

    open fun render(camera: OrthographicCamera, batch: SpriteBatch, delta: Float) {
        entities.forEach { entity -> entity.render(batch, delta) }
    }

    abstract fun dispose()
    abstract fun getWidth(): Int
    abstract fun getHeight(): Int
    abstract fun getLayers(): Int
    abstract fun getLevelNum(): Int

    private fun getPixelWidth(): Int {
        return getWidth() * TileType.tileSize
    }

    private fun getPixelHeight(): Int {
        return getHeight() * TileType.tileSize
    }

    // Gets a tile by pixel position within the game world at a specified layer
    fun getTileTypeByLocation(layer: Int, x: Float, y: Float): TileType? {
        return getTileTypeByCoordinate(layer, (x / TileType.tileSize).toInt(), (y / TileType.tileSize).toInt())
    }

    // Gets a tile at its coordinate within the map at a specified layer
    abstract fun getTileTypeByCoordinate(layer: Int, col: Int, row: Int): TileType?

    // Returns the world position given the grid coordinates
    fun getLocationByGridCoordinates(col: Int, row: Int): Vector2 {
        return Vector2((col * TileType.tileSize).toFloat(), (row * TileType.tileSize).toFloat())
    }

    // Checks if the tiles at the given position are collidable
    fun doesRectCollideWithMap(x: Float, y: Float, width: Int, height: Int): Boolean {
        if (x < 0 || y < 0 || x + width > getPixelWidth() || y + height > getPixelHeight())
            return true

        var row = (y / TileType.tileSize).toInt()
        var col = (x / TileType.tileSize).toInt()
        while (row < ceil((y + height) / TileType.tileSize)) {
            while (col < ceil((x + width) / TileType.tileSize)) {
                for (layer in 0 until getLayers()) {
                    val type = getTileTypeByCoordinate(layer, col, row)
                    if (type != null && type.isCollidable) {
                        return true
                    }
                }
                col++
            }
            row++
        }
        return false
    }

    // Check for collectible items and remove them from the map
    fun checkForCollectibles(x: Float, y: Float, width: Int, height: Int): Pair<TileType, Vector2> {
        val row = ((y + height / 2) / TileType.tileSize).toInt()
        val col = ((x + width) / TileType.tileSize).toInt()

        for (layer in 0 until getLayers()) {
            val type = getTileTypeByCoordinate(layer, col, row)
            if (type != null && type.isCollectible)
                return Pair(type, Vector2(col.toFloat(), row.toFloat()))
        }
        return Pair(TileType.Air, Vector2(col.toFloat(), row.toFloat()))
    }

    // Removes the tile at given position
    fun removeTile(pos: Vector2) {
        (tiledMap.layers.get(0) as TiledMapTileLayer).setCell(pos.x.toInt(), pos.y.toInt(), null)
    }
}