/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.HashMap;

/**
 *
 * @author SHOTbyGUN
 */
public class IntCache {
    
    public HashMap<Object, int[]> caches = new HashMap<Object, int[]>();
    int i;
    
    public int getValue(int[] cache, int x) {
        
        i = cache[0] - x;
        if(i < 1)
            i += cache.length - 1;
        
        return cache[i];
        
    }
    
    public int[] getCache(Object cache) {
        if(caches.containsKey(cache))
            return caches.get(cache);
        else {
             // No cache... create one
            int[] returnValue = new int[scopelite.ScopeLite.maxScreenWidth];
            returnValue[0] = 1;
            //LinkedList<Integer> returnValue = new LinkedList<Integer>();
            caches.put(cache, returnValue);
            return returnValue;
        }
            
    }
    
    
    public void addValue(int[] cache, int value) {
        
        if(++cache[0] >= cache.length) {
            cache[0] = 1;
        }
        
        cache[cache[0]] = value;
            
    }
}
