package com.dam46338.projeto.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.dam46338.projeto.MyGdxGame

class LoginScreen(private val main: MyGdxGame) : Screen, InputProcessor {
    private val stage: Stage = Stage()
    private val batch: SpriteBatch = SpriteBatch()
    private val resources = main.resources
    private val scaleX: Float = 4f
    private val scaleY: Float = 4f
    private var loginAsLabel: Label
    private var registerClicked: Boolean = false
    private var loginClicked: Boolean = false
    private lateinit var email: String
    private lateinit var fieldEmail: TextField
    private lateinit var fieldPass: TextField

    init {
        Gdx.input.inputProcessor = stage
        val rootTable = Table()
        rootTable.setPosition(stage.width / 2 - ((resources["frame4"] as Texture).width / 2f * scaleX),
                stage.height / 2 - ((resources["frame4"] as Texture).height / 2f * scaleY))
        rootTable.setSize((resources["frame4"] as Texture).width.toFloat() * scaleX,
                (resources["frame4"] as Texture).height.toFloat() * scaleY)
        rootTable.background = TextureRegionDrawable(TextureRegion(resources["frame4"] as Texture))

        val titleLabel = Label("Account",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        titleLabel.setFontScale(2f)
        titleLabel.setAlignment(Align.center)

        val buttonsTable = Table()
        val loginLabel = Label("Login",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.GREEN))
        loginLabel.setFontScale(2f)
        loginLabel.setAlignment(Align.center)

        val registerLabel = Label("Register",
                Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        registerLabel.setFontScale(2f)
        registerLabel.setAlignment(Align.center)

        loginLabel.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                performAction(fieldEmail.text, fieldPass.text, "Login")
                main.audioManager.playSound("button")
            }
        })

        registerLabel.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                performAction(fieldEmail.text, fieldPass.text, "Register")
                main.audioManager.playSound("button")
            }
        })

        buttonsTable.add(loginLabel).center().expandX().fillX()
                .height((resources["frame4"] as Texture).height.toFloat() / (scaleY - 2.8f))
                .padLeft(285f)
        buttonsTable.add(registerLabel).center().expandX().padLeft(245f)

        rootTable.add(titleLabel).expandX().height((resources["frame4"] as Texture).height.toFloat() / (scaleY - 2.5f))
        rootTable.row()
        rootTable.add(defineInputs()).expandY().center().pad(30f, 100f, 30f, 100f)
        rootTable.row()
        rootTable.add(buttonsTable).left()
        stage.addActor(rootTable)

        val textureExitNotPressed = resources["buttonExit"] as Texture
        val textureExitPressed = resources["buttonExitPressed"] as Texture
        val exitButton = Image(textureExitNotPressed)
        exitButton.setSize(exitButton.width * scaleY, exitButton.height * scaleY)
        exitButton.setPosition(stage.width / 2 + rootTable.width / 2 - 20,
                stage.height - titleLabel.height - ((resources["frame4"] as Texture).height) - 125)

        exitButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                exitButton.drawable = TextureRegionDrawable(TextureRegion(textureExitPressed))
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                exitButton.drawable = TextureRegionDrawable(TextureRegion(textureExitNotPressed))
                main.setScreen(MyGdxGame.Screens.Initial)
                main.audioManager.playSound("button")
            }
        })
        stage.addActor(exitButton)

        val loginAsText = if (main.firebase.getLoginState() == 1)
            "Logged in with email: ${main.firebase.getCurrentUserEmail()}" else "Register or Login"
        loginAsLabel = Label(loginAsText, Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        loginAsLabel.setFontScale(1.5f)
        loginAsLabel.setAlignment(Align.center)
        loginAsLabel.setPosition(main.screenWidth / 2 - loginAsLabel.width / 2, main.screenHeight - loginAsLabel.height - 50)
        stage.addActor(loginAsLabel)
    }

    private fun defineInputs(): Table {
        val inputTable = Table()

        val textLabelEmail = Label("Email", Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        textLabelEmail.setFontScale(1.5f)
        textLabelEmail.setAlignment(Align.right)

        val textLabelPass = Label("Password", Label.LabelStyle(resources["font"] as BitmapFont, Color.WHITE))
        textLabelPass.setFontScale(1.5f)
        textLabelPass.setAlignment(Align.right)

        val backColor = Pixmap(100, 100, Pixmap.Format.RGB888)
        backColor.setColor(Color(28 / 255f, 26 / 255f, 48 / 255f, 1f))
        backColor.fill()

        val style = TextFieldStyle()
        val font = resources["font"] as BitmapFont
        font.data.setScale(1.3f)
        style.font = font
        style.fontColor = Color.WHITE
        style.background = TextureRegionDrawable(Texture(backColor))

        fieldEmail = TextField("", style)
        fieldEmail.setSize(100f, 100f)
        fieldEmail.setBlinkTime(5f)
        fieldEmail.messageText = "Email"

        fieldPass = TextField("", style)
        fieldPass.setSize(100f, 100f)
        fieldPass.setBlinkTime(5f)
        fieldPass.messageText = "Password"
        fieldPass.isPasswordMode = true
        fieldPass.setPasswordCharacter('*')

        inputTable.add(textLabelEmail).right().pad(0f, 30f, 0f, 30f)
        inputTable.add(fieldEmail).size((resources["frame4"] as Texture).width * 2f,
                (resources["frame4"] as Texture).height / 2f)
        inputTable.row().padTop(30f)
        inputTable.add(textLabelPass).right().pad(30f, 30f, 0f, 30f)
        inputTable.add(fieldPass).size((resources["frame4"] as Texture).width * 2f,
                (resources["frame4"] as Texture).height / 2f)
        return inputTable
    }

    private fun performAction(email: String, password: String, type: String) {
        if (email != "" && password != "") {
            main.firebase.resetStates()
            main.resetShopItems()
            if (type == "Login") {
                main.firebase.loginUser(email, password)
                loginClicked = true
            } else {
                main.firebase.registerUser(email, password)
                registerClicked = true
            }
            this.email = email
        } else {
            loginAsLabel.setText("One of the fields is empty!")
        }
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        batch.draw(resources["background"] as Texture, 0f, 0f, main.screenWidth, main.screenHeight)
        batch.end()

        if (registerClicked) {
            when {
                main.firebase.getRegisterState() == 1 -> {
                    loginAsLabel.setText("Registered with email: $email. Please Login")
                    registerClicked = false
                    main.firebase.resetStates()
                }
                main.firebase.getRegisterState() == 0 -> {
                    loginAsLabel.setText("Register failed!")
                    registerClicked = false
                    main.firebase.resetStates()
                }
                else -> loginAsLabel.setText("Connecting to database...")
            }
        }

        if (loginClicked) {
            when {
                main.firebase.getLoginState() == 1 -> {
                    loginAsLabel.setText("Logged in with email: $email")
                    loginClicked = false
                    main.getNumCoins()
                    main.getItems()
                }
                main.firebase.getLoginState() == 0 -> {
                    loginAsLabel.setText("Login failed!")
                    loginClicked = false
                    main.firebase.resetStates()
                }
                else -> loginAsLabel.setText("Connecting to database...")
            }
        }

        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        batch.dispose()
        stage.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK) {
            main.setScreen(MyGdxGame.Screens.Initial)
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}