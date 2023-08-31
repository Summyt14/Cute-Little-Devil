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
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.dam46338.projeto.MyGdxGame


class SettingsScreen(private val main: MyGdxGame) : Screen, InputProcessor {
    private val stage: Stage = Stage()
    private val batch: SpriteBatch = SpriteBatch()
    private val resources = main.resources
    private val scaleX: Float = 4f
    private val scaleY: Float = 3.5f
    private val currentMusicVolume: Float = main.audioManager.currentMusicVolume
    private val currentSoundVolume: Float = main.audioManager.currentSoundVolume

    init {
        Gdx.input.inputProcessor = stage

        val rootTable = Table()
        rootTable.setPosition(stage.width / 2 - ((resources["frame1"] as Texture).width / 2f * scaleX),
                stage.height / 2 - ((resources["frame1"] as Texture).height / 2f * scaleY))
        rootTable.setSize((resources["frame1"] as Texture).width.toFloat() * scaleX,
                (resources["frame1"] as Texture).height.toFloat() * scaleY)
        rootTable.background = TextureRegionDrawable(TextureRegion(resources["frame1"] as Texture))

        val titleLabel = Label("Settings",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        titleLabel.setFontScale(2f)
        titleLabel.setAlignment(Align.center)

        val textureExitNotPressed = resources["buttonExit"] as Texture
        val textureExitPressed = resources["buttonExitPressed"] as Texture
        val exitButton = Image(textureExitNotPressed)
        exitButton.setSize(exitButton.width * scaleY, exitButton.height * scaleY)
        exitButton.setPosition(stage.width / 2 + rootTable.width / 2,
                stage.height - titleLabel.height - ((resources["frame1"] as Texture).height) - 15)

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

        val musicLabel = Label("Music",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        musicLabel.setFontScale(1.5f)
        musicLabel.setAlignment(Align.topLeft)

        val soundLabel = Label("Sounds",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        soundLabel.setFontScale(1.5f)
        soundLabel.setAlignment(Align.topLeft)

        val textureSliderBackground = resources["sliderBack"] as Texture
        val textureSliderFront = resources["sliderFront"] as Texture
        val textureSliderKnob = resources["sliderKnob"] as Texture
        val sliderStyle = SliderStyle()
        sliderStyle.background = TextureRegionDrawable(TextureRegion(textureSliderBackground))
        sliderStyle.knob = TextureRegionDrawable(TextureRegion(textureSliderKnob))
        sliderStyle.disabledBackground = TextureRegionDrawable(TextureRegion(textureSliderFront))


        val sliderMusic = Slider(0f, 1f, 0.01f, false, sliderStyle)
        sliderMusic.value = currentMusicVolume
        sliderMusic.setSize(500f, 50f)
        sliderMusic.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                main.audioManager.setMusicVolume(sliderMusic.value)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        val sliderSounds = Slider(0f, 1f, 0.01f, false, sliderStyle)
        sliderSounds.value = currentSoundVolume
        sliderSounds.setSize(500f, 50f)
        sliderSounds.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                main.audioManager.setSoundVolume(sliderSounds.value)
            }

            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }
        })

        val table = Table()
        table.add(musicLabel).padRight(40f)
        table.add(sliderMusic).size(sliderMusic.width, sliderMusic.height)
        table.row().padTop(100f)
        table.add(soundLabel).padRight(40f)
        table.add(sliderSounds).size(sliderSounds.width, sliderSounds.height)

        rootTable.add(titleLabel).expandX().height((resources["frame1"] as Texture).height.toFloat() / (scaleY - 1.5f))
        rootTable.row()
        rootTable.add(table).fillX().expandY().pad(30f, 100f, 100f, 100f)
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