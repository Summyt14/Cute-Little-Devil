package com.dam46338.projeto.entities

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.dam46338.projeto.world.GameMap
import kotlin.math.floor

abstract class Entity(col: Int, row: Int, private var invertFacing: Boolean,
                      var type: EntityType, protected var map: GameMap) {
    var pos: Vector2 = map.getLocationByGridCoordinates(col, row)
    var currentHealth: Int = 0
    var grounded: Boolean = false
    protected lateinit var animations: MutableMap<String, Animation<TextureRegion>>
    protected var currAnimation: Animation<TextureRegion>? = null
    protected var startingAnimTime: Float = 0f
    protected var elapsedTime: Float = 0f
    protected var elapsedTimeAnim: Float = 0f
    protected var velocityY: Float = 0f
    protected var realizingAction: Boolean = false
    protected open var facingRight: Boolean = true
    protected var moving: Boolean = false
    private var offsetY: Float = 0f

    open fun update(deltaTime: Float, gravity: Float) {
        // Apply gravity
        var newY = pos.y
        velocityY += gravity * deltaTime * getWeight()
        newY += velocityY * deltaTime
        if (map.doesRectCollideWithMap(pos.x , newY + offsetY, getWidth(), getHeight())) {
            // Entity was falling and hit the ground
            if (velocityY < 0) {
                pos.y = floor(pos.y)
                grounded = true
            }
            velocityY = 0f
        } else {
            pos.y = newY
            grounded = false
        }
    }

    abstract fun render(batch: SpriteBatch, delta: Float)

    protected fun moveX(amount: Float) {
        val newX = pos.x + amount
        if (!map.doesRectCollideWithMap(newX, pos.y + offsetY, getWidth(), getHeight())) {
            pos.x = newX
            facingRight = if (invertFacing) amount <= 0 else amount > 0
        }
    }

    open fun receiveDamage(damage: Int) {
        if (currentHealth <= 0) return
        currentHealth -= damage
        map.main.audioManager.playSound("ouch")
    }

    fun drawOneTimeAnimation(batch: SpriteBatch, delta: Float) {
        if (!currAnimation!!.isAnimationFinished(elapsedTimeAnim)) {
            elapsedTimeAnim += delta
            batch.draw(currAnimation!!.getKeyFrame(elapsedTimeAnim),
                    pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
        } else {
            elapsedTimeAnim = 0f
        }
    }

    fun drawTexture(batch: SpriteBatch, texture: TextureRegion) {
        batch.draw(texture, pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
    }

    fun getWidth(): Int {
        return type.width
    }

    fun getHeight(): Int {
        return type.height
    }

    fun getWeight(): Int {
        return type.weight
    }
}