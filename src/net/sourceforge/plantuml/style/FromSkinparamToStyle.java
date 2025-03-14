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
package net.sourceforge.plantuml.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FromSkinparamToStyle {

	static class Data {
		final private PName propertyName;
		final private SName[] styleNames;

		Data(PName propertyName, SName[] styleNames) {
			this.propertyName = propertyName;
			this.styleNames = styleNames;
		}
	}

	private static final Map<String, List<Data>> knowlegde = new HashMap<String, List<Data>>();

	static {
		addConvert("participantClickableBackgroundColor", PName.BackGroundColor, SName.participant, SName.clickable);
		addConvert("participantClickableBorderColor", PName.LineColor, SName.participant, SName.clickable);
		addMagic(SName.participant);

		addMagic(SName.boundary);
		addMagic(SName.control);
		addMagic(SName.collections);
		addMagic(SName.actor);
		addMagic(SName.database);
		addMagic(SName.entity);

		addConFont("header", SName.header);
		addConFont("footer", SName.footer);

		addConvert("defaultFontSize", PName.FontSize, SName.element);

		addConvert("sequenceStereotypeFontSize", PName.FontSize, SName.stereotype);
		addConvert("sequenceStereotypeFontStyle", PName.FontStyle, SName.stereotype);
		addConvert("sequenceStereotypeFontColor", PName.FontColor, SName.stereotype);
		addConvert("sequenceStereotypeFontName", PName.FontName, SName.stereotype);
		addConvert("SequenceReferenceBorderColor", PName.LineColor, SName.reference);
		addConvert("SequenceReferenceBorderColor", PName.LineColor, SName.referenceHeader);
		addConvert("SequenceReferenceBackgroundColor", PName.BackGroundColor, SName.reference);
		addConvert("sequenceReferenceHeaderBackgroundColor", PName.BackGroundColor, SName.referenceHeader);
		addConFont("sequenceReference", SName.reference);
		addConFont("sequenceReference", SName.referenceHeader);
		addConvert("sequenceGroupBorderThickness", PName.LineThickness, SName.group);
		addConvert("SequenceGroupBorderColor", PName.LineColor, SName.group);
		addConvert("SequenceGroupBorderColor", PName.LineColor, SName.groupHeader);
		addConvert("SequenceGroupBackgroundColor", PName.BackGroundColor, SName.groupHeader);
		addConFont("SequenceGroup", SName.group);
		addConFont("SequenceGroupHeader", SName.groupHeader);
		addConvert("SequenceBoxBorderColor", PName.LineColor, SName.box);
		addConvert("SequenceBoxBackgroundColor", PName.BackGroundColor, SName.box);
		addConvert("SequenceLifeLineBorderColor", PName.LineColor, SName.lifeLine);
		addConvert("SequenceLifeLineBackgroundColor", PName.BackGroundColor, SName.lifeLine);
		addConvert("sequenceDividerBackgroundColor", PName.BackGroundColor, SName.separator);
		addConvert("sequenceDividerBorderColor", PName.LineColor, SName.separator);
		addConFont("sequenceDivider", SName.separator);
		addConvert("sequenceDividerBorderThickness", PName.LineThickness, SName.separator);
		addConvert("SequenceMessageAlignment", PName.HorizontalAlignment, SName.arrow);

		addConFont("note", SName.note);
		addConvert("noteBorderThickness", PName.LineThickness, SName.note);
		addConvert("noteBackgroundColor", PName.BackGroundColor, SName.note);

		addConvert("packageBackgroundColor", PName.BackGroundColor, SName.group);
		addConvert("packageBorderColor", PName.LineColor, SName.group);
		addMagic(SName.package_);

		addConvert("PartitionBorderColor", PName.LineColor, SName.partition);
		addConvert("PartitionBackgroundColor", PName.BackGroundColor, SName.partition);
		addConFont("Partition", SName.partition);

		addConvert("hyperlinkColor", PName.HyperLinkColor, SName.root);

		addConvert("activityStartColor", PName.BackGroundColor, SName.circle, SName.start);
		addConvert("activityEndColor", PName.LineColor, SName.circle, SName.end);
		addConvert("activityStopColor", PName.LineColor, SName.circle, SName.stop);
		addConvert("activityBarColor", PName.LineColor, SName.activityBar);
		addConvert("activityBorderColor", PName.LineColor, SName.activity);
		addConvert("activityBorderThickness", PName.LineThickness, SName.activity);
		addConvert("activityBackgroundColor", PName.BackGroundColor, SName.activity);
		addConFont("activity", SName.activity);
		addConvert("activityDiamondBackgroundColor", PName.BackGroundColor, SName.diamond);
		addConvert("activityDiamondBorderColor", PName.LineColor, SName.diamond);
		addConFont("activityDiamond", SName.diamond);

		addConFont("arrow", SName.arrow);
		addConvert("arrowThickness", PName.LineThickness, SName.arrow);
		addConvert("arrowColor", PName.LineColor, SName.arrow);
		addConvert("arrowStyle", PName.LineStyle, SName.arrow);

		addConvert("defaulttextalignment", PName.HorizontalAlignment, SName.root);
		addConvert("defaultFontName", PName.FontName, SName.root);
		addConvert("defaultFontColor", PName.FontColor, SName.root);

		addConFont("SwimlaneTitle", SName.swimlane);
		addConvert("SwimlaneTitleBackgroundColor", PName.BackGroundColor, SName.swimlane);
		addConvert("SwimlaneBorderColor", PName.LineColor, SName.swimlane);
		addConvert("SwimlaneBorderThickness", PName.LineThickness, SName.swimlane);

		addConvert("roundCorner", PName.RoundCorner, SName.root);

		addConvert("titleBorderThickness", PName.LineThickness, SName.title);
		addConvert("titleBorderColor", PName.LineColor, SName.title);
		addConvert("titleBackgroundColor", PName.BackGroundColor, SName.title);
		addConvert("titleBorderRoundCorner", PName.RoundCorner, SName.title);
		addConFont("title", SName.title);

		addConvert("legendBorderThickness", PName.LineThickness, SName.legend);
		addConvert("legendBorderColor", PName.LineColor, SName.legend);
		addConvert("legendBackgroundColor", PName.BackGroundColor, SName.legend);
		addConvert("legendBorderRoundCorner", PName.RoundCorner, SName.legend);
		addConFont("legend", SName.legend);

		addConvert("noteTextAlignment", PName.HorizontalAlignment, SName.note);

		addConvert("BackgroundColor", PName.BackGroundColor, SName.document);

		addConvert("classBackgroundColor", PName.BackGroundColor, SName.class_);
		addConvert("classBorderColor", PName.LineColor, SName.class_);
		addConFont("class", SName.class_);
		addConFont("classAttribute", SName.class_);
		addConvert("classBorderThickness", PName.LineThickness, SName.class_);

		addConvert("objectBackgroundColor", PName.BackGroundColor, SName.object);
		addConvert("objectBorderColor", PName.LineColor, SName.object);
		addConFont("object", SName.object);
		addConFont("objectAttribute", SName.object);
		addConvert("objectBorderThickness", PName.LineThickness, SName.object);

		addConvert("stateBackgroundColor", PName.BackGroundColor, SName.state);
		addConvert("stateBorderColor", PName.LineColor, SName.state);
		addConFont("state", SName.state);
		addConFont("stateAttribute", SName.state);
		addConvert("stateBorderThickness", PName.LineThickness, SName.state);

		addMagic(SName.agent);
		addMagic(SName.artifact);
		addMagic(SName.card);
		addMagic(SName.interface_);
		addMagic(SName.cloud);
		addMagic(SName.component);
		addMagic(SName.file);
		addMagic(SName.folder);
		addMagic(SName.frame);
		addMagic(SName.hexagon);
		addMagic(SName.node);
		addMagic(SName.person);
		addMagic(SName.queue);
		addMagic(SName.rectangle);
		addMagic(SName.stack);
		addMagic(SName.storage);
		addMagic(SName.usecase);
		addMagic(SName.map);

	}

	private static void addMagic(SName sname) {
		final String cleanName = sname.name().replace("_", "");
		addConvert(cleanName + "BackgroundColor", PName.BackGroundColor, sname);
		addConvert(cleanName + "BorderColor", PName.LineColor, sname);
		addConvert(cleanName + "BorderThickness", PName.LineThickness, sname);
		addConFont(cleanName, sname);
	}

	private final List<Style> styles = new ArrayList<>();
	private String stereo = null;

	public FromSkinparamToStyle(String key, String value, final AutomaticCounter counter) {
		if (value.equals("right:right")) {
			value = "right";
		}
		if (key.contains("<<")) {
			final StringTokenizer st = new StringTokenizer(key, "<>");
			key = st.nextToken();
			stereo = st.nextToken();
		}

		final List<Data> datas = knowlegde.get(key.toLowerCase());

		if (datas != null) {
			for (Data data : datas) {
				addStyle(data.propertyName, ValueImpl.regular(value, counter), data.styleNames);
			}
		} else if (key.equalsIgnoreCase("shadowing")) {
			addStyle(PName.Shadowing, getShadowingValue(value, counter), SName.root);
		} else if (key.equalsIgnoreCase("noteshadowing")) {
			addStyle(PName.Shadowing, getShadowingValue(value, counter), SName.root, SName.note);
		}
	}

	private ValueImpl getShadowingValue(final String value, final AutomaticCounter counter) {
		if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no")) {
			return ValueImpl.regular("0", counter);
		}
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) {
			return ValueImpl.regular("3", counter);
		}
		return ValueImpl.regular(value, counter);
	}

	private void addStyle(PName propertyName, Value value, SName... styleNames) {
		final Map<PName, Value> map = new EnumMap<PName, Value>(PName.class);
		map.put(propertyName, value);
		StyleSignature sig = StyleSignature.of(styleNames);
		if (stereo != null)
			for (String s : stereo.split("\\&"))
				sig = sig.add(s);

		styles.add(new Style(sig, map));
	}

	public List<Style> getStyles() {
		return Collections.unmodifiableList(styles);
	}

	private static void addConvert(String skinparam, PName propertyName, SName... styleNames) {
		skinparam = skinparam.toLowerCase();
		List<Data> datas = knowlegde.get(skinparam);
		if (datas == null) {
			datas = new ArrayList<>();
			knowlegde.put(skinparam, datas);
		}
		datas.add(new Data(propertyName, styleNames));
	}

	private static void addConFont(String skinparam, SName... styleNames) {
		addConvert(skinparam + "FontSize", PName.FontSize, styleNames);
		addConvert(skinparam + "FontStyle", PName.FontStyle, styleNames);
		addConvert(skinparam + "FontColor", PName.FontColor, styleNames);
		addConvert(skinparam + "FontName", PName.FontName, styleNames);
	}

}