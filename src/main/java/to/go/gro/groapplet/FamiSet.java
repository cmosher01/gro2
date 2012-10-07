package to.go.gro.groapplet;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FamiSet
{
	private final List<Fami> mrFami = new ArrayList<>();

	public void add(Fami fami)
	{
		this.mrFami.add(fami);
	}

	public void init(Graphics g)
	{
		for (Iterator<Fami> i = this.mrFami.iterator(); i.hasNext();)
		{
			Fami fami = i.next();
			fami.calc(g);
		}
	}

	public void paint(Graphics g)
	{
		Rectangle2D clip = g.getClipBounds();

		for (Iterator<Fami> i = this.mrFami.iterator(); i.hasNext();)
		{
			Fami fami = i.next();

			if (fami.sect(clip))
				fami.paint(g);
		}
	}
}
