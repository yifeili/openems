package io.openems.common.jsonrpc.request;

import java.util.TreeSet;
import java.util.UUID;

import com.google.gson.JsonObject;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.jsonrpc.base.GenericJsonrpcRequest;
import io.openems.common.jsonrpc.base.JsonrpcRequest;
import io.openems.common.types.ChannelAddress;
import io.openems.common.utils.JsonUtils;

/**
 * Represents a JSON-RPC Request to subscribe to Channels.
 * 
 * This is used by UI to get regular updates on specific channels.
 * 
 * <pre>
 * {
 *   "jsonrpc":"2.0",
 * 		"id":"UUID",
 * 		"method":"setBatteryCellUnderVoltageProtection",
 *   	"params":{
 * 			"bmsid": "bms1",
 *     		"cellUnderVoltageProtection": 2800,
 * 			"cellUnderVoltageProtectionRecover": 2850
 *     	}      
 *   }
 * }
 * </pre>
 */
public class SetCellUnderVoltageProtectionRequest extends JsonrpcRequest {

	public final static String METHOD = "setCellUnderVoltageProtection";

	private final static String BMSID = "bmsid";
	private final static String CELL_UNDER_VOLTAGE_PROTECTION = "cellUnderVoltageProtection";
	private final static String CELL_UNDER_VOLTAGE_PROTECTION_RECOVER = "cellUnderVoltageProtectionRecover";

	private String bmsId;
	private int cellUnderVoltageProtection = 0;
	private int cellUnderVoltageProtectionRecover = 0;

	public static SetCellUnderVoltageProtectionRequest from(JsonrpcRequest r) throws OpenemsNamedException {
		JsonObject p = r.getParams();
		String bmsid = JsonUtils.getAsString(p, BMSID);
		int cellUnderVoltageProtection = JsonUtils.getAsInt(p, CELL_UNDER_VOLTAGE_PROTECTION);
		int cellUnderVoltageProtectionRecover = JsonUtils.getAsInt(p, CELL_UNDER_VOLTAGE_PROTECTION_RECOVER);

		SetCellUnderVoltageProtectionRequest result = new SetCellUnderVoltageProtectionRequest(//
				r.getId(), METHOD, bmsid, cellUnderVoltageProtection, cellUnderVoltageProtectionRecover //
		);

		return result;
	}

	public SetCellUnderVoltageProtectionRequest( //
			UUID id, String method, String bmsId, int cellUnderVoltageProtection, int cellUnderVoltageProtectionRecover //
	) { //
		super(id, method);
		this.bmsId = bmsId;
		this.cellUnderVoltageProtection = cellUnderVoltageProtection;
		this.cellUnderVoltageProtectionRecover = cellUnderVoltageProtectionRecover;
	}


	public static SetCellUnderVoltageProtectionRequest from(JsonObject j) throws OpenemsNamedException {
		return from(GenericJsonrpcRequest.from(j));
	}

	private final TreeSet<ChannelAddress> channels = new TreeSet<>();

	public SetCellUnderVoltageProtectionRequest(UUID id, int count) {
		super(id, METHOD);
	}

	public SetCellUnderVoltageProtectionRequest(int count) {
		this(UUID.randomUUID(), count);
	}

	public TreeSet<ChannelAddress> getChannels() {
		return channels;
	}

	public String getBmsId() {
		return bmsId;
	}
	
	public int getCellUnderVoltageProtection() {
		return cellUnderVoltageProtection;
	}
	
	public int getCellUnderVoltageProtectionRecover() {
		return cellUnderVoltageProtectionRecover;
	}

	@Override
	public JsonObject getParams() {
		return JsonUtils.buildJsonObject() //
				.addProperty(BMSID, this.getBmsId()) //
				.addProperty(CELL_UNDER_VOLTAGE_PROTECTION, this.getCellUnderVoltageProtection()) //
				.addProperty(CELL_UNDER_VOLTAGE_PROTECTION_RECOVER, this.getCellUnderVoltageProtectionRecover()) //
				.build();
	}
}
