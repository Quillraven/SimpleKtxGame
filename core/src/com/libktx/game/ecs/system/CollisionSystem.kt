package com.libktx.game.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetManager
import com.libktx.game.assets.SoundAssets
import com.libktx.game.assets.get
import com.libktx.game.ecs.component.BucketComponent
import com.libktx.game.ecs.component.CollisionComponent
import com.libktx.game.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class CollisionSystem(bucket: Entity, assets: AssetManager) : IteratingSystem(allOf(TransformComponent::class, CollisionComponent::class).get()) {
    private val dropSound = assets[SoundAssets.Drop]
    private val bucketBounds = bucket[TransformComponent.mapper]!!.bounds
    private val bucketCmp = bucket[BucketComponent.mapper]!!

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            if (transform.bounds.y < 0) {
                engine.removeEntity(entity)
            } else if (transform.bounds.overlaps(bucketBounds)) {
                bucketCmp.dropsGathered++
                dropSound.play()
                engine.removeEntity(entity)
            }
        }
    }
}