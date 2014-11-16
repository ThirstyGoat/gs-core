package org.graphstream.ui.swingViewer;

import org.graphstream.ui.graphicGraph.GraphicGraph;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * a layer renderer that delegates to a list of renderable instances
 * <p>
 * User: bowen
 * Date: 11/16/14
 */
public class CompositeLayerRenderer implements LayerRenderer
{
    private final List<LayerRenderer> renderers = new ArrayList<>();


    public int size()
    {
        return this.renderers.size();
    }


    public void clear()
    {
        this.renderers.clear();
    }


    public boolean addRenderer(final LayerRenderer l)
    {
        if (null == l)
        {
            return false;
        }
        return this.renderers.add(l);
    }


    public boolean removeRenderer(final LayerRenderer l)
    {
        if (null == l)
        {
            return false;
        }
        return this.renderers.remove(l);
    }


    @Override
    public void render(Graphics2D graphics, GraphicGraph graph, double px2Gu, int widthPx, int heightPx, double minXGu, double minYGu, double maxXGu, double maxYGu)
    {
        for (final LayerRenderer renderer : this.renderers)
        {
            renderer.render(graphics, graph, px2Gu, widthPx, heightPx, minXGu, minYGu, maxXGu, maxYGu);
        }
    }
}
