package com.dam46338.projeto

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable
import com.dam46338.projeto.screens.*
import com.dam46338.projeto.utils.AssetsLoader
import com.dam46338.projeto.utils.AudioManager
import com.dam46338.projeto.utils.Firebase

class MyGdxGame(val firebase: Firebase) : Game() {
    lateinit var assetsLoader: AssetsLoader
    lateinit var resources: Map<String, Disposable>
    lateinit var animations: MutableMap<String, MutableMap<String, Animation<TextureRegion>>>
    lateinit var audioManager: AudioManager
    private var coins: Int = 0
    var screenWidth: Float = 0f
    var screenHeight: Float = 0f
    var shopItems: ArrayList<Item> = ArrayList()

    enum class Screens { Initial, Shop, LevelSelect, Settings, Login, Help, LevelComplete, Game }

    override fun create() {
        assetsLoader = AssetsLoader()
        resources = assetsLoader.resources
        animations = assetsLoader.animationsArr
        audioManager = AudioManager()

        screenWidth = Gdx.graphics.width.toFloat()
        screenHeight = Gdx.graphics.height.toFloat()
        shopItems = arrayListOf(
                Item("Pink Monster", 0, animations["pinkMonster"]!!["Monster"]!!.keyFrames.get(0), acquired = true, equipped = true),
                Item("Dude Monster", 20, animations["dudeMonster"]!!["Monster"]!!.keyFrames.get(0)),
                Item("Owlet Monster", 30, animations["owletMonster"]!!["Monster"]!!.keyFrames.get(0)),
                Item("More Damage", 50, (resources["iconsAtlas"] as TextureAtlas).regions[62]),
                Item("More Energy", 50, (resources["iconsAtlas"] as TextureAtlas).regions[59]),
                Item("Lightning Throw", 75, animations["magicEffects"]!!["Lightning"]!!.keyFrames.get(5)),
                Item("Overpowered Bubble", 100, animations["magicEffects"]!!["Shield"]!!.keyFrames.get(0)))

        setScreen(Screens.Initial)
    }

    fun setScreen(screen: Screens, level: Int = 1) {
        when (screen) {
            Screens.Initial -> setScreen(InitialScreen(this))
            Screens.Shop -> setScreen(ShopScreen(this))
            Screens.LevelSelect -> setScreen(LevelSelectScreen(this))
            Screens.Settings -> setScreen(SettingsScreen(this))
            Screens.Login -> setScreen(LoginScreen(this))
            Screens.Help -> setScreen(HelpScreen(this))
            Screens.LevelComplete -> setScreen(InitialScreen(this))
            Screens.Game -> setScreen(GameScreen(this, level))
        }

        if (screen == Screens.Game) {
            when (level) {
                1 -> audioManager.playMusic(AudioManager.MusicType.Overworld)
                2 -> audioManager.playMusic(AudioManager.MusicType.Cave)
                3 -> audioManager.playMusic(AudioManager.MusicType.Hell)
            }
        } else {
            if (audioManager.currentMusicType != AudioManager.MusicType.Intro)
                audioManager.playMusic(AudioManager.MusicType.Intro)
        }
    }

    override fun dispose() {
        assetsLoader.dispose()
        audioManager.dispose()
    }

    fun resetShopItems() {
        for (i in 1 until shopItems.size)
            shopItems[i].acquired = false
    }

    fun getNumCoins(): Int {
        coins = firebase.getCoins()
        return coins
    }

    fun getItems(): ArrayList<String> {
        val itemNamesDb = firebase.getItems()
        for (i in 0 until shopItems.size) {
            for (j in 0 until itemNamesDb.size) {
                println(itemNamesDb[j])
                if (shopItems[i].name == itemNamesDb[j]) {
                    shopItems[i].acquired = true
                }
            }
        }
        return itemNamesDb
    }

    fun setNumCoins(value: Int) {
        coins = value
        firebase.setCoinsInDb(coins)
    }

    fun addItemInDb(name: String) {
        firebase.addItemInDb(name)
    }
}