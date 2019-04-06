package io.openems.edge.demo.arduino;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "Demo Arduino", //
		description = "Reads Voltage from an Arduino")
@interface Config {

	String id() default "arduino0";

	boolean enabled() default true;

	@AttributeDefinition(name = "Port-Name", description = "The name of the serial port - e.g. '/dev/ttyUSB0' or 'COM3'")
	String portName() default "/dev/ttyUSB0";

	String webconsole_configurationFactory_nameHint() default "Demo Arduino [{id}]";
}