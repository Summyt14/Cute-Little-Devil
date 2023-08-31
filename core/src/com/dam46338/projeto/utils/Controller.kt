package com.dam46338.projeto.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.dam46338.projeto.MyGdxGame


class Controller(private val main: MyGdxGame, camera: OrthographicCamera,
                 batch: SpriteBatch, private val resources: Map<String, Disposable>) {
    private val stage: Stage = Stage(FitViewport(camera.viewportWidth, camera.viewportHeight), batch)
    var upPressed: Boolean = false
    var leftPressed: Boolean = false
    var rightPressed: Boolean = false
    var attackPressed: Boolean = false
    var throwPressed: Boolean = false
    private var healthAmount: Int = 100
    private var energyAmount: Int = 100
    private var newHealthAmount: Int = 0
    private var newEnergyAmount: Int = 0
    private var maxBarWidth: Float = 0f
    private var iconSize: Float = 40f
    private lateinit var healthBarFull: Image
    private lateinit var energyBarFull: Image

    init {
        Gdx.input.inputProcessor = stage

        val rootTable = Table()
        rootTable.setFillParent(true)
        val movementTable = defineMovementButtons()
        val attackTable = defineAttackButtons()
        val barsTable = defineBars()
        val pauseTable = definePauseButton()

        rootTable.add(barsTable).expand().top().left().pad(10f, 10f, 5f, 0f)
        rootTable.add(pauseTable).expand().top().right().pad(10f, 0f, 5f, 10f)
        rootTable.row()
        rootTable.add(movementTable).expand().bottom().left().pad(0f, 10f, 20f, 0f)
        rootTable.add(attackTable).expand().bottom().right().pad(0f, 0f, 10f, 10f)
        stage.addActor(rootTable)
    }

    private fun defineMovementButtons(): Table {
        val movementTable = Table()
        movementTable.left().bottom()

        val rightImg = Image((resources["iconsAtlas"] as TextureAtlas).regions[24])
        rightImg.setSize(iconSize, iconSize)
        rightImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                rightPressed = true
                rightImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsPressedAtlas"] as TextureAtlas).regions[24]))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                rightPressed = false
                rightImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsAtlas"] as TextureAtlas).regions[24]))
            }
        })

        val leftImg = Image((resources["iconsAtlas"] as TextureAtlas).regions[25])
        leftImg.setSize(iconSize, iconSize)
        leftImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                leftPressed = true
                leftImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsPressedAtlas"] as TextureAtlas).regions[25]))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                leftPressed = false
                leftImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsAtlas"] as TextureAtlas).regions[25]))
            }
        })

        movementTable.add(leftImg).size(leftImg.width, leftImg.height)
        movementTable.add().size(leftImg.width, leftImg.height)
        movementTable.add(rightImg).size(rightImg.width, rightImg.height)
        return movementTable
    }

    private fun defineAttackButtons(): Table {
        val attackTable = Table()
        attackTable.right().bottom()

        val upImg = Image((resources["iconsAtlas"] as TextureAtlas).regions[26])
        upImg.setSize(iconSize, iconSize)
        upImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                upPressed = true
                upImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsPressedAtlas"] as TextureAtlas).regions[26]))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                upPressed = false
                upImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsAtlas"] as TextureAtlas).regions[26]))
            }
        })

        val meleeImg = Image((resources["iconsAtlas"] as TextureAtlas).regions[62])
        meleeImg.setSize(iconSize - 5, iconSize - 5)
        meleeImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                attackPressed = true
                meleeImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsPressedAtlas"] as TextureAtlas).regions[62]))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                attackPressed = false
                meleeImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsAtlas"] as TextureAtlas).regions[62]))
            }
        })

        val throwImg = Image((resources["iconsAtlas"] as TextureAtlas).regions[63])
        throwImg.setSize(iconSize - 5, iconSize - 5)
        throwImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                throwPressed = true
                throwImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsPressedAtlas"] as TextureAtlas).regions[63]))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                throwPressed = false
                throwImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsAtlas"] as TextureAtlas).regions[63]))
            }
        })

        attackTable.add().size(meleeImg.width, meleeImg.height)
        attackTable.add(upImg).size(upImg.width, upImg.height)
        attackTable.row().pad(5f, 5f, 10f, 5f)
        attackTable.add(meleeImg).size(meleeImg.width, meleeImg.height)
        attackTable.add(throwImg).size(throwImg.width, throwImg.height)
        return attackTable
    }

    private fun defineBars(): Table {
        val barsTable = Table()
        barsTable.left().top()

        val healthBarEmpty = Image((resources["barsAtlas"] as TextureAtlas).findRegion("Energybar_empty"))
        val energyBarEmpty = Image((resources["barsAtlas"] as TextureAtlas).findRegion("Energybar_empty"))
        healthBarFull = Image((resources["barsAtlas"] as TextureAtlas).findRegion("Healthbar_full"))
        energyBarFull = Image((resources["barsAtlas"] as TextureAtlas).findRegion("Energybar_full"))
        maxBarWidth = healthBarEmpty.width

        val healthStack = Stack()
        healthBarEmpty.setSize(healthBarEmpty.width, healthBarEmpty.height)
        healthBarFull.setSize(healthBarFull.width, healthBarFull.height)
        healthStack.add(healthBarEmpty)
        healthStack.add(healthBarFull)

        val energyStack = Stack()
        energyBarEmpty.setSize(energyBarEmpty.width, energyBarEmpty.height)
        energyBarFull.setSize(energyBarFull.width, energyBarFull.height)
        energyStack.add(energyBarEmpty)
        energyStack.add(energyBarFull)

        barsTable.add(healthStack).size(healthBarEmpty.width, healthBarEmpty.height)
        barsTable.row().pad(3f, 0f, 0f, 0f)
        barsTable.add(energyStack).size(energyBarEmpty.width, energyBarEmpty.height)
        return barsTable
    }

    private fun definePauseButton(): Table {
        val pauseTable = Table()
        pauseTable.top().right()
        val pauseImg = Image((resources["iconsAtlas"] as TextureAtlas).regions[8])
        pauseImg.setSize(iconSize - 15, iconSize - 15)
        pauseImg.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                attackPressed = true
                pauseImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsPressedAtlas"] as TextureAtlas).regions[8]))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                attackPressed = false
                pauseImg.drawable = TextureRegionDrawable(TextureRegion(
                        (resources["iconsAtlas"] as TextureAtlas).regions[8]))
                main.setScreen(MyGdxGame.Screens.Initial)
            }
        })

        pauseTable.add(pauseImg).size(pauseImg.width, pauseImg.height)
        return pauseTable
    }

    fun setHealth(value: Int) {
        newHealthAmount = value
    }

    fun setEnergy(value: Int) {
        newEnergyAmount = value
    }

    fun draw() {
        healthAmount = Interpolation.linear.apply(healthAmount.toFloat(), newHealthAmount.toFloat(), 0.1f).toInt()
        energyAmount = Interpolation.linear.apply(energyAmount.toFloat(), newEnergyAmount.toFloat(), 0.1f).toInt()

        healthBarFull.width = maxBarWidth * (healthAmount / 100f)
        energyBarFull.width = maxBarWidth * (energyAmount / 100f)

        stage.draw()
    }

    fun dispose() {
        stage.dispose()
    }
}
