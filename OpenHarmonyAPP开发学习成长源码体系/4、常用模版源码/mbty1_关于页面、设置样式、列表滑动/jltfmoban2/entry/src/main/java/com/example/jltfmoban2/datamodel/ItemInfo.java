package com.example.jltfmoban2.datamodel;

import com.example.jltfmoban2.typefactory.TypeFactory;

/**
 * Item model
 * Implement this interface to support other item model
 */
public interface ItemInfo {
    /**
     * Get layout Resource ID
     *
     * @param typeFactory typeFactory instance
     * @return Resource ID
     */
    int getType(TypeFactory typeFactory);
}
