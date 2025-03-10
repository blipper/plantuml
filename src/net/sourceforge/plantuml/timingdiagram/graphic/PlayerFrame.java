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
 */
package net.sourceforge.plantuml.timingdiagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class PlayerFrame {

	private final ISkinParam skinParam;
	private final TextBlock title;

	public PlayerFrame(TextBlock title, ISkinParam skinParam) {
		this.title = title;
		this.skinParam = skinParam;
	}

	private StyleSignature getStyleSignature() {
		return StyleSignature.of(SName.root, SName.element, SName.timingDiagram);
	}

	private HColor getLineColor() {
		if (UseStyle.useBetaStyle() == false)
			return HColorUtils.BLACK;

		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return style.value(PName.LineColor).asColor(skinParam.getThemeStyle(), skinParam.getIHtmlColorSet());
	}

	private UStroke getUStroke() {
		if (UseStyle.useBetaStyle() == false)
			return new UStroke(2.0);
		final Style style = getStyleSignature().getMergedStyle(skinParam.getCurrentStyleBuilder());
		return style.getStroke();
	}

	public void drawFrameTitle(UGraphic ug) {
		title.drawU(ug);
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		ug = ug.apply(getLineColor()).apply(getUStroke());
		final double widthTmp = dimTitle.getWidth() + 1;
		final double height = title.calculateDimension(ug.getStringBounder()).getHeight() + 1;
		drawLine(ug, -TimingDiagram.marginX1, height, widthTmp, height, widthTmp + 10, 0);
	}

	private void drawLine(UGraphic ug, double... coord) {
		for (int i = 0; i < coord.length - 2; i += 2) {
			final double x1 = coord[i];
			final double y1 = coord[i + 1];
			final double x2 = coord[i + 2];
			final double y2 = coord[i + 3];
			ug.apply(new UTranslate(x1, y1)).draw(new ULine(x2 - x1, y2 - y1));
		}
	}

}
