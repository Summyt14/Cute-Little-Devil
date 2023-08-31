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
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.dam46338.projeto.MyGdxGame
import com.dam46338.projeto.utils.AudioManager

class InitialScreen(private val main: MyGdxGame) : Screen, InputProcessor {
    private val stage: Stage = Stage()
    private val batch: SpriteBatch = SpriteBatch()
    private val resources = main.resources
    private val buttonScale: Float = 2.5f
    private val iconSize: Float = 115f

    init {
        Gdx.input.inputProcessor = stage
        val rootTable = Table()
        rootTable.setFillParent(true)

        val tableInfo = defineLeftButton()
        val tableCenter = defineMiddleButtons()
        val tableSettings = defineRightButtons()

        rootTable.add(tableInfo).expandX().top().left().padLeft(30f)
        rootTable.add(tableCenter).center().padLeft(80f)
        rootTable.add(tableSettings).expandX().top().right().padRight(30f)
        stage.addActor(rootTable)

        main.getNumCoins()
    }

    private fun defineLeftButton(): Table {
        val tableInfo = Table()
        tableInfo.setPosition(0f, 0f)
        val textureInfoNotPressed = (resources["iconsAtlas"] as TextureAtlas).regions[3]
        val textureInfoPressed = (resources["iconsPressedAtlas"] as TextureAtlas).regions[3]
        val infoImg = Image(textureInfoNotPressed)
        infoImg.setSize(iconSize, iconSize)
        infoImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                infoImg.drawable = TextureRegionDrawable(TextureRegion(textureInfoPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                infoImg.drawable = TextureRegionDrawable(TextureRegion(textureInfoNotPressed))
                main.setScreen(MyGdxGame.Screens.Help)
                main.audioManager.playSound("button")
            }
        })

        tableInfo.add(infoImg).size(infoImg.width, infoImg.height)
        return tableInfo
    }

    private fun defineMiddleButtons(): Table {
        val tableCenter = Table()
        tableCenter.setPosition(main.screenWidth / 2, main.screenHeight / 2)

        val textureButton = resources["button"] as Texture
        val textureButtonPressed = resources["buttonPressed"] as Texture
        val logo = Image(resources["logo"] as Texture)
        val playButton = Image(textureButton)
        val shopButton = Image(textureButton)
        val quitButton = Image(textureButton)
        val font = resources["font"] as BitmapFont
        logo.setSize(logo.width * 2.5f, logo.height * 2.5f)
        playButton.setSize(playButton.width * buttonScale, playButton.height * buttonScale)
        shopButton.setSize(shopButton.width * buttonScale, shopButton.height * buttonScale)
        quitButton.setSize(quitButton.width * buttonScale, quitButton.height * buttonScale)

        val playStack = Stack()
        val playLabel = Label("Play", Label.LabelStyle(font, Color.WHITE))
        playLabel.setFontScale(2.5f)
        playLabel.setAlignment(Align.center)
        playStack.add(playButton)
        playStack.add(playLabel)

        val shopStack = Stack()
        val shopLabel = Label("Shop", Label.LabelStyle(font, Color.WHITE))
        shopLabel.setFontScale(2.5f)
        shopLabel.setAlignment(Align.center)
        shopStack.add(shopButton)
        shopStack.add(shopLabel)

        val quitStack = Stack()
        val quitLabel = Label("Quit", Label.LabelStyle(font, Color.WHITE))
        quitLabel.setFontScale(2.5f)
        quitLabel.setAlignment(Align.center)
        quitStack.add(quitButton)
        quitStack.add(quitLabel)

        playStack.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                playButton.drawable = TextureRegionDrawable(TextureRegion(textureButtonPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                playButton.drawable = TextureRegionDrawable(TextureRegion(textureButton))
                main.setScreen(MyGdxGame.Screens.LevelSelect)
                main.audioManager.playSound("button")
            }
        })

        shopStack.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                shopButton.drawable = TextureRegionDrawable(TextureRegion(textureButtonPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                shopButton.drawable = TextureRegionDrawable(TextureRegion(textureButton))
                main.setScreen(MyGdxGame.Screens.Shop)
                main.audioManager.playSound("button")
            }
        })

        quitStack.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                quitButton.drawable = TextureRegionDrawable(TextureRegion(textureButtonPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                quitButton.drawable = TextureRegionDrawable(TextureRegion(textureButton))
                main.audioManager.playSound("button")
                Gdx.app.exit()
            }
        })

        tableCenter.add(logo).size(logo.width, logo.height)
        tableCenter.row().pad(30f, 0f, 0f, 0f)
        tableCenter.add(playStack).size(playButton.width, playButton.height)
        tableCenter.row().pad(30f, 0f, 0f, 0f)
        tableCenter.add(shopStack).size(shopButton.width, shopButton.height)
        tableCenter.row().pad(30f, 0f, 0f, 0f)
        tableCenter.add(quitStack).size(quitButton.width, quitButton.height)
        return tableCenter
    }

    private fun defineRightButtons(): Table {
        val tableSettings = Table()
        tableSettings.setPosition(main.screenWidth, 0f)

        val textureLoginNotPressed = (resources["iconsAtlas"] as TextureAtlas).regions[4]
        val textureLoginPressed = (resources["iconsPressedAtlas"] as TextureAtlas).regions[4]
        val loginImg = Image(textureLoginNotPressed)
        loginImg.setSize(iconSize, iconSize)
        loginImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                loginImg.drawable = TextureRegionDrawable(TextureRegion(textureLoginPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                loginImg.drawable = TextureRegionDrawable(TextureRegion(textureLoginNotPressed))
                main.setScreen(MyGdxGame.Screens.Login)
                main.audioManager.playSound("button")
            }
        })

        val textureSettingsNotPressed = (resources["iconsAtlas"] as TextureAtlas).regions[38]
        val textureSettingsPressed = (resources["iconsPressedAtlas"] as TextureAtlas).regions[38]
        val settingsImg = Image(textureSettingsNotPressed)
        settingsImg.setSize(iconSize, iconSize)
        settingsImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                settingsImg.drawable = TextureRegionDrawable(TextureRegion(textureSettingsPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                settingsImg.drawable = TextureRegionDrawable(TextureRegion(textureSettingsNotPressed))
                main.setScreen(MyGdxGame.Screens.Settings)
                main.audioManager.playSound("button")
            }
        })

        tableSettings.add(loginImg).size(loginImg.width, loginImg.height).padRight(25f)
        tableSettings.add(settingsImg).size(settingsImg.width, settingsImg.height)
        return tableSettings
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
            Gdx.app.exit()
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