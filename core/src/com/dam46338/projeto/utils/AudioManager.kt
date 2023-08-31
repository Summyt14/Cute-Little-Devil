package com.dam46338.projeto.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

class AudioManager {
    private val musics: ArrayList<Music> = arrayListOf()
    private val sounds: MutableMap<String, Sound> = mutableMapOf()
    private var currentMusic: Music
    var currentMusicVolume: Float = 0.5f
    var currentSoundVolume: Float = 0.5f
    var currentMusicType: MusicType? = null

    enum class MusicType { Intro, Overworld, Cave, Hell }

    init {
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("audio/IntroMusic.mp3")))
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("audio/OverworldMusic.mp3")))
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("audio/CaveMusic.mp3")))
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("audio/HellMusic.mp3")))
        sounds["jump"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/jump.mp3"))
        sounds["pew"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/pew.mp3"))
        sounds["eat"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/eat.mp3"))
        sounds["coin"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/coin.mp3"))
        sounds["punch"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/punch.mp3"))
        sounds["ouch"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/ouch.mp3"))
        sounds["key"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/key.mp3"))
        sounds["chest"] = Gdx.audio.newSound(Gdx.files.internal("audio/characters/chest.mp3"))
        sounds["buy"] = Gdx.audio.newSound(Gdx.files.internal("audio/BuySound.mp3"))
        sounds["button"] = Gdx.audio.newSound(Gdx.files.internal("audio/ButtonClick.mp3"))
        currentMusic = musics[0]
    }

    fun playMusic(type: MusicType) {
        currentMusic.stop()
        currentMusic = when (type) {
            MusicType.Intro -> musics[0]
            MusicType.Overworld -> musics[1]
            MusicType.Cave -> musics[2]
            MusicType.Hell -> musics[3]
        }
        currentMusicType = type
        currentMusic.volume = currentMusicVolume
        currentMusic.isLooping = true
        currentMusic.play()
    }

    fun playSound(name: String) {
        val sound = sounds[name]
        val soundId = sound!!.play(currentSoundVolume)
        sound.setLooping(soundId, false)
    }

    fun setMusicVolume(value: Float) {
        currentMusicVolume = value
        currentMusic.volume = currentMusicVolume
    }

    fun setSoundVolume(value: Float) {
        currentSoundVolume = value
    }

    fun dispose() {
        musics.forEach { music -> music.dispose() }
        for (sound in sounds.values) sound.dispose()
    }
}