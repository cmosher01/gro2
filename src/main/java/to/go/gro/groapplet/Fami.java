package to.go.gro.groapplet;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Fami
{
    private static final int BAR_HEIGHT = 4;
    private static final int MARRIAGE_SPACING = 20;
    private static final int CHILD_LINE_DISTANCE = 10;
    private static final int CHILD_HEIGHT = 10;

    private Indi husb;
    private Indi wife;
    private List<Indi> rChild = new ArrayList<>();

    private Line2D parentBar1;
    private Line2D parentBar2;

    private Line2D descentBar1;
    private Line2D descentBar2;
    private Line2D descentBar3;

    private Line2D childBar;
    private Line2D[] rChildBar;

    private Shape parentBounds; //Polygon
    private Shape descentLine; //Line2D
    private Shape childBounds; //Polygon

    public void setHusb(Indi indi)
    {
        this.husb = indi;
    }

    public void setWife(Indi indi)
    {
        this.wife = indi;
    }

    public void addChild(Indi indi)
    {
        this.rChild.add(indi);
    }

    public void calc(@SuppressWarnings("unused") Graphics g)
    {
        if (this.husb == null && this.wife == null && this.rChild.size()==0)
            return;

        Rectangle2D rect1;
        Rectangle2D rect2;
        if (this.husb == null && this.wife == null)
        {
            rect1 = new Rectangle2D.Double();
            rect2 = new Rectangle2D.Double();
        }
        else if (this.husb == null)
        {
            rect2 = this.wife.getBounds();
            rect1 = this.wife.getBounds();
            rect1.setRect(rect1.getMinX()-MARRIAGE_SPACING,rect1.getY(),0,rect1.getHeight());
        }
        else if (this.wife == null)
        {
            rect1 = this.husb.getBounds();
            rect2 = this.husb.getBounds();
            rect2.setRect(rect2.getMaxX()+MARRIAGE_SPACING,rect2.getY(),0,rect2.getHeight());
        }
        else
        {
            rect1 = this.husb.getBounds();
            rect2 = this.wife.getBounds();
        }

        boolean bHusbandOnRight = (rect1.getX() > rect2.getX());
        if (bHusbandOnRight)
        {
            Rectangle2D temp = rect1;
            rect1 = rect2;
            rect2 = temp;
        }

        Point2D pt1 = new Point2D.Double(rect1.getMaxX(),rect1.getCenterY());
        Point2D pt2 = new Point2D.Double(rect2.getMinX(),rect2.getCenterY());
        if (bHusbandOnRight)
        {
            Point2D temp = pt1;
            pt1 = pt2;
            pt2 = temp;
        }

        double dx = pt2.getX()-pt1.getX();
        double dy = pt2.getY()-pt1.getY();
        double dist = Math.sqrt(dx*dx+dy*dy);
        if (-1e-8 < Math.rint(dist) && Math.rint(dist) < 1e-8)
        	dist = 1;

        double nx = pt1.getX()+(CHILD_LINE_DISTANCE*dx/dist);
        double ny = pt1.getY()+(CHILD_LINE_DISTANCE*dy/dist)+BAR_HEIGHT/2;
        Point2D ptP = new Point2D.Double(nx,ny);




        if (pt1.getX()>0 || pt1.getY()>0 || pt2.getX()>0 || pt2.getY()>0)
        {
            this.parentBar1 = new Line2D.Double(pt1.getX(),pt1.getY()-BAR_HEIGHT/2,pt2.getX(),pt2.getY()-BAR_HEIGHT/2);
            this.parentBar2 = new Line2D.Double(pt1.getX(),pt1.getY()+BAR_HEIGHT/2,pt2.getX(),pt2.getY()+BAR_HEIGHT/2);
        }

        double nTop = Double.MAX_VALUE;
        double nBottom = Double.MIN_VALUE;
        double nLeft = Double.MAX_VALUE;
        double nRight = Double.MIN_VALUE;
        if (!this.rChild.isEmpty())
        {
            Point2D[] rp = new Point2D.Double[this.rChild.size()];
            for (int i = 0; i < rp.length; i++)
            {
                Rectangle2D rect = this.rChild.get(i).getBounds();

                double x = rect.getCenterX();
                double y = rect.getY();
                rp[i] = new Point2D.Double(x,y);

                if (x < nLeft)
                    nLeft = x;
                if (x > nRight)
                    nRight = x;

                if (y < nTop)
                    nTop = y;
                if (y > nBottom)
                    nBottom = y;
            }
            nTop -= CHILD_HEIGHT;
            this.childBar = new Line2D.Double(nLeft,nTop,nRight,nTop);

            this.rChildBar = new Line2D.Double[rp.length];
            for (int i = 0; i < this.rChildBar.length; i++)
            {
                this.rChildBar[i] = new Line2D.Double(rp[i].getX(),rp[i].getY(),rp[i].getX(),nTop);
            }

            if (pt1.getX()>0 || pt1.getY()>0 || pt2.getX()>0 || pt2.getY()>0)
            {
                if (nLeft<ptP.getX() && ptP.getX()<nRight)
                {
                    this.descentBar1 = new Line2D.Double(ptP.getX(),ptP.getY(),ptP.getX(),nTop);
                }
                else
                {
                    this.descentBar1 = new Line2D.Double(ptP.getX(),ptP.getY(),ptP.getX(),nTop-CHILD_HEIGHT);
                    this.descentBar2 = new Line2D.Double(ptP.getX(),nTop-CHILD_HEIGHT,(nRight+nLeft)/2,nTop-CHILD_HEIGHT);
                    this.descentBar3 = new Line2D.Double((nRight+nLeft)/2,nTop-CHILD_HEIGHT,(nRight+nLeft)/2,nTop);
                }
            }
        }



        Polygon pg = new Polygon();
        if (this.parentBar1 != null)
        {
            pg.addPoint((int)this.parentBar1.getX1(),(int)this.parentBar1.getY1());
            pg.addPoint((int)this.parentBar1.getX2(),(int)this.parentBar1.getY2());
            pg.addPoint((int)this.parentBar2.getX2(),(int)this.parentBar2.getY2());
            pg.addPoint((int)this.parentBar2.getX1(),(int)this.parentBar2.getY1());
        }
        this.parentBounds = pg;

        Line2D ln = new Line2D.Double();
        if (this.descentBar1 != null)
            ln.setLine(this.descentBar1);
        this.descentLine = ln;

        Rectangle rect = new Rectangle();
        if (this.descentBar1 != null)
        {
            double y = this.descentBar1.getY2();
            double x = Math.min(nLeft,ptP.getX());
            double w = Math.max(nRight,ptP.getX())-x+1;
            if (w < 1)
                w = 1;
            double h = nBottom-y+1;
            if (h < 1)
                h = 1;
            rect.setRect(x,y,w,h);
        }
        this.childBounds = rect;
    }

    public void paint(Graphics g)
    {
        drawLine(g,this.parentBar1);
        drawLine(g,this.parentBar2);
        drawLine(g,this.descentBar1);
        drawLine(g,this.descentBar2);
        drawLine(g,this.descentBar3);
        drawLine(g,this.childBar);
        if (this.rChildBar != null)
            for (int i = 0; i < this.rChildBar.length; i++)
                drawLine(g,this.rChildBar[i]);
    }

    protected static void drawLine(Graphics g, Line2D line)
    {
        if (line == null)
            return;

        g.drawLine((int)Math.round(line.getX1()),(int)Math.round(line.getY1()),(int)Math.round(line.getX2()),(int)Math.round(line.getY2()));
    }

    public boolean sect(Rectangle2D clip)
    {
        if (this.childBounds.intersects(clip))
            return true;

        if (this.parentBounds.intersects(clip))
            return true;

        if (this.descentLine.intersects(clip))
            return true;

        return false;
    }
}
