package com.libktx.game.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.systems.IntervalSystem
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.MathUtils
import com.libktx.game.assets.TextureAtlasAssets
import com.libktx.game.assets.get
import com.libktx.game.ecs.component.CollisionComponent
import com.libktx.game.ecs.component.MoveComponent
import com.libktx.game.ecs.component.RenderComponent
import com.libktx.game.ecs.component.TransformComponent
import ktx.ashley.entity

class SpawnSystem(assets: AssetManager) : IntervalSystem(1f) {
    private val dropRegion = assets[TextureAtlasAssets.Game].findRegion("drop")

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        // spawn an initial drop when the system is added to the engine
        updateInterval()
    }

    override fun updateInterval() {
        engine.entity {
            with<RenderComponent> {
                sprite.setRegion(dropRegion)
                z = 1
            }
            with<TransformComponent> { bounds.set(MathUtils.random(0f, 800 - 64f), 480f, 64f, 64f) }
            with<MoveComponent> { speed.set(0f, -200f) }
            with<CollisionComponent>()
        }
    }
}