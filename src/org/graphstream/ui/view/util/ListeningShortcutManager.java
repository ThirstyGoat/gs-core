package org.graphstream.ui.view.util;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.EmptySelectionModel;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.SelectionModel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * a key / shortcut manager with node listener callbacks
 * <p>
 * User: bowen Date: 11/15/14
 */
public class ListeningShortcutManager implements ShortcutManager
{
    private View view;

    private GraphicGraph graph;

    private double viewPercent = 1;

    private Point3 viewPos = new Point3();

    private double rotation = 0;

    private final Set<NodeListener> listeners = new CopyOnWriteArraySet<>();

    private SelectionModel selectionModel = EmptySelectionModel.getInstance();

    @Override
    public void init(final GraphicGraph graph, final View view)
    {
        this.graph = graph;
        this.view = view;
        view.addKeyListener(this);
    }

    @Override
    public void release()
    {
        if (this.view != null)
        {
            this.view.removeKeyListener(this);
            this.view = null;
        }
        if (this.graph != null)
        {
            this.graph = null;
        }
        this.selectionModel.clear();
    }

    public boolean addListener(final NodeListener l)
    {
        if (null == l)
        {
            return false;
        }
        return this.listeners.add(l);
    }

    public boolean removeListener(final NodeListener l)
    {
        if (null == l)
        {
            return false;
        }
        return this.listeners.remove(l);
    }

    public SelectionModel getSelectionModel()
    {
        return selectionModel;
    }

    public void setSelectionModel(final SelectionModel model)
    {
        if (null == model)
        {
            throw new IllegalArgumentException("Model cannot be null.");
        }
        this.selectionModel = model;
    }

    @Override
    public void keyPressed(final KeyEvent event)
    {
        final Camera camera = view.getCamera();

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            this.clearSelected();
        }
        else if (event.getKeyCode() == KeyEvent.VK_PAGE_UP)
        {
            camera.setViewPercent(Math.max(0.0001f,
                    camera.getViewPercent() * 0.9f));
        }
        else if (event.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
        {
            camera.setViewPercent(camera.getViewPercent() * 1.1f);
        }
        else if (event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if ((event.getModifiers() & KeyEvent.ALT_MASK) != 0)
            {
                double r = camera.getViewRotation();
                camera.setViewRotation(r - 5);
            }
            else
            {
                double delta = 0;

                if ((event.getModifiers() & KeyEvent.SHIFT_MASK) != 0)
                {
                    delta = camera.getGraphDimension() * 0.1f;
                }
                else
                {
                    delta = camera.getGraphDimension() * 0.01f;
                }

                delta *= camera.getViewPercent();

                Point3 p = camera.getViewCenter();
                camera.setViewCenter(p.x - delta, p.y, 0);
            }
        }
        else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if ((event.getModifiers() & KeyEvent.ALT_MASK) != 0)
            {
                double r = camera.getViewRotation();
                camera.setViewRotation(r + 5);
            }
            else
            {
                double delta = 0;

                if ((event.getModifiers() & KeyEvent.SHIFT_MASK) != 0)
                {
                    delta = camera.getGraphDimension() * 0.1f;
                }
                else
                {
                    delta = camera.getGraphDimension() * 0.01f;
                }

                delta *= camera.getViewPercent();

                Point3 p = camera.getViewCenter();
                camera.setViewCenter(p.x + delta, p.y, 0);
            }
        }
        else if (event.getKeyCode() == KeyEvent.VK_UP)
        {
            double delta = 0;

            if ((event.getModifiers() & KeyEvent.SHIFT_MASK) != 0)
            {
                delta = camera.getGraphDimension() * 0.1f;
            }
            else
            {
                delta = camera.getGraphDimension() * 0.01f;
            }

            delta *= camera.getViewPercent();

            Point3 p = camera.getViewCenter();
            camera.setViewCenter(p.x, p.y + delta, 0);
        }
        else if (event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            double delta = 0;

            if ((event.getModifiers() & KeyEvent.SHIFT_MASK) != 0)
            {
                delta = camera.getGraphDimension() * 0.1f;
            }
            else
            {
                delta = camera.getGraphDimension() * 0.01f;
            }

            delta *= camera.getViewPercent();

            Point3 p = camera.getViewCenter();
            camera.setViewCenter(p.x, p.y - delta, 0);
        }
    }

    @Override
    public void keyReleased(final KeyEvent event)
    {

    }

    @Override
    public void keyTyped(final KeyEvent event)
    {
        if (event.getKeyChar() == 'R')
        {
            this.view.getCamera().resetView();
        }
    }

    private void clearSelected()
    {
        final Collection<GraphicElement> selected = this.selectionModel.list();
        if (selected.isEmpty())
        {
            return;
        }

        this.selectionModel.clear();
        for (final GraphicElement element : selected)
        {
            for (final NodeListener l : this.listeners)
            {
                l.nodeUnselected(element.getId(), element);
            }
        }
    }

    protected void fireRemoved(final String id, final GraphicElement element)
    {
        for (final NodeListener l : this.listeners)
        {
            l.nodeRemoved(element.getId(), element);
        }
    }
}
