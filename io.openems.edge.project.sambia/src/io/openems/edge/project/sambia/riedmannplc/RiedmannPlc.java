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
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.channel.IntegerDoc;
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
		DEBUG_PIVOT_ON(Doc.of(OpenemsType.INTEGER)), //
		PIVOT_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_PIVOT_ON))),
		DEBUG_BOREHOLE1_ON(Doc.of(OpenemsType.INTEGER)), //
		BOREHOLE1_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_BOREHOLE1_ON))),
		DEBUG_BOREHOLE2_ON(Doc.of(OpenemsType.INTEGER)), //
		BOREHOLE2_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_BOREHOLE2_ON))),
		DEBUG_BOREHOLE3_ON(Doc.of(OpenemsType.INTEGER)), //
		BOREHOLE3_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_BOREHOLE3_ON))),
		DEBUG_CLIMA1_ON(Doc.of(OpenemsType.INTEGER)), //
		CLIMA1_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_CLIMA1_ON))),
		DEBUG_CLIMA2_ON(Doc.of(OpenemsType.INTEGER)), //
		CLIMA2_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_CLIMA2_ON))),
		DEBUG_OFFICE_ON(Doc.of(OpenemsType.INTEGER)), //
		OFFICE_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_OFFICE_ON))),
		DEBUG_TRAINEE_CENTER_ON(Doc.of(OpenemsType.INTEGER)), //
		TRAINEE_CENTER_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_TRAINEE_CENTER_ON))),

		DEBUG_SIGNAL_BUS1_ON(Doc.of(OpenemsType.INTEGER)), //
		SIGNAL_BUS1_ON(new IntegerDoc() //
				.accessMode(AccessMode.WRITE_ONLY) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_SIGNAL_BUS1_ON))),
		DEBUG_SIGNAL_BUS2_ON(Doc.of(OpenemsType.INTEGER)), //
		SIGNAL_BUS2_ON(new IntegerDoc() //
				.accessMode(AccessMode.WRITE_ONLY) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_SIGNAL_BUS2_ON))),
		DEBUG_SIGNAL_GRID_ON(Doc.of(OpenemsType.INTEGER)), //
		SIGNAL_GRID_ON(new IntegerDoc() //
				.accessMode(AccessMode.WRITE_ONLY) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_SIGNAL_GRID_ON))),
		DEBUG_SIGNAL_SYSTEM_STOP(Doc.of(OpenemsType.INTEGER)), //
		SIGNAL_SYSTEM_STOP(new IntegerDoc() //
				.accessMode(AccessMode.WRITE_ONLY) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_SIGNAL_SYSTEM_STOP))),
		DEBUG_SIGNAL_WATCHDOG(Doc.of(OpenemsType.INTEGER)), //
		SIGNAL_WATCHDOG(new IntegerDoc() //
				.accessMode(AccessMode.WRITE_ONLY) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_SIGNAL_WATCHDOG))),

		DEBUG_WATERLEVEL_BOREHOLE1_ON(Doc.of(OpenemsType.INTEGER)), //
		WATERLEVEL_BOREHOLE1_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_WATERLEVEL_BOREHOLE1_ON))),
		DEBUG_WATERLEVEL_BOREHOLE1_OFF(Doc.of(OpenemsType.INTEGER)), //
		WATERLEVEL_BOREHOLE1_OFF(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_WATERLEVEL_BOREHOLE1_OFF))),
		DEBUG_WATERLEVEL_BOREHOLE2_ON(Doc.of(OpenemsType.INTEGER)), //
		WATERLEVEL_BOREHOLE2_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_WATERLEVEL_BOREHOLE2_ON))),
		DEBUG_WATERLEVEL_BOREHOLE2_OFF(Doc.of(OpenemsType.INTEGER)), //
		WATERLEVEL_BOREHOLE2_OFF(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_WATERLEVEL_BOREHOLE2_OFF))),
		DEBUG_WATERLEVEL_BOREHOLE3_ON(Doc.of(OpenemsType.INTEGER)), //
		WATERLEVEL_BOREHOLE3_ON(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_WATERLEVEL_BOREHOLE3_ON))),
		DEBUG_WATERLEVEL_BOREHOLE3_OFF(Doc.of(OpenemsType.INTEGER)), //
		WATERLEVEL_BOREHOLE3_OFF(new IntegerDoc() //
				.accessMode(AccessMode.READ_WRITE) //
				.onInit(new IntegerWriteChannel.MirrorToDebugChannel(ChannelId.DEBUG_WATERLEVEL_BOREHOLE3_OFF))),

		WATERLEVEL(Doc.of(OpenemsType.INTEGER)), //
		AUTOMATIC_MODE(Doc.of(OpenemsType.INTEGER)), //
		MANUAL_MODE(Doc.of(OpenemsType.INTEGER)), //
		EMERGENCY_STOP(Doc.of(OpenemsType.INTEGER)), //
		SWITCH_STATE_PIVOT_PUMP(Doc.of(OpenemsType.INTEGER)), //
		SWITCH_STATE_PIVOT_DRIVE(Doc.of(OpenemsType.INTEGER)), //
		ERROR(Doc.of(OpenemsType.INTEGER)) //
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
						m(ChannelId.PIVOT_ON, new SignedWordElement(0)), //
						m(ChannelId.BOREHOLE1_ON, new SignedWordElement(1)), //
						m(ChannelId.BOREHOLE2_ON, new SignedWordElement(2)), //
						m(ChannelId.BOREHOLE3_ON, new SignedWordElement(3)), //
						m(ChannelId.CLIMA1_ON, new SignedWordElement(4)), //
						m(ChannelId.CLIMA2_ON, new SignedWordElement(5)), //
						m(ChannelId.OFFICE_ON, new SignedWordElement(6)), //
						m(ChannelId.TRAINEE_CENTER_ON, new SignedWordElement(7)), //
						m(ChannelId.SIGNAL_BUS1_ON, new SignedWordElement(8)), //
						m(ChannelId.SIGNAL_BUS2_ON, new SignedWordElement(9)), //
						m(ChannelId.SIGNAL_GRID_ON, new SignedWordElement(10)), //
						m(ChannelId.SIGNAL_SYSTEM_STOP, new SignedWordElement(11)), //
						m(ChannelId.SIGNAL_WATCHDOG, new SignedWordElement(12)) //
				), //
				new FC16WriteRegistersTask(20, //
						m(ChannelId.WATERLEVEL_BOREHOLE1_ON, new SignedWordElement(20)), //
						m(ChannelId.WATERLEVEL_BOREHOLE1_OFF, new SignedWordElement(21)), //
						m(ChannelId.WATERLEVEL_BOREHOLE2_ON, new SignedWordElement(22)), //
						m(ChannelId.WATERLEVEL_BOREHOLE2_OFF, new SignedWordElement(23)), //
						m(ChannelId.WATERLEVEL_BOREHOLE3_ON, new SignedWordElement(24)), //
						m(ChannelId.WATERLEVEL_BOREHOLE3_OFF, new SignedWordElement(25)) //
				), //
				new FC3ReadRegistersTask(50, Priority.LOW, //
						m(ChannelId.WATERLEVEL, new SignedWordElement(50)), //
						m(ChannelId.PIVOT_ON, new SignedWordElement(51)), //
						m(ChannelId.BOREHOLE1_ON, new SignedWordElement(52)), //
						m(ChannelId.BOREHOLE2_ON, new SignedWordElement(53)), //
						m(ChannelId.BOREHOLE3_ON, new SignedWordElement(54)), //
						m(ChannelId.CLIMA1_ON, new SignedWordElement(55)), //
						m(ChannelId.CLIMA2_ON, new SignedWordElement(56)), //
						m(ChannelId.OFFICE_ON, new SignedWordElement(57)), //
						m(ChannelId.TRAINEE_CENTER_ON, new SignedWordElement(58)), //
						m(ChannelId.AUTOMATIC_MODE, new SignedWordElement(59)), //
						m(ChannelId.MANUAL_MODE, new SignedWordElement(60)), //
						m(ChannelId.EMERGENCY_STOP, new SignedWordElement(61)), //
						m(ChannelId.SWITCH_STATE_PIVOT_PUMP, new SignedWordElement(62)), //
						m(ChannelId.SWITCH_STATE_PIVOT_DRIVE, new SignedWordElement(63)), //
						m(ChannelId.ERROR, new SignedWordElement(64)), //
						new DummyRegisterElement(65, 69), //
						m(ChannelId.WATERLEVEL_BOREHOLE1_ON, new SignedWordElement(70)), //
						m(ChannelId.WATERLEVEL_BOREHOLE1_OFF, new SignedWordElement(71)), //
						m(ChannelId.WATERLEVEL_BOREHOLE2_ON, new SignedWordElement(72)), //
						m(ChannelId.WATERLEVEL_BOREHOLE2_OFF, new SignedWordElement(73)), //
						m(ChannelId.WATERLEVEL_BOREHOLE3_ON, new SignedWordElement(74)), //
						m(ChannelId.WATERLEVEL_BOREHOLE3_OFF, new SignedWordElement(75)) //
				));
	}
}
