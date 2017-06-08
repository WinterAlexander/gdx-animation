package com.brashmonkey.spriter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brashmonkey.spriter.math.Curve;
import com.brashmonkey.spriter.math.Curve.CurveType;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class parses a SCML file and creates a {@link SCMLProject} instance. If you want to keep track of what is going
 * on during the build process of the objects parsed from the SCML file, you could extend this class and override the
 * load*() methods for pre or post processing. This could be e.g. useful for a loading screen which responds to the
 * current building or parsing state.
 *
 * @author Trixt0r
 */
public class SCMLReader
{
	private TextureAtlas atlas;
	private SCMLProject currentProject;

	/**
	 * Creates a new SCML reader
	 */
	public SCMLReader() {}

	/**
	 * Parses the SCML object save in the given xml string and returns the build data object.
	 *
	 * @param xml the xml string
	 * @return the built data
	 */
	public SCMLProject load(String xml)
	{
		XmlReader reader = new XmlReader();
		return load(reader.parse(xml));
	}

	/**
	 * Parses the SCML objects saved in the given stream and returns the built data object.
	 *
	 * @param stream the stream from the SCML file
	 * @return the built data
	 */
	public SCMLProject load(InputStream stream)
	{
		try
		{
			XmlReader reader = new XmlReader();
			return load(reader.parse(stream));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads the data from the given root element, i.e. the spriter_data node.
	 *
	 * @param root XML root of the SCML file
	 * @return the project file
	 */
	public SCMLProject load(Element root)
	{
		this.currentProject = new SCMLProject();

		loadAssets(root.getChildrenByName("folder"));
		loadEntities(root.getChildrenByName("entity"));

		return currentProject;
	}

	/**
	 * Iterates through the given folders and adds them to the current {@link SCMLProject} object.
	 *
	 * @param folders a list of folders to load
	 */
	private void loadAssets(Array<Element> folders)
	{
		for(Element folder : folders)
		{
			for(Element file : folder.getChildrenByName("file"))
			{
				String name = file.get("name");

				String[] parts = name.split("/");
				name = parts[parts.length - 1].replace(".png", "");

				SpriterAsset asset = new SpriterAsset(
						atlas.findRegion(name),
						name,
						file.getFloat("pivot_x", 0f),
						file.getFloat("pivot_y", 1f));

				currentProject.putAsset(folder.getInt("id"), file.getInt("id"), asset);
			}
		}
	}

	/**
	 * Iterates through the given entities and adds them to the current {@link SCMLProject} object.
	 *
	 * @param entities a list of entities to load
	 */
	private void loadEntities(Array<Element> entities)
	{
		for(Element xmlElement : entities)
		{
			SpriterEntity entity = new SpriterEntity(xmlElement.get("name"));
			currentProject.getSourceEntities().add(entity);

			loadAnimations(xmlElement.getChildrenByName("animation"), entity);
		}
	}

	/**
	 * Iterates through the given animations and adds them to the given {@link SpriterEntity} object.
	 *
	 * @param animations a list of animations to load
	 * @param entity the entity containing the animations maps
	 */
	private void loadAnimations(Array<Element> animations, SpriterEntity entity)
	{
		for(Element xmlElement : animations)
		{
			Array<Element> timelines = xmlElement.getChildrenByName("timeline");
			Element mainline = xmlElement.getChildByName("mainline");

			Array<Element> mainlineKeys = mainline.getChildrenByName("key");
			Animation animation = new Animation(
					new Mainline(mainlineKeys.size),
					xmlElement.get("name"),
					xmlElement.getInt("length"),
					xmlElement.getBoolean("looping", true),
					timelines.size);

			entity.getAnimations().add(animation);
			loadMainlineKeys(mainlineKeys, animation.getMainline());
			loadTimelines(timelines, animation);
			animation.prepare();
		}
	}

	/**
	 * Iterates through the given mainline keys and adds them to the given {@link Mainline} object.
	 *
	 * @param keys a list of mainline keys
	 * @param main the mainline
	 */
	private void loadMainlineKeys(Array<Element> keys, Mainline main)
	{
		for(Element xmlElement : keys)
		{
			Array<Element> objectRefs = xmlElement.getChildrenByName("object_ref");
			Array<Element> boneRefs = xmlElement.getChildrenByName("bone_ref");

			Curve curve = new Curve(CurveType.valueOf(xmlElement.get("curve_type", "linear").toUpperCase()));
			curve.constraints.set(xmlElement.getFloat("c1", 0f), xmlElement.getFloat("c2", 0f), xmlElement.getFloat("c3", 0f), xmlElement.getFloat("c4", 0f));

			MainlineKey key = new MainlineKey(xmlElement.getInt("time", 0), curve, boneRefs.size, objectRefs.size);
			main.getKeys().add(key);
			loadRefs(objectRefs, boneRefs, key);
		}
	}

	/**
	 * Iterates through the given bone and object references and adds them to the given {@link MainlineKey} object.
	 *
	 * @param objectRefs a list of object references
	 * @param boneRefs a list if bone references
	 * @param key the mainline key
	 */
	private void loadRefs(Array<Element> objectRefs, Array<Element> boneRefs, MainlineKey key)
	{
		for(Element xmlElement : boneRefs)
			key.boneRefs.add(
					new BoneRef(
							xmlElement.getInt("timeline"),
							xmlElement.getInt("key"),
							key.getBoneRef(xmlElement.getInt("parent", -1))));


		for(Element xmlElement : objectRefs)
			key.objectRefs.add(
					new ObjectRef(
							xmlElement.getInt("timeline"),
							xmlElement.getInt("key"),
							key.getBoneRef(xmlElement.getInt("parent", -1)),
							xmlElement.getInt("z_index", 0)));

		key.objectRefs.sort();
	}

	/**
	 * Iterates through the given timelines and adds them to the given {@link Animation} object.
	 *
	 * @param timelines a list of timelines
	 * @param animation the animation containing the timelines
	 */
	private void loadTimelines(Array<Element> timelines, Animation animation)
	{
		for(Element xmlElement : timelines)
		{
			Array<Element> keys = xmlElement.getChildrenByName("key");

			Timeline timeline = new Timeline(xmlElement.get("name"), keys.size);

			loadTimelineKeys(keys, timeline);
			animation.getTimelines().add(timeline);
		}
	}

	/**
	 * Iterates through the given timeline keys and adds them to the given {@link Timeline} object.
	 *
	 * @param keys a list if timeline keys
	 * @param timeline the timeline containing the keys
	 */
	private void loadTimelineKeys(Array<Element> keys, Timeline timeline)
	{
		for(Element xmlKey : keys)
		{
			Curve curve = new Curve(CurveType.valueOf(xmlKey.get("curve_type", "linear").toUpperCase()));
			curve.constraints.set(xmlKey.getFloat("c1", 0f), xmlKey.getFloat("c2", 0f), xmlKey.getFloat("c3", 0f), xmlKey.getFloat("c4", 0f));

			TimelineKey key = new TimelineKey(xmlKey.getInt("time", 0), xmlKey.getInt("spin", 1), curve);
			Element obj = xmlKey.getChild(0); //each key tag contains a single object or bone tag
			String type = obj.getName();

			Vector2 position = new Vector2(obj.getFloat("x", 0f), obj.getFloat("y", 0f));
			Vector2 scale = new Vector2(obj.getFloat("scale_x", 1f), obj.getFloat("scale_y", 1f));

			float angle = obj.getFloat("angle", 0f);

			if(type.equalsIgnoreCase("object") || type.equalsIgnoreCase("sprite")) //TODO check which is valid
			{
				SpriterAsset asset = currentProject.getAsset(obj.getInt("folder"), obj.getInt("file")); //corresponding sprite

				float alpha = obj.getFloat("a", 1f);
				Vector2 pivot = new Vector2(obj.getFloat("pivot_x", asset.getPivotX()), obj.getFloat("pivot_y", asset.getPivotY()));

				key.setObject(new SpriterSprite(asset, position, scale, pivot, angle, alpha));
			}
			else if(type.equalsIgnoreCase("bone"))
				key.setObject(new SpriterBone(position, scale, new Vector2(obj.getFloat("pivot_x", 0f), obj.getFloat("pivot_y", 0.5f)), angle));


			timeline.getKeys().add(key);
		}
	}

	public TextureAtlas getAtlas()
	{
		return atlas;
	}

	public void setAtlas(TextureAtlas atlas)
	{
		this.atlas = atlas;
	}
}

