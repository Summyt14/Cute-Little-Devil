package com.dam46338.projeto

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        config.useImmersiveMode = true

        val androidFirebase = AndroidFirebase()
        val myGame = MyGdxGame(androidFirebase)
        initialize(myGame, config)
    }
}