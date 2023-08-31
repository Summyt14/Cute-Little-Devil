package com.dam46338.projeto.utils

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Utils {
    companion object {
        fun flipAnimation(animation: Animation<TextureRegion>, facingRight: Boolean): Animation<TextureRegion> {
            val anim: Animation<TextureRegion> = animation
            if (facingRight) {
                for (textureRegion: TextureRegion in anim.keyFrames.iterator())
                    if (textureRegion.isFlipX) textureRegion.flip(true, false)
            } else {
                for (textureRegion: TextureRegion in anim.keyFrames.iterator())
                    if (!textureRegion.isFlipX) textureRegion.flip(true, false)
            }
            return anim
        }
    }
}