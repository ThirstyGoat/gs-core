package org.graphstream.ui.view.util;

import org.graphstream.ui.graphicGraph.GraphicElement;

import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;

/**
 * a mouse event listener
 * <p>
 * User: bowen
 * Date: 8/14/14
 */
public interface NodeListener
{
    public enum Button
    {
        LEFT, MIDDLE, RIGHT;


        public static Button fromSwing(final MouseEvent event)
        {
            if (null == event)
            {
                return null;
            }
            if (SwingUtilities.isLeftMouseButton(event))
            {
                return LEFT;
            }
            if (SwingUtilities.isRightMouseButton(event))
            {
                return RIGHT;
            }
            if (SwingUtilities.isMiddleMouseButton(event))
            {
                return MIDDLE;
            }
            return null;
        }
    }

    /**
     * user clicked on specific node
     */
    void nodeClicked(String nodeId, GraphicElement element, Button button, double x, double y);

    /**
     * user requested popup context menu
     */
    void nodePopup(String nodeId, GraphicElement element, double x, double y);

    /**
     * user selected node
     */
    void nodeSelected(String nodeId, GraphicElement element);

    /**
     * user unselected node
     */
    void nodeUnselected(String nodeId, GraphicElement element);

    /**
     * user removed a node
     */
    void nodeRemoved(String nodeId, GraphicElement element);
}
