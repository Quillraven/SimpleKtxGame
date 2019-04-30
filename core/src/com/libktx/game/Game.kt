package com.libktx.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.libktx.game.screen.MainMenuScreen

class Game : com.badlogic.gdx.Game() {
    public lateinit var batch: SpriteBatch
    public lateinit var font: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        // use LibGDX's default Arial font
        font = BitmapFont()
        this.setScreen(MainMenuScreen(this))
    }

    override fun render() {
        super.render()  // important!
    }

    override fun dispose() {
        this.getScreen().dispose()

        batch.dispose()
        font.dispose()
    }
}
