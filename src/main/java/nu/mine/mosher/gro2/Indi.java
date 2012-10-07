package nu.mine.mosher.gro2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Indi
{
	private static final Color bg = Color.WHITE;
	private static final Color fg = Color.BLACK;
	private static final Color bgpush = Color.LIGHT_GRAY;
	private static final Color fgpush = Color.BLACK;
	private static final int LEFT_MARGIN = 3;
	private static final int RIGHT_MARGIN = 4;

	private final int x;
	private final int y;
	private final int mID;
	private final String mName;
	private final String mBirth;
	private final String mDeath;

	private int w;
	private int h;
	private final List<TextLine> mLines = new ArrayList<>();

	private boolean pushed;

	private static class TextLine
	{
		private TextLayout text;
		private float y;

		TextLine(TextLayout text, float y)
		{
			this.text = text;
			this.y = y;
		}

		void draw(Graphics g, int x)
		{
			this.text.draw((Graphics2D) g, x, this.y);
		}
	}

	public Indi(int x, int y, int id, String name, String birth, String death)
	{
		this.x = x;
		this.y = y;
		this.mID = id;
		this.mName = name;
		this.mBirth = birth;
		this.mDeath = death;
	}

	public Rectangle calc(Graphics g, int maxWidth)
	{
		this.mLines.clear();

		if (this.mName.length() > 0)
			calcBreaks(g, this.mName, maxWidth);
		if (this.mBirth.length() > 0)
			calcBreaks(g, this.mBirth, maxWidth);
		if (this.mDeath.length() > 0)
			calcBreaks(g, this.mDeath, maxWidth);

		return new Rectangle(this.x, this.y, this.w, this.h);
	}

	protected void calcBreaks(Graphics gr, String s, int maxWidth)
	{
		Graphics2D g = (Graphics2D) gr;
		AttributedString attr = new AttributedString(s);
		LineBreakMeasurer linebreaker = new LineBreakMeasurer(attr.getIterator(), g.getFontRenderContext());

		float cy = this.y + this.h;
		while (linebreaker.getPosition() < s.length())
		{
			TextLayout text = linebreaker.nextLayout(maxWidth);
			cy += text.getAscent();
			this.w = (int) Math.rint(Math.max(this.w, text.getAdvance() + RIGHT_MARGIN + 1));
			this.mLines.add(new TextLine(text, cy));
			cy += text.getDescent() + text.getLeading();
		}
		this.h = (int) Math.rint(cy) - this.y;
	}

	public boolean sect(RectangularShape rect)
	{
		return rect.intersects(this.x, this.y, this.w, this.h);
	}

	public void paint(Graphics g)
	{
		drawBounds(g);
		drawText(g);
	}

	protected void drawBounds(Graphics g)
	{
		if (this.pushed)
			g.setColor(bgpush);
		else
			g.setColor(bg);
		g.fillRect(this.x, this.y, this.w, this.h);
		if (this.pushed)
			g.setColor(fgpush);
		else
			g.setColor(fg);
		g.drawRect(this.x, this.y, this.w - 1, this.h - 1);
	}

	protected void drawText(Graphics g)
	{
		for (Iterator<TextLine> i = this.mLines.iterator(); i.hasNext();)
		{
			TextLine text = i.next();
			text.draw(g, this.x + LEFT_MARGIN);
		}
	}

	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer(256);
		s.append("Indi: ");
		s.append(this.mName);
		s.append(" [");
		s.append(this.x);
		s.append(",");
		s.append(this.y);
		s.append(",");
		s.append(this.w);
		s.append(",");
		s.append(this.h);
		s.append("]");
		return s.toString();
	}

	public Rectangle2D getBounds()
	{
		return new Rectangle2D.Double(this.x, this.y, this.w, this.h);
	}

	public boolean isOn(Point point)
	{
		return this.x <= point.x && point.x <= this.x + this.w && this.y <= point.y && point.y <= this.y + this.h;
	}

	public void hit(boolean isHit)
	{
		this.pushed = isHit;
	}

	public boolean isHit()
	{
		return this.pushed;
	}

	public String getRelativeURL()
	{
		return "?indi=" + this.mID;
	}
}
