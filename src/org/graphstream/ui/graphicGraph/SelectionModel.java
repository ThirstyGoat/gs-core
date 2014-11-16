package org.graphstream.ui.graphicGraph;

import java.util.Collection;

/**
 * a graph selection model
 * <p>
 * User: bowen
 * Date: 11/16/14
 */
public interface SelectionModel
{
    void clear();
    void add(GraphicElement element);
    void remove(GraphicElement element);
    boolean contains(GraphicElement element);
    Collection<GraphicElement> list();
}
