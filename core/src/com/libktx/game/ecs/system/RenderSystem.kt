package com.libktx.game.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.libktx.game.ecs.component.BucketComponent
import com.libktx.game.ecs.component.RenderComponent
import com.libktx.game.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use

class RenderSystem(bucket: Entity,
                   private val batch: Batch,
                   private val font: BitmapFont,
                   private val camera: OrthographicCamera) : SortedIteratingSystem(
        allOf(TransformComponent::class, RenderComponent::class).get(),
        // compareBy is used to render entities by their z-index (=bucket is drawn in the background; raindrops are drawn in the foreground)
        compareBy { entity: Entity -> entity[RenderComponent.mapper]?.z }) {
    private val bucketCmp = bucket[BucketComponent.mapper]!!

    override fun update(deltaTime: Float) {
        forceSort()
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        batch.projectionMatrix = camera.combined
        // draw all entities in one batch
        batch.use {
            super.update(deltaTime)
            font.draw(batch, "Drops Collected: ${bucketCmp.dropsGathered}", 0f, 480f)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity[TransformComponent.mapper]?.let { transform ->
            entity[RenderComponent.mapper]?.let { render ->
                batch.draw(render.sprite, transform.bounds.x, transform.bounds.y)
            }
        }
    }
}