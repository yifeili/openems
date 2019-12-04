package io.openems.common.jsonrpc.base;

import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.utils.JsonUtils;
import io.openems.common.utils.StringUtils;

/**
 * Represents a JSON-RPC Message.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   ...
 * }
 * </pre>
 * 
 * @see <a href="https://www.jsonrpc.org/specification">JSON-RPC
 *      specification</a>
 */
public abstract class JsonrpcMessage {



	public final static String JSONRPC_VERSION = "2.0";
	
	private static final String JSON_ELEMENT_JSONRPC = "jsonrpc";
	public static final String JSON_ELEMENT_METHOD = "method";
	public static final String JSON_ELEMENT_RESULT = "result";
	public static final String JSON_ELEMENT_ID = "id";
	public static final String JSON_ELEMENT_ERROR = "error";
	private static final String JSON_ELEMENT_PARAMS = "params";

	public static JsonrpcMessage from(String json) throws OpenemsNamedException {
		return from(JsonUtils.parseToJsonObject(json));
	}

	public static JsonrpcMessage from(JsonObject j) throws OpenemsNamedException {
		if (j.has(JSON_ELEMENT_METHOD) && j.has(JsonrpcMessage.JSON_ELEMENT_PARAMS)) {
			if (j.has(JsonrpcMessage.JSON_ELEMENT_ID)) {
				return GenericJsonrpcRequest.from(j);
			} else {
				return GenericJsonrpcNotification.from(j);
			}

		} else if (j.has(JSON_ELEMENT_RESULT)) {
			return GenericJsonrpcResponseSuccess.from(j);

		} else if (j.has(JSON_ELEMENT_ERROR)) {
			return JsonrpcResponseError.from(j);
		}
		throw new OpenemsException(
				"JsonrpcMessage is not a valid Request, Result or Notification: " + StringUtils.toShortString(j, 100));
	}

	public JsonObject toJsonObject() {
		return JsonUtils.buildJsonObject() //
				.addProperty(JsonrpcMessage.JSON_ELEMENT_JSONRPC, JSONRPC_VERSION) //
				.build();
	}

	/**
	 * Returns this JsonrpcMessage as a JSON String
	 */
	@Override
	public String toString() {
		return this.toJsonObject().toString();
	}

}
