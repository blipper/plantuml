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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolCollections extends USymbol {

	private final SkinParameter skinParameter;
	// private final HorizontalAlignment stereotypeAlignement;

	public USymbolCollections(SkinParameter skinParameter) {
		this.skinParameter = skinParameter;
		// this.stereotypeAlignement = stereotypeAlignement;
	}

//	@Override
//	public USymbol withStereoAlignment(HorizontalAlignment alignment) {
//		return new USymbolCollections(skinParameter, alignment);
//	}

	@Override
	public SkinParameter getSkinParameter() {
		return skinParameter;
	}
	
	@Override
	public SName getSName() {
		return SName.collections;
	}


	private void drawCollections(UGraphic ug, double width, double height, double shadowing, double roundCorner) {
		final URectangle small = new URectangle(width - getDeltaCollection(), height - getDeltaCollection())
				.rounded(roundCorner);
		small.setDeltaShadow(shadowing);

		ug.apply(new UTranslate(getDeltaCollection(), getDeltaCollection())).draw(small);
		small.setDeltaShadow(0);
		ug.apply(UTranslate.dy(0)).draw(small);
	}

	private Margin getMargin() {
		return new Margin(10, 10, 10, 10);
	}

	private double getDeltaCollection() {
		return 4;
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, dim);
				ug = symbolContext.apply(ug);
				drawCollections(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow(),
						symbolContext.getRoundCorner());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, stereoAlignment);
				tb.drawU(ug.apply(new UTranslate(margin.getX1() - getDeltaCollection() / 2,
						margin.getY1() - getDeltaCollection() / 2)));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final SymbolContext symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawCollections(ug, dim.getWidth(), dim.getHeight(), symbolContext.getDeltaShadow(),
						symbolContext.getRoundCorner());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereoX;
				final double posStereoY;
				if (stereoAlignment == HorizontalAlignment.RIGHT) {
					posStereoX = width - dimStereo.getWidth() - getMargin().getX1() / 2;
					posStereoY = getMargin().getY1() / 2;
				} else {
					posStereoX = (width - dimStereo.getWidth()) / 2;
					posStereoY = 2;
				}
				stereotype.drawU(ug.apply(new UTranslate(posStereoX, posStereoY)));
				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 2 + dimStereo.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

}