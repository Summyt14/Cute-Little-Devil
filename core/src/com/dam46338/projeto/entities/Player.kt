package com.dam46338.projeto.entities

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.dam46338.projeto.MyGdxGame
import com.dam46338.projeto.utils.Utils
import com.dam46338.projeto.world.GameMap
import com.dam46338.projeto.world.TileType
import kotlin.math.abs

class Player(col: Int, row: Int, map: GameMap) : Entity(col, row, false, EntityType.Player, map) {
    var numCoins: Int = 0
    private var currentEnergy: Int = startingEnergy
    private var currAction: Actions = Actions.Idle
    private var throwables: ArrayList<Throwable> = ArrayList()
    private var throwablesToRemove: ArrayList<Throwable> = ArrayList()
    private var throwableType: EntityType

    private enum class Actions { Idle, Attack1, Throw, Hurt, Dead }

    init {
        animations = when {
            map.main.shopItems[1].equipped -> map.animations["dudeMonster"]!!
            map.main.shopItems[2].equipped -> map.animations["owletMonster"]!!
            else -> map.animations["pinkMonster"]!!
        }
        meleeAttackDamage = if (map.main.shopItems[3].equipped) 10 else 5
        throwCost = if (map.main.shopItems[4].equipped) 10 else 25
        throwableType = when {
            map.main.shopItems[5].equipped -> EntityType.Lightning
            map.main.shopItems[6].equipped -> EntityType.OverpoweredShield
            else -> EntityType.Fireball
        }

        currentHealth = startingHealth
        map.controller.setHealth(currentHealth)
        map.controller.setEnergy(currentEnergy)
    }

    override fun update(deltaTime: Float, gravity: Float) {
        handleMovement(deltaTime, gravity)
        handleThrowables(deltaTime, gravity)
        handleCollectibles()

        if (currentHealth <= 0 || pos.y < 50)
            map.main.setScreen(MyGdxGame.Screens.Game, map.getLevelNum())
    }

    private fun handleMovement(deltaTime: Float, gravity: Float) {
        // Jump
        if (map.controller.upPressed && grounded) {
            velocityY += jumpVelocity * getWeight()
            map.main.audioManager.playSound("jump")
        }
        // Hold jump to jump higher
        else if (map.controller.upPressed && !grounded && velocityY > 0)
            velocityY += jumpVelocity * getWeight() * deltaTime
        // Apply gravity
        super.update(deltaTime, gravity)

        moving = when {
            // Move Left
            map.controller.leftPressed && (currAction == Actions.Idle || !grounded) -> {
                moveX(-speed * deltaTime)
                true
            }
            // Move Right
            map.controller.rightPressed && (currAction == Actions.Idle || !grounded) -> {
                moveX(speed * deltaTime)
                true
            }
            else -> false
        }

        realizingAction = when {
            // Attack
            map.controller.attackPressed -> {
                if (currAnimation != null && currAnimation!!.isAnimationFinished(elapsedTimeAnim)) {
                    map.controller.attackPressed = false
                    currAction = Actions.Attack1
                    performAttack()
                    elapsedTimeAnim = 0f
                }
                true
            }

            // Throw
            map.controller.throwPressed -> {
                if (currAnimation != null && currAnimation!!.isAnimationFinished(elapsedTimeAnim)) {
                    map.controller.throwPressed = false
                    currAction = Actions.Throw
                    performThrow()
                    elapsedTimeAnim = 0f
                    map.main.audioManager.playSound("pew")
                }
                true
            }
            // Idle if not realizing action
            else -> {
                if (currAnimation == null) {
                    currAction = Actions.Idle
                    false
                } else {
                    if (currAnimation?.isAnimationFinished(elapsedTimeAnim)!!) {
                        currAction = Actions.Idle
                        false
                    } else true
                }
            }
        }
    }

    private fun handleThrowables(deltaTime: Float, gravity: Float) {
        for (throwable in throwables) {
            throwable.update(deltaTime, gravity)
            if (throwable.hasStoppedMoving()) throwablesToRemove.add(throwable)
        }
        if (throwablesToRemove.size > 0) throwables.removeAll(throwablesToRemove)
    }

