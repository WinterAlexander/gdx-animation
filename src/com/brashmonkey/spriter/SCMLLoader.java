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
 * Created by Alexander Winter on 2017-01-16.
 */
public class SCMLLoader extends SynchronousAssetLoader<SCMLProject, SCMLProjectParameters>
{
	private static final String TEXTURE_ALTAS = "gfx/spriter/spriter.atlas";
	private SCMLReader reader = new SCMLReader();

	public SCMLLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public SCMLProject load(AssetManager assetManager, String fileName, FileHandle file, SCMLProjectParameters parameter)
	{
		reader.setAtlas(assetManager.get(TEXTURE_ALTAS));
		return reader.load(file.read());
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SCMLProjectParameters parameter)
	{
		AssetDescriptor descriptor = new AssetDescriptor<>(TEXTURE_ALTAS, TextureAtlas.class);
		Array<AssetDescriptor> array = new Array<>();
		array.add(descriptor);
		return array;
	}

	public static class SCMLProjectParameters extends AssetLoaderParameters<SCMLProject>
	{

	}
}
