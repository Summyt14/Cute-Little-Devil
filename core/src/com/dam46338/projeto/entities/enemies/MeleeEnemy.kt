package com.dam46338.projeto.entities.enemies

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.dam46338.projeto.entities.EntityType
import com.dam46338.projeto.entities.Player
import com.dam46338.projeto.utils.Utils
import com.dam46338.projeto.world.GameMap
import kotlin.math.abs

class MeleeEnemy(type: EntityType, col: Int, row: Int, map: GameMap, patrolPoints: ArrayList<Vector2>) :
        Enemy(col, row, type, map, patrolPoints) {

    init {
        animations = map.animations[type.id]!!
        currentHealth = startingHealth
    }

    override fun render(batch: SpriteBatch, delta: Float) {
        when {
            moving && currAction == Actions.Patrol -> {
                currAnimation = Utils.flipAnimation(animations["Walk"]!!, facingRight)
                batch.draw(currAnimation!!.getKeyFrame(elapsedTimeAnim, true),
                        pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
                elapsedTimeAnim += delta
            }
            !moving && !realizingAction && currAction != Actions.None -> {
                currAnimation = Utils.flipAnimation(animations["Idle"]!!, facingRight)
                batch.draw(currAnimation!!.getKeyFrame(elapsedTimeAnim, true),
                        pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
                elapsedTimeAnim += delta
            }
            currAction == Actions.Attack -> {
                currAnimation = Utils.flipAnimation(animations["Attack"]!!, isPlayerOnRight())
                drawOneTimeAnimation(batch, delta)
            }
            currAction == Actions.Hurt -> {
                currAnimation = Utils.flipAnimation(animations["Hurt"]!!, facingRight)
                drawOneTimeAnimation(batch, delta)
            }
            currAction == Actions.Dead -> {
                currAnimation = Utils.flipAnimation(animations["Death"]!!, facingRight)
                drawOneTimeAnimation(batch, delta)
            }
            else -> drawTexture(batch, currAnimation!!.keyFrames.get(currAnimation!!.keyFrames.size - 1))
        }
    }

    override fun isPlayerInRange(): Boolean {
        return abs(player.pos.x - pos.x) <= attackRange && abs(player.pos.y - pos.y) <= attackRange
    }

    override fun performAttack() {
        player.receiveDamage(attackDamage)
    }

    override fun getSpeed(): Int {
        return speed
    }

    override fun getWaitForAttackTime(): Int {
        return waitForAttack
    }

    companion object {
        const val speed: Int = 30
        const val attackRange: Int = 25
        const val attackDamage: Int = 10
        const val startingHealth: Int = 25
        const val waitForAttack: Int = 3
    }
}