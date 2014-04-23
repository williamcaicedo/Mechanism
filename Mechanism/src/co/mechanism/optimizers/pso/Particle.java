package co.mechanism.optimizers.pso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.mechanism.core.DefaultSearchAgent;
import co.mechanism.core.ICostFunction;
import co.mechanism.core.ISearchAgent;
import co.mechanism.utils.MersenneTwisterFast;
import co.mechanism.utils.Utils;

public class Particle extends DefaultSearchAgent {

    private int dimensions;
    private double bestValue;
    private SwarmOptimizer swarm;
    private List<Particle> informants;
    private List<Double> velocity;
    private List<Double> bestPosition;
    private List<Double> bestInformantPosition;
    private double selfConfidence;
    private double maxConfidence;
    private final int numberOfInformants;

    public Particle(SwarmOptimizer swarm, List<Double> position, int numberOfInformants) {
        super(position);
        this.selfConfidence = 0.729;
        this.maxConfidence = 1.494;
        this.swarm = swarm;
        this.dimensions = position.size();
        this.velocity = new ArrayList<Double>(dimensions);
        this.bestPosition = new ArrayList<Double>(dimensions);
        this.informants = new ArrayList<Particle>(numberOfInformants);
        this.numberOfInformants = numberOfInformants;
        this.bestValue = Double.POSITIVE_INFINITY;

    }

    public Particle(SwarmOptimizer swarm, List<Double> position, int numberOfInformants, double phi) {
        super(position);
        this.selfConfidence = 1 / (phi - 1 + Math.sqrt(phi * phi - 2 * phi));
        this.maxConfidence = phi * this.selfConfidence;
        this.swarm = swarm;
        this.dimensions = position.size();
        this.velocity = new ArrayList<Double>(dimensions);
        this.bestPosition = new ArrayList<Double>(dimensions);
        this.informants = new ArrayList<Particle>(numberOfInformants);
        this.numberOfInformants = numberOfInformants;
        this.bestValue = Double.POSITIVE_INFINITY;
    }

    @Override
    public Double evaluate(ICostFunction f) {
        Double r = super.evaluate(f);
         if (r < this.bestValue) {
                this.bestPosition = Utils.doubleListDeepCopy(this.getPosition()); 
                //this.bestPosition = this.position;
                this.bestValue = r.doubleValue();
        }
        return r;
    }

    public List<Particle> getInformants() {
        return informants;
    }

    public void setInformants(List<Particle> val) {
        this.informants = val;
    }

    public List<Double> getBestInformantPosition() {
        return bestInformantPosition;
    }

    public List<Double> getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(List<Double> bestPosition) {
        this.bestPosition = bestPosition;
    }

    public List<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(List<Double> velocity) {
        this.velocity = velocity;
    }

    public double getBestValue() {
        return bestValue;
    }

//    public double getValue() {
//        return result;
//    }

    private void selectBestInformantPosition() {
//        double best = this.informants.get(0).getValue();
//        int index = 0;
//        for (int i = 0; i < this.informants.size(); i++) {
//            Particle p = this.informants.get(i);
//            if (p.getValue() < best) {
//                best = p.getValue();
//                index = i;
//            }
//        }
//        this.bestInformantPosition = this.getInformants().get(index).getPosition(); 
        this.bestInformantPosition = Collections.min(this.informants).getBestPosition();

    }

    public void move() {
        double newVelocity;
        double newPosition;
        this.selectInformants();
        this.selectBestInformantPosition();
        MersenneTwisterFast mt = Utils.getMTInstance();
        for (int i = 0; i < this.dimensions; i++) {
            newVelocity = selfConfidence * this.velocity.get(i) + this.maxConfidence * mt.nextDouble() * (this.bestPosition.get(i) - this.getPosition().get(i))
                    + this.maxConfidence * mt.nextDouble() * (this.bestInformantPosition.get(i) - this.getPosition().get(i));
            newPosition = this.getPosition().get(i) + newVelocity;
            //Check and confine particle if needed
            if (newPosition < this.swarm.getLowerBound().get(i)) {
                //this.velocity.set(i,0d);
                newVelocity = -newVelocity * 0.5;
                newPosition = swarm.getUpperBound().get(i);
            } else {
                if (newPosition > this.swarm.getUpperBound().get(i)) {
                    //this.velocity.set(i,0d);
                    newVelocity = -newVelocity * 0.5;
                    newPosition = swarm.getUpperBound().get(i);
                }
            }
            this.velocity.set(i, newVelocity);
            this.getPosition().set(i, newPosition);
        }

    }

    private void selectInformants() {
        int max = swarm.getPopulation().size();
        int informantIndex;
        List<Integer> pIndexes = new ArrayList<Integer>(this.informants.size());
        MersenneTwisterFast mt = Utils.getMTInstance();
        this.informants.clear();
        int i = 0;
        while (i < this.numberOfInformants) {
            informantIndex = mt.nextInt(max);
            if (!pIndexes.contains(informantIndex)) {
                pIndexes.add(informantIndex);
                this.informants.add(swarm.getPopulation().get(informantIndex));
                i++;
            }
        }
    }

    public double getMaxConfidence() {
        return maxConfidence;
    }

    public double getSelfConfidence() {
        return selfConfidence;
    }

     @Override
    public int compareTo(ISearchAgent agent) {
        Particle p = (Particle)agent; 
        if (this.getBestValue() < p.getBestValue()) return -1;
        if (this.getBestValue() > p.getBestValue()) return 1;else return 0;
       //return (int)(this.value - cell.getValue());
    }
    
}
