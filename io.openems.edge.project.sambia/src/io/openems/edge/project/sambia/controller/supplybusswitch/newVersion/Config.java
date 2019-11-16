package io.openems.edge.project.sambia.controller.supplybusswitch.newVersion;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Project Sambia Controller Supply-Bus-Switch New Version", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlSambiaSupplyBusSwitch0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Switch-Delay", description = "Delay between ess disconnected and next ess connected.")
	long switchDelay() default 10_000;

	@AttributeDefinition(name = "PLC Id", description = "PLC component id.")
	String plc_id();

	@AttributeDefinition(name = "Supply Bus1 On Indication", description = "Supply Bus1 On Indication Channel.")
	String supplyBus1OnIndication() default "sps0/SignalBus1On";

	@AttributeDefinition(name = "Supply Bus2 On Indication", description = "Supply Bus2 On Indication Channel.")
	String supplyBus2OnIndication() default "sps0/SignalBus2On";

	@AttributeDefinition(name = "Waterlevel Borehole 1 On", description = "The waterlevel to start Borehole Pump 1.")
	int setWaterLevelBorehole1On() default 50;

	@AttributeDefinition(name = "Waterlevel Borehole 1 Off", description = "The waterlevel to stop Borehole Pump 1.")
	int setWaterLevelBorehole1Off() default 100;

	@AttributeDefinition(name = "Waterlevel Borehole 2 On", description = "The waterlevel to start Borehole Pump 2.")
	int setWaterLevelBorehole2On() default 200;

	@AttributeDefinition(name = "Waterlevel Borehole 2 Off", description = "The waterlevel to stop Borehole Pump 2.")
	int setWaterLevelBorehole2Off() default 300;

	@AttributeDefinition(name = "Waterlevel Borehole 3 On", description = "The waterlevel to start Borehole Pump 3.")
	int setWaterLevelBorehole3On() default 400;

	@AttributeDefinition(name = "Waterlevel Borehole 3 Off", description = "The waterlevel to stop Borehole Pump 3.")
	int setWaterLevelBorehole3Off() default 500;

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBus1Config() default "[{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput0\"},{\"ess\": \"ess2\",\"switchAddress\": \"io2/InputOutput1\"},{\"ess\": \"ess3\",\"switchAddress\": \"io2/InputOutput2\"}]";

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBus2Config() default "[{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput3\"},{\"ess\": \"ess2\",\"switchAddress\": \"io2/InputOutput4\"},{\"ess\": \"ess3\",\"switchAddress\": \"io2/InputOutput5\"}]";

	String webconsole_configurationFactory_nameHint() default "Project Sambia Controller Supply-Bus-Switch New Version [{id}]";

}
