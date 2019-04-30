package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.libktx.game.Game
import ktx.app.KtxScreen
import ktx.graphics.use

class MainMenuScreen(val game: Game) : KtxScreen {
    private var camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }

    override fun render(delta: Float) {
        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.use {
            game.font.draw(it, "Welcome to Drop!!! ", 100f, 150f)
            game.font.draw(it, "Tap anywhere to begin!", 100f, 100f)
        }

        if (Gdx.input.isTouched) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<MainMenuScreen>()
            dispose()
        }
    }
}