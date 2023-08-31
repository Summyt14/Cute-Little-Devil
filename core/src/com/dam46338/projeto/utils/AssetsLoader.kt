package com.dam46338.projeto.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable


class AssetsLoader : BaseAssetsLoader() {
    var resources = mutableMapOf<String, Disposable>()
    var animationsArr = mutableMapOf<String, MutableMap<String, Animation<TextureRegion>>>()

    // Load assets to textures and build animation
    init {
        // Images
        resources["background"] = Texture(Gdx.files.internal("mainmenu/Background.png"))
        resources["logo"] = Texture(Gdx.files.internal("mainmenu/logo.png"))
        resources["font"] = BitmapFont(Gdx.files.internal("font/Planes.fnt"))
        resources["button"] = Texture(Gdx.files.internal("mainmenu/button.png"))
        resources["buttonPressed"] = Texture(Gdx.files.internal("mainmenu/buttonPressed.png"))
        resources["buttonExit"] = Texture(Gdx.files.internal("mainmenu/buttonExit.png"))
        resources["buttonExitPressed"] = Texture(Gdx.files.internal("mainmenu/buttonExitPressed.png"))
        resources["frame1"] = Texture(Gdx.files.internal("mainmenu/frame1.png"))
        resources["frame2"] = Texture(Gdx.files.internal("mainmenu/frame2.png"))
        resources["frame3"] = Texture(Gdx.files.internal("mainmenu/frame3.png"))
        resources["frame4"] = Texture(Gdx.files.internal("mainmenu/frame4.png"))
        resources["sliderBack"] = Texture(Gdx.files.internal("mainmenu/Band_off.png"))
        resources["sliderFront"] = Texture(Gdx.files.internal("mainmenu/Band_on.png"))
        resources["sliderKnob"] = Texture(Gdx.files.internal("mainmenu/Slider.png"))
        resources["iconsAtlas"] = TextureAtlas(Gdx.files.internal("icons/Icons.txt"))
        resources["iconsPressedAtlas"] = TextureAtlas(Gdx.files.internal("icons/IconsPressed.txt"))
        resources["barsAtlas"] = TextureAtlas(Gdx.files.internal("bars/bars.txt"))
        resources["magicAtlas"] = TextureAtlas(Gdx.files.internal("magiceffects/magiceffects.txt"))
        resources["pinkMonsterAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/heroes/PinkMonster.txt"))
        resources["owletMonsterAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/heroes/OwletMonster.txt"))
        resources["dudeMonsterAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/heroes/DudeMonster.txt"))
        resources["bearAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/enemies/bear.txt"))
        resources["purpleBlobAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/enemies/purple_blob.txt"))
        resources["redSkullAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/enemies/red_skull.txt"))
        resources["tinySkullAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/enemies/tiny_skull.txt"))
        resources["yellowSkullAtlas"] = TextureAtlas(Gdx.files.internal("world/entities/enemies/yellow_skull.txt"))
        // Animations
        animationsArr["magicEffects"] = createAnimationArray(resources["magicAtlas"] as TextureAtlas)
        animationsArr["pinkMonster"] = createAnimationArray(resources["pinkMonsterAtlas"] as TextureAtlas)
        animationsArr["owletMonster"] = createAnimationArray(resources["owletMonsterAtlas"] as TextureAtlas)
        animationsArr["dudeMonster"] = createAnimationArray(resources["dudeMonsterAtlas"] as TextureAtlas)
        animationsArr["bear"] = createAnimationArray(resources["bearAtlas"] as TextureAtlas)
        animationsArr["purpleBlob"] = createAnimationArray(resources["purpleBlobAtlas"] as TextureAtlas)
        animationsArr["redSkull"] = createAnimationArray(resources["redSkullAtlas"] as TextureAtlas)
        animationsArr["tinySkull"] = createAnimationArray(resources["tinySkullAtlas"] as TextureAtlas)
        animationsArr["yellowSkull"] = createAnimationArray(resources["yellowSkullAtlas"] as TextureAtlas)
    }

    fun dispose() {
        resources.forEach { (_, disposable) -> disposable.dispose() }
    }
}

open class BaseAssetsLoader {
    companion object {
        fun createAnimationArray(textureAtlas: TextureAtlas):
                MutableMap<String, Animation<TextureRegion>> {
            val regions: Array<AtlasRegion> = textureAtlas.regions
            val animations = mutableMapOf<String, Animation<TextureRegion>>()
            for (i in 0 until regions.size) {
                animations[regions[i].name] = createCharacterAnimation(regions[i].texture,
                        regions[i].regionY, regions[i].index,
                        regions[i].originalWidth / regions[i].index, regions[i].originalHeight)
            }
            return animations
        }

        private fun createCharacterAnimation(texture: Texture, rowNum: Int, tileCount: Int,
                                             width: Int, height: Int): Animation<TextureRegion> {
            val frames = Array<TextureRegion>()
            for (i in 0 until tileCount) {
                frames.add(TextureRegion(texture, i * width + 1, rowNum + 1, width, height))
            }
            return Animation(1 / 10f, frames)
        }
    }
}