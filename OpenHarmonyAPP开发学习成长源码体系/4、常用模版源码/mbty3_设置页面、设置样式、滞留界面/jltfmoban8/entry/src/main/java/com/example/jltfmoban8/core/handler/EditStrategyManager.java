package com.example.jltfmoban8.core.handler;

import com.example.jltfmoban8.core.strategy.IEditStrategy;
import com.example.jltfmoban8.core.exceptions.EditActionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Edit Strategy Manager
 * Manage Image Edit Strategies
 */
public class EditStrategyManager {
    private Map<String, IEditStrategy> strategyMap = new HashMap<>();

    /**
     * Obtains a Strategy
     *
     * @param name Strategy name
     * @return IEditStrategy instance
     */
    public IEditStrategy get(String name) {
        return strategyMap.get(name);
    }

    /**
     * Check whether a strategy exists
     *
     * @param name Strategy name
     * @return True if strategy already added, otherwise False.
     */
    public boolean has(String name) {
        return strategyMap.containsKey(name);
    }

    /**
     * Add edit strategy to manager
     *
     * @param strategy Image edit strategy
     * @throws EditActionException Add strategy exceptions
     */
    public void addStrategy(IEditStrategy strategy) throws EditActionException {
        String strgyName = strategy.getName();
        if (strgyName == null || strgyName.isEmpty()) {
            throw new EditActionException("Strategy Name is undefined");
        }
        if (strategyMap.containsKey(strgyName)) {
            throw new EditActionException("EditStrategy already exists: " + strgyName);
        }

        strategyMap.put(strgyName, strategy);
    }

    /**
     * Obtains all loaded strategies
     *
     * @return Set of names of strategies
     */
    public Set<String> getAllLoadedStrategies() {
        return strategyMap.keySet();
    }

}
