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

import java.util.Comparator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author spaus
 */
public class ParameterDatabaseTreeModel
    extends DefaultTreeModel {

    private boolean visibleLeaves;
    
    /**
     * @param root
     */
    public ParameterDatabaseTreeModel(TreeNode root) {
        super(root);
        visibleLeaves = true;
        }

    /**
     * @param root
     * @param asksAllowsChildren
     */
    public ParameterDatabaseTreeModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        visibleLeaves = true;
        }

    /**
     * @param visibleLeaves
     */
    public void setVisibleLeaves(boolean visibleLeaves) {
        this.visibleLeaves = visibleLeaves;
        }
    
    public boolean getVisibleLeaves() {
        return visibleLeaves;
        }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object parent, int index) {
        if (!visibleLeaves) {
            if (parent instanceof ParameterDatabaseTreeNode) {
                return ((ParameterDatabaseTreeNode)parent).getChildAt(index,visibleLeaves);
                }
            }
        
        return ((TreeNode)parent).getChildAt(index);
        }
    
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        if (!visibleLeaves) {
            if (parent instanceof ParameterDatabaseTreeNode) {
                return ((ParameterDatabaseTreeNode)parent).getChildCount(visibleLeaves);
                }
            }
        
        return ((TreeNode)parent).getChildCount();
        }
    
    /**
     * @param parent
     * @param comp
     */
    public void sort(Object parent, Comparator comp) {
        ((ParameterDatabaseTreeNode)parent).sort(comp);
        }
    }
