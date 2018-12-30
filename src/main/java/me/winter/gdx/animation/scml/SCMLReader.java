package me.winter.gdx.animation.scml;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import me.winter.gdx.animation.AnimatedPart;
import me.winter.gdx.animation.Animation;
import me.winter.gdx.animation.Entity;
import me.winter.gdx.animation.Mainline;
import me.winter.gdx.animation.MainlineKey;
import me.winter.gdx.animation.ObjectRef;
import me.winter.gdx.animation.Sprite;
import me.winter.gdx.animation.SpriteTimeline;
import me.winter.gdx.animation.Timeline;
import me.winter.gdx.animation.TimelineKey;
import me.winter.gdx.animation.drawable.TextureSpriteDrawable;
import me.winter.gdx.animation.math.Curve;
import me.winter.gdx.animation.math.Curve.CurveType;

import java.io.InputStream;
import java.util.Locale;

/**
 * File parser for .SCML files (spriter format)
 *
 * @author Alexander Winter
 */
public class SCMLReader
{
	private TextureAtlas atlas;
	private SCMLProject currentProject;

	/**
	 * Since zIndex are for timeline but stored in a different section of the xml, they need to be temporarily mapped while loading
	 */
	private ObjectMap<Integer, Integer> zIndexTempMap = new ObjectMap<>();

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
		XmlReader reader = new XmlReader();
		return load(reader.parse(stream));
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

				TextureSpriteDrawable asset = new TextureSpriteDrawable(
						atlas.findRegion(name),
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
			Entity entity = new Entity(xmlElement.get("name"));

			loadAnimations(xmlElement.getChildrenByName("animation"), entity);

			currentProject.getSourceEntities().add(entity);
		}
	}

	/**
	 * Iterates through the given animations and adds them to the given {@link Entity} object.
	 *
	 * @param animations a list of animations to load
	 * @param entity the entity containing the animations maps
	 */
	private void loadAnimations(Array<Element> animations, Entity entity)
	{
		for(Element xmlElement : animations)
		{
			Array<Element> xmlTimelines = xmlElement.getChildrenByName("timeline");
			Element xmlMainline = xmlElement.getChildByName("mainline");

			Array<Element> mainlineKeys = xmlMainline.getChildrenByName("key");

			Mainline mainline = new Mainline(mainlineKeys.size);
			Array<Timeline> timelines = new Array<>(xmlTimelines.size);

			loadTimelines(mainlineKeys, xmlTimelines, mainline, timelines);

			//in spriter, you can place a key both at 0 and at the length for a total possible keys of length + 1,
			//to handle this, we assume the actual length is +1 the one displayed in spriter
			Animation animation = new Animation(xmlElement.get("name"),
					xmlElement.getInt("length") + 1,
					xmlElement.getBoolean("looping", true),
					mainline,
					timelines);

			entity.getAnimations().add(animation);
		}
	}

	/**
	 * Loads all the timelines of the animation and the mainline
	 * mainline contains information about the graph and zIndexes
	 *
	 * @param xmlMainlineKeys a list of mainline keys
	 * @param mainline the mainline
	 */
	private void loadTimelines(Array<Element> xmlMainlineKeys, Array<Element> xmlTimelines, Mainline mainline, Array<Timeline> timelines)
	{
		zIndexTempMap.clear();

		for(Element xmlElement : xmlMainlineKeys)
		{
			Array<Element> xmlObjectRefs = xmlElement.getChildrenByName("object_ref");
			Array<Element> xmlBoneRefs = xmlElement.getChildrenByName("bone_ref");

			Curve curve = new Curve(CurveType.valueOf(xmlElement.get("curve_type", "linear").toUpperCase(Locale.ENGLISH)));
			curve.constraints.set(xmlElement.getFloat("c1", 0f), xmlElement.getFloat("c2", 0f), xmlElement.getFloat("c3", 0f), xmlElement.getFloat("c4", 0f));

			Array<ObjectRef> objectRefs = new Array<>(xmlBoneRefs.size + xmlObjectRefs.size);

			for(Element xmlBoneRef : xmlBoneRefs)
			{
				int parentId = xmlBoneRef.getInt("parent", -1);
				ObjectRef parent = parentId != -1 ? objectRefs.get(parentId) : null;

				objectRefs.add(new ObjectRef(xmlBoneRef.getInt("timeline"), xmlBoneRef.getInt("key"), parent));
			}

			for(Element xmlObjectRef : xmlObjectRefs)
			{
				int parentId = xmlObjectRef.getInt("parent", -1);
				ObjectRef parent = parentId != -1 ? objectRefs.get(parentId) : null;

				int timeline = xmlObjectRef.getInt("timeline");

				objectRefs.add(new ObjectRef(timeline, xmlObjectRef.getInt("key"), parent));

				zIndexTempMap.put(timeline, xmlObjectRef.getInt("z_index", 0));
			}


			mainline.getKeys().add(new MainlineKey(xmlElement.getInt("time", 0), curve, objectRefs));
		}

		for(Element xmlElement : xmlTimelines)
		{
			Array<TimelineKey> timelineKeys = loadTimelineKeys(xmlElement.getChildrenByName("key"));

			int id = xmlElement.getInt("id");
			String name = xmlElement.get("name");

			if(timelineKeys.size == 0 || !(timelineKeys.get(0).getObject() instanceof Sprite))
				timelines.add(new Timeline(id, name, timelineKeys));
			else
			{
				int timelineId = xmlElement.getInt("id", -1);

				timelines.add(new SpriteTimeline(id, name, timelineKeys, zIndexTempMap.get(timelineId)));
			}

		}
	}
	/**
	 * Iterates through the given timeline keys
	 *
	 * @param keys a list if timeline keys as xml
	 *
	 * @return array of timeline keys
	 */
	private Array<TimelineKey> loadTimelineKeys(Array<Element> keys)
	{
		Array<TimelineKey> timelineKeys = new Array<>(keys.size);

		for(Element xmlKey : keys)
		{
			Curve curve = new Curve(CurveType.valueOf(xmlKey.get("curve_type", "linear").toUpperCase(Locale.ENGLISH)));
			curve.constraints.set(xmlKey.getFloat("c1", 0f), xmlKey.getFloat("c2", 0f), xmlKey.getFloat("c3", 0f), xmlKey.getFloat("c4", 0f));

			TimelineKey key = new TimelineKey(xmlKey.getInt("time", 0), xmlKey.getInt("spin", 1), curve);
			Element obj = xmlKey.getChild(0); //each key tag contains a single object or bone tag
			String type = obj.getName();

			Vector2 position = new Vector2(obj.getFloat("x", 0f), obj.getFloat("y", 0f));
			Vector2 scale = new Vector2(obj.getFloat("scale_x", 1f), obj.getFloat("scale_y", 1f));

			float angle = obj.getFloat("angle", 0f);

			if(type.equalsIgnoreCase("object") || type.equalsIgnoreCase("sprite"))
			{
				TextureSpriteDrawable asset = currentProject.getAsset(obj.getInt("folder"), obj.getInt("file")); //corresponding sprite

				float alpha = obj.getFloat("a", 1f);
				key.setObject(new Sprite(asset, position, scale, angle, alpha));
			}
			else if(type.equalsIgnoreCase("bone"))
				key.setObject(new AnimatedPart(position, scale, angle));


			timelineKeys.add(key);
		}

		return timelineKeys;
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

