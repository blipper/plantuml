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
package net.sourceforge.plantuml.salt;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.ScaleSimple;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.api.ThemeStyle;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandFactorySprite;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.factory.AbstractElementFactoryComplex;
import net.sourceforge.plantuml.salt.factory.ElementFactory;
import net.sourceforge.plantuml.salt.factory.ElementFactoryBorder;
import net.sourceforge.plantuml.salt.factory.ElementFactoryButton;
import net.sourceforge.plantuml.salt.factory.ElementFactoryCheckboxOff;
import net.sourceforge.plantuml.salt.factory.ElementFactoryCheckboxOn;
import net.sourceforge.plantuml.salt.factory.ElementFactoryDroplist;
import net.sourceforge.plantuml.salt.factory.ElementFactoryImage;
import net.sourceforge.plantuml.salt.factory.ElementFactoryLine;
import net.sourceforge.plantuml.salt.factory.ElementFactoryMenu;
import net.sourceforge.plantuml.salt.factory.ElementFactoryPyramid;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRadioOff;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRadioOn;
import net.sourceforge.plantuml.salt.factory.ElementFactoryRetrieveFromDictonnary;
import net.sourceforge.plantuml.salt.factory.ElementFactoryScroll;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTab;
import net.sourceforge.plantuml.salt.factory.ElementFactoryText;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTextField;
import net.sourceforge.plantuml.salt.factory.ElementFactoryTree;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

public class PSystemSalt extends TitledDiagram implements WithSprite {

	private final List<String> data;
	private final Dictionary dictionary = new Dictionary();

	@Deprecated
	public PSystemSalt(ThemeStyle style, UmlSource source, List<String> data) {
		super(style, source, UmlDiagramType.SALT);
		this.data = data;
	}

	public PSystemSalt(ThemeStyle style, UmlSource source) {
		this(style, source, new ArrayList<String>());
	}

	public void add(String s) {
		data.add(s);
	}

	@Override
	final protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		try {
			final Element salt = createElement(manageSprite());
			final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(getSkinParam());
			final Dimension2D size = salt.getPreferredDimension(stringBounder, 0, 0);
			return createImageBuilder(fileFormatOption).drawable(getTextBlock(salt, size)).write(os);
		} catch (Exception e) {
			e.printStackTrace();
			UmlDiagram.exportDiagramError(os, e, fileFormatOption, seed(), getMetadata(), "none",
					new ArrayList<String>());
			return ImageDataSimple.error();
		}
	}

	private TextBlockBackcolored getTextBlock(final Element salt, final Dimension2D size) {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				ug = ug.apply(getBlack());
				salt.drawU(ug, 0, new Dimension2DDouble(size.getWidth(), size.getHeight()));
				salt.drawU(ug, 1, new Dimension2DDouble(size.getWidth(), size.getHeight()));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return size;
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public HColor getBackcolor() {
				return getSkinParam().getBackgroundColor();
			}
		};
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Salt)");
	}

	public void addSprite(String name, Sprite sprite) {
		dictionary.addSprite(name, sprite);
	}

	private List<String> manageSprite() {

		final Command<WithSprite> cmd = new CommandFactorySprite().createMultiLine(false);

		final List<String> result = new ArrayList<>();
		for (Iterator<String> it = data.iterator(); it.hasNext();) {
			String s = it.next();
			if (s.equals("hide stereotype")) {
				// System.err.println("skipping " + s);
			} else if (s.startsWith("skinparam ")) {
				// System.err.println("skipping " + s);
			} else if (s.startsWith("scale ")) {
				final double scale = Double.parseDouble(s.substring("scale ".length()));
				this.setScale(new ScaleSimple(scale));
				// System.err.println("skipping " + s);
			} else if (s.startsWith("sprite $")) {
				BlocLines bloc = BlocLines.singleString(s);
				do {
					s = it.next();
					bloc = bloc.addString(s);
				} while (s.equals("}") == false);
				try {
					final CommandExecutionResult cmdResult = cmd.execute(this, bloc);
				} catch (NoSuchColorException e) {
				}
			} else {
				result.add(s);
			}
		}
		return result;
	}

	private Element createElement(List<String> data) {

		final DataSourceImpl source = new DataSourceImpl(data);

		final Collection<AbstractElementFactoryComplex> cpx = new ArrayList<>();

		// cpx.add(new ElementFactorySimpleFrame(source, dictionnary));
		cpx.add(new ElementFactoryPyramid(source, dictionary));
		cpx.add(new ElementFactoryScroll(source, dictionary));
		cpx.add(new ElementFactoryBorder(source, dictionary));

		for (AbstractElementFactoryComplex f : cpx) {
			addSimpleFactory(f, source, dictionary);
		}
		for (AbstractElementFactoryComplex f1 : cpx) {
			for (AbstractElementFactoryComplex f2 : cpx) {
				f1.addFactory(f2);
			}
		}

		for (ElementFactory f : cpx) {
			if (f.ready()) {
				Log.info("Using " + f);
				return f.create().getElement();
			}
		}

		Log.println("data=" + data);
		throw new IllegalArgumentException();

	}

	private static void addSimpleFactory(final AbstractElementFactoryComplex cpxFactory, final DataSource source,
			Dictionary dictionary) {
		cpxFactory.addFactory(new ElementFactoryMenu(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryTree(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryTab(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryLine(source));
		cpxFactory.addFactory(new ElementFactoryTextField(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryButton(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryDroplist(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryRadioOn(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryRadioOff(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryCheckboxOn(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryCheckboxOff(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryImage(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryRetrieveFromDictonnary(source, dictionary));
		cpxFactory.addFactory(new ElementFactoryText(source, dictionary));
	}

	private boolean iamSalt;

	public void setIamSalt(boolean iamSalt) {
		this.iamSalt = true;
	}

	public final boolean isIamSalt() {
		return iamSalt;
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(5);
	}

	private HColor getBlack() {
		return HColorUtils.BLACK;
	}
}
