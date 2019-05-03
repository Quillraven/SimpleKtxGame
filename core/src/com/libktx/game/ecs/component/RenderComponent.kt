package com.libktx.game.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.mapperFor

class RenderComponent : Component {
    companion object {
        val mapper = mapperFor<RenderComponent>()
    }

    val sprite = Sprite()
    var z = 0
}