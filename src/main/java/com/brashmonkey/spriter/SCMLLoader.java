package com.brashmonkey.spriter;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.SCMLLoader.SCMLProjectParameters;

/**
 * Undocumented :(
 * <p>
 * Created on 2017-01-16.
 *
 * @author Alexander Winter
 */
public class SCMLLoader extends SynchronousAssetLoader<SCMLProject, SCMLProjectParameters>
{
	private SCMLReader reader = new SCMLReader();

	public SCMLLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public SCMLProject load(AssetManager assetManager, String fileName, FileHandle file, SCMLProjectParameters params)
	{
		reader.setAtlas(assetManager.get(params.textureAtlasName));
		return reader.load(file.read());
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SCMLProjectParameters params)
	{
		AssetDescriptor descriptor = new AssetDescriptor<>(params.textureAtlasName, TextureAtlas.class);
		Array<AssetDescriptor> array = new Array<>();
		array.add(descriptor);
		return array;
	}

	public static class SCMLProjectParameters extends AssetLoaderParameters<SCMLProject>
	{
		public SCMLProjectParameters(String textureAtlasName)
		{
			this.textureAtlasName = textureAtlasName;
		}

		public String textureAtlasName;
	}
}
