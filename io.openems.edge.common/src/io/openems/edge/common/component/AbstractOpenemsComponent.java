package io.openems.edge.common.component;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CaseFormat;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.types.EdgeConfig;
import io.openems.edge.common.channel.BooleanDoc;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.DoubleDoc;
import io.openems.edge.common.channel.FloatDoc;
import io.openems.edge.common.channel.IntegerDoc;
import io.openems.edge.common.channel.LongDoc;
import io.openems.edge.common.channel.ShortDoc;
import io.openems.edge.common.channel.StateChannel;
import io.openems.edge.common.channel.StringDoc;
import io.openems.edge.common.channel.internal.OpenemsTypeDoc;

/**
 * This is the default implementation of the {@link OpenemsComponent} interface.
 * 
 * {@link #activate(ComponentContext, String, boolean)} and
 * {@link #deactivate()} methods should be called by the corresponding methods
 * in the OSGi component.
 */
public abstract class AbstractOpenemsComponent implements OpenemsComponent {

	private static final String PROPERTY_CHANNEL_ID_PREFIX = "_PROPERTY_";
	private static final AtomicInteger NEXT_GENERATED_COMPONENT_ID = new AtomicInteger(-1);

	private final Logger log = LoggerFactory.getLogger(AbstractOpenemsComponent.class);

	/**
	 * Holds all Channels by their Channel-ID String representation (in
	 * CaseFormat.UPPER_CAMEL)
	 */
	private final Map<String, Channel<?>> channels = Collections.synchronizedMap(new HashMap<>());

	private String id = null;
	private String alias = null;
	private ComponentContext componentContext = null;
	private boolean enabled = true;

	/**
	 * Default constructor for AbstractOpenemsComponent.
	 * 
	 * <p>
	 * Automatically initializes (i.e. creates {@link Channel} instances for each
	 * given {@link ChannelId} using the Channel-{@link Doc}.
	 * 
	 * <p>
	 * It is important to list all Channel-ID enums of all inherited
	 * OpenEMS-Natures, i.e. for every OpenEMS Java interface you are implementing,
	 * you need to list the interface' ChannelID-enum here like
	 * Interface.ChannelId.values().
	 * 
	 * <p>
	 * Use as follows:
	 * 
	 * <pre>
	 * public YourPhantasticOpenemsComponent() {
	 * 	super(//
	 * 			OpenemsComponent.ChannelId.values(), //
	 * 			YourPhantasticOpenemsComponent.ChannelId.values());
	 * }
	 * </pre>
	 * 
	 * <p>
	 * Note: the separation in firstInitialChannelIds and furtherInitialChannelIds
	 * is only there to enforce that calling the constructor cannot be forgotten.
	 * This way it needs to be called with at least one parameter - which is always
	 * at least "OpenemsComponent.ChannelId.values()". Just use it as if it was:
	 * 
	 * <pre>
	 * AbstractOpenemsComponent(ChannelId[]... channelIds)
	 * </pre>
	 * 
	 * @param firstInitialChannelIds   the Channel-IDs to initialize.
	 * @param furtherInitialChannelIds the Channel-IDs to initialize.
	 */
	protected AbstractOpenemsComponent(io.openems.edge.common.channel.ChannelId[] firstInitialChannelIds,
			io.openems.edge.common.channel.ChannelId[]... furtherInitialChannelIds) {
		this.addChannels(firstInitialChannelIds);
		this.addChannels(furtherInitialChannelIds);
	}

	/**
	 * Handles @Activate of implementations. Prints log output.
	 * 
	 * @param context the OSGi ComponentContext
	 * @param id      the unique OpenEMS Component ID
	 * @param alias   Human-readable name of this Component. Typically
	 *                'config.alias()'. Defaults to 'id' if empty
	 * @param enabled is the Component enabled?
	 */
	protected void activate(ComponentContext context, String id, String alias, boolean enabled) {
		this.updateContext(context, id, alias, enabled);

		if (isEnabled()) {
			this.logMessage("Activate");
		} else {
			this.logMessage("Activate DISABLED");
		}
	}

	/**
	 * Handles @Modified of implementations.
	 * 
	 * @param context the OSGi ComponentContext
	 * @param id      the unique OpenEMS Component ID
	 * @param alias   Human-readable name of this Component. Typically
	 *                'config.alias()'. Defaults to 'id' if empty
	 * @param enabled is the Component enabled?
	 */
	protected void modified(ComponentContext context, String id, String alias, boolean enabled) {
		this.updateContext(context, id, alias, enabled);

		if (isEnabled()) {
			this.logMessage("Modified");
		} else {
			this.logMessage("Modified DISABLED");
		}
	}

