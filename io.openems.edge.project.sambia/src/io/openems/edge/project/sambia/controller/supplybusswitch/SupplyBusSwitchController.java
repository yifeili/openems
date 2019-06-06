package io.openems.edge.project.sambia.controller.supplybusswitch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.openems.common.exceptions.OpenemsException;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.ChannelAddress;
import io.openems.common.utils.JsonUtils;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;

@Designate(ocd = Config.class, factory = true)
@Component(name = "Project.Sambia.Controller.SupplyBusSwitch", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class SupplyBusSwitchController extends AbstractOpenemsComponent implements Controller, OpenemsComponent {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

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

	public SupplyBusSwitchController() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				Controller.ChannelId.values(), //
				ChannelId.values() //
		);
	}

	@Reference
	protected ComponentManager componentManager;

	private final Set<String> activeEssIdsForSupplyBuses = new HashSet<>();

	private final List<SupplyBus> supplybuses = new ArrayList<>();

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {
		super.activate(context, config.id(), config.alias(), config.enabled());

		this.supplybuses.addAll(this.generateSupplybuses(config));
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public void run() throws OpenemsNamedException {
		for (SupplyBus sb : supplybuses) {
			sb.run();
		}

		// TODO start Ess in On-Grid-Mode
//		for (String essId : this.config.ess_ids()) {
//			EssFeneconCommercial40Impl ess = this.componentManager.getComponent(essId);
//			GridMode gridMode = ess.getGridMode().value().asEnum();
//			WriteChannel<SetWorkState> setWorkStateChannel = ess
//					.channel(EssFeneconCommercial40Impl.ChannelId.SET_WORK_STATE);
//			if (gridMode.equals(GridMode.ON_GRID)) {
//				// start all ess
//				setWorkStateChannel.setNextWriteValue(SetWorkState.START);
//			} else {
//				setWorkStateChannel.setNextWriteValue(SetWorkState.STANDBY);
//			}
//		}
	}

	private List<SupplyBus> generateSupplybuses(Config config) throws OpenemsNamedException {
		List<SupplyBus> buses = new ArrayList<>();

		JsonArray supplyBusConfigs = JsonUtils.getAsJsonArray(JsonUtils.parse(config.supplyBusConfig()));
		for (JsonElement supplyBusConfig : supplyBusConfigs) {
			String name = JsonUtils.getAsString(supplyBusConfig, "bus");

			ChannelAddress supplybusOnIndication = ChannelAddress.fromString(//
					JsonUtils.getAsString(supplyBusConfig, "supplybusOnIndication"));

			JsonArray loads = JsonUtils.getAsJsonArray(supplyBusConfig, "loads");
			List<ChannelAddress> loadChannels = new ArrayList<>();
			for (JsonElement load : loads) {
				loadChannels.add(ChannelAddress.fromString(JsonUtils.getAsString(load)));
			}

			String primaryEssId = JsonUtils.getAsString(supplyBusConfig, "primaryEss");

			Map<String, ChannelAddress> switchEssMapping = new HashMap<>();
			JsonArray switches = JsonUtils.getAsJsonArray(supplyBusConfig, "switches");
			for (JsonElement switchElement : switches) {
				String essId = JsonUtils.getAsString(switchElement, "ess");
				ChannelAddress channel = ChannelAddress
						.fromString(JsonUtils.getAsString(switchElement, "switchAddress"));
				switchEssMapping.put(essId, channel);
			}
			SupplyBus sb = new SupplyBus(this, switchEssMapping, name, primaryEssId, config.switchDelay(),
					supplybusOnIndication, loadChannels);
			buses.add(sb);
		}
		return buses;
	}

	@Override
	protected void logInfo(Logger log, String message) {
		super.logInfo(log, message);
	}

	@Override
	protected void logError(Logger log, String message) {
		super.logError(log, message);
	}

	protected synchronized void addActiveEssIdForSupplyBus(String essId) throws OpenemsException {
		this.logInfo(this.log, "addActiveEssIdForSupplyBus " + essId);
		if (!this.activeEssIdsForSupplyBuses.add(essId)) {
			throw new OpenemsException("ESS had already been marked as Active by another Bus");
		}
	}

	public synchronized void removeActiveEssIdForSupplyBus(String essId) {
		this.logInfo(this.log, "removeActiveEssIdForSupplyBus " + essId);
		this.activeEssIdsForSupplyBuses.remove(essId);
	}

	public synchronized boolean isActiveEssIdForSupplyBus(String activeEssId) {
		return this.activeEssIdsForSupplyBuses.contains(activeEssId);
	}

}
