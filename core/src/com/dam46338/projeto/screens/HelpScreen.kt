package com.dam46338.projeto.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.dam46338.projeto.MyGdxGame

class HelpScreen(private val main: MyGdxGame) : Screen, InputProcessor {
    private val stage: Stage = Stage()
    private val batch: SpriteBatch = SpriteBatch()
    private val resources = main.resources
    private val scaleX: Float = 4f
    private val scaleY: Float = 3f
    private val description: String = "The main objective of the game is to collect the key to open the" +
            " chest that unlocks the next level. There will be enemies on your way, so be careful." +
            "\nYou can collect food to regenerate health and stamina and coins to spend in the shop." +
            "\nYou need to login to save your coins and purchased items.\n\n\n" +
            "This game was developed by:\nJose Siopa A46338"

    init {
        Gdx.input.inputProcessor = stage
        val rootTable = Table()
        rootTable.setPosition(stage.width / 2 - ((resources["frame2"] as Texture).width / 2f * scaleX),
                stage.height / 2 - ((resources["frame2"] as Texture).height / 2f * scaleY))
        rootTable.setSize((resources["frame2"] as Texture).width.toFloat() * scaleX,
                (resources["frame2"] as Texture).height.toFloat() * scaleY)
        rootTable.background = TextureRegionDrawable(TextureRegion(resources["frame2"] as Texture))

        val textLabel = Label("Help",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        textLabel.setFontScale(2f)
        textLabel.setAlignment(Align.center)

        val textLabel2 = Label(description, Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        textLabel2.setFontScale(1.5f)
        textLabel2.setAlignment(Align.topLeft)
        textLabel2.wrap = true

        val textureExitNotPressed = resources["buttonExit"] as Texture
        val textureExitPressed = resources["buttonExitPressed"] as Texture
        val exitButton = Image(textureExitNotPressed)
        exitButton.setSize(exitButton.width * scaleY, exitButton.height * scaleY)
        exitButton.setPosition(stage.width / 2 + rootTable.width / 2,
                stage.height - textLabel.height - ((resources["frame2"] as Texture).height / 2) - 10)

        exitButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                exitButton.drawable = TextureRegionDrawable(TextureRegion(textureExitPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                exitButton.drawable = TextureRegionDrawable(TextureRegion(textureExitNotPressed))
                main.setScreen(MyGdxGame.Screens.Initial)
                main.audioManager.playSound("button")
            }
        })

        rootTable.add(textLabel).expandX().height((resources["frame2"] as Texture).height.toFloat() / scaleY)
        rootTable.row()
        rootTable.add(textLabel2).expandY().fillX().fillY().pad(30f, 100f, 100f, 100f)
        stage.addActor(rootTable)
        stage.addActor(exitButton)
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(resources["background"] as Texture, 0f, 0f, main.screenWidth, main.screenHeight)
        batch.end()
        stage.draw()
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
        stage.dispose()
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