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
package org.graphstream.graph.implementations;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.DepthFirstIterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.SourceBase;

/**
 * <p>
 * This class provides a basic implementation of {@code Node} interface, to
 * minimize the effort required to implement this interface.
 * </p>
 * 
 * <p>
 * This class implements all the methods of
 * {@link org.graphstream.graph.implementations#AbstractElement} and most of the
 * methods of {@link org.graphstream.graph#Node} (there are "only" ten abstract
 * methods). In addition to these, subclasses must provide implementations for
 * {@link #addEdgeCallback(AbstractEdge)} and
 * {@link #removeEdgeCallback(AbstractEdge)} which are called by the parent
 * graph when an edge incident to this node is added to or removed from the
 * graph. This class has a low memory overhead (one reference as field).
 * </p>
 */
public abstract class AbstractNode extends AbstractElement implements Node {

	/**
	 * The graph to which this node belongs
	 */
	protected AbstractGraph graph;


	/**
	 * Constructs a new node. This constructor copies the parameters into the
	 * corresponding fields
	 * 
	 * @param graph
	 *            The graph to which this node belongs.
	 * @param id
	 *            Unique identifier of this node.
	 */
	protected AbstractNode(AbstractGraph graph, String id) {
		super(id);
		this.graph = graph;
	}

	@Override
	protected void attributeChanged(AttributeChangeEvent event,
			String attribute, Object oldValue, Object newValue) {
		graph.listeners.sendAttributeChangedEvent(id,
				SourceBase.ElementType.NODE, attribute, event, oldValue,
				newValue);
	}

	@Override
	protected boolean nullAttributesAreErrors() {
		return graph.nullAttributesAreErrors();
	}

    @Override
	public Graph getGraph() {
		return graph;
	}

	public abstract int getDegree();

	public abstract int getInDegree();

	public abstract int getOutDegree();

    @Override
	public boolean hasEdgeToward(Node node) {
		return getEdgeToward(node) != null;
	}

    @Override
	public boolean hasEdgeToward(int index) {
		return getEdgeToward(index) != null;
	}

    @Override
	public boolean hasEdgeToward(String id) {
		return getEdgeToward(id) != null;
	}

    @Override
	public boolean hasEdgeFrom(Node node) {
		return getEdgeFrom(node) != null;
	}

    @Override
	public boolean hasEdgeFrom(int index) {
		return getEdgeFrom(index) != null;
	}

    @Override
	public boolean hasEdgeFrom(String id) {
		return getEdgeFrom(id) != null;
	}

    @Override
	public boolean hasEdgeBetween(Node node) {
		return getEdgeBetween(node) != null;
	}

    @Override
	public boolean hasEdgeBetween(int index) {
		return getEdgeBetween(index) != null;
	}

    @Override
	public boolean hasEdgeBetween(String id) {
		return getEdgeBetween(id) != null;
	}

	public abstract <T extends Edge> T getEdgeToward(Node node);

    @Override
	public <T extends Edge> T getEdgeToward(int index) {
		return getEdgeToward(graph.getNode(index));
	}

    @Override
	public <T extends Edge> T getEdgeToward(String id) {
		return getEdgeToward(graph.getNode(id));
	}

	public abstract <T extends Edge> T getEdgeFrom(Node node);

    @Override
	public <T extends Edge> T getEdgeFrom(int index) {
		return getEdgeFrom(graph.getNode(index));
	}

    @Override
	public <T extends Edge> T getEdgeFrom(String id) {
		return getEdgeFrom(graph.getNode(id));
	}

	public abstract <T extends Edge> T getEdgeBetween(Node node);

    @Override
	public <T extends Edge> T getEdgeBetween(int index) {
		return getEdgeBetween(graph.getNode(index));
	}

    @Override
	public <T extends Edge> T getEdgeBetween(String id) {
		return getEdgeBetween(graph.getNode(id));
	}

	public abstract <T extends Edge> Iterator<T> getEdgeIterator();

	public abstract <T extends Edge> Iterator<T> getEnteringEdgeIterator();

	public abstract <T extends Edge> Iterator<T> getLeavingEdgeIterator();

