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
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.dam46338.projeto.MyGdxGame

class ShopScreen(private val main: MyGdxGame) : Screen, InputProcessor {
    private val stage: Stage = Stage()
    private val batch: SpriteBatch = SpriteBatch()
    private val resources = main.resources
    private val scaleX: Float = 4f
    private val scaleY: Float = 3f
    private val coinsLabel: Label
    private var coins = main.getNumCoins()

    init {
        Gdx.input.inputProcessor = stage
        val rootTable = Table()
        rootTable.setPosition(stage.width / 2 - ((resources["frame2"] as Texture).width / 2f * scaleX),
                stage.height / 2 - ((resources["frame2"] as Texture).height / 2f * scaleY))
        rootTable.setSize((resources["frame2"] as Texture).width.toFloat() * scaleX,
                (resources["frame2"] as Texture).height.toFloat() * scaleY)
        rootTable.background = TextureRegionDrawable(TextureRegion(resources["frame2"] as Texture))

        val titleLabel = Label("Shop",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        titleLabel.setFontScale(2f)
        titleLabel.setAlignment(Align.center)

        val textureExitNotPressed = resources["buttonExit"] as Texture
        val textureExitPressed = resources["buttonExitPressed"] as Texture
        val exitButton = Image(textureExitNotPressed)
        exitButton.setSize(exitButton.width * scaleY, exitButton.height * scaleY)
        exitButton.setPosition(stage.width / 2 + rootTable.width / 2,
                stage.height + 8 - ((resources["frame2"] as Texture).height / scaleY * 2))

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

        val scrollTable = defineScrollTable()
        val scroller = ScrollPane(scrollTable)

        coinsLabel = Label("$coins C",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.GOLD))
        coinsLabel.setFontScale(2.5f)
        coinsLabel.setAlignment(Align.center)
        coinsLabel.setPosition(main.screenWidth - coinsLabel.width - 120, main.screenHeight - coinsLabel.height - 50)

        rootTable.add(titleLabel).height((resources["frame2"] as Texture).height.toFloat() / scaleY)
        rootTable.row()
        rootTable.add(scroller).width(1000f)
                .pad(30f, 0f, 100f, 0f)
        stage.addActor(rootTable)
        stage.addActor(exitButton)
        stage.addActor(coinsLabel)
    }

    private fun defineScrollTable(): Table {
        val scrollTable = Table()
        val textureButton = resources["button"] as Texture
        val textureButtonPressed = resources["buttonPressed"] as Texture
        val font = resources["font"] as BitmapFont

        for (item in main.shopItems) {
            val itemIcon = Image(item.texture)

            val itemLabel = Label(item.name, Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
            itemLabel.setFontScale(1.5f)
            itemLabel.setAlignment(Align.left)

            val buttonImg = Image(if (item.equipped) textureButtonPressed else textureButton)
            val buttonStack = Stack()
            val buttonLabelText = if (item.acquired) "Equip" else "${item.cost} C"
            val buttonLabel = Label(buttonLabelText, Label.LabelStyle(font, Color.WHITE))
            buttonLabel.setFontScale(2f)
            buttonLabel.setAlignment(Align.center)
            buttonStack.add(buttonImg)
            buttonStack.add(buttonLabel)
            buttonStack.addListener(object : InputListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                    buttonImg.drawable = TextureRegionDrawable(TextureRegion(textureButtonPressed))
                    return true
                }

                override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                    buttonImg.drawable = TextureRegionDrawable(TextureRegion(textureButton))
                    if (!item.acquired && coins >= item.cost) {
                        item.acquired = true
                        coins -= item.cost
                        buttonLabel.setText("Equip")
                        coinsLabel.setText("$coins C")
                        main.setNumCoins(coins)
                        main.addItemInDb(item.name)
                        main.audioManager.playSound("buy")
                    } else if (!item.equipped && item.acquired) {
                        item.equipped = true
                        buttonImg.drawable = TextureRegionDrawable(TextureRegion(textureButtonPressed))
                        main.audioManager.playSound("button")
                    } else if (item.equipped && item.acquired) {
                        item.equipped = false
                        main.audioManager.playSound("button")
                    }
                }
            })

            val itemTable = Table()
            itemTable.add(itemIcon).size(32f * 4, 32f * 4)
            itemTable.add(itemLabel).padLeft(15f)
            scrollTable.add(itemTable).left()
            scrollTable.add(buttonStack).right().padLeft(30f).size(250f, 32f * 4)
            scrollTable.row().padTop(50f)
        }

        return scrollTable
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(resources["background"] as Texture, 0f, 0f, main.screenWidth, main.screenHeight)
        batch.end()
        stage.act()
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

class Item(val name: String, val cost: Int, val texture: TextureRegion, var acquired: Boolean = false, var equipped: Boolean = false)