package com.dam46338.projeto.world

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.dam46338.projeto.MyGdxGame
import com.dam46338.projeto.entities.EntityType
import com.dam46338.projeto.entities.enemies.MeleeEnemy
import com.dam46338.projeto.utils.Controller

class Lvl1Map(main: MyGdxGame, controller: Controller) :
        GameMap(main, TmxMapLoader().load("world/Level1Map.tmx"), controller, main.animations) {
    private var tiledMapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    init {
        entities.add(MeleeEnemy(EntityType.Bear, 13, 9, this,
                arrayListOf(Vector2(10f, 6f), Vector2(13f, 6f))))
        entities.add(MeleeEnemy(EntityType.PurpleBlob,29, 8, this,
                arrayListOf(Vector2(22f, 7f), Vector2(29f, 7f))))
        entities.add(MeleeEnemy(EntityType.Bear,32, 14, this,
                arrayListOf(Vector2(32f, 13f), Vector2(35f, 13f))))
        entities.add(MeleeEnemy(EntityType.PurpleBlob,40, 17, this,
                arrayListOf(Vector2(38f, 16f), Vector2(41f, 16f))))
        entities.add(MeleeEnemy(EntityType.RedSkull,49, 8, this,
                arrayListOf(Vector2(47f, 7f), Vector2(52f, 7f))))
        entities.add(MeleeEnemy(EntityType.RedSkull,61, 7, this,
                arrayListOf(Vector2(60f, 6f), Vector2(72f, 6f))))
        entities.add(MeleeEnemy(EntityType.YellowSkull,71, 7, this,
                arrayListOf(Vector2(59f, 6f), Vector2(71f, 6f))))
        entities.add(MeleeEnemy(EntityType.TinySkull,83, 12, this,
                arrayListOf(Vector2(83f, 11f), Vector2(85f, 11f))))
        entities.add(player)
    }

    override fun update(delta: Float) {
        super.update(delta)
    }

    override fun render(camera: OrthographicCamera, batch: SpriteBatch, delta: Float) {
        tiledMapRenderer.setView(camera)
        tiledMapRenderer.render()

        batch.projectionMatrix = camera.combined
        batch.begin()
        super.render(camera, batch, delta)
        batch.end()
    }

    override fun dispose() {
        tiledMap.dispose()
    }

    override fun getWidth(): Int {
        return (tiledMap.layers.get(0) as TiledMapTileLayer).width
    }

    override fun getHeight(): Int {
        return (tiledMap.layers.get(0) as TiledMapTileLayer).height
    }

    override fun getLayers(): Int {
        return tiledMap.layers.count
    }

    override fun getTileTypeByCoordinate(layer: Int, col: Int, row: Int): TileType? {
        val mapLayer = tiledMap.layers.get(layer)
        if (mapLayer !is TiledMapTileLayer) return null

        val cell = mapLayer.getCell(col, row)
        if (cell != null) {
            val tile = cell.tile
            if (tile != null)
                return TileType.getTileTypeById(tile.id)
        }

        return null
    }

    override fun getLevelNum(): Int {
        return 1
    }
}