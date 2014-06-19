/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package es.gpc.gp.ec.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author spaus
 */
class ParameterDatabaseTreeNode
    extends DefaultMutableTreeNode
    implements Comparable {

    /**
     * 
     */
    public ParameterDatabaseTreeNode() {
        super();
        }

    /**
     * @param userObject
     */
    public ParameterDatabaseTreeNode(Object userObject) {
        super(userObject);
        }

    /**
     * @param userObject
     * @param allowsChildren
     */
    public ParameterDatabaseTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
        }
    
    /**
     * @param index
     * @param visibleLeaves
     * @return
     */
    public Object getChildAt(int index, boolean visibleLeaves) {
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
            }

        if (!visibleLeaves) {
            int nonLeafIndex = -1;
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                TreeNode n = (TreeNode)e.nextElement();
                if (!n.isLeaf()) {
                    if (++nonLeafIndex == index)
                        return n;
                    }
                }
            
            throw new ArrayIndexOutOfBoundsException("index = "+index+", children = "+getChildCount(visibleLeaves));
            }
        
        return super.getChildAt(index);
        }
    
    /**
     * @param visibleLeaves
     * @return
     */
    public int getChildCount(boolean visibleLeaves) {
        if (!visibleLeaves) {
            int nonLeafCount = 0;
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                TreeNode n = (TreeNode)e.nextElement();
                if (!n.isLeaf()) ++nonLeafCount;
                }
            
            return nonLeafCount;
            }
        
        return super.getChildCount();
        }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        ParameterDatabaseTreeNode n = (ParameterDatabaseTreeNode)o;

        return ((Comparable)userObject).compareTo(n.userObject);
        }
    
    /**
     * @param comp
     */
    public void sort(Comparator comp) {
        if (children == null) 
            return;
        
        Object[] childArr = children.toArray();
        Arrays.sort(childArr, comp);
        children = new Vector(Arrays.asList(childArr));
        
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            ParameterDatabaseTreeNode n = 
                (ParameterDatabaseTreeNode)e.nextElement();
            n.sort(comp);
            }
        }
    }
