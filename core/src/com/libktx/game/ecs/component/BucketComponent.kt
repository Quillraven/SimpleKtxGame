package com.libktx.game.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class BucketComponent : Component {
    companion object {
        val mapper = mapperFor<BucketComponent>()
    }

    var dropsGathered = 0
}