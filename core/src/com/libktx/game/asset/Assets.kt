package com.libktx.game.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.load

enum class AtlasAssets {
    Images;

    internal val path = "images/${name.toLowerCase()}.atlas"
}

fun AssetManager.load(asset: AtlasAssets) = load<TextureAtlas>(asset.path)
operator fun AssetManager.get(asset: AtlasAssets): TextureAtlas = get(asset.path)

enum class SoundAssets {
    Drop;

    internal val path = "sounds/${name.toLowerCase()}.wav"
}

fun AssetManager.load(asset: SoundAssets) = load<Sound>(asset.path)
operator fun AssetManager.get(asset: SoundAssets): Sound = get(asset.path)

enum class MusicAssets {
    Rain;

    internal val path = "music/${name.toLowerCase()}.mp3"
}

fun AssetManager.load(asset: MusicAssets) = load<Music>(asset.path)
operator fun AssetManager.get(asset: MusicAssets): Music = get(asset.path)