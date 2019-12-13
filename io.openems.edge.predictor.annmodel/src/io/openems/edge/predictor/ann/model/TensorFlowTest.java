package io.openems.edge.predictor.ann.model;

import java.io.UnsupportedEncodingException;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

/**
 * Testing TensorFlow, taken from the tutorial:
 * <p>https://www.tensorflow.org/install/lang_java</p>
 * 
 * @author Jan Seidemann
 */
public class TensorFlowTest {

	public static void main(String[] args) {
		try (Graph g = new Graph()) {
			final String value = "Hello from " + TensorFlow.version();

			try (Tensor<?> t = Tensor.create(value.getBytes("UTF-8"))) {
				g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try (Session s = new Session(g); Tensor<?> output = s.runner().fetch("MyConst").run().get(0)) {
				System.out.println(new String(output.bytesValue(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
