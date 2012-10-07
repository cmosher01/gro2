package nu.mine.mosher.gro2;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class IndiSet
{
	private final List<Indi> mrIndi = new ArrayList<>();

	private int mMaxWidth;



	public void setMaxWidth(int maxWidth)
	{
		this.mMaxWidth = maxWidth;
	}

	public void add(Indi indi)
	{
		this.mrIndi.add(indi);
	}

	public Rectangle init(Graphics g)
	{
		Rectangle bounds = new Rectangle();

		for (Iterator<Indi> i = this.mrIndi.iterator(); i.hasNext();)
		{
			Indi indi = i.next();

			Rectangle boundsIndi = indi.calc(g, this.mMaxWidth);
			bounds.add(boundsIndi);
		}

		return bounds;
	}

	public void paint(Graphics g)
	{
		RectangularShape clip = g.getClipBounds();

		for (Iterator<Indi> i = this.mrIndi.iterator(); i.hasNext();)
		{
			Indi indi = i.next();

			if (indi.sect(clip))
				indi.paint(g);
		}
	}

	public Indi isOnIndi(Point point)
	{
		for (Iterator<Indi> i = this.mrIndi.iterator(); i.hasNext();)
		{
			Indi indi = i.next();

			if (indi.isOn(point))
				return indi;
		}

		return null;
	}
}
