/*
 * Copyright 2006 - 2013
 *     Stefan Balev     <stefan.balev@graphstream-project.org>
 *     Julien Baudry    <julien.baudry@graphstream-project.org>
 *     Antoine Dutot    <antoine.dutot@graphstream-project.org>
 *     Yoann Pigné      <yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin   <guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */
package org.graphstream.stream.sync;

import java.security.AccessControlException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SinkTime
{
    /**
     * Key used to disable synchro. Just run : java -DSYNC_DISABLE_KEY ...
     */
    public static final String SYNC_DISABLE_KEY = "org.graphstream.stream.sync.disable";

    /**
     * Flag used to disable sync.
     */
    protected static final boolean disableSync;

    /*
     * The following code is used to prevent AccessControlException to be thrown
     * when trying to get the value of the property (in applets for example).
     */
    static
    {
        boolean off;
        try
        {
            off = System.getProperty(SYNC_DISABLE_KEY) != null;
        }
        catch (AccessControlException ex)
        {
            off = false;
        }
        disableSync = off;
    }

    /**
     * Map storing times of sources.
     */
    private final Map<String, Long> times = new ConcurrentHashMap<>();

    /**
     * Update timeId for a source.
     *
     * @param sourceId
     * @param timeId
     * @return true if time has been updated
     */
    protected boolean setTimeFor(String sourceId, long timeId)
    {
        final Long knownTimeId = times.get(sourceId);

        if (knownTimeId == null)
        {
            times.put(sourceId, timeId);
            return true;
        }
        if (timeId > knownTimeId)
        {
            times.put(sourceId, timeId);
            return true;
        }

        return false;
    }

    /**
     * Allow to know if event is new for this source. This updates the timeId
     * mapped to the source.
     *
     * @param sourceId
     * @param timeId
     * @return true if event is new for the source
     */
    public boolean isNewEvent(String sourceId, long timeId)
    {
        return disableSync || setTimeFor(sourceId, timeId);
    }
}
