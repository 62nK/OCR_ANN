public interface Constants {

    /** Neural Network **/
    // Neuron Configuration
    int LAYER_COUNT = 3;
    int INPUT_LAYER_NEURON_COUNT = 1024;
    int HIDDEN_LAYER_NEURON_COUNT = 32;
    int OUTPUT_LAYER_NEURON_COUNT = 26;

    /** FLAGS **/
    // Debug: std output messages
    boolean DEBUG = true;

    // Training
    int EPOCHS = 1;
    double LEARNING_RATE = 0.1; // -1 for learning rate = 1/(1+epoch#)
    double MOMENTUM = 0.2;

    // IO
    String PATH = "src/cfg/";
    String BACKUP = "backup.ann";
    String FILENAME = LAYER_COUNT+"L"+INPUT_LAYER_NEURON_COUNT+"I"+HIDDEN_LAYER_NEURON_COUNT+"H"+OUTPUT_LAYER_NEURON_COUNT+"O.ann";

    // JavaFX
    double IMAGE_SIZE = 200;
    double HEIGHT = 315;
    double WIDTH = 465;
}
