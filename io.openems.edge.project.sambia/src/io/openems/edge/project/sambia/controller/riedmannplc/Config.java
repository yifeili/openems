package io.openems.edge.project.sambia.controller.riedmannplc;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "Project Sambia Controller Riedmann PLC", //
		description = "")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ctrlSambiaRiedmannPlc0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Ess-ID", description = "ID of Ess device. The Ess to stop on system stop. Also used for Off-Grid indication for the SPS.")
	String ess_id();
	
	@AttributeDefinition(name = "Riedmann-SPS-ID", description = "ID of Riedmann PLC device.")
	String plc_id();

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

	@AttributeDefinition(name = "Soc Hysteresis", description = "Hysteresis for the switching of the loads.")
	int socHysteresis();
	
	@AttributeDefinition(name = "Soc Load 1 Off", description = "Below this Soc the Load 1 (Clima1 & Clima2) will be disconnected.")
	int socLoad1Off();
	
	@AttributeDefinition(name = "Soc Load 2 Off", description = "Below this Soc the Load 2 (Pivot) will be disconnected.")
	int socLoad2Off();
	
	@AttributeDefinition(name = "Soc Load 3 Off", description = "Below this Soc the Load 3 (Borehole 1, 2 & 3) will be disconnected.")
	int socLoad3Off();

	@AttributeDefinition(name = "Soc Load 4 Off", description = "Below this Soc the Load 4 (Office & TraineeCenter) will be disconnected.")
	int socLoad4Off();
	
	String webconsole_configurationFactory_nameHint() default "Project Sambia Controller Riedmann PLC [{id}]";
	
}