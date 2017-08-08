/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part1;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class TreeNode<E extends Comparable<E>> {

	// Attributes
	protected E element;
	protected ArrayList<TreeNode<E>> children = new ArrayList<>();
	protected TreeNode<E> parent;
	protected int depth;
	protected int id;
	protected int arity;

	// No Argument Constructor
	public TreeNode() {

	}

	// Constructor With Parameters
	public TreeNode(int id, E e, TreeNode<E> p, int d, int a) {
		this.id = id;
		element = e;
		parent = p;
		depth = d;
		arity = a;

	}

	// Accessor Methods
	public int getID() {
		return id;
	}

	public E getElement() {
		return element;
	}

	public TreeNode<E> getChild(int index) {
		return children.get(index);
	}

	public TreeNode<E> getParent() {
		return parent;
	}

	public int getDepth() {
		return depth;
	}

	public int getArity() {
		return arity;
	}

	// Mutator Methods

	public void setNode(TreeNode<E> node) {
		this.id = node.id;
		this.element = node.element;
		this.children = node.children;
		this.parent = node.parent;
		this.depth = node.depth;
		this.arity = node.arity;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setElement(E e) {
		element = e;
	}

	public void setChild(TreeNode<E> e, int index) {
		while (children.size() < (index + 1))
			children.add(new TreeNode<E>());

		children.set(index, e);
	}

	public void setParent(TreeNode<E> p) {
		parent = p;
	}

	public void setDepth(int d) {
		depth = d;
	}

}