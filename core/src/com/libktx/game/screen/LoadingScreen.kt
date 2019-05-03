package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.libktx.game.Game
import com.libktx.game.assets.MusicAssets
import com.libktx.game.assets.SoundAssets
import com.libktx.game.assets.TextureAtlasAssets
import com.libktx.game.assets.load
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(val game: Game) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, 800f, 480f) }

    override fun show() {
        MusicAssets.values().forEach { game.assets.load(it) }
        SoundAssets.values().forEach { game.assets.load(it) }
        TextureAtlasAssets.values().forEach { game.assets.load(it) }
    }

    override fun render(delta: Float) {
        // continue loading our assets
        game.assets.update()

        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.use {
            game.font.draw(it, "Welcome to Drop!!! ", 100f, 150f)
            if (game.assets.isFinished) {
                game.font.draw(it, "Tap anywhere to begin!", 100f, 100f)
            } else {
                game.font.draw(it, "Loading assets...", 100f, 100f)
            }
        }

        if (Gdx.input.isTouched && game.assets.isFinished) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
    }
}