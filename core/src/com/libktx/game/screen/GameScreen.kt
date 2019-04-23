package com.libktx.game.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.Viewport
import com.libktx.game.asset.AtlasAssets
import com.libktx.game.asset.MusicAssets
import com.libktx.game.asset.SoundAssets
import com.libktx.game.asset.get
import ktx.app.KtxScreen
import ktx.assets.invoke
import ktx.assets.pool
import ktx.collections.gdxListOf
import ktx.graphics.use
import ktx.inject.Context
import ktx.log.logger

class GameScreen(context: Context) : KtxScreen {
    private val log = logger<GameScreen>()

    private val batch: Batch = context.inject()
    private val camera: Camera = context.inject<Viewport>().camera
    private val assets: AssetManager = context.inject()

    private val touchPos = Vector3()
    private val bucket = Rectangle(800f / 2f - 64f / 2f, 20f, 64f, 64f)
    // use a pool for the raindrops to avoid memory allocation and garbage collection
    private val raindropsPool = pool { Rectangle() }
    private val raindrops = gdxListOf<Rectangle>()
    private var lastDropTime = 0L

    // game assets
    private val dropImage = assets[AtlasAssets.Images].findRegion("drop")
    private val bucketImage = assets[AtlasAssets.Images].findRegion("bucket")
    private val dropSound = assets[SoundAssets.Drop]
    private val rainMusic = assets[MusicAssets.Rain].apply { isLooping = true }

    override fun show() {
        rainMusic.play()
        spawnRaindrop()
    }

    private fun spawnRaindrop() {
        raindrops.add(raindropsPool().set(MathUtils.random(0f, 800 - 64f), 480f, 64f, 64f))
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun render(delta: Float) {
        // render bucket and drops
        batch.use {
            batch.draw(bucketImage, bucket.x, bucket.y)
            raindrops.forEach { batch.draw(dropImage, it.x, it.y) }
        }

        // move bucket according to input
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucket.x = touchPos.x - 64 / 2
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * delta
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * delta
        bucket.x = MathUtils.clamp(bucket.x, 0f, 800f - 64f)

        // spawn a new drop periodically
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnRaindrop()
        }

        // move raindrops and remove them if the either leave the screen or get caught by the bucket
        raindrops.forEach { raindrop ->
            raindrop.y -= 200 * delta
            if (raindrop.y < 0) {
                raindrops.remove()
                raindropsPool(raindrop)
            } else if (raindrop.overlaps(bucket)) {
                dropSound.play()
                raindrops.remove()
                raindropsPool(raindrop)
            }
        }
    }

    override fun dispose() {
        log.debug { "${raindrops.size} remaining raindrops and ${raindropsPool.free} drop(s) in pool" }
    }
}