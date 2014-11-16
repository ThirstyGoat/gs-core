package org.graphstream.ui.graphicGraph;

import java.util.Collection;
import java.util.Collections;

/**
 * an empty/null selection model
 * <p>
 * User: bowen
 * Date: 11/16/14
 */
public class EmptySelectionModel implements SelectionModel
{
    private static final EmptySelectionModel instance = new EmptySelectionModel();


    public static SelectionModel getInstance()
    {
        return instance;
    }


    @Override
    public void clear()
    {

    }


    @Override
    public void add(GraphicElement element)
    {

    }


    @Override
    public void remove(GraphicElement element)
    {

    }


    @Override
    public boolean contains(GraphicElement element)
    {
        return false;
    }


    @Override
    public Collection<GraphicElement> list()
    {
        return Collections.emptyList();
    }
}
