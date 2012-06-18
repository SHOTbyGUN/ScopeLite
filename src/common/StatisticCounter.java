/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author SHOTbyGUN
 */
public class StatisticCounter {
    
    private int sum;
    private int[] pool;
    private int i, a;
    
    public StatisticCounter(int amount) {
        this.pool = new int[amount];
        i = 0;
    }
    
    public int getAverage() {
        sum = 0;
        for(a = 0; a < pool.length; a++) {
            sum += pool[a]; 
        }
        return sum / pool.length;
    }
    
    public void setValue(int newValue) {
        pool[i] = newValue;
        if(++i >= pool.length)
            i = 0;
    }
    
    public int getMin() {
        sum = Integer.MAX_VALUE;
        for(a = 0; a < pool.length; a++) {
            if(pool[a] < sum)
                sum = pool[a];
        }
        return sum;
    }
    
    public int getMax() {
        sum = Integer.MIN_VALUE;
        for(a = 0; a < pool.length; a++) {
            if(pool[a] > sum)
                sum = pool[a];
        }
        return sum;
    }
    
    public int getSum() {
        sum = 0;
        for(a = 0; a < pool.length; a++) {
            sum += pool[a];
        }
        return sum;
    }
    
}
