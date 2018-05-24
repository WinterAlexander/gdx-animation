package me.winter.gdx.animation.drawable;

import com.badlogic.gdx.graphics.g2d.Batch;
import me.winter.gdx.animation.Sprite;

/**
 * Something that can draw itself given a Sprite.
 * <p>
 * Created on 2018-05-24.
 *
 * @author Alexander Winter
 */
public interface SpriteDrawable
{
	void draw(Sprite sprite, Batch batch);
}
