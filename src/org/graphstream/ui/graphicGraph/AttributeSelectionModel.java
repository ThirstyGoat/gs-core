package org.graphstream.ui.graphicGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * a selection model based on element attributes
 * <p>
 * User: bowen
 * Date: 11/16/14
 */
public class AttributeSelectionModel implements SelectionModel
{
    private final Map<String, GraphicElement> items = new TreeMap<>();


    @Override
    public void clear()
    {
        for (final GraphicElement element : this.items.values())
        {
            element.removeAttribute("ui.selected");
        }
        this.items.clear();
    }


    @Override
    public void add(final GraphicElement element)
    {
        if (null == element)
        {
            return;
        }
        final String key = element.getId();
        this.items.put(key, element);
        element.addAttribute("ui.selected");
    }


    @Override
    public void remove(final GraphicElement element)
    {
        if (null == element)
        {
            return;
        }
        final String key = element.getId();
        this.items.remove(key);
        element.removeAttribute("ui.selected");
    }


    @Override
    public boolean contains(final GraphicElement element)
    {
        if (null == element)
        {
            return false;
        }
        final String key = element.getId();
        return this.items.containsKey(key);
    }


    @Override
    public Collection<GraphicElement> list()
    {
        return new ArrayList<>(this.items.values());
    }
}
