package com.winteralexander.gdx.animation.scml;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.animation.scml.SCMLLoader.SCMLProjectParameters;

/**
 * Loads a SCML file (Spriter format) into LibGDX's AssetManager
 * <p>
 * Created on 2017-01-16.
 *
 * @author Alexander Winter
 */
public class SCMLLoader extends SynchronousAssetLoader<SCMLProject, SCMLProjectParameters> {
	private final SCMLReader reader;

	public SCMLLoader(FileHandleResolver resolver) {
		this(resolver, new SCMLReader());
	}

	public SCMLLoader(FileHandleResolver resolver, SCMLReader reader) {
		super(resolver);
		this.reader = reader;
	}

	@Override
	public SCMLProject load(AssetManager assetManager, String fileName, FileHandle file,
	                        SCMLProjectParameters params) {
		reader.setAtlas(assetManager.get(params.textureAtlasName));
		return reader.load(file.read());
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
	                                              SCMLProjectParameters params) {
		AssetDescriptor<TextureAtlas> descriptor = new AssetDescriptor<>(params.textureAtlasName,
				TextureAtlas.class);
		Array<AssetDescriptor> array = new Array<>();
		array.add(descriptor);
		return array;
	}

	public static class SCMLProjectParameters extends AssetLoaderParameters<SCMLProject> {
		public String textureAtlasName;

		public SCMLProjectParameters(String textureAtlasName) {
			this.textureAtlasName = textureAtlasName;
		}
	}
}
