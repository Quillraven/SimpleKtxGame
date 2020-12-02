package com.libktx.game.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.libktx.game.assets.MusicAssets
import com.libktx.game.assets.TextureAtlasAssets
import com.libktx.game.assets.get
import com.libktx.game.ecs.component.BucketComponent
import com.libktx.game.ecs.component.MoveComponent
import com.libktx.game.ecs.component.RenderComponent
import com.libktx.game.ecs.component.TransformComponent
import com.libktx.game.ecs.system.CollisionSystem
import com.libktx.game.ecs.system.MoveSystem
import com.libktx.game.ecs.system.RenderSystem
import com.libktx.game.ecs.system.SpawnSystem
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with

class GameScreen(
    private val batch: Batch,
    private val font: BitmapFont,
    private val assets: AssetManager,
    private val camera: OrthographicCamera,
    private val engine: PooledEngine
) : KtxScreen {
    // create bucket entity
    private val bucket = engine.entity {
        with<BucketComponent>()
        with<TransformComponent> { bounds.set(800f / 2f - 64f / 2f, 20f, 64f, 64f) }
        with<MoveComponent>()
        with<RenderComponent>()
    }

    // create the touchPos to store mouse click position
    private val touchPos = Vector3()

    override fun render(delta: Float) {
        // process user input
        if (Gdx.input.isTouched) {
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            camera.unproject(touchPos)
            bucket[TransformComponent.mapper]?.let { transform -> transform.bounds.x = touchPos.x - 64f / 2f }
        }
        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> bucket[MoveComponent.mapper]?.let { move ->
                move.speed.x = -200f
            }
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> bucket[MoveComponent.mapper]?.let { move ->
                move.speed.x = 200f
            }
            else -> bucket[MoveComponent.mapper]?.let { move -> move.speed.x = 0f }
        }

        // everything is now done withing our entity engine --> update it every frame
        engine.update(delta)
    }

    override fun show() {
        // start the playback of the background music when the screen is shown
        assets[MusicAssets.Rain].apply { isLooping = true }.play()
        // set bucket sprite
        bucket[RenderComponent.mapper]?.sprite?.setRegion(assets[TextureAtlasAssets.Game].findRegion("bucket"))
        // initialize entity engine
        engine.apply {
            // add systems
            addSystem(SpawnSystem(assets))
            addSystem(MoveSystem())
            addSystem(RenderSystem(bucket, batch, font, camera))
            // add CollisionSystem last as it removes entities and this should always
            // happen at the end of an engine update frame
            addSystem(CollisionSystem(bucket, assets))
        }
    }
}