    @Override
	public <T extends Edge> Iterable<T> getEachEdge() {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return getEdgeIterator();
			}
		};
	}

    @Override
	public <T extends Edge> Iterable<T> getEachEnteringEdge() {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return getEnteringEdgeIterator();
			}
		};
	}

    @Override
	public <T extends Edge> Iterable<T> getEachLeavingEdge() {
		return new Iterable<T>() {
			public Iterator<T> iterator() {
				return getLeavingEdgeIterator();
			}
		};
	}

    @Override
	public <T extends Edge> Collection<T> getEdgeSet() {
		return new AbstractCollection<T>() {
			@Override
			public Iterator<T> iterator() {
				return getEdgeIterator();
			}

			@Override
			public int size() {
				return getDegree();
			}
		};
	}

    @Override
	public <T extends Edge> Collection<T> getEnteringEdgeSet() {
		return new AbstractCollection<T>() {
			@Override
			public Iterator<T> iterator() {
				return getEnteringEdgeIterator();
			}

			@Override
			public int size() {
				return getInDegree();
			}
		};
	}

    @Override
	public <T extends Edge> Collection<T> getLeavingEdgeSet() {
		return new AbstractCollection<T>() {
			@Override
			public Iterator<T> iterator() {
				return getLeavingEdgeIterator();
			}

			@Override
			public int size() {
				return getOutDegree();
			}
		};
	}

    @Override
	public Iterator<Edge> iterator() {
		return getEdgeIterator();
	}

	public abstract <T extends Edge> T getEdge(int i);

	public abstract <T extends Edge> T getEnteringEdge(int i);

	public abstract <T extends Edge> T getLeavingEdge(int i);

    @Override
	public <T extends Node> Iterator<T> getNeighborNodeIterator() {
		return new Iterator<T>() {
			Iterator<Edge> edgeIt = getEdgeIterator();
			HashSet<T> visited = new HashSet<T>(getDegree());
			T next;
			{
				gotoNext();
			}

			private void gotoNext() {
				while (edgeIt.hasNext()) {
					next = edgeIt.next().getOpposite(AbstractNode.this);
					if (!visited.contains(next)) {
						visited.add(next);
						return;
					}
				}
				next = null;
			}

			public boolean hasNext() {
				return next != null;
			}

			public T next() {
				if (next == null)
					throw new NoSuchElementException();
				T current = next;
				gotoNext();
				return current;
			}

			public void remove() {
				throw new UnsupportedOperationException("This iterator does not support remove");
			}
		};
	}

    @Override
	public <T extends Node> Iterator<T> getBreadthFirstIterator() {
		// XXX change it when the old iterator disappears
		// XXX change the return type to have access to the other methods
		return new BreadthFirstIterator<T>(this);
	}

    @Override
	public <T extends Node> Iterator<T> getBreadthFirstIterator(boolean directed) {
		// XXX change it when the old iterator disappears
		// XXX change the return type to have access to the other methods
		return new BreadthFirstIterator<T>(this, directed);
	}

    @Override
	public <T extends Node> Iterator<T> getDepthFirstIterator() {
		// XXX change it when the old iterator disappears
		// XXX change the return type to have access to the other methods
		return new DepthFirstIterator<T>(this);
	}

    @Override
	public <T extends Node> Iterator<T> getDepthFirstIterator(boolean directed) {
		// XXX change it when the old iterator disappears
		// XXX change the return type to have access to the other methods
		return new DepthFirstIterator<T>(this, directed);
	}

	/**
	 * This method is called automatically when an edge incident to this node is
	 * created. Subclasses use it to add the edge to their data structure.
	 * 
	 * @param edge
	 *            a new edge incident to this node
	 */
	protected abstract boolean addEdgeCallback(AbstractEdge edge);

	/**
	 * This method is called automatically before removing an edge incident to
	 * this node. Subclasses use it to remove the edge from their data
	 * structure.
	 * 
	 * @param edge
	 *            an edge incident to this node that will be removed
	 */
	protected abstract void removeEdgeCallback(AbstractEdge edge);

	/**
	 * This method is called for each node when the graph is cleared. Subclasses
	 * may use it to clear their data structures in order to facilitate the
	 * garbage collection.
	 */
	protected abstract void clearCallback();

	/**
	 * Checks if an edge enters this node. Utility method that can be useful in
	 * subclasses.
	 * 
	 * @param e
	 *            an edge
	 * @return {@code true} if {@code e} is entering edge for this node.
	 */
	public boolean isEnteringEdge(Edge e) {
		return e.getTargetNode() == this
				|| (!e.isDirected() && e.getSourceNode() == this);
	}

	/**
	 * Checks if an edge leaves this node. Utility method that can be useful in
	 * subclasses.
	 * 
	 * @param e
	 *            an edge
	 * @return {@code true} if {@code e} is leaving edge for this node.
	 */
	public boolean isLeavingEdge(Edge e) {
		return e.getSourceNode() == this
				|| (!e.isDirected() && e.getTargetNode() == this);
	}

	/**
	 * Checks if an edge is incident to this node. Utility method that can be
	 * useful in subclasses.
	 * 
	 * @param e
	 *            an edge
	 * @return {@code true} if {@code e} is incident edge for this node.
	 */
	public boolean isIncidentEdge(Edge e) {
		return e.getSourceNode() == this || e.getTargetNode() == this;
	}
}
