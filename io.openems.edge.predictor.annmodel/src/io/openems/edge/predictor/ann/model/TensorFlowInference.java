package io.openems.edge.predictor.ann.model;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;

/**
 * Testing TensorFlow inference, by loading a previously saved model that was trained with Python
 * 
 * @author Jan Seidemann
 */
public class TensorFlowInference {
	
	public static void main(String[] args) {
		// load tensorflow model
        SavedModelBundle savedModelBundle = SavedModelBundle.load("resources/reg_nn", "serve");

        // predict output for one test sample
        float[][] input = { {-0.39982927F, -0.48361547F, -0.86940196F, -0.25683275F, -0.3615597F,
                -0.39790979F, -0.84607575F,  0.52864277F, -0.51114231F, -1.094663F,
                0.78447637F,  0.44807713F, -0.41415936F} };
        Tensor<?> result = savedModelBundle.session().runner()
                .feed("dense_10_input", Tensor.create(input))
                .fetch("dense_12/BiasAdd")
                .run().get(0);

        float[][] output = new float[1][1];
        result.copyTo(output);
        System.out.println(output[0][0]);
	}

}
