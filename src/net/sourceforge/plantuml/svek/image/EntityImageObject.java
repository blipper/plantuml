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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UseStyle;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Ports;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.svek.WithPorts;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.UGroupType;
import net.sourceforge.plantuml.ugraphic.ULayoutGroup;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageObject extends AbstractEntityImage implements Stencil, WithPorts {

	final private TextBlock name;
	final private TextBlock stereo;
	final private TextBlock fields;
	final private Url url;
	final private double roundCorner;

	final private LineConfigurable lineConfig;

	public EntityImageObject(ILeaf entity, ISkinParam skinParam, PortionShower portionShower) {
		super(entity, skinParam);
		this.lineConfig = entity;
		final Stereotype stereotype = entity.getStereotype();
		this.roundCorner = skinParam.getRoundCorner(CornerParam.DEFAULT, null);

		final FontConfiguration fcHeader;
		if (UseStyle.useBetaStyle())
			fcHeader = getStyleHeader().getFontConfiguration(getSkinParam().getThemeStyle(),
					getSkinParam().getIHtmlColorSet());
		else
			fcHeader = new FontConfiguration(getSkinParam(), FontParam.OBJECT, stereotype);

		final TextBlock tmp = getUnderlinedName(entity).create(fcHeader, HorizontalAlignment.CENTER, skinParam);
		this.name = TextBlockUtils.withMargin(tmp, 2, 2);
		if (stereotype == null || stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR) == null
				|| portionShower.showPortion(EntityPortion.STEREOTYPE, entity) == false)
			this.stereo = null;
		else
			this.stereo = Display.create(stereotype.getLabels(skinParam.guillemet())).create(
					new FontConfiguration(getSkinParam(), FontParam.OBJECT_STEREOTYPE, stereotype),
					HorizontalAlignment.CENTER, skinParam);

		final boolean showFields = portionShower.showPortion(EntityPortion.FIELD, entity);

		if (entity.getBodier().getFieldsToDisplay().size() == 0)
			this.fields = new TextBlockLineBefore(getStyle().value(PName.LineThickness).asDouble(),
					new TextBlockEmpty(10, 16));
		else
			this.fields = entity.getBodier().getBody(FontParam.OBJECT_ATTRIBUTE, skinParam, false, showFields,
					entity.getStereotype(), getStyle(), null);

		this.url = entity.getUrl99();

	}

	private Style getStyle() {
		return StyleSignature.of(SName.root, SName.element, SName.objectDiagram, SName.object)
				.with(getEntity().getStereotype()).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	private Style getStyleHeader() {
		return StyleSignature.of(SName.root, SName.element, SName.objectDiagram, SName.object, SName.header)
				.with(getEntity().getStereotype()).getMergedStyle(getSkinParam().getCurrentStyleBuilder());
	}

	private Display getUnderlinedName(ILeaf entity) {
		if (getSkinParam().strictUmlStyle())
			return entity.getDisplay().underlinedName();

		return entity.getDisplay();
	}

	private int marginEmptyFieldsOrMethod = 13;

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimFields = fields.calculateDimension(stringBounder);
		double width = Math.max(dimFields.getWidth(), dimTitle.getWidth() + 2 * xMarginCircle);
		if (width < getSkinParam().minClassWidth())
			width = getSkinParam().minClassWidth();

		final double height = getMethodOrFieldHeight(dimFields) + dimTitle.getHeight();
		return new Dimension2DDouble(width, height);
	}

	final public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimTitle = getTitleDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal).rounded(roundCorner);

		final HColor borderColor;
		final UStroke stroke;
		HColor backcolor = getEntity().getColors().getColor(ColorType.BACK);

		if (UseStyle.useBetaStyle()) {
			final Style style = getStyle();
			borderColor = style.value(PName.LineColor).asColor(getSkinParam().getThemeStyle(),
					getSkinParam().getIHtmlColorSet());
			if (backcolor == null)
				backcolor = style.value(PName.BackGroundColor).asColor(getSkinParam().getThemeStyle(),
						getSkinParam().getIHtmlColorSet());
			rect.setDeltaShadow(style.value(PName.Shadowing).asDouble());
			stroke = style.getStroke();

		} else {
			if (getSkinParam().shadowing(getEntity().getStereotype()))
				rect.setDeltaShadow(4);
			borderColor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.objectBorder);
			if (backcolor == null)
				backcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.objectBackground);
			stroke = getStroke();

		}

		ug = ug.apply(borderColor).apply(backcolor.bg());

		if (url != null)
			ug.startUrl(url);


		Map<UGroupType, String> typeIDent = new EnumMap<>(UGroupType.class);;
		typeIDent.put(UGroupType.CLASS, "elem " + getEntity().getCode() + " selected");
		typeIDent.put(UGroupType.ID, "elem_" + getEntity().getCode());
		ug.startGroup(typeIDent);
		ug.apply(stroke).draw(rect);

		final ULayoutGroup header = new ULayoutGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		if (stereo != null)
			header.add(stereo);

		header.add(name);
		header.drawU(ug, dimTotal.getWidth(), dimTitle.getHeight());

		final UGraphic ug2 = UGraphicStencil.create(ug, this, stroke);
		fields.drawU(ug2.apply(UTranslate.dy(dimTitle.getHeight())));

		if (url != null)
			ug.closeUrl();

		ug.closeGroup();
	}

	private UStroke getStroke() {
		UStroke stroke = lineConfig.getColors().getSpecificLineStroke();
		if (stroke == null)
			stroke = getSkinParam().getThickness(LineParam.objectBorder, getStereo());

		if (stroke == null)
			stroke = new UStroke(1.5);

		return stroke;
	}

	private double getMethodOrFieldHeight(final Dimension2D dim) {
		final double fieldsHeight = dim.getHeight();
		if (fieldsHeight == 0)
			return marginEmptyFieldsOrMethod;

		return fieldsHeight;
	}

	private int xMarginCircle = 5;

	private Dimension2D getTitleDimension(StringBounder stringBounder) {
		return getNameAndSteretypeDimension(stringBounder);
	}

	private Dimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo == null ? new Dimension2DDouble(0, 0)
				: stereo.calculateDimension(stringBounder);
		final Dimension2D nameAndStereo = new Dimension2DDouble(Math.max(nameDim.getWidth(), stereoDim.getWidth()),
				nameDim.getHeight() + stereoDim.getHeight());
		return nameAndStereo;
	}

	public ShapeType getShapeType() {
		if (((ILeaf) getEntity()).getPortShortNames().size() > 0)
			return ShapeType.RECTANGLE_HTML_FOR_PORTS;

		return ShapeType.RECTANGLE;
	}

	public double getStartingX(StringBounder stringBounder, double y) {
		return 0;
	}

	public double getEndingX(StringBounder stringBounder, double y) {
		return calculateDimension(stringBounder).getWidth();
	}

	@Override
	public Ports getPorts(StringBounder stringBounder) {
		final Dimension2D dimHeader = getNameAndSteretypeDimension(stringBounder);
		if (fields instanceof WithPorts)
			return ((WithPorts) fields).getPorts(stringBounder).translateY(dimHeader.getHeight());
		return new Ports();
	}

}
