package to.go.gro.groapplet;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

public class GROMouseListener extends MouseInputAdapter
{
	private Indi mLastIndi;
	private final FamilyChart fc;

	public GROMouseListener(FamilyChart fc)
	{
		this.fc = fc;
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		this.mLastIndi = this.fc.hitIndi(evt.getPoint());
		if (this.mLastIndi != null)
		{
			this.mLastIndi.hit(true);
			this.fc.repaint();
		}
		super.mousePressed(evt);
	}

	@Override
	public void mouseReleased(MouseEvent evt)
	{
		if (this.mLastIndi != null)
		{
			if (this.mLastIndi.isHit())
			{
				this.mLastIndi.hit(false);
				this.fc.repaint();
				this.fc.gotoIndi(this.mLastIndi);
			}
			this.mLastIndi = null;
		}
		super.mouseReleased(evt);
	}

	@Override
	public void mouseDragged(MouseEvent evt)
	{
		Indi indi = this.fc.hitIndi(evt.getPoint());
		if (indi != this.mLastIndi && this.mLastIndi != null && this.mLastIndi.isHit())
		{
			this.mLastIndi.hit(false);
			this.fc.repaint();
		}
		else if (indi == this.mLastIndi && this.mLastIndi != null && !this.mLastIndi.isHit())
		{
			this.mLastIndi.hit(true);
			this.fc.repaint();
		}
		super.mouseDragged(evt);
	}
}
