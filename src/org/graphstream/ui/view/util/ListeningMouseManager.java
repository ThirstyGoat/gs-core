package org.graphstream.ui.view.util;

import org.graphstream.ui.graphicGraph.AttributeSelectionModel;
import org.graphstream.ui.graphicGraph.GraphicEdge;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.graphicGraph.GraphicSprite;
import org.graphstream.ui.graphicGraph.SelectionModel;
import org.graphstream.ui.view.View;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * a mouse manager that notifies listeners mouse user-interface events
 * <p>
 * User: bowen Date: 8/14/14
 */
public class ListeningMouseManager implements MouseManager
{
    /**
     * the view this manager is tied to
     */
    private View view;

    /**
     * the graph context
     */
    private GraphicGraph graph;

    /**
     * mouse listener set
     */
    private final Set<NodeListener> listeners = new CopyOnWriteArraySet<>();

    /**
     * the currently selected/active elements
     */
    private GraphicElement activeElement;

    /**
     * the mouse [x,y] value used for selection start position
     */
    protected Point2D selectionStart = null;

    /**
     * the graphic element selection model
     */
    private SelectionModel selectionModel = new AttributeSelectionModel();


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
    public void init(final GraphicGraph graph, final View view)
    {
        this.view = view;
        this.graph = graph;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }


    @Override
    public void release()
    {
        this.view.removeMouseListener(this);
        this.view.removeMouseMotionListener(this);
        this.selectionModel.clear();
    }


    @Override
    public void mouseClicked(final MouseEvent event)
    {
        if (event.isPopupTrigger())
        {
            this.firePopup(event);
        }
    }


    @Override
    public void mousePressed(final MouseEvent event)
    {
        final NodeListener.Button button = NodeListener.Button.fromSwing(event);
        final boolean multiSelect = event.isShiftDown();
        final boolean toggleSelect;
        if (NodeListener.Button.LEFT.equals(button))
        {
            toggleSelect = event.isControlDown() || event.isMetaDown();
        }
        else
        {
            toggleSelect = false;
        }
        this.handleMousePressed(button, multiSelect, toggleSelect, event.getX(), event.getY());
    }


    @Override
    public void mouseReleased(final MouseEvent event)
    {
        this.handleMouseReleased(event.getX(), event.getY());
    }


    @Override
    public void mouseDragged(final MouseEvent event)
    {
        final NodeListener.Button button = NodeListener.Button.fromSwing(event);
        this.handleMouseDragged(button, event.getX(), event.getY());
    }


    @Override
    public void mouseEntered(final MouseEvent event)
    {

    }


    @Override
    public void mouseExited(final MouseEvent event)
    {

    }


    @Override
    public void mouseMoved(final MouseEvent e)
    {

    }


    private void handleMousePressed(final NodeListener.Button button, final boolean multiSelect, final boolean toggleSelect, final double x, final double y)
    {
        this.clearSelectionArea();
        this.activeElement = this.view.findNodeOrSpriteAt(x, y);

        if (this.activeElement != null)
        {
            // user clicked on specific element - determine if this is part of extended selection or click event
            this.view.freezeElement(this.activeElement, true);
            if (NodeListener.Button.LEFT.equals(button))
            {
                if (multiSelect)
                {
                    this.selectElement(this.activeElement, true);
                }
                else if (toggleSelect)
                {
                    final boolean selected = this.selectionModel.contains(this.activeElement);
                    this.selectElement(this.activeElement, !selected);
                }
                else
                {
                    this.clearSelectedItems();
                    this.selectElement(this.activeElement, true);
                    this.clickElement(this.activeElement, true);
                }
            }
        }
        else
        {
            // user clicked on empty space - start selection
            this.view.requestFocus();
            if (NodeListener.Button.LEFT.equals(button))
            {
                this.selectionStart = new Point2D.Double(x, y);
                if (!multiSelect && !toggleSelect)
                {
                    this.clearSelectedItems();
                }
                this.view.beginSelectionAt(this.selectionStart.getX(), this.selectionStart.getY());
            }
        }
    }


    private void handleMouseReleased(final double x, final double y)
    {
        if (this.activeElement != null)
        {
            // handle element selection or click
            this.view.freezeElement(this.activeElement, false);
            this.clickElement(this.activeElement, false);
            this.activeElement = null;
        }

        if (this.selectionStart != null)
        {
            // get selection bounds
            double x1 = this.selectionStart.getX();
            double y1 = this.selectionStart.getY();
            double x2 = x;
            double y2 = y;
            if (x1 > x2)
            {
                // swap
                final double t = x1;
                x1 = x2;
                x2 = t;
            }
            if (y1 > y2)
            {
                // swap
                final double t = y1;
                y1 = y2;
                y2 = t;
            }

            // select elements in area
            for (final GraphicElement element : view.allNodesOrSpritesIn(x1, y1, x2, y2))
            {
                this.selectElement(element, true);
            }
            this.view.endSelectionAt(x2, y2);

            // reset context
            this.clearSelectionArea();
        }
    }


    private void handleMouseDragged(final NodeListener.Button button, final double x, final double y)
    {
        if (NodeListener.Button.LEFT.equals(button) && this.activeElement != null)
        {
            this.view.moveElementAtPx(this.activeElement, x, y);
        }
        if (this.selectionStart != null)
        {
            this.view.selectionGrowsAt(x, y);
        }
    }


    private void clearSelectionArea()
    {
        this.activeElement = null;
        this.selectionStart = null;
    }


    private void clearSelectedItems()
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


    private void clickElement(final GraphicElement element, final boolean clicked)
    {
        if (null == element)
        {
            return;
        }

        if (clicked)
        {
            element.addAttribute("ui.clicked");
        }
        else
        {
            element.removeAttribute("ui.clicked");
        }
    }


    private void selectElement(final GraphicElement element, final boolean selected)
    {
        if (null == element)
        {
            return;
        }

        final String id = this.findNode(element);
        if (null == id)
        {
            return;
        }

        if (selected && !this.selectionModel.contains(element))
        {
            this.selectionModel.add(element);
            this.fireSelected(id, element);
        }
        else if (!selected && this.selectionModel.contains(element))
        {
            this.selectionModel.remove(element);
            this.fireUnselected(id, element);
        }
    }


    private String findNode(final GraphicElement element)
    {
        // attempt to get node id from graph element
        if (element instanceof GraphicNode)
        {
            // element id is node id
            return element.getId();
        }
        else if (element instanceof GraphicEdge)
        {
            // edge selection not supported
            return null;
        }
        else if (element instanceof GraphicSprite)
        {
            // get node id from sprite
            final GraphicSprite sprite = (GraphicSprite) element;
            final GraphicNode node = sprite.getNodeAttachment();
            if (null == node)
            {
                return null;
            }
            return node.getId();
        }
        return null;
    }


    private void firePopup(final MouseEvent event)
    {
        final GraphicElement element = this.view.findNodeOrSpriteAt(event.getX(), event.getY());
        final String node = this.findNode(element);
        for (final NodeListener l : this.listeners)
        {
            l.nodePopup(node, element, event.getX(), event.getY());
        }
    }


    private void fireSelected(final String id, final GraphicElement element)
    {
        if (null == element)
        {
            return;
        }
        if (null == id)
        {
            return;
        }

        for (final NodeListener l : this.listeners)
        {
            l.nodeSelected(id, element);
        }
    }


    private void fireUnselected(final String id, final GraphicElement element)
    {
        if (null == element)
        {
            return;
        }
        if (null == id)
        {
            return;
        }

        for (final NodeListener l : this.listeners)
        {
            l.nodeUnselected(id, element);
        }
    }
}
