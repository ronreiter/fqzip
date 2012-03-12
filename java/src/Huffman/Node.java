package Huffman;


import java.io.Serializable;

/**
 * A node in a code tree. This class has two and only two subclasses: InternalNode, Leaf.
 */
public abstract class Node implements Serializable {
	
	Node() {}  // Package-private to prevent accidental subclassing outside of this package
	
}
