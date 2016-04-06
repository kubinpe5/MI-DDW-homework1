/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework1.GATE;

/**
 *
 * @author Standard
 */
public class ItemFeatures {
    private double relevance;
    private int count;

    public ItemFeatures(double relevance, int count) {
        this.relevance = relevance;
        this.count = count;
    }

    public double getRelevance() {
        return relevance;
    }

    public int getCount() {
        return count;
    }
    
    public void increment() {
        count++;
    }

    @Override
    public String toString() {
        return "ItemFeatures{" + "relevance=" + relevance + ", count=" + count + '}';
    }
}
