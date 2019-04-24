package ArtificialNeuralNetwork;

import java.util.HashMap;
import java.util.Map;

public class Neuron implements NetworkInterface{

    // Parameters
    private int layerID, neuronID;
    private TYPE neuronType;
    private double[] dendrites, synapses; // inputs and weights
    private double axon; // output
    private double[] previousEpochDelta, currentEpochDeltaAccumulator;

    // Constructors
    public Neuron(int layerID, int neuronID, TYPE neuronType, int dendritesCount){
        this.layerID = layerID;
        this.neuronID = neuronID;
        this.neuronType = neuronType;
        dendrites = new double[dendritesCount];
        synapses = new double[dendritesCount];
        previousEpochDelta = new double[dendritesCount];
        currentEpochDeltaAccumulator = new double[dendritesCount];
        initSynapses();
    }
    public Neuron(Map<String, Double> synapseMap, int layerID, int neuronID, TYPE neuronType, int dendritesCount){
        this.layerID = layerID;
        this.neuronID = neuronID;
        this.neuronType = neuronType;
        dendrites = new double[dendritesCount];
        synapses = new double[dendritesCount];
        previousEpochDelta = new double[dendritesCount];
        currentEpochDeltaAccumulator = new double[dendritesCount];
        for (int index=0; index<synapses.length; index++)
            synapses[index] = synapseMap.get("sy"+index);
    }

    // Methods
    @Override
    public void feedForward(double[] input){
        if(neuronType == TYPE.input)
            setDendrites(new double[] {input[neuronID]});
        else
            setDendrites(input);
        axon = squashing(sum(dendrites));
    }
    public double[] backPropagate(double learningRate, double deltaTargetObtained, double alpha, boolean isNewEpoch){
        double[] correctionArray = new double[synapses.length];
        for(int index=0; index<synapses.length; index++) {
            if(isNewEpoch) {
                previousEpochDelta[index] = currentEpochDeltaAccumulator[index];
                currentEpochDeltaAccumulator[index] = 0;
            }
            double delta = difference(index, learningRate, deltaTargetObtained);
            currentEpochDeltaAccumulator[index] += delta;
            correctionArray[index] = delta(deltaTargetObtained)*synapses[index];
            synapses[index] += delta + alpha * previousEpochDelta[index];
        }
        return correctionArray;
    }

    // Getters
    public double getAxon(){
        return axon;
    }

    // Setters
    public void setDendrites(double[] dendrites){
        this.dendrites = dendrites;
    }
    @Override
    public void resetSynapses(){
        initSynapses();
    }

    // Services
    private void initSynapses(){
        if(neuronType == TYPE.input)
            synapses[0]=1;
        else
            for(int index=0; index<synapses.length; index++)
                synapses[index] = Math.random()-0.5;
    }
    private double squashing(double s){
        if(neuronType== TYPE.input)
            axon = s;
        else
            switch (SQUASHING_FUNCTION){
                case sign:
                    axon = s>0?1:-1;
                    break;
                case step:
                    axon = s>0?1:0;
                    break;
                case linear:
                    axon = s;
                    break;
                case sigmoid:
                    axon = sigmoid(s);
                    break;
            }
        return axon;
    }
    private double sigmoid(double s){
        return 1.0/(1+Math.exp(-s));
//        return Math.tanh(s);
    }

    private double sum(double[] dendrites){
        double sum = 0;
        for(int index=0; index<dendrites.length; index++)
            sum += dendrites[index]*synapses[index];
        return sum;
    }
    private double delta(double deltaTargetObtained){
        return axon*(1-axon)*deltaTargetObtained;
//        return 1- Math.tanh(deltaTargetObtained)*Math.tanh(deltaTargetObtained);
    }
    private double difference(int dendriteIndex, double learningRate, double deltaTargetObtained){
        return learningRate*delta(deltaTargetObtained)*dendrites[dendriteIndex];
    }

    // IO
    public Map<String, Double> toMap(){
        Map<String, Double> synapseMap = new HashMap<>();
        for(int i=0; i<synapses.length; i++)
            synapseMap.put("sy"+i, synapses[i]);
        return synapseMap;
    }
    public void toNeurons(Map<String, Double> synapseMap){
        for(int i=0; i<synapses.length; i++)
            synapses[i] = synapseMap.get("sy"+i);
    }

    @Override
    public String outputToString() {
        return null;
    }
}
