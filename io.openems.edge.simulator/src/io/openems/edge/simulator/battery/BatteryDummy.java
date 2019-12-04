package io.openems.edge.simulator.battery;

import java.util.concurrent.CompletableFuture;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.openems.common.exceptions.OpenemsError;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.jsonrpc.base.JsonrpcMessage;
import io.openems.common.jsonrpc.base.JsonrpcRequest;
import io.openems.common.jsonrpc.base.JsonrpcResponseSuccess;
import io.openems.common.session.Role;
import io.openems.common.session.User;
import io.openems.common.utils.JsonUtils;
import io.openems.edge.battery.api.Battery;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.jsonapi.JsonApi;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Simulator.Bms", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE //
)
public class BatteryDummy extends AbstractOpenemsComponent implements Battery, OpenemsComponent, EventHandler, JsonApi {

	public final static String METHOD_SET_BATTERY_CELL_UNDERVOLTAGE_PROTECTION = "setBatteryCellUnderVoltageProtection";
	public final static String CELL_UNDER_VOLTAGE_PROTECTION = "cellUnderVoltageProtection";
	public final static String CELL_UNDER_VOLTAGE_PROTECTION_RECOVER = "cellUnderVoltageProtectionRecover";
	
	private int disChargeMinVoltage;
	private int chargeMaxVoltage;
	private int disChargeMaxCurrent;
	private int chargeMaxCurrent;
	private int soc;
	private int soh;
	private int temperature;
	private int capacityKWh;
	private int voltage;
	private int minCellVoltage; // in mV

	public BatteryDummy() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Battery.ChannelId.values() //
		);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.disChargeMinVoltage = config.disChargeMinVoltage();
		this.chargeMaxVoltage = config.chargeMaxVoltage();
		this.disChargeMaxCurrent = config.disChargeMaxCurrent();
		this.chargeMaxCurrent = config.chargeMaxCurrent();
		this.soc = config.soc();
		this.soh = config.soh();
		this.temperature = config.temperature();
		this.capacityKWh = config.capacityKWh();
		this.voltage = config.voltage();
		this.minCellVoltage = config.minCellVoltage_mV();
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE:
			this.updateChannels();
			break;
		}
	}

	private void updateChannels() {
		this.getDischargeMinVoltage().setNextValue(this.disChargeMinVoltage);
		this.getChargeMaxVoltage().setNextValue(this.chargeMaxVoltage);
		this.getDischargeMaxCurrent().setNextValue(this.disChargeMaxCurrent);
		this.getChargeMaxCurrent().setNextValue(this.chargeMaxCurrent);
		this.getSoc().setNextValue(this.soc);
		this.getSoh().setNextValue(this.soh);
		this.getMinCellTemperature().setNextValue(this.temperature);
		this.getMaxCellTemperature().setNextValue(this.temperature);
		this.getCapacity().setNextValue(this.capacityKWh);

		this.getVoltage().setNextValue(this.voltage);
		this.getMinCellVoltage().setNextValue(this.minCellVoltage);
		this.getMaxCellVoltage().setNextValue(this.minCellVoltage);

		this.getReadyForWorking().setNextValue(true);
	}

	@Override
	public CompletableFuture<? extends JsonrpcResponseSuccess> handleJsonrpcRequest(//
			User user, JsonrpcRequest request) throws OpenemsNamedException //
	{
		user.assertRoleIsAtLeast("handleJsonrpcRequest", Role.ADMIN);

		JsonObject o = request.getParams();
		String method = o.get(JsonrpcMessage.METHOD).getAsString();
		int cellUnderVoltageProtection = o.get(CELL_UNDER_VOLTAGE_PROTECTION).getAsInt();
		int cellUnderVoltageProtectionRecover = o.get(CELL_UNDER_VOLTAGE_PROTECTION_RECOVER).getAsInt();

		switch (method) {

		case METHOD_SET_BATTERY_CELL_UNDERVOLTAGE_PROTECTION:
			return this.setCellUnderVoltageProtection(//
					request,//
					cellUnderVoltageProtection,//					
					cellUnderVoltageProtectionRecover//
			);

		default:
			throw OpenemsError.JSONRPC_UNHANDLED_METHOD.exception(request.getMethod());
		}
	}

	private CompletableFuture<? extends JsonrpcResponseSuccess> setCellUnderVoltageProtection( //
			JsonrpcRequest request, //
			int cellUnderVoltageProtection,//
			int cellUnderVoltageProtectionRecover//
	) throws OpenemsNamedException { //

		JsonObject message = new JsonObject();
		message.add(JsonrpcRequest.ID, new JsonPrimitive(request.getId().toString()));

		try {
			System.out.println("SETTING RANGES SUCCESSFUL!");
			message.add(JsonrpcRequest.RESULT, JsonUtils.parse("{message: \"Set the ranges was successful\"}"));
		} catch (Exception e) {
			message.add(JsonrpcRequest.RESULT, JsonUtils.parse("{message: \"Set the ranges was not successful\"}"));
			message.add(JsonrpcRequest.ERROR, JsonUtils.parse("{errormessage: \"" + e.getMessage() + "\"}"));
		}

		JsonrpcResponseSuccess response = JsonrpcResponseSuccess.from(message);
		return CompletableFuture.completedFuture(response);
	}

}
