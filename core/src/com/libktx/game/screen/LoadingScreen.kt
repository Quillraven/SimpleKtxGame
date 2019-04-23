package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.libktx.game.Game
import com.libktx.game.asset.AtlasAssets
import com.libktx.game.asset.MusicAssets
import com.libktx.game.asset.SoundAssets
import com.libktx.game.asset.load
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.inject.Context

class LoadingScreen(private val context: Context) : KtxScreen {
    private val batch: Batch = context.inject()
    private val font: BitmapFont = context.inject()
    private val game: Game = context.inject()
    private val assets: AssetManager = context.inject()

    override fun show() {
        // load all assets for the game
        AtlasAssets.values().forEach { assets.load(it) }
        SoundAssets.values().forEach { assets.load(it) }
        MusicAssets.values().forEach { assets.load(it) }
    }

    override fun render(delta: Float) {
        batch.use {
            font.draw(it, "Welcome to Drop!!!", 300f, 250f)
            // update asset manager
            if (assets.update()) {
                font.draw(it, "Tap anywhere to begin!", 300f, 200f)
            } else {
                font.draw(it, "Loading assets...", 300f, 200f)
            }
        }

        if (Gdx.input.isTouched && assets.isFinished) {
            // if the player touches the screen and all assets are loaded
            // then we can switch to the game screen
            game.addScreen(GameScreen(context))
            game.setScreen<GameScreen>()
            // and remove the LoadingScreen as it is no longer needed
            game.removeScreen<LoadingScreen>()
            dispose()
        }
    }
}