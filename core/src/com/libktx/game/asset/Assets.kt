package com.libktx.game.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.getAsset
import ktx.assets.load

enum class AtlasAssets {
    Images;

    private val path = "images/${name.toLowerCase()}.atlas"

    fun load(assets: AssetManager) = assets.load<TextureAtlas>(path)
    operator fun invoke(assets: AssetManager) = assets.getAsset<TextureAtlas>(path)
}

enum class SoundAssets {
    Drop;

    private val path = "sounds/${name.toLowerCase()}.wav"

    fun load(assets: AssetManager) = assets.load<Sound>(path)
    operator fun invoke(assets: AssetManager) = assets.getAsset<Sound>(path)
}

enum class MusicAssets {
    Rain;

    private val path = "music/${name.toLowerCase()}.mp3"

    fun load(assets: AssetManager) = assets.load<Music>(path)
    operator fun invoke(assets: AssetManager) = assets.getAsset<Music>(path)
}