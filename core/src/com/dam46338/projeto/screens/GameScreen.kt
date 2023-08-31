package com.dam46338.projeto.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import com.dam46338.projeto.MyGdxGame
import com.dam46338.projeto.utils.AudioManager
import com.dam46338.projeto.utils.Controller
import com.dam46338.projeto.world.GameMap
import com.dam46338.projeto.world.Lvl1Map

class GameScreen(private val main: MyGdxGame, level: Int) : Screen, InputProcessor {
    private var batch: SpriteBatch = SpriteBatch()
    private var resources: Map<String, Disposable> = main.resources
    private var camera: OrthographicCamera
    private var gameMap: GameMap
    private var controller: Controller
    private var elapsedTime: Float = 0f
    private var lastTargetX: Float = 0f

    init {
        Gdx.input.inputProcessor = this

        camera = OrthographicCamera()
        camera.setToOrtho(false, main.screenWidth / 4, main.screenHeight / 4)
        camera.update()

        controller = Controller(main, camera, batch, resources)

        gameMap = when (level) {
            1 -> Lvl1Map(main, controller)
            else -> Lvl1Map(main, controller)
        }
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        elapsedTime += delta
        batch.begin()
        // draw background
        batch.draw(resources["background"] as Texture, 0f, 0f, main.screenWidth / 4, main.screenHeight / 4)
        batch.end()

        gameMap.update(delta)
        gameMap.render(camera, batch, delta)
        controller.draw()

        val cameraPosition = camera.position.cpy()
        val playerIdx = gameMap.entities.size - 1
        val target = Vector3(gameMap.entities[playerIdx].pos.x, gameMap.entities[playerIdx].pos.y + 25, 0f)
        if (gameMap.getLocationByGridCoordinates(9, 4).x < target.x) {
            lastTargetX = target.x
            cameraPosition.lerp(target, 0.05f)
            camera.position.set(cameraPosition)
        } else if (lastTargetX != 0f) {
            target.x = lastTargetX
            cameraPosition.lerp(target, 0.05f)
            camera.position.set(cameraPosition)
        }

        camera.update()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        batch.dispose()
        gameMap.dispose()
        controller.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            main.setScreen(MyGdxGame.Screens.Initial)
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}