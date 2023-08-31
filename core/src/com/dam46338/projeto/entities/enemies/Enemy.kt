package com.dam46338.projeto.entities.enemies

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.dam46338.projeto.entities.Entity
import com.dam46338.projeto.entities.EntityType
import com.dam46338.projeto.entities.Player
import com.dam46338.projeto.world.GameMap
import kotlin.math.abs
import kotlin.random.Random

abstract class Enemy(col: Int, row: Int, type: EntityType, map: GameMap, private val patrolPoints: ArrayList<Vector2>) :
        Entity(col, row, true, type, map) {
    var active: Boolean = false
    private var currentIdx: Int = 0
    private var currentPoint: Vector2
    private var isWaiting: Boolean = false
    private var savedTimestamp: Float = 0f
    protected var player: Player = map.player
    protected var currAction: Actions = Actions.Patrol

    enum class Actions { Patrol, Attack, Hurt, Dead, None }

    init {
        for (i in 0 until patrolPoints.size) {
            patrolPoints[i] = map.getLocationByGridCoordinates(patrolPoints[i].x.toInt(), patrolPoints[i].y.toInt())
        }
        currentPoint = patrolPoints[currentIdx]
    }

    override fun update(deltaTime: Float, gravity: Float) {
        elapsedTime += deltaTime
        // Wait until enemy appears on screen
        if (abs(pos.x - player.pos.x) >= Gdx.graphics.width / 4) return
        else active = true
        super.update(deltaTime, gravity)

        when (currAction) {
            Actions.Patrol -> {
                if (isPlayerInRange()) {
                    currAction = Actions.Attack
                    return
                }

                realizingAction = false
                if (abs(pos.x - currentPoint.x) >= 1 && !isWaiting) {
                    moveTo(currentPoint, deltaTime)
                    savedTimestamp = elapsedTime
                } else {
                    wait(Random.nextInt(2, 5))
                }
            }
            Actions.Attack -> {
                realizingAction = true
                moving = false
                if (currAnimation == null || currAnimation!!.isAnimationFinished(elapsedTimeAnim)) {
                    realizingAction = false
                    performAttack()
                    currAction = Actions.Patrol
                    elapsedTimeAnim = 0f
                }
            }
            Actions.Hurt -> {
                realizingAction = true
                moving = false
                if (currAnimation == null || currAnimation!!.isAnimationFinished(elapsedTimeAnim)) {
                    elapsedTimeAnim = 0f
                    realizingAction = false
                    currAction = Actions.Patrol
                }
            }
            Actions.Dead -> {
                realizingAction = true
                moving = false
                if (currAnimation == null || currAnimation!!.isAnimationFinished(elapsedTimeAnim)) {
                    elapsedTimeAnim = 0f
                    realizingAction = false
                    currAction = Actions.None
                }
            }
            Actions.None -> return
        }
    }

    private fun moveTo(point: Vector2, deltaTime: Float) {
        moving = true
        if (point.x < pos.x) moveX(-getSpeed() * deltaTime)
        else moveX(getSpeed() * deltaTime)
    }

    private fun wait(seconds: Int) {
        isWaiting = true
        moving = false
        if (elapsedTime - savedTimestamp >= seconds) {
            isWaiting = false
            elapsedTime = 0f
            elapsedTimeAnim = 0f
            currentIdx++
            currentIdx %= patrolPoints.size
            currentPoint = patrolPoints[currentIdx]
        }
    }

    override fun receiveDamage(damage: Int) {
        super.receiveDamage(damage)
        if (currAction != Actions.None)
            currAction = if (currentHealth <= 0) Actions.Dead else Actions.Hurt
        realizingAction = true
        elapsedTimeAnim = 0f
    }

    protected fun isPlayerOnRight() : Boolean {
        return pos.x - player.pos.x > 0
    }

    abstract fun isPlayerInRange(): Boolean
    abstract fun performAttack()
    abstract fun getSpeed(): Int
    abstract fun getWaitForAttackTime(): Int
}