	/**
	 * Handles @Deactivate of implementations. Prints log output.
	 */
	protected void deactivate() {
		this.logMessage("Deactivate");
		// deactivate all Channels
		for (Channel<?> channel : this.channels.values()) {
			channel.deactivate();
		}
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public ComponentContext getComponentContext() {
		if (this.componentContext == null) {
			this.logWarn(this.log,
					"ComponentContext is null. Please make sure to call AbstractOpenemsComponent.activate()-method early!");
		}
		return this.componentContext;
	}

	/**
	 * Helper method to update the Context on @Activate and @Modified.
	 * 
	 * @param context the OSGi ComponentContext
	 * @param id      the unique OpenEMS Component ID
	 * @param alias   Human-readable name of this Component. Typically
	 *                'config.alias()'. Defaults to 'id' if empty
	 * @param enabled is the Component enabled?
	 */
	private void updateContext(ComponentContext context, String id, String alias, boolean enabled) {
		if (id == null || id.trim().isEmpty()) {
			if (this.id == null) {
				this.id = "_component" + AbstractOpenemsComponent.NEXT_GENERATED_COMPONENT_ID.incrementAndGet();
			}
		} else {
			this.id = id;
		}

		if (alias == null || alias.trim().isEmpty()) {
			this.alias = this.id;
		} else {
			this.alias = alias;
		}

		this.enabled = enabled;
		this.componentContext = context;

		this.addChannelsForProperties(context);
	}

	/**
	 * Add a Channel for each Property and set the configured value.
	 * 
	 * <p>
	 * If the Property key is "enabled" then a Channel with the ID
	 * "_PropertyEnabled" is generated.
	 * 
	 * @param context the {@link ComponentContext}
	 */
	private void addChannelsForProperties(ComponentContext context) {
		if (context == null) {
			return;
		}
		Dictionary<String, Object> properties = context.getProperties();
		if (properties == null) {
			return;
		}

		Enumeration<String> keys = properties.keys();
		while (keys.hasMoreElements()) {
			try {
				String key = keys.nextElement();
				Object value = properties.get(key);
				if (EdgeConfig.ignorePropertyKey(key)) {
					// ignore
					continue;
				}
				switch (key) {
				case "alias":
					// ignore
					continue;
				case "enabled":
					// special treatment as the value always comes as a String
					if (value != null && value instanceof String) {
						value = ((String) value).toLowerCase().equals("true");
					}
				}
				key = key.replace(".", "_");

				String channelName = PROPERTY_CHANNEL_ID_PREFIX
						+ CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);

				Channel<?> channel = this.channels
						.get(io.openems.edge.common.channel.ChannelId.channelIdUpperToCamel(channelName));
				if (channel == null) {
					// Channel does not already exist -> create new Channel
					Doc doc = AbstractOpenemsComponent.getDocFromObject(value);
					io.openems.edge.common.channel.ChannelId channelId = new io.openems.edge.common.channel.ChannelId() {

						@Override
						public String name() {
							return channelName;
						}

						@Override
						public Doc doc() {
							return doc;
						}
					};
					channel = this.addChannel(channelId);
				}
				channel.setNextValue(value);

			} catch (OpenemsException e) {
				this.logWarn(this.log, "Unable to add Property Channel: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initializes the given Channel-ID.
	 * 
	 * <ul>
	 * <li>Creates an object instance from Channel-Doc
	 * <li>Registers the Channel
	 * </ul>
	 * 
	 * @param channelId the given Channel-ID
	 * @return the newly created Channel
	 */
	protected Channel<?> addChannel(io.openems.edge.common.channel.ChannelId channelId) {
		Doc doc = channelId.doc();
		Channel<?> channel = doc.createChannelInstance(this, channelId);
		this.addChannel(channel);
		return channel;
	}

	/**
	 * Adds a Channel to this Component.
	 * 
	 * @param channel the Channel
	 * @throws NullPointerException     if the Channel was not initialized.
	 * @throws IllegalArgumentException if the Channel-ID had already been added.
	 */
	private void addChannel(Channel<?> channel) throws NullPointerException, IllegalArgumentException {
		if (channel == null) {
			throw new NullPointerException(
					"Trying to add 'null' Channel. Hint: Check for missing handling of Enum value.");
		}
		if (this.channels.containsKey(channel.channelId().id())) {
			throw new IllegalArgumentException(
					"Duplicated Channel-ID [" + channel.channelId().id() + "] for Component [" + this.id + "]");
		}
		// Add Channel to channels list
		this.channels.put(channel.channelId().id(), channel);
		// Handle StateChannels
		if (channel instanceof StateChannel) {
			this.getStateChannel().addChannel((StateChannel) channel);
		}
	}

	/**
	 * Initializes the given Channel-IDs.
	 * 
	 * <ul>
	 * <li>Creates object instances from Channel-Doc
	 * <li>Registers the Channels
	 * </ul>
	 * 
	 * @param initialChannelIds the given Channel-IDs
	 */
	protected void addChannels(io.openems.edge.common.channel.ChannelId[] initialChannelIds) {
		for (io.openems.edge.common.channel.ChannelId channelId : initialChannelIds) {
			this.addChannel(channelId);
		}
	}

	/**
	 * Initializes the given Channel-IDs.
	 * 
	 * <ul>
	 * <li>Creates object instances from Channel-Doc
	 * <li>Registers the Channels
	 * </ul>
	 * 
	 * @param initialChannelIds the given Channel-IDs
	 */
	protected void addChannels(io.openems.edge.common.channel.ChannelId[][] initialChannelIds) {
		for (io.openems.edge.common.channel.ChannelId[] channelIds : initialChannelIds) {
			for (io.openems.edge.common.channel.ChannelId channelId : channelIds) {
				this.addChannel(channelId);
			}
		}
	}

	private void logMessage(String reason) {
		String packageName = this.getClass().getPackage().getName();
		if (packageName.startsWith("io.openems.")) {
			packageName = packageName.substring(11);
		}
		this.logInfo(this.log, reason + " " + this.getClass().getSimpleName() + " [" + packageName + "]");
	}

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public String alias() {
		return this.alias;
	}

	@Override
	public Channel<?> _channel(String channelName) {
		Channel<?> channel = this.channels.get(channelName);
		return channel;
	}

	/**
	 * Removes a Channel from this Component.
	 * 
	 * @param channel the Channel
	 */
	// TODO remove Channel(s) using Channel-ID; see addChannels()-method above.
	protected void removeChannel(Channel<?> channel) {
		// Add Channel to channels list
		this.channels.remove(channel.channelId().id(), channel);
		// Handle StateChannels
		if (channel instanceof StateChannel) {
			this.getStateChannel().removeChannel((StateChannel) channel);
		}
	}

	@Override
	public Collection<Channel<?>> channels() {
		return this.channels.values();
	}

	/**
	 * Log a debug message including the Component ID.
	 * 
	 * @param log     the Logger instance
	 * @param message the message
	 */
	protected void logDebug(Logger log, String message) {
		// TODO use log.debug(String, Object...) to improve speed
		log.debug("[" + this.id() + "] " + message);
	}

	/**
	 * Log an info message including the Component ID.
	 * 
	 * @param log     the Logger instance
	 * @param message the message
	 */
	protected void logInfo(Logger log, String message) {
		log.info("[" + this.id() + "] " + message);
	}

	/**
	 * Log a warn message including the Component ID.
	 * 
	 * @param log     the Logger instance
	 * @param message the message
	 */
	protected void logWarn(Logger log, String message) {
		log.warn("[" + this.id() + "] " + message);
	}

	/**
	 * Log an error message including the Component ID.
	 * 
	 * @param log     the Logger instance
	 * @param message the message
	 */
	protected void logError(Logger log, String message) {
		log.error("[" + this.id() + "] " + message);
	}

	/**
	 * Gets an {@link OpenemsTypeDoc} from an Object.
	 * 
	 * @param value the Object
	 * @return the {@link OpenemsTypeDoc}
	 * @throws OpenemsException if the TypeDoc cannot be guessed from the Object.
	 */
	private static OpenemsTypeDoc<?> getDocFromObject(Object value) throws OpenemsException {
		if (value instanceof Boolean) {
			return new BooleanDoc();
		} else if (value instanceof Float) {
			return new FloatDoc();
		} else if (value instanceof Double) {
			return new DoubleDoc();
		} else if (value instanceof Short) {
			return new ShortDoc();
		} else if (value instanceof Integer) {
			return new IntegerDoc();
		} else if (value instanceof Long) {
			return new LongDoc();
		} else if (value instanceof String) {
			return new StringDoc();
		} else if (value.getClass().isArray()) {
			return new StringDoc();
		}
		throw new OpenemsException(
				"Unable to find OpenemsType for Class [" + value.getClass() + "] of Object [" + value + "]");
	}
}
