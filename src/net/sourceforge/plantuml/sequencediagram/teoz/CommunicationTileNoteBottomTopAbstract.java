/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public abstract class CommunicationTileNoteBottomTopAbstract extends AbstractTile {

	protected final Tile tile;
	protected final AbstractMessage message;
	protected final Rose skin;
	protected final ISkinParam skinParam;
	protected final Note noteOnMessage;

	final public Event getEvent() {
		return message;
	}

	@Override
	final public double getContactPointRelative() {
		return tile.getContactPointRelative();
	}

	public CommunicationTileNoteBottomTopAbstract(Tile tile, AbstractMessage message, Rose skin, ISkinParam skinParam,
			Note noteOnMessage) {
		super(((AbstractTile) tile).getStringBounder());
		this.tile = tile;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		this.noteOnMessage = noteOnMessage;
	}

	@Override
	final protected void callbackY_internal(double y) {
		tile.callbackY(y);
	}

	final protected Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponentNote(noteOnMessage.getUsedStyles(), ComponentType.NOTE,
				noteOnMessage.getSkinParamBackcolored(skinParam), noteOnMessage.getStrings());
		return comp;
	}

	final protected Real getNotePosition(StringBounder stringBounder) {
		final Real minX = tile.getMinX();
		return minX;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = new Area(dim.getWidth(), dim.getHeight());
		((UDrawable) tile).drawU(ug);

		final double middleMsg = (tile.getMinX().getCurrentValue() + tile.getMaxX().getCurrentValue()) / 2;

		final double xNote = getNotePosition(stringBounder).getCurrentValue();
		final double yNote = tile.getPreferredHeight();

		comp.drawU(ug.apply(new UTranslate(xNote, yNote + spacey)), area, (Context2D) ug);

		drawLine(ug, middleMsg, tile.getContactPointRelative(), xNote + dim.getWidth() / 2,
				yNote + spacey + Rose.paddingY);

	}

	protected final double spacey = 10;

	protected final void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		final HColor color;

		if (UseStyle.useBetaStyle()) {
			final Style style = StyleSignature.of(SName.root, SName.element, SName.sequenceDiagram)
					.getMergedStyle(skinParam.getCurrentStyleBuilder());
			color = style.value(PName.LineColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
		} else
			color = new Rose().getHtmlColor(skinParam, ColorParam.arrow);

		final double dx = x2 - x1;
		final double dy = y2 - y1;

		ug.apply(new UTranslate(x1, y1)).apply(color).apply(new UStroke(2, 2, 1)).draw(new ULine(dx, dy));

	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final Dimension2D dim = comp.getPreferredDimension(getStringBounder());
		return tile.getPreferredHeight() + dim.getHeight() + spacey;
	}

	public void addConstraints() {
		tile.addConstraints();
	}

	public Real getMinX() {
		return tile.getMinX();
	}

	public Real getMaxX() {
		return tile.getMaxX();
	}

}
