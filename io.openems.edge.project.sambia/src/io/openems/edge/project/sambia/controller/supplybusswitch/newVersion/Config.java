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

//	@AttributeDefinition(name = "Pivot On", description = "Pivot On.")
//	String setPivotOn() default "sps0/setPivotOn";
//
//	@AttributeDefinition(name = "Office On", description = "Office On.")
//	String setOfficeOn() default "sps0/setOfficeOn";
//
//	@AttributeDefinition(name = "Trainee Center", description = "Trainee Center.")
//	String setTraineeCenterOn() default "sps0/setTraineeCenterOn";
//
//	@AttributeDefinition(name = "Borehole 1 On", description = "Borehole 1 On.")
//	String setBorehole1On() default "sps0/setBorehole1On";
//
//	@AttributeDefinition(name = "Borehole 2 On", description = "Borehole 2 On.")
//	String setBorehole2On() default "sps0/setBorehole2On";
//
//	@AttributeDefinition(name = "Borehole 3 On", description = "Borehole 3 On.")
//	String setBorehole3On() default "sps0/setBorehole3On";
//
//	@AttributeDefinition(name = "Clima 1 On", description = "Clima 1 On.")
//	String setClima1On() default "sps0/setClima1On";
//
//	@AttributeDefinition(name = "Clima 2 On", description = "Clima 2 On.")
//	String setClima2On() default "sps0/setClima2On";

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBus1Config() default "[{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput0\"},{\"ess\": \"ess2\",\"switchAddress\": \"io2/InputOutput1\"},{\"ess\": \"ess3\",\"switchAddress\": \"io2/InputOutput2\"}]";

	@AttributeDefinition(name = "Supply-Bus", description = "Collection of the switches for the supplyBus. Each array represents the switches for one supply bus.")
	String supplyBus2Config() default "[{\"ess\": \"ess1\",\"switchAddress\": \"io2/InputOutput3\"},{\"ess\": \"ess2\",\"switchAddress\": \"io2/InputOutput4\"},{\"ess\": \"ess3\",\"switchAddress\": \"io2/InputOutput5\"}]";

	String webconsole_configurationFactory_nameHint() default "Project Sambia Controller Supply-Bus-Switch New Version [{id}]";

}
