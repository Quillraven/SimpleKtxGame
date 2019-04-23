package com.libktx.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.libktx.game.screen.LoadingScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.log.logger

class Game : KtxGame<KtxScreen>() {
    private val log = logger<Game>()

    private val context = Context()
    private val viewport = FitViewport(800f, 480f)
    private val batch by lazy { SpriteBatch() }

    override fun create() {
        // creating the game context
        // context contains all instances that are relevant for the entire game
        // Note: context.dispose() will also dispose all instances that extended 'Disposable' interface
        context.register {
            bindSingleton<Batch>(batch)
            bindSingleton<Viewport>(viewport)
            bindSingleton(BitmapFont())
            bindSingleton(AssetManager())
            bindSingleton(this@Game)
        }

        // create LoadingScreen to load assets of game
        // pass context to get access to relevant instances
        addScreen(LoadingScreen(context))
        setScreen<LoadingScreen>()

        super.create()
    }

    override fun render() {
        // always update camera and set batch projection matrix as this is needed for all screens
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        super.render()
    }

    override fun resize(width: Int, height: Int) {
        log.debug { "Resizing to $width x $height" }
        viewport.update(width, height, true)
        super.resize(width, height)
    }

    override fun dispose() {
        context.dispose()
        super.dispose()
    }
}
