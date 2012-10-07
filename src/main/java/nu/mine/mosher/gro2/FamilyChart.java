package nu.mine.mosher.gro2;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;

public class FamilyChart extends JPanel
{
	private final Applet mApplet;
	private final IndiSet mIndis;
	private final FamiSet mFamis;
	private boolean mInitialized;

	public FamilyChart(Applet applet, IndiSet indis, FamiSet famis)
	{
		this.mApplet = applet;
		this.mIndis = indis;
		this.mFamis = famis;
		GROMouseListener ml = new GROMouseListener(this);
		addMouseListener(ml);
		addMouseMotionListener(ml);
	}

	@Override
	public void paint(Graphics g)
	{
		if (!this.mInitialized)
			init(g);

		Rectangle clip = g.getClipBounds();
		g.clearRect(clip.x, clip.y, clip.width, clip.height);

		this.mFamis.paint(g);
		this.mIndis.paint(g);
	}

	protected void init(Graphics g)
	{
		Rectangle bounds = this.mIndis.init(g);
		this.mFamis.init(g);

		Dimension dimBounds = new Dimension(bounds.width, bounds.height);
		setSize(dimBounds);
		setPreferredSize(dimBounds);

		this.mInitialized = true;
	}

	public Indi hitIndi(Point point)
	{
		return this.mIndis.isOnIndi(point);
	}

	public void gotoIndi(Indi mLastIndi)
	{
		String relurl = mLastIndi.getRelativeURL();
		URL url = null;
		try
		{
			url = new URL(this.mApplet.getDocumentBase(), relurl);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		this.mApplet.getAppletContext().showDocument(url);
	}
}
