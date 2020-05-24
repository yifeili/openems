package io.openems.edge.simulator.app;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.jsonrpc.base.JsonrpcRequest;
import io.openems.common.jsonrpc.request.CreateComponentConfigRequest;
import io.openems.common.types.ChannelAddress;
import io.openems.common.utils.JsonUtils;

/**
 * Executes a simulation.
 * 
 * <pre>
 * {
 *   "jsonrpc": "2.0",
 *   "id": "UUID",
 *   "method": "executeSimulation",
 *   "params": {
 *     components: {
 *       "componentId": {
 *     	   "factoryPid": string,
 *         "properties": [{
 *           "name": string,
 *           "value": any 
 *         }]
 *       },
 *       "clock": {
 *         "start": "yyyy-mm-ddTHH:MM:00.00Z", // ISO_INSTANT
 *         "end": "yyyy-mm-ddTHH:MM:00.00Z", // ISO_INSTANT
 *         "timeleap": number [s],
 *         "executeCycleTwice": boolean // Execute every Cycle twice to simulate immediate execution
 *       },
 *       "collect": [
 *         "_sum/GridActivePower",... // Channels to be collected for response
 *       ]
 *     }
 *   }
 * }
 * </pre>
 */
public class ExecuteSimulationRequest extends JsonrpcRequest {

	public static class Clock {
		public static Clock from(JsonObject j) throws OpenemsNamedException {
			ZonedDateTime start = ZonedDateTime.parse(JsonUtils.getAsString(j, "start"));
			ZonedDateTime end = ZonedDateTime.parse(JsonUtils.getAsString(j, "end"));
			int timeleapPerCycle = JsonUtils.getAsInt(j, "timeleapPerCycle");
			boolean executeCycleTwice = JsonUtils.getAsOptionalBoolean(j, "executeCycleTwice").orElse(false);
			return new Clock(start, end, timeleapPerCycle, executeCycleTwice);
		}

		public final ZonedDateTime start;
		public final ZonedDateTime end;
		public final int timeleapPerCycle;
		public final boolean executeCycleTwice;

		private Clock(ZonedDateTime start, ZonedDateTime end, int timeleapPerCycle, boolean executeCycleTwice) {
			this.start = start;
			this.end = end;
			this.timeleapPerCycle = timeleapPerCycle;
			this.executeCycleTwice = executeCycleTwice;
		}
	}

	public static final String METHOD = "executeSimulation";

	public static ExecuteSimulationRequest from(JsonrpcRequest r) throws OpenemsNamedException {
		JsonObject p = r.getParams();
		List<CreateComponentConfigRequest> components = new ArrayList<>();
		JsonArray jComponents = JsonUtils.getAsJsonArray(p, "components");
		for (JsonElement jComponent : jComponents) {
			components.add(CreateComponentConfigRequest.from(JsonUtils.getAsJsonObject(jComponent)));
		}
		Clock clock = Clock.from(JsonUtils.getAsJsonObject(p, "clock"));
		List<ChannelAddress> collects = new ArrayList<>();
		JsonArray jCollects = JsonUtils.getAsJsonArray(p, "collect");
		for (JsonElement jCollect : jCollects) {
			collects.add(ChannelAddress.fromString(JsonUtils.getAsString(jCollect)));
		}
		return new ExecuteSimulationRequest(r.getId(), components, clock, collects);
	}

	public final List<CreateComponentConfigRequest> components;
	public final Clock clock;
	public final List<ChannelAddress> collects;

	public ExecuteSimulationRequest(List<CreateComponentConfigRequest> components, Clock clock,
			List<ChannelAddress> collects) {
		this(UUID.randomUUID(), components, clock, collects);
	}

	public ExecuteSimulationRequest(UUID id, List<CreateComponentConfigRequest> components, Clock clock,
			List<ChannelAddress> collects) {
		super(id, METHOD);
		this.components = components;
		this.clock = clock;
		this.collects = collects;
	}

	@Override
	public JsonObject getParams() {
		return new JsonObject();
	}

}
