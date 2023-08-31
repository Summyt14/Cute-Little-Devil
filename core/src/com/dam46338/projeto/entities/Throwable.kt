package com.dam46338.projeto.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.dam46338.projeto.entities.enemies.Enemy
import com.dam46338.projeto.utils.Utils
import com.dam46338.projeto.world.GameMap
import kotlin.math.abs

class Throwable(pos: Vector2, override var facingRight: Boolean, private var hitPlayer: Boolean,
                type: EntityType, map: GameMap) : Entity((pos.x / type.width).toInt(),
        (pos.y / type.height).toInt(), false, type, map) {
    private var lastPos: Vector2 = pos.cpy()
    private var lastEntityHit: Entity? = null

    init {
        animations = map.animations["magicEffects"]!!
        currAnimation = when (type) {
            EntityType.MidasTouch -> {
                damage = 10
                animations["Midas-touch"]
            }
            EntityType.BlackHole -> {
                damage = 10
                animations["Black-hole"]
            }
            EntityType.Lightning -> {
                damage = 15
                animations["Lightning"]
            }
            EntityType.OverpoweredShield -> {
                damage = 25
                animations["Shield"]
            }
            else -> animations["Fire-ball"]
        }
    }

    override fun update(deltaTime: Float, gravity: Float) {
        super.update(deltaTime, gravity)

        if (facingRight) moveX(speed * deltaTime)
        else moveX(-speed * deltaTime)
        elapsedTimeAnim += deltaTime
        if (!hitPlayer) checkForEnemies()
        else checkForPlayer()
    }

    private fun checkForEnemies() {
        for (entity in map.entities) {
            if (lastEntityHit != entity && entity.type != EntityType.Player && entity.currentHealth > 0 &&
                    (entity as Enemy).active && abs(entity.pos.x - pos.x) <= hitRange &&
                    abs(entity.pos.y - pos.y) <= hitRange) {
                lastEntityHit = entity
                entity.receiveDamage(damage)
            }
        }
    }

    private fun checkForPlayer() {
        if (lastEntityHit != map.player && map.player.currentHealth > 0 &&
                abs(map.player.pos.x - pos.x) <= hitRange &&
                abs(map.player.pos.y - pos.y) <= hitRange) {
            lastEntityHit = map.player
            map.player.receiveDamage(damage)
        }
    }

    fun hasStoppedMoving(): Boolean {
        return if (lastPos == pos || grounded || lastEntityHit != null) true
        else {
            lastPos = pos.cpy()
            false
        }
    }

    override fun render(batch: SpriteBatch, delta: Float) {
        currAnimation = Utils.flipAnimation(currAnimation!!, facingRight)
        batch.draw(currAnimation!!.getKeyFrame(elapsedTimeAnim, true),
                pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
    }

    companion object {
        const val speed: Int = 300
        const val hitRange: Int = 20
        var damage: Int = 5
    }
}