    private fun handleCollectibles() {
        val tile = map.checkForCollectibles(pos.x, pos.y, getWidth(), getHeight())
        when (tile.first) {
            TileType.Coin -> {
                numCoins++
                map.removeTile(tile.second)
                map.main.audioManager.playSound("coin")
            }
            TileType.Key -> {
                map.keyCollected = true
                map.removeTile(tile.second)
                map.main.audioManager.playSound("key")
            }
            TileType.Chest -> if (map.keyCollected) {
                map.chestCollected = true
                map.main.audioManager.playSound("chest")
            }
            TileType.Cake -> {
                currentHealth += 25
                if (currentHealth > 100) currentHealth = 100
                map.controller.setHealth(currentHealth)
                map.removeTile(tile.second)
                map.main.audioManager.playSound("eat")
            }
            TileType.Chicken, TileType.Apple -> {
                currentEnergy = 100
                map.controller.setEnergy(currentEnergy)
                map.removeTile(tile.second)
                map.main.audioManager.playSound("eat")
            }
            else -> return
        }
    }

    private fun performAttack() {
        for (entity in map.entities) {
            if (entity.type != EntityType.Player && abs(entity.pos.x - pos.x) <= meleeAttackRange &&
                    abs(entity.pos.y - pos.y) <= meleeAttackRange) {
                entity.receiveDamage(meleeAttackDamage)
                map.main.audioManager.playSound("punch")
            }
        }
    }

    private fun performThrow() {
        if (currentEnergy <= 0) return
        val throwablePos = pos.cpy().add(5f, 10f)
        val throwable = Throwable(throwablePos, facingRight, false, throwableType, map)
        throwables.add(throwable)
        currentEnergy -= throwCost
        map.controller.setEnergy(currentEnergy)
    }

    override fun receiveDamage(damage: Int) {
        super.receiveDamage(damage)
        map.controller.setHealth(currentHealth)
    }

    override fun render(batch: SpriteBatch, delta: Float) {
        when {
            moving && !realizingAction -> {
                currAnimation = Utils.flipAnimation(animations["Monster_Run"]!!, facingRight)
                batch.draw(currAnimation!!.getKeyFrame(elapsedTimeAnim, true),
                        pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
                elapsedTimeAnim += delta
            }
            currAction == Actions.Idle -> {
                currAnimation = Utils.flipAnimation(animations["Monster_Idle"]!!, facingRight)
                batch.draw(currAnimation!!.getKeyFrame(elapsedTimeAnim, true),
                        pos.x, pos.y, getWidth().toFloat(), getHeight().toFloat())
                elapsedTimeAnim += delta
            }
            currAction == Actions.Attack1 -> {
                currAnimation = Utils.flipAnimation(animations["Monster_Attack1"]!!, facingRight)
                drawOneTimeAnimation(batch, delta)
            }
            currAction == Actions.Throw -> {
                currAnimation = Utils.flipAnimation(animations["Monster_Throw"]!!, facingRight)
                drawOneTimeAnimation(batch, delta)
            }
            currAction == Actions.Hurt -> {
                currAnimation = Utils.flipAnimation(animations["Monster_Hurt"]!!, facingRight)
                drawOneTimeAnimation(batch, delta)
            }
            currAction == Actions.Dead -> {
                currAnimation = Utils.flipAnimation(animations["Monster_Death"]!!, facingRight)
                drawOneTimeAnimation(batch, delta)
            }
            else -> drawTexture(batch, currAnimation!!.keyFrames.get(currAnimation!!.keyFrames.size - 1))
        }

        for (throwable in throwables) {
            throwable.render(batch, delta)
        }
    }

    companion object {
        const val speed: Int = 100
        const val jumpVelocity: Float = 4.8f
        const val startingHealth: Int = 100
        const val startingEnergy: Int = 100
        const val meleeAttackRange: Int = 30
        var meleeAttackDamage: Int = 5
        var throwCost: Int = 25
    }
}