package io.openems.edge.controller.api.websocket;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.java_websocket.WebSocket;
import org.ops4j.pax.logging.spi.PaxAppender;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;

import io.openems.common.exceptions.OpenemsError;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.notification.EdgeConfigNotification;
import io.openems.common.jsonrpc.notification.EdgeRpcNotification;
import io.openems.common.jsonrpc.request.SubscribeSystemLogRequest;
import io.openems.common.types.EdgeConfig;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.user.EdgeUser;
import io.openems.edge.common.user.UserService;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.controller.api.common.ApiWorker;
import io.openems.edge.timedata.api.Timedata;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "Controller.Api.Websocket", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = "org.ops4j.pax.logging.appender.name=Controller.Api.Websocket")
public class WebsocketApi extends AbstractOpenemsComponent
		implements Controller, OpenemsComponent, PaxAppender, EventHandler {

	public static final String EDGE_ID = "0";
	public static final String EDGE_COMMENT = "";
	public static final String EDGE_PRODUCT_TYPE = "";

	public static final int DEFAULT_PORT = 8075;

	protected final ApiWorker apiWorker = new ApiWorker();

	private final SystemLogHandler systemLogHandler;

	protected WebsocketServer server = null;

	/**
	 * Stores valid session tokens for authentication via Cookie.
	 */
	protected final Map<UUID, EdgeUser> sessionTokens = new ConcurrentHashMap<>();

	@Reference
	protected ComponentManager componentManager;

	@Reference
	protected UserService userService;

	@Reference(policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.OPTIONAL)
	private volatile Timedata timedata = null;

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		;
		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	public WebsocketApi() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				ChannelId.values() //
		);
		this.systemLogHandler = new SystemLogHandler(this);
	}

	@Activate
	protected void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		if (!this.isEnabled()) {
			// abort if disabled
			return;
		}
		this.apiWorker.setTimeoutSeconds(config.apiTimeout());
		this.startServer(config.port());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
		this.stopServer();
	}

	/**
	 * Create and start new server.
	 * 
	 * @param port the port
	 */
	private synchronized void startServer(int port) {
		this.server = new WebsocketServer(this, "Websocket Api", port);
		this.server.start();
	}

	/**
	 * Stop existing websocket server.
	 */
	private synchronized void stopServer() {
		if (this.server != null) {
			this.server.stop();
		}
	}

	@Override
	public void run() throws OpenemsNamedException {
		this.apiWorker.run();
	}

	@Override
	protected final void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	protected final void logWarn(Logger log, String message) {
		super.logWarn(log, message);
	}

	@Override
	public void doAppend(PaxLoggingEvent event) {
		this.systemLogHandler.handlePaxLoggingEvent(event);
	}

	/**
	 * Gets the WebSocket connection attachment for a UI token.
	 * 
	 * @param token the UI token
	 * @return the WsData
	 * @throws OpenemsNamedException if there is no connection with this token
	 */
	protected WsData getWsDataForTokenOrError(UUID token) throws OpenemsNamedException {
		Collection<WebSocket> connections = this.server.getConnections();
		for (Iterator<WebSocket> iter = connections.iterator(); iter.hasNext();) {
			WebSocket websocket = iter.next();
			WsData wsData = websocket.getAttachment();
			UUID thisToken = wsData.getSessionToken();
			if (thisToken != null && thisToken.equals(token)) {
				return wsData;
			}
		}
		throw OpenemsError.BACKEND_NO_UI_WITH_TOKEN.exception(token);
	}

	/**
	 * Handles a SubscribeSystemLogRequest by forwarding it to the
	 * 'SystemLogHandler'.
	 * 
	 * @param token   the UI token
	 * @param request the SubscribeSystemLogRequest
	 * @throws OpenemsNamedException on error
	 */
	protected void handleSubscribeSystemLogRequest(UUID token, SubscribeSystemLogRequest request)
			throws OpenemsNamedException {
		this.systemLogHandler.handleSubscribeSystemLogRequest(token, request);
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CONFIG_UPDATE:
			if (this.server.getConnections().isEmpty()) {
				// No Connections? It's not required to build the EdgeConfig.
				return;
			}
			EdgeConfig config = (EdgeConfig) event.getProperty(EdgeEventConstants.TOPIC_CONFIG_UPDATE_KEY);
			EdgeConfigNotification message = new EdgeConfigNotification(config);
			this.server.broadcastMessage(new EdgeRpcNotification(WebsocketApi.EDGE_ID, message));
		}
	}

	/**
	 * Gets the Timedata service.
	 * 
	 * @return the service
	 * @throws OpenemsException if the timeservice is not available
	 */
	public Timedata getTimedata() throws OpenemsException {
		if (this.timedata != null) {
			return this.timedata;
		}
		throw new OpenemsException("There is no Timedata-Service available!");
	}
}
