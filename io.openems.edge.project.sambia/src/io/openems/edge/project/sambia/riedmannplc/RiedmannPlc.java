package io.openems.edge.project.sambia.riedmannplc;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.channel.AccessMode;
import io.openems.common.types.OpenemsType;
import io.openems.edge.bridge.modbus.api.AbstractOpenemsModbusComponent;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.DummyRegisterElement;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC16WriteRegistersTask;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.taskmanager.Priority;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Project.Sambia.RiedmannPLC", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class RiedmannPlc extends AbstractOpenemsModbusComponent implements OpenemsComponent {

	@Reference
	protected ConfigurationAdmin cm;

	public RiedmannPlc() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				ChannelId.values() //
		);
	}

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}

	@Activate
	void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm, "Modbus",
				config.modbus_id());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		SET_PIVOT_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_BOREHOLE1_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_BOREHOLE2_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_BOREHOLE3_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_CLIMA1_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_CLIMA2_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_OFFICE_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_TRAINEE_CENTER_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SIGNAL_BUS1_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SIGNAL_BUS2_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SIGNAL_GRID_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SIGNAL_SYSTEM_STOP(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SIGNAL_WATCHDOG(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //

		SET_WATERLEVEL_BOREHOLE1_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_WATERLEVEL_BOREHOLE1_OFF(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_WATERLEVEL_BOREHOLE2_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_WATERLEVEL_BOREHOLE2_OFF(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_WATERLEVEL_BOREHOLE3_ON(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //
		SET_WATERLEVEL_BOREHOLE3_OFF(Doc.of(OpenemsType.INTEGER) //
				.accessMode(AccessMode.WRITE_ONLY)), //

		WATERLEVEL(Doc.of(OpenemsType.INTEGER)), //
		GET_PIVOT_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_BOREHOLE1_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_BOREHOLE2_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_BOREHOLE3_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_CLIMA1_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_CLIMA2_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_OFFICE_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_TRAINEE_CENTER_ON(Doc.of(OpenemsType.INTEGER)), //
		AUTOMATIC_MODE(Doc.of(OpenemsType.INTEGER)), //
		MANUAL_MODE(Doc.of(OpenemsType.INTEGER)), //
		EMERGENCY_STOP(Doc.of(OpenemsType.INTEGER)), //
		SWITCH_STATE_PIVOT_PUMP(Doc.of(OpenemsType.INTEGER)), //
		SWITCH_STATE_PIVOT_DRIVE(Doc.of(OpenemsType.INTEGER)), //
		ERROR(Doc.of(OpenemsType.INTEGER)), //
		GET_WATER_LEVEL_BOREHOLE1_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_WATER_LEVEL_BOREHOLE1_OFF(Doc.of(OpenemsType.INTEGER)), //
		GET_WATER_LEVEL_BOREHOLE2_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_WATER_LEVEL_BOREHOLE2_OFF(Doc.of(OpenemsType.INTEGER)), //
		GET_WATER_LEVEL_BOREHOLE3_ON(Doc.of(OpenemsType.INTEGER)), //
		GET_WATER_LEVEL_BOREHOLE3_OFF(Doc.of(OpenemsType.INTEGER)), //
		;
		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		public Doc doc() {
			return this.doc;
		}
	}

	@Override
	protected ModbusProtocol defineModbusProtocol() {
		return new ModbusProtocol(this, //
				new FC16WriteRegistersTask(0, //
						m(ChannelId.SET_PIVOT_ON, new SignedWordElement(0)), //
						m(ChannelId.SET_BOREHOLE1_ON, new SignedWordElement(1)), //
						m(ChannelId.SET_BOREHOLE2_ON, new SignedWordElement(2)), //
						m(ChannelId.SET_BOREHOLE3_ON, new SignedWordElement(3)), //
						m(ChannelId.SET_CLIMA1_ON, new SignedWordElement(4)), //
						m(ChannelId.SET_CLIMA2_ON, new SignedWordElement(5)), //
						m(ChannelId.SET_OFFICE_ON, new SignedWordElement(6)), //
						m(ChannelId.SET_TRAINEE_CENTER_ON, new SignedWordElement(7)), //
						m(ChannelId.SIGNAL_BUS1_ON, new SignedWordElement(8)), //
						m(ChannelId.SIGNAL_BUS2_ON, new SignedWordElement(9)), //
						m(ChannelId.SIGNAL_GRID_ON, new SignedWordElement(10)), //
						m(ChannelId.SIGNAL_SYSTEM_STOP, new SignedWordElement(11)), //
						m(ChannelId.SIGNAL_WATCHDOG, new SignedWordElement(12)) //
				), //
				new FC16WriteRegistersTask(20, //
						m(ChannelId.SET_WATERLEVEL_BOREHOLE1_ON, new SignedWordElement(20)), //
						m(ChannelId.SET_WATERLEVEL_BOREHOLE1_OFF, new SignedWordElement(21)), //
						m(ChannelId.SET_WATERLEVEL_BOREHOLE2_ON, new SignedWordElement(22)), //
						m(ChannelId.SET_WATERLEVEL_BOREHOLE2_OFF, new SignedWordElement(23)), //
						m(ChannelId.SET_WATERLEVEL_BOREHOLE3_ON, new SignedWordElement(24)), //
						m(ChannelId.SET_WATERLEVEL_BOREHOLE3_OFF, new SignedWordElement(25)) //
				), //
				new FC3ReadRegistersTask(50, Priority.LOW, //
						m(ChannelId.WATERLEVEL, new SignedWordElement(50)), //
						m(ChannelId.GET_PIVOT_ON, new SignedWordElement(51)), //
						m(ChannelId.GET_BOREHOLE1_ON, new SignedWordElement(52)), //
						m(ChannelId.GET_BOREHOLE2_ON, new SignedWordElement(53)), //
						m(ChannelId.GET_BOREHOLE3_ON, new SignedWordElement(54)), //
						m(ChannelId.GET_CLIMA1_ON, new SignedWordElement(55)), //
						m(ChannelId.GET_CLIMA2_ON, new SignedWordElement(56)), //
						m(ChannelId.GET_OFFICE_ON, new SignedWordElement(57)), //
						m(ChannelId.GET_TRAINEE_CENTER_ON, new SignedWordElement(58)), //
						m(ChannelId.AUTOMATIC_MODE, new SignedWordElement(59)), //
						m(ChannelId.MANUAL_MODE, new SignedWordElement(60)), //
						m(ChannelId.EMERGENCY_STOP, new SignedWordElement(61)), //
						m(ChannelId.SWITCH_STATE_PIVOT_PUMP, new SignedWordElement(62)), //
						m(ChannelId.SWITCH_STATE_PIVOT_DRIVE, new SignedWordElement(63)), //
						m(ChannelId.ERROR, new SignedWordElement(64)), //
						new DummyRegisterElement(65, 69), //
						m(ChannelId.GET_WATER_LEVEL_BOREHOLE1_ON, new SignedWordElement(70)), //
						m(ChannelId.GET_WATER_LEVEL_BOREHOLE1_OFF, new SignedWordElement(71)), //
						m(ChannelId.GET_WATER_LEVEL_BOREHOLE2_ON, new SignedWordElement(72)), //
						m(ChannelId.GET_WATER_LEVEL_BOREHOLE2_OFF, new SignedWordElement(73)), //
						m(ChannelId.GET_WATER_LEVEL_BOREHOLE3_ON, new SignedWordElement(74)), //
						m(ChannelId.GET_WATER_LEVEL_BOREHOLE3_OFF, new SignedWordElement(75)) //
				));
	}
}
